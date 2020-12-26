import paho.mqtt.client as mqtt
import json
import base64
import threading
import time
import uuid

MQTT_ADDR = "192.168.122.10"

client = mqtt.Client()
client.connect(MQTT_ADDR, 1883, 60)

recv_buf = {}

def send_form(topic: str, form: dict):
    payload = base64.b64encode( json.dumps(form).encode("utf-8") )
    client.publish(topic, payload)

def create_env(language: str, initDB: str):
    form_id = str(uuid.uuid4())
    form = {
        "id": form_id,
        "request": "createEnv",
        "language": language,
        # "env": "", # Return Value
        "initDB": initDB,
        "sendTime": time.time()
    }
    send_form("env/send", form)
    while True:
        if form_id in recv_buf: return recv_buf[form_id]
        else: time.sleep(1)

def judge_code(language: str, test_cases: list, testee_code: str, sample_code: str, judger=""):
    form_id = str(uuid.uuid4())
    form = {
        "id": form_id,
        "request": "judgeCode",
        "testCases": test_cases,
        "language": language,
        "code": testee_code,
        "ansCode": sample_code,
        "sendTime": time.time()
    }
    if judger != "": form["extension"] = judger
    send_form("code/send", form)
    while True:
        if form_id in recv_buf: return recv_buf[form_id]
        else: time.sleep(1)

def test_create_env():
    print("Start Test: Create Env")
    with open("test/filmdb.sql", "r") as f: 
        init_sql = "".join(f.readlines())
    
    for lang in ("PostgreSQL", ):
        reply = create_env(lang, init_sql)
        print(reply)
        print("Elapsed Time: %ds" % str(reply["totalTime"]))

def test_judge_code():
    print("Start Test: Judge Code")
    language = "PostgreSQL"
    test_cases = [{"env":"192.168.122.10:5000/debcda17-2c7e-43e4-93f4-bae0478cf99e"}]
    testee_code = "select * from movies limit 1000"
    sample_code = testee_code
    
    result = []
    for num_cases in (240, 288, 432, 576): # (1, 2, 4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48, 56, 64, 72, 80, 88, 96, 112, 128, 144, 176, 208, 240, 288, 432, 576):
        reply = judge_code(language, test_cases*num_cases, testee_code, sample_code)
        totalTime = reply["totalTime"]
        print("num_cases: %d, Elapsed Time: %ds" % (num_cases, totalTime))
        result.append((num_cases, totalTime, reply))
        with open("test_judge_code.json", "w") as f: f.write(json.dumps(result))

def test_trigger():
    init_sql = """
create table stuff (
  thing text
);"""

    testee_code = """
create or replace function stuff_inserting() returns trigger language plpgsql as $$
begin

  return null;

end $$;

create trigger inserting before insert on stuff for each row execute procedure stuff_inserting();
"""

    sample_code = """
insert into stuff values ('sample');
select * from stuff;
"""

    judger = """
def judger(sample_code:str, testee_code:str, form:dict, db_warp) -> bool:
    before = db_warp.exec_sql(sample_code)
    db_warp.exec_sql(testee_code, ret=False)
    after = db_warp.exec_sql(sample_code)
    return before == after
"""

    language = "PostgreSQL"

    reply = create_env(language, init_sql)
    print(reply)
    
    test_cases = [{"env":reply["env"]}]
    # test_cases = [{"env": "192.168.122.10:5000/11685850-f87c-4639-a109-41dedb02ea37"}]
    
    reply = judge_code(language, test_cases, testee_code, sample_code, judger)
    print(reply)

def start_test():
    print("Start Test")
    # test_create_env()
    # test_judge_code()
    test_trigger()

def on_connect(client:mqtt.Client, userdata, flags, rc):
    client.subscribe("env/recv")
    client.subscribe("code/recv")
    threading.Thread(target=start_test).start()

def on_message(client:mqtt.Client, userdata, msg):
    form = json.loads(base64.b64decode(msg.payload).decode("utf-8"))
    form["recvTime"] = time.time()
    form["totalTime"] = form["recvTime"] - form["sendTime"]
    recv_buf[ form["id"] ] = form

client.on_connect = on_connect
client.on_message = on_message

if __name__ == "__main__":
    client.loop_forever()