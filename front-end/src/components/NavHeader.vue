<template>
  <div>
    <div id='header'>
      <el-menu theme="light" mode="horizontal" class="oj-menu" style="background-color: transparent;">
        <div class="logo" v-if="this.clientWidth > 616" @click="goHome()"></div>
        <router-link to="/" tag="el-menu-item" v-if="this.clientWidth > 616">
          <i class="el-icon-s-home"></i>
          <span style="color: black">Home</span>
        </router-link>
        <router-link to="/contest" tag="el-menu-item" v-if="this.clientWidth > 616">
          <i class="el-icon-s-opportunity"></i>
          <span style="color: black">Contests</span>
        </router-link>
        <router-link to="/main" tag="el-menu-item" id='que' v-if="this.clientWidth > 616">
          <i class="el-icon-edit"></i>
          <span style="color: black">Questions</span>
        </router-link>
        <el-submenu name="about" index='4' v-if="this.clientWidth > 616">
          <template slot='title'>
            <i class="el-icon-info"></i>
            <span style="color: black">About</span>
          </template>
          <el-menu-item name="/about" index="4-1">
          </el-menu-item>
          <el-menu-item name="/FAQ" index="4-2">
          </el-menu-item>
        </el-submenu>
        <el-submenu name="admin" v-if="this.$store.state.myUser.role === 'ROLE_SA' || this.$store.state.myUser.role === 'ROLE_TA'" index="1" >
          <template slot="title"><i class="el-icon-s-data"></i><span style="color: black">Admin</span></template>
          <el-menu-item @click="uploadQuestionV=true" style="color:black;" index="1">Upload question</el-menu-item>
          <router-link to="/adminMain/createContest" tag="el-menu-item">
            <span style="color: black">Create contest</span>
          </router-link>
          <router-link to="/adminMain/submissionRecord" tag="el-menu-item">
            <span style="color: black">Submission Records</span>
          </router-link>
          <el-submenu name="AnnouncementManagement" index="1-2">
            <template slot="title"><span style="color: black">Announcement Control</span></template>
            <el-menu-item @click="createAnnouncementVisible=true">Create Announcement</el-menu-item>
            <el-menu-item @click="handleDeleteNotice">Delete Announcement</el-menu-item>
          </el-submenu>
        </el-submenu>
        <el-submenu name="allMenu" v-if="this.clientWidth < 616" index="2" :style="'margin-left:' + (this.clientWidth / 2 - 138 / 2) + 'px;'">
          <template slot="title"><div class="logo"></div></template>
          <router-link to="/" tag="el-menu-item" >
            <i class="el-icon-s-home"></i>
            <span style="color: black">Home</span>
          </router-link>
          <router-link to="/contest" tag="el-menu-item">
            <i class="el-icon-s-opportunity"></i>
            <span style="color: black">Contests</span>
          </router-link>
          <router-link to="/main" tag="el-menu-item">
            <i class="el-icon-edit"></i>
            <span style="color: black">Questions</span>
          </router-link>
          <el-submenu name="about" index='2-4'>
            <template slot='title'>
              <i class="el-icon-info"></i>
              <span style="color: black">About</span>
            </template>
            <el-menu-item name="/about" index="2-4-1">
            </el-menu-item>
            <el-menu-item name="/FAQ" index="2-4-2">
            </el-menu-item>
          </el-submenu>
        </el-submenu>
        <template v-if="Login_show">
          <el-button round style="float: right;margin-right: 10px;margin-top: 13px;" @click="loginVisible=true">Login
          </el-button>
          <el-button round style="float: right;margin-right: 10px;margin-top: 13px;" @click="registerVisible=true">
            Register
          </el-button>
        </template>
        <template v-else>
          <div style="float: right;margin-right: 5%; font-size: 30px; margin-top: 13px;"><i
              class="el-icon-user-solid"></i>
            <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="a">Log out</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </template>
      </el-menu>
    </div>
    <el-dialog title="Delete Announcement" :visible.sync="DeleteAnnouncementVisible">
      <el-table
          ref="multipleTable"
          :data="AnnouncementData"
          tooltip-effect="dark"
          style="width: 100%"
          @selection-change="handleSelectionChange">
        <el-table-column
            type="selection">
        </el-table-column>
        <el-table-column
            label="Id"
            prop="id" sortable>
        </el-table-column>
        <el-table-column
            prop="author"
            label="Author">
        </el-table-column>
        <el-table-column
            prop="topic"
            label="Topic"
            show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="Content">
          <template slot-scope="scope">{{ scope.row.content | ellipsis }}</template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="DeleteAnnouncementVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitDeleteOp">Delete</el-button>
      </div>
    </el-dialog>
    <el-dialog title="Create Announcement" :visible.sync="createAnnouncementVisible">
      <el-form :model="CreateAnnouncementForm">
          <el-form-item label="Topic: " prop="topic" :label-width="formLabelWidth">
            <el-input v-model="CreateAnnouncementForm.topic" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="Content: " prop="content" :label-width="formLabelWidth">
            <el-input v-model="CreateAnnouncementForm.content"
                      type="textarea"
                      :autosize="{ minRows: 2, maxRows: 4}"
                      placeholder="Please input the content"></el-input>
          </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createAnnouncementVisible = false">Cancel</el-button>
        <el-button type="primary" @click="createNotice">Create</el-button>
      </div>
    </el-dialog>
    <el-dialog title="Login" :visible.sync="loginVisible" width="450px">
      <el-form :model="loginFrom" :rules="loginRules" :ref="loginFrom">
        <el-col :span="20">
          <el-form-item label="用户名" prop="name" :label-width="formLabelWidth">
            <el-input v-model="loginFrom.name" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-form-item label="密码" prop="password" :label-width="formLabelWidth">
            <el-input v-model="loginFrom.password" autocomplete="off" show-password></el-input>
          </el-form-item>
        </el-col>
        <el-button type="primary" @click="submit">确 定</el-button>
        <el-button @click="loginVisible = false">取 消</el-button>
      </el-form>
    </el-dialog>
    <el-dialog title="注册" :visible.sync="registerVisible" width="450px">
      <el-form :model="registerForm" :rules="registerRules" :ref="registerForm">
        <el-col :span="20">
          <el-form-item label="用户名" prop="u_name" :label-width="formLabelWidth">
            <el-input v-model="registerForm.u_name" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-form-item label="姓名" prop="name" :label-width="formLabelWidth">
            <el-input v-model="registerForm.name" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-form-item label="密码" prop="password1" :label-width="formLabelWidth">
            <el-input v-model="registerForm.password1" autocomplete="off" show-password></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-form-item label="再次输入密码" prop="password2" :label-width="formLabelWidth">
            <el-input v-model="registerForm.password2" autocomplete="off" show-password></el-input>
          </el-form-item>
        </el-col>
      </el-form>
      <el-button type="primary" @click="handleRegister">确 定</el-button>
      <el-button @click="registerVisible = false">取 消</el-button>
    </el-dialog>
    <el-dialog title="Add Question" :visible.sync="uploadQuestionV">
      <el-form  label-width="200px">
        <el-form-item label="Database type">
          <el-radio-group v-model="dbtype">
            <el-radio label="PostgreSQL">PostgreSQL</el-radio>
            <el-radio label="MySQL">MySQL</el-radio>
            <el-radio label="SQLite">SQLite</el-radio>
            <el-radio label="ALL">ALL</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Difficulty">
          <el-radio-group v-model="difficulty">
            <el-radio label="Low">Low</el-radio>
            <el-radio label="Mid">Mid</el-radio>
            <el-radio label="Hard">Hard</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Question Description(.md): ">
          <el-upload class="upload-demo"
                     ref="upload"
                     :limit=1
                     action="this is fake action"
                     accept=".md"
                     :on-preview="handlePreviewF1"
                     :on-remove="handleRemoveF1"
                     :on-change="handleChangeF1"
                     :on-exceed="handleExceedF1"
                     :file-list="fileListF1"
                     :auto-upload="false">
            <el-button slot="trigger" size="small" type="primary">Select File</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="Standard Answer File(.sql):" prop="files">
          <el-upload class="upload-demo"
                     ref="upload"
                     :limit=1
                     action="this is fake action"
                     accept=".sql"
                     :on-preview="handlePreviewF2"
                     :on-remove="handleRemoveF2"
                     :on-change="handleChangeF2"
                     :on-exceed="handleExceedF2"
                     :file-list="fileListF2"
                     :auto-upload="false">
            <el-button slot="trigger" size="small" type="primary">Select File</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="Testcases Files(.sql): " prop="files">
          <el-upload class="upload-demo"
                     ref="upload"
                     action="this is fake action"
                     accept=".sql"
                     :on-preview="handlePreviewF3"
                     :on-remove="handleRemoveF3"
                     :on-change="handleChangeF3"
                     :file-list="fileListF3"
                     :auto-upload="false">
            <el-button slot="trigger" size="small" type="primary">Select File</el-button>
          </el-upload>
        </el-form-item>
        <div id="tip">Tips: The question description file name is the question name.</div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button class="submit-button" @click="submitUpload()">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import api from '@/views/api'
import qs from 'qs'
import {mapGetters, mapActions} from 'vuex'

export default {
  mounted() {
    window.addEventListener('scroll', this.handleScroll);
    window.addEventListener('resize', () => this.clientWidth = document.body.clientWidth, false);
    if (this.$store.state.myUser.id !== undefined) {
      this.Login_show = false;
    }
  },
  watch: {
    clientWidth(val){
      this.clientWidth = val;
      console.log(this.clientWidth)
    }
  },
  data() {
    var registerPassword = (rule, value, callback) => {
      if (value !== this.registerForm.password1) {
        callback(new Error('两次密码输入不一样！'));
      } else {
        callback();
      }
    };
    return {
      AnnouncementData: [{
        id: 123,
        author: '123',
        topic: '1234',
        content: '123123123123123123123123123123'
      }],
      multipleSelection: [],
      DeleteAnnouncementVisible: false,
      createAnnouncementVisible: false,
      CreateAnnouncementForm:{
        topic: '',
        content: ''
      },
      clientWidth: document.body.clientWidth,
      fileListF1: [],
      fileListF2: [],
      fileListF3: [],
      options: [
        {
          value: 'MySQL',
          label: 'MySQL'
        },
        {
          value: 'PostgreSQL',
          label: 'PostgreSQL'
        },
        {
          value: 'SQLite',
          label: 'SQLite'
        },
        {
          value: 'ALL',
          label: 'ALL'
        }
      ],
      dbtype: '',
      difficulty: '',
      uploadQuestionV: false,
      loginVisible: false,
      registerVisible: false,
      Login_show: true,
      loginFrom: {
        name: '11811805',
        password: '123456'
      },
      registerForm: {
        u_name: '',
        name: '',
        password1: '',
        password2: ''
      },
      formLabelWidth: '120px',
      loginRules: {
        name: [{
          required: true,
          message: "请输入您的用户名即学号",
          trigger: 'change'
        },
          {
            validator(rule, value, callback) {
              if (/(^[1-9]\d*$)/.test(value)) {
                callback()
              } else {
                callback(new Error('请输入正确的用户名'))
              }
            },
            trigger: 'blur'
          }
        ],
        password: [{
          required: true,
          message: "请输入您的密码",
          trigger: 'change'
        }]
      },
      registerRules: {
        u_name: [{
          required: true,
          message: "请输入您的用户名即学号",
          trigger: 'change'
        },
          {
            validator(rule, value, callback) {
              if (/(^[1-9]\d*$)/.test(value)) {
                callback()
              } else {
                callback(new Error('请输入正确的用户名'))
              }
            },
            trigger: 'blur'
          }
        ],
        password1: [{
          required: true,
          message: "请输入您的密码",
          trigger: 'blur'
        }],
        password2: [{
          required: true,
          message: "请再次输入您的密码",
          trigger: 'blur'
        },
          {
            validator: registerPassword,
            message: '密码输入不一致',
            trigger: 'change'
          },
          {
            validator: registerPassword,
            message: '密码输入不一致',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  filters: {
    ellipsis(value){
      if(!value)
        return ''
      if(value.length > 8) {
        return value.slice(0, 8) + '...'
      }else{
        return value
      }
    }
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val;
      console.log(val);
    },
    createAnnouncement(){},
    goHome(){
      this.$router.push('/');
    },
    ...mapActions(['shareUser']),
    submit() {
      let data = {
        username: this.loginFrom.name,
        password: this.loginFrom.password
      }
      api.login(qs.stringify(data)).then(res => {
        console.log(res);
        if (res.error) {
          alert(res.error)
        } else {
          if (res.data === 'Bad credentials') {
            alert("Fail to login, please check your account and password.")
            this.loginFrom.password = '';
          } else {
            this.loginVisible = false;
            this.Login_show = false;
            this.shareUser(res.data)
          }
        }
      })
    },
    handleRegister() {
      let data = {
        username: this.registerForm.u_name,
        password: this.registerForm.password1,
        name: this.registerForm.name,
        role: 'STU'
      }
      api.register(qs.stringify(data)).then(res => {
        if (res.error) {
          alert(res.error)
        } else {
          console.log(res)
          if (res.data === 'Create user successful' && res.status === 200) {
            alert('Create user successfully')
            this.registerVisible = false
          }
        }
      })
    },
    handleCommand(command) {
      if (command === 'a') {
        this.handleLogout();
      }
    },
    handleLogout() {
      api.handleLogout().then(res => {
        this.$store.state.myUser = {}
        alert('Logout sucessfully')
        this.Login_show = true;
      })
    },
    submitUpload() {
      let formdata = new FormData()
      formdata.append("ansFile",this.fileListF2[0].raw)
      formdata.append("questionFile", this.fileListF1[0].raw)
      formdata.append("author", this.$store.state.myUser.id)
      formdata.append("degree", this.difficulty)
      formdata.append("dbType", this.dbtype)
      api.uploadQuestion(formdata).then(res =>{
        console.log(res)
        if(res.data.search('success')!==-1){
          let questionIdList = res.data.match(/\d+/g)
          let questionId = questionIdList[0]
          for(let i = 0;i< this.fileListF3.length;i++){
            let formdata_testcase = new FormData();
            formdata_testcase.append("initFile",this.fileListF3[i].raw)
            formdata_testcase.append("questionId",questionId)
            api.uploadTestcase(formdata_testcase).then(res =>{
              console.log(res)
            })
          }
          alert('add question successfully!')
        }
      })


      console.log(this.dbtype)
    },
    handleRemoveF1(file, fileList) {
      console.log(file, fileList);
    },
    handlePreviewF1(file) {
      console.log(file);
    },
    handleChangeF1(file, fileList) {
      this.fileListF1 = fileList

    },
    handleExceedF1() {
      alert("file number exceed, limit 1")
    }, handleRemoveF2(file, fileList) {
      console.log(file, fileList);
    },
    handlePreviewF2(file) {
      console.log(file);
    },
    handleChangeF2(file, fileList) {
      this.fileListF2 = fileList

    },
    handleExceedF2() {
      alert("file number exceed, limit 1")
    },
    handleRemoveF3(file, fileList) {
      console.log(file, fileList);
    },
    handlePreviewF3(file) {
      console.log(file);
    },
    handleChangeF3(file, fileList) {
      this.fileListF3 = fileList
    },
    createNotice(){
      let data = {
        author: this.$store.state.myUser.username,
        content: this.CreateAnnouncementForm.content,
        topic:this.CreateAnnouncementForm.topic
      }
      api.uploadNotice(qs.stringify(data)).then(res =>{

        if (res.data ==='upload successful'){

          alert('Upload Successfully!')
        }
      })
    },
    getNotices(){
      api.getAllNotice().then(res =>{
        if(res.status === 200){
          this.AnnouncementData = []
          for(let i = 0; i < res.data.length ; i++){
            let unitForm = {
              id: res.data[i].id,
              author: res.data[i].author.name,
              topic:res.data[i].topic,
              content:res.data[i].content
            }
            this.AnnouncementData.push(unitForm)
          }
        }
        console.log(res)
      })
    },
    handleDeleteNotice(){
      this.getNotices();
      this.DeleteAnnouncementVisible = true
    },
    submitDeleteOp(){
      for(let i = 0; i < this.multipleSelection.length;i++){
        api.deleteNotice(qs.stringify({Id:this.multipleSelection[i].id}))
      }
      alert('Deleted')

    }
  },
  components: {},
}
</script>

<style>
#header {
  min-width: 1100px;
  position: fixed;
  top: 0;
  left: 0;
  height: 60px;
  width: 100%;
  z-index: 1000;
  background-size: 100% 15px;
  padding: 0;
  /*box-shadow: 0 8px 5px 0 rgba(0, 0, 0, 0.1);*/
}

.logo {
  font-size: 20px;
  float: left;
  margin-top: 9px;
  line-height: 60px;
  background-image: url(../assets/dblogo.png);
  width: 138px;
  height: 43px;
  background-size: cover;
}

#tip{
  color: #dca32e;
}
</style>
