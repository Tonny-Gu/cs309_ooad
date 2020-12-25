<template>
  <div>
    <div id="header">
      <el-menu
        theme="light"
        mode="horizontal"
        class="oj-menu"
        style="background-color: transparent"
      >
        <div class="logo" v-if="this.clientWidth > 752" @click="goHome()"></div>
        <el-menu-item
          @click="$router.push({ path: '/' })"
          v-if="this.clientWidth > 752"
          index="1"
        >
          <template slot="title">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-icon-lifang"></use>
            </svg>
            <span style="color: black"> Home </span>
          </template>
        </el-menu-item>

        <el-menu-item
          @click="$router.push({ path: '/contest' })"
          v-if="this.clientWidth > 752"
          index="2"
          :disabled="this.$store.state.myUser.id === undefined"
        >
          <template slot="title">
            <span style="color: black">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-shiyanshi"></use>
              </svg>

              Contests</span
            >
          </template>
        </el-menu-item>

        <el-menu-item
          @click="$router.push({ path: '/main' })"
          v-if="this.clientWidth > 752"
          index="3"
        >
          <template slot="title">
            <span style="color: black">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-icon-sousuo"></use>
              </svg>

              Questions</span
            >
          </template>
        </el-menu-item>

        <el-submenu name="about" index="4" v-if="this.clientWidth > 752">
          <template slot="title">
            <span style="color: black">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-icon-yichang"></use>
              </svg>

              About</span
            >
          </template>
          <el-menu-item name="/about" index="4-1"> </el-menu-item>
          <el-menu-item name="/FAQ" index="4-2"> </el-menu-item>
        </el-submenu>

        <el-submenu
          name="admin"
          v-if="
            (this.$store.state.myUser.role === 'ROLE_SA' ||
              this.$store.state.myUser.role === 'ROLE_TA') &&
            this.clientWidth > 752
          "
          index="5"
        >
          <template slot="title"
            ><span style="color: black">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-icon-fuwuqijiankong"></use>
              </svg>

              Admin</span
            ></template
          >
          <el-menu-item
            @click="handleUploadQuestion"
            style="color: black"
            index="5-1"
          >
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-tianjia2"></use>
            </svg>
            Upload question</el-menu-item
          >

          <el-menu-item
            @click="$router.push({ path: '/adminMain/ContestControl' })"
            v-if="this.clientWidth > 752"
            index="5-2"
          >
            <template slot="title">
              <span style="color: black">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-bianji"></use>
                </svg>

                Contest Control</span
              >
            </template>
          </el-menu-item>

          <el-menu-item
            @click="$router.push({ path: '/adminMain/submissionRecord' })"
            v-if="this.clientWidth > 752"
            index="5-3"
          >
            <template slot="title">
              <span style="color: black">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-sousuo"></use>
                </svg>

                Submission Records</span
              >
            </template>
          </el-menu-item>

          <el-submenu name="AnnouncementManagement" index="5-4">
            <template slot="title"
              ><span style="color: black">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-A"></use>
                </svg>

                Announcement Control</span
              ></template
            >
            <el-menu-item
              @click="handleCreateAnnouncement"
              style="color: black"
            >
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-icon-test1"></use>
              </svg>

              Create Announcement</el-menu-item
            >
            <el-menu-item @click="handleDeleteNotice" style="color: black">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-icon-test"></use>
              </svg>
              Delete Announcement</el-menu-item
            >
          </el-submenu>
          <el-menu-item
            style="color: black"
            v-if="this.$store.state.myUser.role === 'ROLE_TA'"
            @click="handleModifyPermission"
          >
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-icon-test1"></use>
            </svg>
            Modify Permissions
          </el-menu-item>
          
        </el-submenu>
        <el-submenu
          name="allMenu"
          v-if="this.clientWidth < 752"
          index="2"
          :style="'margin-left:' + (this.clientWidth / 2 - 138 / 2) + 'px;'"
        >
          <template slot="title"><div class="logo"></div></template>
          <router-link to="/" tag="el-menu-item">
            <span style="color: black">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-icon-lifang"></use>
              </svg>

              Home</span
            >
          </router-link>
          <router-link to="/contest" tag="el-menu-item">
            <span style="color: black">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-shiyanshi"></use>
              </svg>

              Contests</span
            >
          </router-link>
          <router-link to="/main" tag="el-menu-item" id="que">
            <span style="color: black">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-icon-sousuo"></use>
              </svg>

              Questions</span
            >
          </router-link>
          <el-submenu name="about" index="4">
            <template slot="title">
              <span style="color: black">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-icon-yichang"></use>
                </svg>

                About</span
              >
            </template>
            <el-menu-item name="/about" index="4-1"> </el-menu-item>
            <el-menu-item name="/FAQ" index="4-2"> </el-menu-item>
          </el-submenu>
          <el-submenu
            name="admin"
            v-if="
              this.$store.state.myUser.role === 'ROLE_SA' ||
              this.$store.state.myUser.role === 'ROLE_TA'
            "
            index="1"
          >
            <template slot="title"
              ><span style="color: black">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-icon-fuwuqijiankong"></use>
                </svg>

                Admin</span
              ></template
            >
            <el-menu-item
              @click="handleUploadQuestion"
              style="color: black"
              index="1"
            >
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-tianjia2"></use>
              </svg>
              Upload question</el-menu-item
            >
            <router-link to="/adminMain/createContest" tag="el-menu-item">
              <span style="color: black">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-bianji"></use>
                </svg>

                Create contest</span
              >
            </router-link>
            <router-link to="/adminMain/submissionRecord" tag="el-menu-item">
              <span style="color: black">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-sousuo"></use>
                </svg>

                Submission Records</span
              >
            </router-link>
            <el-submenu name="AnnouncementManagement" index="1-2">
              <template slot="title"
                ><span style="color: black">
                  <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-A"></use>
                  </svg>

                  Announcement Control</span
                ></template
              >
              <el-menu-item
                @click="handleCreateAnnouncement"
                style="color: black"
              >
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-icon-test1"></use>
                </svg>

                Create Announcement</el-menu-item
              >
              <el-menu-item @click="handleDeleteNotice" style="color: black">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-icon-test"></use>
                </svg>
                Delete Announcement</el-menu-item
              >
            </el-submenu>
            <el-menu-item
              style="color: black"
              v-if="this.$store.state.myUser.role === 'ROLE_TA'"
              @click="handleModifyPermission"
            >
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-icon-test1"></use>
              </svg>
              Modify Permissions
            </el-menu-item>
            
          </el-submenu>
          <template v-if="Login_show">
            <el-button
              round
              style="float: right; margin-right: 10px; margin-top: 13px"
              @click="loginVisible = true"
              >Login
            </el-button>
            <el-button
              round
              style="float: right; margin-right: 10px; margin-top: 13px"
              @click="registerVisible = true"
            >
              Register
            </el-button>
          </template>
          <template v-else>
            <div
              style="
                float: right;
                margin-right: 5%;
                font-size: 30px;
                margin-top: 13px;
              "
            >
              <svg class="iconRole" aria-hidden="true">
                <use xlink:href="#icon-icon-quanxian"></use>
              </svg>

              <el-dropdown @command="handleCommand">
                <span class="el-dropdown-link">
                  <i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="a">
                    <svg class="icon" aria-hidden="true">
                      <use xlink:href="#icon-SignOut"></use>
                    </svg>
                    Log out</el-dropdown-item
                  >
                  <el-dropdown-item command="b">
                    <svg class="icon" aria-hidden="true">
                      <use xlink:href="#icon-mima1"></use>
                    </svg>

                    Change Password</el-dropdown-item
                  >
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </template>
        </el-submenu>
        <template v-if="Login_show">
          <el-button
            round
            style="float: right; margin-right: 10px; margin-top: 13px"
            @click="loginVisible = true"
            >Login
          </el-button>
          <el-button
            round
            style="float: right; margin-right: 10px; margin-top: 13px"
            @click="registerVisible = true"
          >
            Register
          </el-button>
        </template>
        <template v-else>
          <div
            style="
              float: right;
              margin-right: 5%;
              font-size: 30px;
              margin-top: 13px;
            "
          >
            <svg class="iconRole" aria-hidden="true">
              <use xlink:href="#icon-icon-quanxian"></use>
            </svg>

            <el-dropdown @command="handleCommand">
              <span class="el-dropdown-link">
                <i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="a">
                  <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-SignOut"></use>
                  </svg>
                  Log out</el-dropdown-item
                >
                <el-dropdown-item command="b">
                  <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-mima1"></use>
                  </svg>

                  Change Password</el-dropdown-item
                >
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </template>
      </el-menu>
    </div>
    <el-dialog
      title="Delete Announcement"
      :visible.sync="DeleteAnnouncementVisible"
    >
      <el-table
        ref="multipleTable"
        :data="AnnouncementData"
        tooltip-effect="dark"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection"> </el-table-column>
        <el-table-column label="Id" prop="id" sortable> </el-table-column>
        <el-table-column prop="author" label="Author"> </el-table-column>
        <el-table-column prop="topic" label="Topic" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="Content">
          <template slot-scope="scope">{{
            scope.row.content | ellipsis
          }}</template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="DeleteAnnouncementVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitDeleteOp">Delete</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="Create Announcement"
      :visible.sync="createAnnouncementVisible"
    >
      <el-form :model="CreateAnnouncementForm">
        <el-form-item
          label="Topic: "
          prop="topic"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model="CreateAnnouncementForm.topic"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="Content: "
          prop="content"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model="CreateAnnouncementForm.content"
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4 }"
            placeholder="Please input the content"
          ></el-input>
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
          <el-form-item
            label="Username"
            prop="name"
            :label-width="formLabelWidth"
          >
            <el-input v-model="loginFrom.name" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-form-item
            label="Password"
            prop="password"
            :label-width="formLabelWidth"
          >
            <el-input
              v-model="loginFrom.password"
              autocomplete="off"
              show-password
            ></el-input>
            <el-link
              type="info"
              @click="handleForget"
              style="float: right; font-size: 10px"
              :underline="false"
              >Forget Password?</el-link
            >
          </el-form-item>
        </el-col>
        <el-button type="primary" @click="submit">Confirm</el-button>
        <el-button @click="loginVisible = false">Cancel</el-button>
      </el-form>
    </el-dialog>
    <el-dialog title="Sign UP" :visible.sync="registerVisible" width="450px">
      <el-form :model="registerForm" :rules="registerRules" :ref="registerForm">
        <el-col :span="20">
          <el-form-item label="SID" prop="u_name" :label-width="formLabelWidth">
            <el-input
              v-model="registerForm.u_name"
              autocomplete="off"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-form-item
            label="Nick Name"
            prop="name"
            :label-width="formLabelWidth"
          >
            <el-input v-model="registerForm.name" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-form-item
            label="Password"
            prop="password1"
            :label-width="formLabelWidth"
          >
            <el-input
              v-model="registerForm.password1"
              autocomplete="off"
              show-password
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-form-item
            label="Repeat"
            prop="password2"
            :label-width="formLabelWidth"
          >
            <el-input
              v-model="registerForm.password2"
              autocomplete="off"
              show-password
            ></el-input>
          </el-form-item>
        </el-col>
      </el-form>
      <el-button type="primary" @click="handleRegister">Confirm</el-button>
      <el-button @click="registerVisible = false">Concel</el-button>
    </el-dialog>
    <el-dialog
      title="Change Password"
      :visible.sync="changePWVisible"
      width="450px"
    >
      <el-form :model="changePWForm" :rules="changePWrules" :ref="changePWForm">
        <el-col :span="20">
          <el-form-item label="SID" prop="sid" :label-width="formLabelWidth">
            <el-input v-model="changePWForm.sid" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-form-item
            label="Code"
            prop="frontcode"
            :label-width="formLabelWidth"
          >
            <el-input
              v-model="changePWForm.frontcode"
              autocomplete="off"
              style="width: 40%; float: left; margin-right: 5%"
            ></el-input>
            <vue-img-verify
              @getImgCode="getImgCode"
              ref="vueImgVerify"
              style="width: 30%; float: left"
            />
          </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-form-item
            label="Mail Code"
            prop="code"
            :label-width="formLabelWidth"
          >
            <el-input
              v-model="changePWForm.code"
              autocomplete="off"
              style="float: left; width: 65%; margin-right: 10px"
            ></el-input>
            <el-button
              type="warning"
              icon="el-icon-message"
              @click="sendEmail"
              round
              style="float: left"
              :disabled="disableMailCode"
            ></el-button>
          </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-form-item
            label="New Password"
            prop="newpassword"
            :label-width="formLabelWidth"
          >
            <el-input
              v-model="changePWForm.password"
              autocomplete="off"
              show-password
            ></el-input>
          </el-form-item>
        </el-col>
        <el-button type="primary" @click="submitChangePW">Confirm</el-button>
        <el-button @click="changePWVisible = false">Cancel</el-button>
      </el-form>
    </el-dialog>
    <el-dialog title="Add Question" :visible.sync="uploadQuestionV">
      <el-form label-width="200px">
        <el-form-item label="Database type">
          <el-radio-group v-model="dbtype">
            <el-radio label="PostgreSQL">PostgreSQL</el-radio>
            <el-radio label="MySQL">MySQL</el-radio>
            <el-radio label="SQLite">SQLite</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Difficulty">
          <el-radio-group v-model="difficulty">
            <el-radio label="Easy">Easy</el-radio>
            <el-radio label="Mid">Mid</el-radio>
            <el-radio label="Hard">Hard</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Question Description(.md): ">
          <el-upload
            class="upload-demo"
            ref="upload"
            :limit="1"
            action="this is fake action"
            accept=".md"
            :on-preview="handlePreviewF1"
            :on-remove="handleRemoveF1"
            :on-change="handleChangeF1"
            :on-exceed="handleExceedF1"
            :file-list="fileListF1"
            :auto-upload="false"
          >
            <el-button slot="trigger" size="small" type="primary"
              >Select File</el-button
            >
          </el-upload>
        </el-form-item>
        <el-form-item label="Standard Answer File(.sql):" prop="files">
          <el-upload
            class="upload-demo"
            ref="upload"
            :limit="1"
            action="this is fake action"
            accept=".sql;.sqlite"
            :on-preview="handlePreviewF2"
            :on-remove="handleRemoveF2"
            :on-change="handleChangeF2"
            :on-exceed="handleExceedF2"
            :file-list="fileListF2"
            :auto-upload="false"
          >
            <el-button slot="trigger" size="small" type="primary"
              >Select File</el-button
            >
          </el-upload>
        </el-form-item>
        <el-form-item label="Testcases Files(.sql): " prop="files">
          <el-upload
            class="upload-demo"
            ref="upload"
            action="this is fake action"
            accept=".sql;.sqlite"
            :on-preview="handlePreviewF3"
            :on-remove="handleRemoveF3"
            :on-change="handleChangeF3"
            :file-list="fileListF3"
            :auto-upload="false"
          >
            <el-button slot="trigger" size="small" type="primary"
              >Select File</el-button
            >
          </el-upload>
        </el-form-item>
        <el-form-item label="Extension File(.py): " prop="files">
          <el-upload
            class="upload-demo"
            ref="upload"
            action="this is fake action"
            accept=".py"
            :on-preview="handlePreviewF4"
            :on-remove="handleRemoveF4"
            :on-change="handleChangeF4"
            :file-list="fileListF4"
            :auto-upload="false"
          >
            <el-button slot="trigger" size="small" type="primary"
              >Select File</el-button
            >
          </el-upload>
        </el-form-item>
        <div id="tip">
          Tips: The question description file name is the question name.
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button class="submit-button" @click="submitUpload()"
          >Upload</el-button
        >
      </div>
    </el-dialog>
    <el-dialog
      title="Modify Permission"
      :visible.sync="changePermissionVS"
      width="450px"
    >
      <el-form :model="ModifyPermission">
        <el-form-item label="SID: " :label-width="formLabelWidth">
          <el-input v-model="ModifyPermission.SID"></el-input>
        </el-form-item>
        <el-form-item
          label="Permission: "
          prop="permission"
          :label-width="formLabelWidth"
        >
          <el-radio-group v-model="StudentPermission">
            <el-radio label="TA">TA</el-radio>
            <el-radio label="SA">SA</el-radio>
            <el-radio label="Student">Student</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-button class="submit-button" @click="submitModifyPermission()"
          >Submit</el-button
        >
        <el-button class="submit-button" @click="changePermissionVS = false"
          >Cancel</el-button
        >
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/views/api";
import qs from "qs";
import { mapGetters, mapActions } from "vuex";
import vueImgVerify from "./vue-img-verify";
import iconfont from "../assets/icons/iconfont";
export default {
  mounted() {
    window.addEventListener("scroll", this.handleScroll);
    window.addEventListener(
      "resize",
      () => (this.clientWidth = document.body.clientWidth),
      false
    );
    if (this.$store.state.myUser.id !== undefined) {
      this.Login_show = false;
    }
  },
  watch: {
    clientWidth(val) {
      this.clientWidth = val;
    },
  },
  data() {
    var registerPassword = (rule, value, callback) => {
      if (value !== this.registerForm.password1) {
        callback(new Error("两次密码输入不一样！"));
      } else {
        callback();
      }
    };
    return {
      StudentsTranscript:[],
      TranscriptV: false,
      ModifyPermission: {
        SID: null,
      },
      StudentPermission: "",
      series: [14, 23, 21, 17, 15, 10, 12, 17, 21],
      chartOptions: {
        chart: {
          type: "polarArea",
        },
        stroke: {
          colors: ["#fff"],
        },
        fill: {
          opacity: 0.8,
        },
        responsive: [
          {
            breakpoint: 480,
            options: {
              chart: {
                width: 200,
              },
              legend: {
                position: "bottom",
              },
            },
          },
        ],
      },
      AnnouncementData: [],
      multipleSelection: [],
      disableMailCode: false,
      DeleteAnnouncementVisible: false,
      createAnnouncementVisible: false,
      CreateAnnouncementForm: {
        topic: "",
        content: "",
      },
      clientWidth: document.body.clientWidth,
      fileListF1: [],
      fileListF2: [],
      fileListF3: [],
      fileListF4: [],
      options: [
        {
          value: "MySQL",
          label: "MySQL",
        },
        {
          value: "PostgreSQL",
          label: "PostgreSQL",
        },
        {
          value: "SQLite",
          label: "SQLite",
        },
        {
          value: "ALL",
          label: "ALL",
        },
      ],
      changePermissionVS: false,
      dbtype: "",
      difficulty: "",
      uploadQuestionV: false,
      loginVisible: false,
      registerVisible: false,
      changePWVisible: false,
      Login_show: true,
      loginFrom: {
        name: "11811805",
        password: "123456",
      },
      registerForm: {
        u_name: "",
        name: "",
        password1: "",
        password2: "",
      },
      changePWForm: {
        sid: "",
        code: "",
        password: "",
        frontcode: "",
      },
      formLabelWidth: "120px",
      loginRules: {
        name: [
          {
            required: true,
            message: "请输入您的用户名即学号",
            trigger: "change",
          },
          {
            validator(rule, value, callback) {
            },
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            trigger: "change",
          },
        ],
      },
      registerRules: {
        u_name: [
          {
            required: true,
            message: "请输入您的用户名即学号",
            trigger: "change",
          },
          {
            validator(rule, value, callback) {
            
            },
            trigger: "blur",
          },
        ],
        password1: [
          {
            required: true,
            message: "请输入您的密码",
            trigger: "blur",
          },
        ],
        password2: [
          {
            required: true,
            message: "请再次输入您的密码",
            trigger: "blur",
          },
          {
            validator: registerPassword,
            message: "密码输入不一致",
            trigger: "change",
          },
          {
            validator: registerPassword,
            message: "密码输入不一致",
            trigger: "blur",
          },
        ],
      },
      changePWrules: {
        newpassword: [
          {
            required: true,
            message: "Please input new password",
            trigger: "blur",
          },
        ],
        frontcode: [
          {
            required: true,
            message: "Please input verification code",
            trigger: "blur",
          },
        ],
        code: [
          {
            required: true,
            message: "Please input mail code",
            trigger: "blur",
          },
        ],
        sid: [
          {
            required: true,
            message: "Please Input your SID",
            trigger: "blur",
          },
          {
            validator(rule, value, callback) {
             
            },
            trigger: "blur",
          },
        ],
      },
    };
  },
  filters: {
    ellipsis(value) {
      if (!value) return "";
      if (value.length > 8) {
        return value.slice(0, 8) + "...";
      } else {
        return value;
      }
    },
  },
  methods: {
    handleTranscript() {
      this.TranscriptV = true;
    },
    handleModifyPermission() {
      this.changePermissionVS = true;
      this.ModifyPermission = { SID: null };
      this.StudentPermission = "";
    },
    submitModifyPermission() {
      let data = {
        role: this.StudentPermission,
        username: this.ModifyPermission.SID,
      };
      api.changePermission(qs.stringify(data)).then((res) => {
        if (res.data === "Modify Success") {
          this.$message({
            message: "Modify Success",
            type: "success",
          });
        } else {
          this.$message.error("Modify Failed Try it again.");
        }
      });
    },
    handleCreateAnnouncement() {
      this.createAnnouncementVisible = true;
      this.CreateAnnouncementForm = {};
    },
    handleUploadQuestion() {
      this.uploadQuestionV = true;
      this.dbtype = "";
      this.difficulty = "";
      this.fileListF4 =[];
      this.fileListF3 = [];
      this.fileListF2 = [];
      this.fileListF1 = [];
    },
    handleForget() {
      this.loginVisible = false;
      this.changePWVisible = true;
      this.handleChangePassword();
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    createAnnouncement() {},
    goHome() {
      this.$router.push("/");
    },
    getImgCode(code) {
      this.imgCode = code;
    },
    // 点击按钮获取验证码
    handleClick() {
      this.imgCode = this.$refs.vueImgVerify.methods.draw();
    },
    ...mapActions(["shareUser"]),
    submit() {
      let data = {
        username: this.loginFrom.name,
        password: this.loginFrom.password,
      };
      api.login(qs.stringify(data)).then((res) => {
        if (res.error) {
          this.$message.erro(res.error);
        } else {
          if (res.data === "Bad credentials") {
            this.$message.error(
              "Fail to login, please check your account and password."
            );
            this.loginFrom.password = "";
          } else {
            this.$message({
              message: "Login Successfully!",
              type: "success",
            });
            this.loginVisible = false;
            this.Login_show = false;
            this.shareUser(res.data);
          }
        }
      });
    },
    handleRegister() {
      let data = {
        username: this.registerForm.u_name,
        password: this.registerForm.password1,
        name: this.registerForm.name,
        role: "STU",
      };
      api.register(qs.stringify(data)).then((res) => {
        if (res.error) {
          this.$message.erro("Sign up failed.");
        } else {
          if (res.data === "Create user successful" && res.status === 200) {
            this.$message({
              message: "Sign up successfully!",
              type: "success",
            });
            this.registerVisible = false;
          }
        }
      });
    },
    handleCommand(command) {
      if (command === "a") {
        this.handleLogout();
      }
      if (command === "b") {
        this.handleChangePassword();
      }
    },
    handleLogout() {
      api.handleLogout().then((res) => {
        if (res.data === "log out successful")
          this.$message({
            message: "See ya~",
            type: "success",
          });
        this.$store.state.myUser = {};
        this.Login_show = true;
        this.$router.push("/");
      });
    },
    handleChangePassword() {
      // this.handleClick()
      this.disableMailCode = false;
      this.changePWVisible = true;
    },
    sendEmail() {
      if (this.imgCode === this.changePWForm.frontcode) {
        let data = {
          username: this.changePWForm.sid,
        };
        api.sendCode(qs.stringify(data)).then((res) => {
          this.disableMailCode = true;
          if (res.data === "Sender Success") {
            this.$message({
              message: "Check your shchool Email",
              type: "success",
            });
            this.disableMailCode = true;
          } else {
            this.$message.error("Something Wrong, Try it again!");
          }
        });
      } else {
        alert("Verification Code error!");
      }
    },
    confirmAlert(){
      this.$alert('Are you sure to submit', 'Confirm', {
          confirmButtonText: '确定',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${ action }`
            });
          }
        });
    },
    submitUpload() {
       
      let formdata = new FormData();
      formdata.append("ansFile", this.fileListF2[0].raw);
      formdata.append("questionFile", this.fileListF1[0].raw);
      formdata.append("author", this.$store.state.myUser.id);
      formdata.append("degree", this.difficulty);
      formdata.append("dbType", this.dbtype);
      if (this.fileListF4.length > 0) {
        formdata.append("extenFile", this.fileListF4[0].raw);
      }
      api.uploadQuestion(formdata).then((res) => {
        if (res.data.search("success") !== -1) {
          let questionIdList = res.data.match(/\d+/g);
          let questionId = questionIdList[0];
          let flag = 0;
          for (let i = 0; i < this.fileListF3.length; i++) {
            let formdata_testcase = new FormData();
            formdata_testcase.append("initFile", this.fileListF3[i].raw);
            formdata_testcase.append("questionId", questionId);
            api.uploadTestcase(formdata_testcase).then((res) => {
              if (res.data.search("success") === -1) {
                flag = 1;
              }
            });
          }
          if (flag === 1) {
            this.$message.error("upload testcase fail.Please Contect Admin");
          } else {
            this.$message({
              message: "Upload question success",
              type: "success",
            });
          }
        }else{
          this.$message.error(res.data);
        }
      });
    },
    handleRemoveF1(file, fileList) {
    },
    handlePreviewF1(file) {
    },
    handleChangeF1(file, fileList) {
      this.fileListF1 = fileList;
    },
    handleExceedF1() {
      alert("file number exceed, limit 1");
    },
    handleRemoveF2(file, fileList) {
    },
    handlePreviewF2(file) {
    },
    handleChangeF2(file, fileList) {
      this.fileListF2 = fileList;
    },
    handleExceedF2() {
      alert("file number exceed, limit 1");
    },
    handleRemoveF3(file, fileList) {
    },
    handlePreviewF3(file) {
    },
    handleChangeF3(file, fileList) {
      this.fileListF3 = fileList;
    },
    handleRemoveF4(file, fileList) {
    },
    handlePreviewF4(file) {
    },
    handleChangeF4(file, fileList) {
      this.fileListF4 = fileList;
    },
    createNotice() {
      let data = {
        author: this.$store.state.myUser.username,
        content: this.CreateAnnouncementForm.content,
        topic: this.CreateAnnouncementForm.topic,
      };
      api.uploadNotice(qs.stringify(data)).then((res) => {
        if (res.data === "upload successful") {
          this.$message({
            message:"upload successfu",
            type:"success"
          })
        }else{
           this.$message.error("Something goes wrong")
        }
      });
      this.createAnnouncementVisible = false;
    },
    getNotices() {
      api.getAllNotice().then((res) => {
        if (res.status === 200) {
          this.AnnouncementData = [];
          for (let i = 0; i < res.data.length; i++) {
            let unitForm = {
              id: res.data[i].id,
              author: res.data[i].author.name,
              topic: res.data[i].topic,
              content: res.data[i].content,
            };
            this.AnnouncementData.push(unitForm);
          }
        }
      });
    },
    handleDeleteNotice() {
      this.getNotices();
      this.DeleteAnnouncementVisible = true;
    },
    submitDeleteOp() {
      for (let i = 0; i < this.multipleSelection.length; i++) {
        api.deleteNotice(qs.stringify({ Id: this.multipleSelection[i].id }));
      }
      alert("Deleted");
      this.AnnouncementData = [];
      this.getNotices();
    },
    submitChangePW() {
      let data = {
        code: this.changePWForm.code,
        password: this.changePWForm.password,
        username: this.changePWForm.sid,
      };
      api.changePassword(qs.stringify(data)).then((res) => {
        if (res.data === "Modify Success") {
          this.changePWForm = {};
          this.$message({
            message:"Modify Success",
            type:"success"
          })
          this.changePWVisible = false;
        }else{
          this.$message.error("Something goes wrong")
        }
      });
    },
  },
  components: {
    vueImgVerify,
  },
};
</script>

<style>
.icon {
  width: 1.8em;
  height: 1.8em;
  fill: currentColor;
  overflow: hidden;
}

.iconRole {
  width: 1.2em;
  height: 1.2em;
  fill: currentColor;
  overflow: hidden;
}

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

#tip {
  color: #dca32e;
}
</style>
