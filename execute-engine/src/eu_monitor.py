from typing import List

import paho.mqtt.client as mqtt
import subprocess
import shutil
import sys
import hashlib
import json
import os
import uuid
import threading
import queue
import time
import logging
import base64
import traceback
import pwd

TEMP_DIR = pwd.getpwuid( os.getuid() ).pw_dir + "/temp"
BASE_IMAGE = "dboj_base:4"
REGISTRY_ADDR = "192.168.122.10:5000"
MQTT_ADDR = "192.168.122.10"
LOG_MAX_LEN = 5000

REMOTE_BASE_IMAGE = REGISTRY_ADDR + "/" + BASE_IMAGE
CWD_PATH = os.path.dirname(os.path.realpath(__file__))
UID = os.getuid()
GID = os.getgid()

judge_tasks = queue.Queue()
create_tasks = queue.Queue()

def on_connect(client:mqtt.Client, userdata, flags, rc):
    logging.debug("MQTT Connected with result code "+str(rc))
    for topic in TOPIC_HANDLER.keys(): client.subscribe(topic)

def on_message(client:mqtt.Client, userdata, msg):
    decoded_msg = base64.b64decode(msg.payload).decode("utf-8")
    logging.debug("Topic %s - Received message: %s" % (msg.topic, decoded_msg[:LOG_MAX_LEN]))
    TOPIC_HANDLER[msg.topic](client, userdata, decoded_msg)

def on_judge_code(client:mqtt.Client, userdata, decoded_msg):
    try:
        form = json.loads(decoded_msg)
        form.update({
            "request": "judgeCode"
        })

        cases, out, err = [], "", ""
        
        for case in form["testCases"]:
            taskid, accepted_flag = str(uuid.uuid4()), str(uuid.uuid4())
            stdin_path  = "%s/%s.stdin" % (TEMP_DIR, taskid)
            stdout_path = "%s/%s.stdout" % (TEMP_DIR, taskid)
            stderr_path = "%s/%s.stderr" % (TEMP_DIR, taskid)

            form["acceptedFlag"] = accepted_flag

            with open(stdin_path, "w") as f: f.write(json.dumps(form))
            ret = subprocess.run("LSB_CONTAINER_IMAGE=%s bsub -W 10 -c 1 -M 500MB -app dockerapp -i %s -o %s -e %s python3 eu_launcher.py" % 
                (case["env"], stdin_path, stdout_path, stderr_path), shell=True, check=True, capture_output=True)
            out += ret.stdout.decode("utf-8")
            err += ret.stderr.decode("utf-8")
            
            case.update({
                "taskid": taskid,
                "acceptedFlag": accepted_flag,
                "stdin": stdin_path,
                "stdout": stdout_path,
                "stderr": stderr_path
            })
            cases.append(case)

        form.update({
            "acceptedFlag": "",
            "testCases": cases,
            "info": {
                "stdout": out,
                "stderr": err
            }
        })
        judge_tasks.put({
            "form": form
        })
    except Exception:
        logging.error(traceback.format_exc())

def on_create_env(client:mqtt.Client, userdata, decoded_msg):
    try:
        form = json.loads(decoded_msg)
        form.update({
            "request": "createEnv",
            # "initDB": base64.b64decode(form["initDB"]).decode("utf-8")
        })
        create_tasks.put({
            "form": form
        })
    except Exception:
        logging.error(traceback.format_exc())

TOPIC_HANDLER = {
    "code/send": on_judge_code,
    "env/send": on_create_env
}

def thread_check_result(client:mqtt.Client):
    while True:
        try:
            time.sleep(1)
            unfinished_task = queue.Queue()
            while not judge_tasks.empty():
                task:dict = judge_tasks.get()
                form:dict = task["form"]

                is_finished = True
                for case in form["testCases"]:
                    is_finished &= os.path.exists(case["stdout"])
                    is_finished &= os.path.exists(case["stderr"])
                
                if not is_finished:
                    unfinished_task.put(task)
                    continue
                
                cases:List[dict] = []
                is_accepted = True
                for case in form["testCases"]:
                    stdin_path, stdout_path, stderr_path = case["stdin"], case["stdout"], case["stderr"]
                    with open(stdout_path, "r") as f: out = "".join(f.readlines())
                    with open(stderr_path, "r") as f: err = "".join(f.readlines())
                    result = "Accepted" if case["acceptedFlag"] in out else "Rejected"
                    is_accepted &= (result == "Accepted")
                    case.update({
                        "status": result,
                        "info": {
                            "stdout": out,
                            "stderr": err
                        }
                    })
                    cases.append(case)
                    for tmpfile in (stdin_path, stdout_path, stderr_path): os.remove(tmpfile)
                
                form.update({
                    "testCases": cases,
                    "status": "Accepted" if is_accepted else "Rejected"
                })

                form_str = json.dumps(form)
                client.publish("code/recv", base64.b64encode(form_str.encode("utf-8")))
                logging.debug("Judged Code: %s" % form_str[:LOG_MAX_LEN])
                
            while not unfinished_task.empty(): judge_tasks.put(unfinished_task.get())
        except Exception:
            logging.error(traceback.format_exc())
    
def thread_create_env(client:mqtt.Client):
    while True:
        try:
            time.sleep(1)
            while not create_tasks.empty():
                task:dict = create_tasks.get()
                form:dict = task["form"]
                imageid = str(uuid.uuid4())

                remote_imageid = REGISTRY_ADDR + "/" + imageid
                form_str = json.dumps(form)
                ret = [
                    subprocess.run("docker run -i --name %s -v %s:%s -u %d:%d -w %s %s python3 eu_launcher.py" %
                        (imageid, CWD_PATH, CWD_PATH, UID, GID, CWD_PATH, REMOTE_BASE_IMAGE),
                        input=form_str.encode("utf-8"), shell=True, check=False, capture_output=True),
                    subprocess.run("docker commit %s %s" %
                        (imageid, remote_imageid), shell=True, check=False, capture_output=True),
                    subprocess.run("docker push %s" % remote_imageid, shell=True, check=False, capture_output=True),
                    subprocess.run("docker rm -f %s" % imageid, shell=True, check=False, capture_output=True)
                ]

                out, err, status = "", "", "Success"
                for result in ret:
                    result:subprocess.CompletedProcess
                    out += result.stdout.decode("utf-8")
                    err += result.stderr.decode("utf-8")
                    if result.returncode != 0: status = "Fail"

                if status == "Fail": subprocess.run("docker rmi %s" % remote_imageid, shell=True, capture_output=True)

                form.update({
                    "env": remote_imageid,
                    "status": status,
                    "info": {
                        "stdout": out,
                        "stderr": err
                    },
                    "initDB": ""
                })

                form_str = json.dumps(form)
                client.publish("env/recv", base64.b64encode(form_str.encode("utf-8")))
                logging.debug("Created Env: %s" % form_str[:LOG_MAX_LEN])
        except Exception:
            logging.error(traceback.format_exc())

if __name__ == "__main__":
    logging.basicConfig(format="%(asctime)s %(message)s", level=logging.DEBUG)

    if os.path.exists(TEMP_DIR): shutil.rmtree(TEMP_DIR)
    os.umask(000)
    os.makedirs(TEMP_DIR, 0o777)

    client = mqtt.Client()
    client.on_connect = on_connect
    client.on_message = on_message

    client.connect(MQTT_ADDR, 1883, 60)

    threads: List[threading.Thread] = [
        threading.Thread(target=thread_check_result, args=(client, )),
        threading.Thread(target=thread_create_env, args=(client, )),
    ]
    for thread in threads: thread.start()
    client.loop_forever()
    
