<template>
  <div>
    <div id='header'>
      <el-menu theme="light" mode="horizontal" class="oj-menu" style="background-color: transparent;">
        <div id="logo"><span>DBOJ</span></div>
        <router-link to="/" tag="el-menu-item">
          <i class="el-icon-s-home"></i>
          <span style="color: black">Home</span>
        </router-link>
        <router-link to="/contest" tag="el-menu-item">
          <i class="el-icon-s-opportunity"></i>
          <span style="color: black">Contests</span>
        </router-link>
        <router-link to="/main" tag="el-menu-item" id='que'>
          <i class="el-icon-edit"></i>
          <span style="color: black">Questions</span>
        </router-link>
        <router-link to="/forum" tag="el-menu-item">
          <i class="el-icon-chat-line-round"></i>
          <span style="color: black">Forum</span>
        </router-link>
        <el-submenu name="about" index='4'>
          <template slot='title'>
            <i class="el-icon-info"></i>
            <span style="color: black">About</span>
          </template>
          <el-menu-item name="/about" index="1">
          </el-menu-item>
          <el-menu-item name="/FAQ" index="2">
          </el-menu-item>
        </el-submenu>
        <router-link to="/adminMain" tag="el-menu-item"
                     v-if="this.$store.state.myUser.role === 'ROLE_SA' || this.$store.state.myUser.role === 'ROLE_TA'">
          <i class="el-icon-s-data"></i>
          <span style="color: black">Admin</span>
        </router-link>
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
  </div>
</template>

<script>
import api from '@/views/api'
import qs from 'qs'
import {mapGetters, mapActions} from 'vuex'

export default {
  mounted() {
    window.addEventListener('scroll', this.handleScroll);
    if (this.$store.state.myUser.id !== undefined) {
      this.Login_show = false;
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
      url: require('../assets/database_logo.jpg'),
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
  methods: {
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
  /*box-shadow: 0 8px 5px 0 rgba(0, 0, 0, 0.1);*/
}

#logo {
  margin-left: 2%;
  margin-right: 2%;
  font-size: 20px;
  float: left;
  line-height: 60px;
}
</style>
