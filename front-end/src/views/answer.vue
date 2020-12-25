<template>
  <div class="answer">
    <NavHeader></NavHeader>
    <div id="Main">
      <div id="question_block">
        <el-menu class="el-menu-demo" mode="horizontal">
          <el-menu-item @click="Show = 1"> Question Description </el-menu-item>
          <el-menu-item @click="handleSubmissionRecord" v-if="!this.mobileFlag">
            Submission Record
          </el-menu-item>
        </el-menu>
        <div
          id="question_description"
          v-html="questionContent"
          v-if="Show === 1"
          style="text-align: left; margin-left: 2%"
        ></div>
        <div v-if="Show === 2 && !this.mobileFlag">
          <el-dialog
            title="Detail"
            :visible.sync="DetailVisible"
            :append-to-body="true"
          >
            <li v-for="info in this.Detail.info">
              Test Case{{ info["id"] }}-----Status: {{ info["status"] }}
            </li>
            <el-divider></el-divider>
            <div>{{ this.Detail.code }}</div>
          </el-dialog>
          <el-table
            :data="pageList"
            tooltip-effect="dark"
            style="width: 100%; overflow: auto; text-align-all: center"
          >
            <el-table-column
              property="id"
              label="Submit Id"
              align="center"
              sortable
            ></el-table-column>

            <el-table-column
              property="submitTime"
              label="Submit Time"
              align="center"
              sortable
            ></el-table-column>
            <el-table-column
              property="contest"
              label="Contest"
              align="center"
            ></el-table-column>
            <el-table-column
              property="questionTitle"
              label="Question"
              align="center"
            ></el-table-column>
            <el-table-column property="status" label="Status" align="center">
              <template slot-scope="scope">
                <el-button
                  type="primary"
                  v-if="scope.row.status === 'Accepted' && Show === 2"
                  style="background-color: #67c23a; width: 100px; border: black"
                  size="small"
                  >{{ scope.row.status }}</el-button
                >
                <el-button
                  type="primary"
                  v-else-if="scope.row.status === 'Wrong Answer' && Show === 2"
                  style="background-color: #f56c6c; width: 100px; border: black"
                  size="small"
                  >Wrong</el-button
                >
                <el-button
                  type="primary"
                  v-else-if="scope.row.status === 'Time Limited' && Show === 2"
                  style="background-color: #ffa500; width: 100px; border: black"
                  size="small"
                  >TLE</el-button
                >
                <el-button
                  type="primary"
                  size="small"
                  v-else-if="Show === 2"
                  style="
                    background-color: rgb(245, 187, 1);
                    width: 100px;
                    border: black;
                  "
                  >Waiting</el-button
                >
              </template>
            </el-table-column>
            <el-table-column label="Code/Info" align="center">
              <template slot-scope="scope">
                <el-button
                  type="primary"
                  @click="showDetail(scope.row)"
                  size="small"
                  >Detail</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <div class="block">
            <el-pagination
              id="PageControl"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="currentPage1"
              :page-sizes="[10, 20, 30, 40]"
              :page-size="10"
              layout="total, sizes, prev, pager, next, jumper"
              :total="rawList.length"
            ></el-pagination>
          </div>
        </div>
      </div>
      <div
        id="edit_block"
        style="text-align: -webkit-auto"
        v-if="!this.mobileFlag"
      >
        <div id="edit_header">
          <div id="language">
            <el-select
              v-model="language"
              filterable
              placeholder="Please choose the language you use"
              style="width: 130px; margin-top: 10px; margin-right: 10px"
              size="large"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </div>
          <div id="format_div">
            <el-tooltip
              class="item"
              effect="dark"
              content="Format you code"
              placement="top-start"
              style="margin-top: 10px"
            >
              <el-button plain v-on:click="format()">Format</el-button>
            </el-tooltip>
          </div>
          <div id="upload" style="margin-top: 1%">
            <el-tooltip
              class="item"
              effect="dark"
              content="Submit you Code"
              placement="top-start"
            >
              <el-button
                style="width: 90px"
                @click="submitCode"
                v-if="waitTime === 0"
                type="success"
                >Submit<i class="el-icon-upload el-icon-right"></i>
              </el-button>
              <el-button
                style="width: 90px"
                v-else
                disabled
                :loading="true"
                type="danger"
                >{{ waitTime }}
              </el-button>
              <!--              <el-link style='font-size: 20px; margin-top: 20px;margin-left: 8px;color: greenyellow;'><i class='el-icon-upload'></i>Submit</el-link>-->
            </el-tooltip>
          </div>
          <div id="load_history" style="margin-top: 1%">
            <el-tooltip
              class="item"
              effect="dark"
              content="Reload the last code"
              placement="top-start"
            >
              <el-button icon="el-icon-edit" @click="getLatestCode"></el-button>
              <!--              <el-link style='font-size: 20px; margin-top: 20px;margin-left: 8px;color: blue;'><i class='el-icon-info'></i>Reload</el-link>-->
            </el-tooltip>
          </div>
          <div id="clear" style="margin-top: 1%">
            <el-tooltip
              class="item"
              effect="dark"
              content="Clear the code"
              placement="top-start"
            >
              <el-button icon="el-icon-delete" @click="clear()"></el-button>
              <!--              <el-link style='font-size: 20px; margin-top: 20px; color: red;'><i class='el-icon-delete'></i>Clear</el-link>-->
            </el-tooltip>
          </div>
        </div>
        <div>
          <textarea
            ref="mycode"
            class="codesql"
            v-model="value"
            autoRefresh="true"
          >
          </textarea>
        </div>
      </div>
    </div>
    <el-dialog :visible.sync="returnDialog">
      <el-table :data="submitData">
        <el-table-column property="status" label="status">
          <template slot-scope="scope">
            <div v-if="scope.row.status === 'running'">
              <i class="el-icon-loading"></i>running
            </div>
            <el-button
              type="primary"
              v-if="scope.row.status === 'Accepted'"
              style="background-color: #67c23a; width: 100px; border: black"
              size="small"
              >{{ scope.row.status }}</el-button
            >
            <el-button
              type="primary"
              v-else-if="scope.row.status === 'Wrong Answer'"
              style="background-color: #f56c6c; width: 100px; border: black"
              size="small"
              >Wrong</el-button
            >
            <el-button
              type="primary"
              v-else-if="scope.row.status === 'Time Limited'"
              style="background-color: #ffa500; width: 100px; border: black"
              size="small"
              >TLE</el-button
            >
          </template>
        </el-table-column>
        <el-table-column property="language" label="language"></el-table-column>
        <el-table-column
          property="submissionTime"
          label="submission time"
        ></el-table-column>
        <el-table-column property="runId" label="run Id"></el-table-column>
      </el-table>
      <el-card class="box-card">
        <div class="text item" style="text-align: left">
          <li v-for="info in submitData[0].returnContent">
            Test Case{{ info["id"] }}-----Status:{{ info["status"] }}
          </li>
        </div>
      </el-card>
      <div style="margin-top: 20px; height: 20px">
        <el-button type="primary" style="float: right" @click="handleYes"
          >Confirm</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
// @ is an alias to /src
import NavHeader from "../components/NavHeader.vue";
import questionDescription from "../components/questionDescr.vue";
import sqlFormatter from "sql-formatter";
import "codemirror/theme/ambiance.css";
import "codemirror/theme/idea.css";
import "codemirror/lib/codemirror.css";
import "codemirror/addon/hint/show-hint.css";
import api from "@/views/api";
import Qs from "qs";

let CodeMirror = require("codemirror/lib/codemirror");
require("codemirror/addon/edit/matchbrackets");
require("codemirror/addon/selection/active-line");
require("codemirror/mode/sql/sql");
require("codemirror/addon/hint/show-hint");
require("codemirror/addon/hint/sql-hint");
let timer;
let subTimer;
export default {
  name: "answer",
  methods: {
    _isMobile() {
      let flag = navigator.userAgent.match(
        /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
      );
      return flag;
    },
    clear() {
      this.editor.setValue("");
    },
    handleRank() {
      this.Show = 3;
      this.getRankInContest();
    },
    handleYes() {
      this.returnDialog = false;
      this.submitData = [
        {
          status: "running",
          language: "",
          submissionTime: "",
          runId: "",
          returnContent: "",
        },
      ];
    },
    handleSubmissionRecord: function () {
      this.Show = 2;
      this.getQuestionSubmissionInContest();
    },
    handleSizeChange: function (pageSize) {
      this.pageSize = pageSize;
      this.handleCurrentChange(this.currentPage1);
    },
    handleCurrentChange: function (currentPage) {
      this.currentPage1 = currentPage;
      this.currentChangePage(this.rawList, currentPage);
    },
    currentChangePage(list, currentPage) {
      let from = (currentPage - 1) * this.pageSize;
      let to = currentPage * this.pageSize;
      this.pageList = [];
      for (; from < to; from++) {
        if (list[from]) {
          this.pageList.push(list[from]);
        }
      }
    },
    formateStatus(statusName) {
      if (statusName === "Pass")
        return `<span style="color: #4bfe20">${statusName}</span>`;
      return `<span style="color: #f30112">${statusName}</span>`;
    },
    handleOpen(contest_question) {
      this.questionContent = contest_question.content;
      this.chosenId = contest_question.id;
      this.chosenTitle = contest_question.name;
      this.drawer = false;
      this.options = [
        {
          value: contest_question.dbType,
          label: contest_question.dbType,
        },
      ];
    },
    setVal() {
      if (this.editor) {
        if (this.value === "") {
          this.editor.setValue("");
        } else {
          this.editor.setValue(this.value);
        }
      }
    },
    //代码格式化方法
    format() {
      /*获取文本编辑器内容*/
      let sqlContent = "";
      sqlContent = this.editor.getValue();
      /*将sql内容进行格式后放入编辑器中*/
      this.editor.setValue(sqlFormatter.format(sqlContent));
    },
    getQuestionInContest() {
      api.getQuestionById(this.$route.params.id).then((res) => {
        let data = {
          name: res.data.name,
          degree: res.data.degree,
          dbType: res.data.dbType,
          content: this.b64_to_utf8(res.data.content),
          id: res.data.id,
        };
        this.questionsInContest.push(data);
        this.questionContent = this.questionsInContest[0]["content"];
        this.chosenId = this.questionsInContest[0]["id"];
        this.chosenTitle = this.questionsInContest[0]["name"];
        this.options = [
          {
            value: data.dbType,
            label: data.dbType,
          },
        ];
        this.language = data.dbType
      });
    },
    submitCode() {
      this.stopTimer();
      let data = {
        user_id: this.$store.state.myUser.id,
        question_id: this.chosenId,
        contest_id: 1,
        code: this.editor.getValue(),
        language: this.language,
      };

      api.submitCode(Qs.stringify(data)).then((res) => {
        let n = Number(res.data);
        if(res.data === 'Not logged in'){
          this.$message.error("Not logged in")
        }else if(!isNaN(n)){
        this.submissionId = res.data;
        this.returnDialog = true;
        this.lopQuest();
        this.waitTime = 10;
        }else{
          this.$message.error("Something goes wrong")
        }
      });
      
    },
    lopQuest() {
      timer = window.setInterval(() => {
        setTimeout(this.getResult(), 0);
      }, 1000);
    },
    getResult() {
      let data = {
        id: this.submissionId,
      };
      api.getResult(Qs.stringify(data)).then((res) => {
        this.submissionStatus = res.data.status;
        if (this.submissionStatus !== "Submit") {
          this.submitData[0].status = this.submissionStatus;
          this.submitData[0].language = res.data.language;
          this.submitData[0].submissionTime = res.data.submitTime;
          this.submitData[0].runId = res.data.id;
          this.submitData[0].returnContent = JSON.parse(res.data.info);
          this.timerFlag = true;
        }
        if (this.waitTime > 0) this.waitTime -= 1;
        if (this.waitTime == 0 && this.timerFlag) {
          this.stopTimer();
          this.timerFlag = false;
        }
        if (this.waitTime == 0 && !this.returnDialog) {
          this.stopTimer();
        }
      });
    },
    stopTimer() {
      window.clearInterval(timer);
    },
    getLatestCode() {
      let data = {
        contest_id: 1,
        id: this.$store.state.myUser.id,
        question_id: this.chosenId,
        recent: true,
      };
      api.getLatestSubmitCode(Qs.stringify(data)).then((res) => {
        if (res.status === 200) {
          this.latestCode = this.b64_to_utf8(res.data[0].code);
          this.editor.setValue(this.latestCode);
        }
      });
    },
    getQuestionSubmissionInContest() {
      let data = {
        contest_id: 1,
        user_id: this.$store.state.myUser.id,
        question_id: this.chosenId,
        withCode: true,
      };

      api.getSubmissionCondi(Qs.stringify(data)).then((res) => {
        this.rawList = [];
        for (let i = 0; i < res.data.length; i++) {
          let unit = {
            id: res.data[i].id,
            status: res.data[i].status,
            submitTime: res.data[i].submitTime,
            contest: res.data[i].contest.name,
            questionTitle: res.data[i].question.name,
            info: res.data[i].info,
            code: this.b64_to_utf8(res.data[i].code),
          };
          this.rawList.push(unit);
          this.handleCurrentChange(this.currentPage1);
        }
      });
    },
    getRankInContest() {
      this.pageListForRank = [];
      api.getContestRank(1).then((res) => {
        for (let i = 0; i < res.data.length; i++) {
          let unit = {
            rank: i + 1,
            sid: res.data[i][0],
            name: res.data[i][1],
            acNumber: res.data[i][2],
            acTime: res.data[i][3],
          };
          this.pageListForRank.push(unit);
        }
      });
    },
    b64_to_utf8(str) {
      return decodeURIComponent(escape(window.atob(str)));
    },
    showDetail(row) {
      this.Detail = {};
      this.Detail["code"] = row.code;
      if(row.info != '')
        this.Detail["info"] = JSON.parse(row.info);
      else
        this.Detail["info"] = []
      this.DetailVisible = true;
    },
  },
  components: {
    NavHeader,
    questionDescription,
  },
  data() {
    return {
      timerFlag: false,
      mobileFlag: false,
      clientWidth: document.body.clientWidth,
      waitTime: 0,
      Detail: {},
      DetailVisible: false,
      returnDialog: false,
      submissionId: "",
      submissionStatus: "",
      latestCode: "",
      submitData: [
        {
          status: "running",
          language: "",
          submissionTime: "",
          runId: "",
          returnContent: "",
        },
      ],
      rawList: [],
      currentQuestion: "",
      currentPage1: 1,
      pageSize: 10,
      pageList: [],
      pageListForRank: [],
      current_activity: 1,
      chosenId: "",
      chosenTitle: "",
      drawer: false,
      Show: 1,
      editor: null,
      options: [
        {
          value: "PostgreSQL",
          label: "PostgreSQL",
        },
        {
          value: "Mysql",
          label: "Mysql",
        },
        {
          value: "SQLite",
          label: "SQLite",
        },
      ],
      language: "PostgreSQL",
      questionContent: "",
      questionsInContest: [],
    };
  },
  props: {
    value: {
      type: String,
      default: "",
    },
    sqlStyle: {
      type: String,
      default: "PostgreSQL",
    },
    readOnly: {
      type: [Boolean, String],
    },
  },
  watch: {
    newVal(newV, oldV) {
      if (this.editor) {
        this.$emit("changeTextarea", this.editor.getValue());
      }
    },
  },
  computed: {
    newVal() {
      if (this.editor) {
        return this.editor.getValue();
      }
    },
  },

  mounted() {
    if (this._isMobile() !== null) {
      this.mobileFlag = true;
      let question_block = document.getElementById("question_block");
      question_block.style.width = "90%";
    }
    this.handleCurrentChange(1);
    this.getQuestionInContest();
    let edit_block = document.getElementById("edit_block");
    edit_block.style.height = window.innerHeight - 60 + "px";
    let question_block = document.getElementById("question_block");
    question_block.style.height = window.innerHeight - 60 + "px";
    let mime = "text/x-mariadb";
    let theme = "idea";
    this.editor = CodeMirror.fromTextArea(this.$refs.mycode, {
      value: this.value,
      mode: mime,
      indentWithTabs: true,
      indentUnit: 2,
      smartIndent: true,
      lineNumbers: true,
      matchBrackets: true,
      cursorHeight: 1,
      lineWrapping: true,
      readOnly: this.readOnly,
      theme: "idea",
      // autofocus: true,
      extraKeys: {
        Ctrl: "autocomplete",
      }, //自定义快捷键
      hintOptions: {
        //自定义提示选项
        // 当匹配只有一项的时候是否自动补全
        completeSingle: false,
        // tables: {
        //     users: ['name', 'score', 'birthDate'],
        //     countries: ['name', 'population', 'size']
        // }
      },
    });
    //代码自动提示功能，记住使用cursorActivity事件不要使用change事件，这是一个坑，那样页面直接会卡死
    this.editor.on("inputRead", () => {
      this.editor.showHint();
    });
  },
};
</script>
<style>
#format_div {
  float: left;
}

#question_list_button {
  margin: 0 auto;
  margin-left: -5px;
  font-size: 30px;
  writing-mode: vertical-lr; /*从左向右 从右向左是 writing-mode: vertical-rl;*/
  writing-mode: tb-lr; /*IE浏览器的从左向右 从右向左是 writing-mode: tb-rl；*/
}

#user_choice {
  float: right;
  font-size: 40px;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}

.el-icon-arrow-down {
  font-size: 12px;
}

#Main {
  overflow: hidden;
}

#edit_block {
  top: 60px;
  right: 0.5%;
  width: 40%;
  position: fixed;
  float: left;
  overflow: auto;
}

#question_block {
  top: 60px;
  left: 5%;
  height: 100%;
  width: 49%;
  position: fixed;
  background-size: cover;
  float: left;
  overflow: auto;
}

#edit_header {
  top: 50px;
  width: 100%;
  height: 60px;
}

#language {
  float: left;
}

#upload {
  float: right;
  margin-left: 5px;
  margin-right: 1%;
}

#clear {
  float: right;
  margin-left: 5px;
}

#load_history {
  float: right;
  margin-left: 5px;
}

.CodeMirror {
  font-family: Consolas, monospace;
  border: 1px solid #eee;
  height: calc(100vh - 90px);
  font-size: 20px;
}

@font-face {
  font-family: Consolas;
  src: url("../assets/consola-1.ttf");
}
</style>
