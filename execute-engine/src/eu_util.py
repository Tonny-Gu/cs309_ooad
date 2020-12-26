import subprocess
import traceback

class DB_Warp():
    def __init__(self, conn):
        self.conn = conn

    @staticmethod
    def run_service():
        pass

    def exec_sql(self, sql_code: str, ret=True):
        pass
    
    def close(self):
        self.conn.close()


class PSQL_Warp(DB_Warp):
    db_lib = __import__("psycopg2")

    def __init__(self):
        super().__init__(
            self.db_lib.connect(
                user="postgres",
                database="test"))

    @staticmethod
    def run_service():
        subprocess.run("sudo service postgresql start", check=True, shell=True)
    
    def exec_sql(self, sql_code: str, ret=True):
        with self.conn.cursor() as cursor:
            cursor.execute(sql_code)
            result = cursor.fetchall() if ret else ""
            self.conn.commit()
            return result

class MySQL_Warp(DB_Warp):
    db_lib = __import__("mysql.connector").connector

    def __init__(self):
        super().__init__(
            self.db_lib.connect(
                user = "root",
                database = "test"
            )
        )

    @staticmethod
    def run_service():
        while True:
            try: subprocess.run("sudo service mysql start", check=True, shell=True)
            except Exception: traceback.print_exc()
            else: break

    def exec_sql(self, sql_code: str, ret=True):
        # with self.conn.cursor() as cursor:
        cursor = self.conn.cursor(buffered=True)
        try:
            for _ in cursor.execute(sql_code, multi=True, params=None): pass
        except RuntimeError as e:
            if "StopIteration" in str(e): pass
            else: raise e
            # Bug fix for mysql.connector
        self.conn.commit()
        result = cursor.fetchall() if ret else ""
        cursor.close()
        return result

class SQLite_Warp(DB_Warp):
    db_lib = __import__("sqlite3")

    def __init__(self):
        super().__init__(self.db_lib.connect("/home/testee/test.db"))
    
    def exec_sql(self, sql_code: str, ret=True):
        cursor = self.conn.cursor()
        cursor.executescript(sql_code)
        self.conn.commit()
        result = cursor.fetchall() if ret else ""
        cursor.close()
        return result

DB_LIST = {
    "PostgreSQL": PSQL_Warp,
    "MySQL": MySQL_Warp,
    "SQLite": SQLite_Warp
}
