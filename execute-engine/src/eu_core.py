from eu_util import *

import sys
import json

def default_judger(sample_code:str, testee_code:str, form:dict, db_warp) -> bool:
    sample_answer = db_warp.exec_sql(sample_code) # form["ansCode"])
    testee_answer = db_warp.exec_sql(testee_code) # form["code"])
    return sample_answer == testee_answer

if __name__ == "__main__":
    input_data = "".join(sys.stdin.readlines())
    form = json.loads(input_data)
    db_warp:DB_Warp = DB_LIST[ form["language"] ]()

    if form["request"] == "judgeCode":
        if "extension" in form: exec(form["extension"])
        else: judger = default_judger

        is_accepted = judger(form["ansCode"], form["code"], form, db_warp)
        flag = form["acceptedFlag"] if is_accepted else ""

        result = {
            "isAccepted": is_accepted,
            "acceptedFlag": flag
        }
        print(result)
    elif form["request"] == "createEnv":
        db_warp.exec_sql(form["initDB"], ret=False)
