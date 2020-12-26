from eu_util import *

import subprocess
import sys
import json
import os

CWD_PATH = os.path.dirname(os.path.realpath(__file__))

if __name__ == "__main__":
    input_data = "".join(sys.stdin.readlines())
    form = json.loads(input_data)
    DB_LIST[ form["language"] ].run_service()
    subprocess.run("sudo su - testee -c ' cd %s && python3 eu_core.py'" % CWD_PATH,
                    input=input_data.encode("utf-8"), shell=True, check=True)
    