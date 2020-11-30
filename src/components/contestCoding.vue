<template>
  <div class="answer">
    <NavHeader></NavHeader>
    <div id='Main'>
      <div id="contest_questions">
        <el-button type="info" plain style="position: fixed; top: 45%; left: 0; height: 15%;" @click="drawer=true"><div id="question_list_button">Problems</div></el-button>
        <el-drawer
            title="我是标题"
            :visible.sync="drawer"
            :with-header="false"
            direction="ltr"
        style="width: 1000px;">
          <span>
            <el-menu default-active="1" class="el-menu-vertical-demo" style="height: 100%;">
            <el-menu-item v-for="contest_question in questionsInContest" :key="contest_question.name" @click="handleOpen(contest_question)"><template slot="title"><span>{{contest_question.name}}</span></template></el-menu-item>
            </el-menu>
          </span>
        </el-drawer>
      </div>
      <div id='question_block'>
        <el-menu class="el-menu-demo" mode="horizontal" >
          <el-menu-item @click="Show=true">
            Question Description
          </el-menu-item>
          <el-menu-item @click="handleSubmissionRecord">
            Submission Record
          </el-menu-item>
        </el-menu>
        <div id='question_description' v-html="questionContent" v-if="Show" style="text-align: left;margin-left: 2%;">

        </div>
        <div v-else>
          <el-table :data="pageList" stripe style="width: 100%; text-align-all: center;">
            <el-table-column prop="QuestionId" label="题目号">
            </el-table-column>
            <el-table-column prop="StudentId" label="用户名">
            </el-table-column>
            <el-table-column prop="submitTime" label="提交时间">
            </el-table-column>
            <el-table-column prop="runTime" label="运行时间">
            </el-table-column>
            <el-table-column prop="statusName" label="结果">
              <template slot-scope="scope">
                <span v-html="formateStatus( scope.row.statusName )"></span>
              </template>
            </el-table-column>
          </el-table>
          <div class="block">
            <el-pagination id='PageControl' @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage1"
                           :page-sizes="[10, 20, 30, 40]" :page-size="10" layout="total, sizes, prev, pager, next, jumper" :total="rawList.length"></el-pagination>
          </div>
        </div>
      </div>
      <div id='edit_block' style="text-align: -webkit-auto;">
        <div id="edit_header">
          <div id="language">
            <el-select v-model="language" filterable placeholder="Please choose the language you use" style="width: 130px;margin-top: 10px;margin-right: 10px;"
                       size="large">
              <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div id='format_div'>
            <el-tooltip class="item" effect="dark" content="Format you code" placement="top-start" style="margin-top: 10px;">
              <el-button plain v-on:click='format()'>Format</el-button>
            </el-tooltip>
          </div>
          <div id='upload' style="margin-top: 1%;">
            <el-tooltip class="item" effect="dark" content="Submit you Code" placement="top-start">
               <el-button style="width: 90px;" @click="submitCode">Submit<i class="el-icon-upload el-icon-right"></i></el-button>
<!--              <el-link style='font-size: 20px; margin-top: 20px;margin-left: 8px;color: greenyellow;'><i class='el-icon-upload'></i>Submit</el-link>-->
            </el-tooltip>
          </div>
          <div id='load_history' style="margin-top: 1%;">
            <el-tooltip class="item" effect="dark" content="Reload the last code" placement="top-start">
               <el-button icon="el-icon-edit"></el-button>
<!--              <el-link style='font-size: 20px; margin-top: 20px;margin-left: 8px;color: blue;'><i class='el-icon-info'></i>Reload</el-link>-->
            </el-tooltip>
          </div>
          <div id='clear' style="margin-top: 1%;">
            <el-tooltip class="item" effect="dark" content="Clear the code" placement="top-start">
               <el-button icon="el-icon-delete"></el-button>
<!--              <el-link style='font-size: 20px; margin-top: 20px; color: red;'><i class='el-icon-delete'></i>Clear</el-link>-->
            </el-tooltip>
          </div>
        </div>
        <div>
					<textarea ref="mycode" class="codesql" v-model="value" autoRefresh="true">
			    </textarea>
        </div>
      </div>
    </div>
    <el-dialog :visible.sync="returnDialog" >
      <span slot="title" class="dialog-footer">
        #{{this.chosenTitle}} | {{this.$store.state.myUser.nickname}}'s solution
      </span>

      <el-table :data="submitData">
        <el-table-column property="status" label="status"></el-table-column>
        <el-table-column property="language" label="language"></el-table-column>
        <el-table-column property="submissionTime" label="submission time"></el-table-column>
        <el-table-column property="runId" label="run Id"></el-table-column>
      </el-table>
      <el-card class="box-card">
        <div class="text item" style="text-align: left">
          {{submitData[0].returnContent}}
        </div>
      </el-card>
      <div style="margin-top: 20px; height: 20px;">
        <el-button type="primary" style="float: right" @click="handleYes">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// @ is an alias to /src
import NavHeader from '../components/NavHeader.vue'
import questionDescription from '../components/questionDescr.vue'
import sqlFormatter from "sql-formatter";
import "codemirror/theme/ambiance.css";
import "codemirror/lib/codemirror.css";
import "codemirror/addon/hint/show-hint.css";
import api from '@/views/api'
import Qs from 'qs'
let CodeMirror = require("codemirror/lib/codemirror");
require("codemirror/addon/edit/matchbrackets");
require("codemirror/addon/selection/active-line");
require("codemirror/mode/sql/sql");
require("codemirror/addon/hint/show-hint");
require("codemirror/addon/hint/sql-hint");
let timer;
export default {

  name: 'answer',
  methods: {
    handleYes(){
      this.returnDialog=false
      this.submitData = [{
        status: 'running',
        language: '',
        submissionTime: '',
        runId: '',
        returnContent: ''
      }]
    },
    handleSubmissionRecord: function(){
      this.Show = false;
    },
    handleSizeChange: function(pageSize) {
      this.pageSize = pageSize;
      this.handleCurrentChange(this.currentPage1);
    },
    handleCurrentChange: function(currentPage) {
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
        return (`<span style="color: #4bfe20">${statusName}</span>`)
      return (`<span style="color: #f30112">${statusName}</span>`)
    },
    handleOpen(contest_question){
      this.questionContent = contest_question.content;
      this.chosenId = contest_question.id
      this.chosenTitle = contest_question.name
      this.drawer = false;
    },
    setVal() {
      if (this.editor) {
        if (this.value === '') {
          this.editor.setValue('')
        } else {
          this.editor.setValue(this.value)
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
    getQuestionInContest(){
      api.getQuestionByContest(this.$route.params.contestId).then(res =>{
        console.log(res)
        for(let i = 0; i < res.data.length;i++){
          let data = {
            name:res.data[i].name,
            degree:res.data[i].degree,
            dbType: res.data[i].dbType,
            content:atob(res.data[i].content),
            id:res.data[i].id
          }
          this.questionsInContest.push(data)
        }
        this.questionContent = this.questionsInContest[0]['content']
        this.chosenId = this.questionsInContest[0]['id']
        this.chosenTitle = this.questionsInContest[0]['name']
      })
    },
    submitCode(){
      let data = {
        user_id: this.$store.state.myUser.id,
        question_id: this.chosenId,
        contest_id: this.$route.params.contestId,
        code:this.editor.getValue(),
        language:this.language
      }
      api.submitCode(Qs.stringify(data)).then(res=>{
        this.submissionId = res.data;
        this.returnDialog = true;
        this.lopQuest()
      })
    },
    lopQuest(){
      timer = window.setInterval(() => {
        setTimeout(this.getResult(), 0)
      }, 1000)
    },
    getResult(){
      api.getResult(this.submissionId).then(res =>{
        console.log(res)
        this.submissionStatus = res.data.status;
        if(this.submissionStatus!=='Submit'){
          this.submitData[0].status = this.submissionStatus
          this.submitData[0].language = res.data.language
          this.submitData[0].submissionTime = res.data.submitTime
          this.submitData[0].runId = res.data.id
          this.submitData[0].returnContent = res.data.info
          this.stopTimer()
        }
      })
    },
    stopTimer(){
      window.clearInterval(timer)
    }
  },
  components: {
    NavHeader,
    questionDescription
  },
  data() {
    return {
      returnDialog: false,
      submissionId:'',
      submissionStatus:'',
      submitData: [{
        status: 'running',
        language: '',
        submissionTime: '',
        runId: '',
        returnContent: ''
      }]
      ,rawList: [{
        QuestionId: '321',
        StudentId: '123',
        submitTime: '2020-11-19 10:30',
        runTime: '10ms',
        statusName: "Pass"
      },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '123',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        },
        {
          QuestionId: '23',
          StudentId: '123',
          submitTime: '2020-11-19 10:30',
          runTime: '10ms',
          statusName: "TLE"
        }
      ],
      currentQuestion: '',
      currentPage1: 1,
      pageSize: 10,
      pageList: [],
      current_activity: 1,
      chosenId:'',
      chosenTitle:'',
      drawer: false,
      Show: true,
      editor: null,
      options: [{
        value: 'PostgreSQL',
        label: 'PostgreSQL'
      }, {
        value: 'Mysql',
        label: 'Mysql'
      }, {
        value: 'SQLite',
        label: 'SQLite'
      }],
      language: 'PostgreSQL',
      questionContent: '',
      questionsInContest: [
      ]
    }
  },
  props: {
    value: {
      type: String,
      default: ''
    },
    sqlStyle: {
      type: String,
      default: 'PostgreSQL'
    },
    readOnly: {
      type: [Boolean, String]
    }
  },
  watch: {
    newVal(newV, oldV) {
      if (this.editor) {
        this.$emit('changeTextarea', this.editor.getValue())
      }
    }
  },
  computed: {
    newVal() {
      if (this.editor) {
        return this.editor.getValue()
      }
    }
  },

  mounted() {
    this.handleCurrentChange(1);
    let question_list_button = document.getElementById('question_list_button');
    question_list_button.style.fontSize = (window.innerHeight * 15 / 100) / 8 + 'px'
    this.getQuestionInContest();
    let edit_block = document.getElementById('edit_block');
    edit_block.style.height = (window.innerHeight - 60) + "px";
    let question_block = document.getElementById('question_block');
    question_block.style.height = (window.innerHeight - 60) + "px";
    let contest_questions = document.getElementById('contest_questions');
    contest_questions.style.height = (window.innerHeight - 60) + 'px';
    let mime = 'text/x-mariadb'
    //let theme = 'ambiance'//设置主题，不设置的会使用默认主题
    this.editor = CodeMirror.fromTextArea(this.$refs.mycode, {
      value: this.value,
      mode: mime, //选择对应代码编辑器的语言，我这边选的是数据库，根据个人情况自行设置即可
      indentWithTabs: true,
      indentUnit: 2,
      smartIndent: true,
      lineNumbers: true,
      matchBrackets: true,
      cursorHeight: 1,
      lineWrapping: true,
      readOnly: this.readOnly,
      //theme: '',
      // autofocus: true,
      extraKeys: {
        'Ctrl': 'autocomplete'
      }, //自定义快捷键
      hintOptions: { //自定义提示选项
        // 当匹配只有一项的时候是否自动补全
        completeSingle: false,
        // tables: {
        //     users: ['name', 'score', 'birthDate'],
        //     countries: ['name', 'population', 'size']
        // }
      }
    })
    //代码自动提示功能，记住使用cursorActivity事件不要使用change事件，这是一个坑，那样页面直接会卡死
    this.editor.on('inputRead', () => {
      this.editor.showHint()
    })
  }
}
</script>
<style>
#format_div {
  float: left;
}

#question_list_button{
  margin: 0 auto;
  height: 140px;
  font-size: 30px;
  writing-mode: vertical-lr;/*从左向右 从右向左是 writing-mode: vertical-rl;*/
  writing-mode: tb-lr;/*IE浏览器的从左向右 从右向左是 writing-mode: tb-rl；*/
}

#user_choice {
  float: right;
  font-size: 40px;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}

.el-icon-arrow-down {
  font-size: 12px;
}

#Main {
  overflow: hidden;
}

#contest_questions{
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
  border: 1px solid #eee;
  height: calc(100vh - 90px);
  font-size: 20px;
}
</style>
