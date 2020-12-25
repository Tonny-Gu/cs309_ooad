<template>
  <div id="ContestMenu">
    <div
      id="Annoucement"
      :style="'margin-top: ' + clientHeight / 100 + 'px'"
      v-if="this.clientWidth >= 1094"
    >
      <el-carousel :interval="6000" height="150px" id="annoucementText">
        <el-carousel-item v-for="item in annoucement" :key="item">
          <h3 class="medium" style="word-wrap: break-word" v-html="item"></h3>
        </el-carousel-item>
      </el-carousel>
    </div>
    <div
      id="Annoucement1"
      :style="'margin-top: ' + clientHeight / 100 + 'px'"
      v-else
    >
      <el-carousel :interval="6000" height="150px" id="annoucementText">
        <el-carousel-item v-for="item in annoucement" :key="item">
          <h3 class="medium" style="word-wrap: break-word" v-html="item"></h3>
        </el-carousel-item>
      </el-carousel>
    </div>
    <div
      v-if="this.clientWidth >= 1094"
      id="table-content"
      :style="
        'height: ' +
        ((clientHeight * 99) / 100 - 230) +
        'px;padding:0;margin-bottom: 0; margin-top: 0; top: 0; bottom: 0; overflow: auto;'
      "
    >
      <div :style="'height: ' + tableHeight + 'px; padding: 0; margin: 0;'">
        <el-table :data="pageList" stripe style="width: 100%; top: 20px">
          <el-table-column
            prop="contestId"
            label="Contest Id"
            width="90px"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="contestName"
            label="Contest Name"
            align="center"
          >
            <template slot-scope="scope">
              <div>{{ scope.row.contestTitle }}</div>
            </template>
          </el-table-column>
          <el-table-column
            prop="StartTime"
            label="Start Time"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="EndTime"
            label="End Time"
            align="center"
          ></el-table-column>
          <el-table-column label="Access" align="center"
            ><template slot-scope="scope">
              <el-button
                @click="accessAnswerPage(scope.row.contestId)"
                type="primary"
                v-if="
                  participatedContestId.indexOf(scope.row.contestId) > -1 &&
                  Object.keys($store.state.myUser).length !== 0
                "
                style="background-color: #67c23a; width: 100px"
                size="small"
                :autofocus="true"
                >Enter</el-button
              >
              <el-button
                @click="handleClick(scope.row)"
                type="primary"
                size="small"
                v-else
                style="width: 100px"
                >Join</el-button
              >
            </template></el-table-column
          >
        </el-table>
      </div>
      <div id="block">
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
    <div
      v-if="this.clientWidth < 1094"
      id="table-content1"
      :style="
        'height: ' +
        ((clientHeight * 99) / 100 - 230) +
        'px;padding:0;margin-bottom: 0; margin-top: 0; top: 0; bottom: 0; overflow: auto;width: 100%;'
      "
    >
      <div :style="'height: ' + tableHeight + 'px; padding: 0; margin: 0;'">
        <el-table
          :data="pageList"
          stripe
          style="width: 100%; top: 20px"
          :default-sort="{ prop: 'date', order: 'descending' }"
        >
          <el-table-column
            prop="contestId"
            label="Contest Id"
            width="90px"
            align="center"
            sortable
          ></el-table-column>
          <el-table-column
            prop="contestName"
            label="Contest Name"
            align="center"
          >
            <template slot-scope="scope">
              <div>{{ scope.row.contestTitle }}</div>
            </template>
          </el-table-column>
          <el-table-column
            prop="StartTime"
            label="Start Time"
            align="center"
            sortable
          ></el-table-column>
          <el-table-column
            prop="EndTime"
            label="End Time"
            align="center"
            sortable
          ></el-table-column>
          <el-table-column label="Access" align="center"
            ><template slot-scope="scope">
              <el-button
                @click="accessAnswerPage(scope.row.contestId)"
                type="primary"
                v-if="
                  participatedContestId.indexOf(scope.row.contestId) > -1 &&
                  Object.keys($store.state.myUser).length !== 0
                "
                style="background-color: #67c23a; width: 100px"
                size="small"
                :autofocus="true"
                >Enter</el-button
              >
              <el-button
                @click="handleClick(scope.row)"
                type="primary"
                size="small"
                v-else
                style="width: 100px"
                >Join</el-button
              >
            </template></el-table-column
          >
        </el-table>
      </div>
      <div id="block" v-if="!mobileFlag">
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
    <div class="selfInfo" v-if="this.clientWidth >= 1094">
      <br />
      <div
        style="
          text-align: left;
          margin-left: 5%;
          font-family: 'Microsoft JhengHei';
        "
      >
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-sharpicons_id-card"></use>
        </svg>
        SID:{{ this.$store.state.myUser.username }}
      </div>
      <br />
      <div
        style="
          text-align: left;
          margin-left: 5%;
          font-family: 'Microsoft JhengHei';
        "
      >
        <svg class="icon" aria-hidden="true" @click="showChangeNickname">
          <use xlink:href="#icon-biji"></use>
        </svg>
        Nickname: {{ this.$store.state.myUser.nickname }}
      </div>
      <br />
      <div
        style="
          text-align: left;
          margin-left: 5%;
          font-family: 'Microsoft JhengHei';
        "
      >
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-Bookmark"></use>
        </svg>
        Role:
        <el-tag v-if="this.$store.state.myUser.id !== undefined">{{
          this.$store.state.myUser.role
        }}</el-tag>
      </div>

      <br />
      <el-button
        type="primary"
        style="
          background-color: green;
          width: 80%;
          padding: 0;
          padding-top: 15px;
          padding-bottom: 15px;
        "
        @click="getMySubmission"
        >Submission Record</el-button
      >
      <br />
      <br />
      <br />
      <div id="chart">
        <apexchart
          type="radar"
          height="350"
          :options="chartOptions"
          :series="series"
        ></apexchart>
      </div>
      <!--			<div id='SubmitTime' style="width: 100%;height: 40%;"></div>-->
    </div>

    <el-dialog
      title="Submit Record"
      :visible.sync="SubmissionRecordVisible"
      :append-to-body="true"
    >
      <el-table
        v-loading = 'tableLoad'
        :data="record"
        tooltip-effect="dark"
        style="width: 100%; overflow: auto"
        :style="'height:' + clientHeight / 2 + 'px'"
      >
        <el-table-column
          property="id"
          label="Submit Id"
          align="center"
          sortable
        ></el-table-column>
        <el-table-column
          property="status"
          label="Statue"
          align="center"
          sortable
        >
          <template slot-scope="scope">
            <el-button
              type="primary"
              v-if="scope.row.status === 'Accept'"
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
              size="small"
              v-else
              style="
                background-color: rgb(245, 187, 1);
                width: 100px;
                border: black;
              "
              >Waiting</el-button
            >
          </template>
        </el-table-column>
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
        <el-table-column label="Code/Info" align="center">
          <template slot-scope="scope">
            <el-button type="primary" @click="showDetail(scope.row)"
              >Detail</el-button
            >
          </template>
        </el-table-column>
      </el-table>
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
    </el-dialog>
    <el-dialog
      :visible.sync="changeNicknameV"
      title="Change Nickname"
      append-to-body
    >
      <el-form :model="newNickName">
        <el-form-item label="New Nickname: ">
          <el-input v-model="newNickName.newName"></el-input>
        </el-form-item>
      </el-form>
      <el-button style="margin-left: 80%" @click="changeNickName"
        >Submit</el-button
      >
    </el-dialog>
  </div>
</template>

<script>
import echarts from "echarts";
import api from "@/views/api";
import qs from "qs";
import iconfont from "../assets/icons/iconfont";
import VueApexCharts from "vue-apexcharts";
export default {
  data() {
    return {
      tableLoad: false,
      mobileFlag: false,
      newNickName: { newName: this.$store.state.myUser.nickname },
      changeNicknameV: false,
      totContest: 1,
      partyContest: 0,
      acnumber: 0,
      totnumber: 1,
      series: [
        {
          name: "Series 1",
          data: [
            1,2,3
          ],
        },
      ],
      chartOptions: {
        chart: {
          height: 350,
          type: "radar",
        },
        title: {
          text: "Ability",
        },
        xaxis: {
          categories: ["Correct", "Coverage", "Engage"],
        },
      },
      DetailVisible: false,
      SubmissionRecordVisible: false,
      Detail: {
        info: "",
        code: "",
      },
      record: [],
      clientWidth: document.body.clientWidth,
      clientHeight: document.documentElement.clientHeight,
      annoucement: [],
      rawList: [],
      currentPage1: 1,
      pageSize: 10,
      pageList: [],
      user_name: "123",
      Account_Number: "118118xx",
      nick_name: "test",
      belong_class: "class1",
      resultcharts: "",
      Results: ["Pass", "TLE", "False"],
      ResultsData: [
        {
          value: 335,
          name: "Pass",
        },
        {
          value: 310,
          name: "TLE",
        },
        {
          value: 120,
          name: "False",
        },
      ],
      timecharts: "",
      SubmitTime: [
        "00:00 - 02:00",
        "02:00 - 04:00",
        "04:00 - 06:00",
        "06:00 - 08:00",
        "08:00 - 10:00",
        "10:00 - 12:00",
        "12:00 - 14:00",
        "14:00 - 16:00",
        "16:00 - 18:00",
        "18:00 - 20:00",
        "20:00 - 22:00",
        "22:00 - 00:00",
      ],
      SubmitData: [
        {
          value: 10,
          name: "00:00 - 02:00",
        },
        {
          value: 10,
          name: "02:00 - 04:00",
        },
        {
          value: 10,
          name: "04:00 - 06:00",
        },
        {
          value: 10,
          name: "06:00 - 08:00",
        },
        {
          value: 10,
          name: "08:00 - 10:00",
        },
        {
          value: 10,
          name: "10:00 - 12:00",
        },
        {
          value: 10,
          name: "12:00 - 14:00",
        },
        {
          value: 10,
          name: "14:00 - 16:00",
        },
        {
          value: 10,
          name: "16:00 - 18:00",
        },
        {
          value: 10,
          name: "18:00 - 20:00",
        },
        {
          value: 10,
          name: "20:00 - 22:00",
        },
        {
          value: 10,
          name: "22:00 - 00:00",
        },
      ],
      participatedContestId: [],
      tableHeight: 0,
      attemp: 0,
      allques: 1,
    };
  },
  components: {
    apexchart: VueApexCharts,
  },
  async mounted(){
    this.mountedFunctions();
    if(this.$store.state.myUser.role === 'ROLE_STU'){
    await api.getAllContest().then((res) => {
        this.totContest = res.data.length;
        for (let i = 0; i < res.data.length; i++) {
          let tmpdata = {
            contestId: res.data[i].id,
            contestTitle: res.data[i].name,
            StartTime: res.data[i].beginTime,
            EndTime: res.data[i].endTime,
        
          };
          if (res.data[i].id !== 1) {
            this.rawList.push(tmpdata);
          }
        }
        this.handleCurrentChange(1);
    });
    }else{
       await api.getAdminContest().then((res) => {
        this.totContest = res.data.length;
        for (let i = 0; i < res.data.length; i++) {
          let tmpdata = {
            contestId: res.data[i].id,
            contestTitle: res.data[i].name,
            StartTime: res.data[i].beginTime,
            EndTime: res.data[i].endTime,
          };
          if (res.data[i].id !== 1) {
            this.rawList.push(tmpdata);
          }
        }
        this.handleCurrentChange(1);
    });
    }
    this.annoucement = [];
     await api.getAllNotice().then((res) => {
        for (let i = 0; i < res.data.length; i++) {
          this.annoucement.push(
            '<div style = "font-size: 35px;text-align: center; font-family: Georgia;">' +
              res.data[i].topic +
              "</div>" +
              '<div style = "font-family: Times;">' +
              res.data[i].content +
              "</div>" +
              '<div style = "text-align: right; color: gray;">' +
              res.data[i].author.name +
              "</div>"
          );
        }
      });
     await api.getusersContest(this.$store.state.myUser.id).then((res) => {
        this.participatedContestId = [];
        this.partyContest = res.data.length;
        for (let i = 0; i < res.data.length; i++) {
          if (res.data[i].enable === true) {
            this.participatedContestId[i] = res.data[i].id;
          }
        }
      });
     let data = {
        user_id: this.$store.state.myUser.id,
      };
     await api.getDataAna(qs.stringify(data)).then((res) => {
        this.acnumber = res.data.Accepted;
        this.totnumber = res.data.totSubmit;
      });
     await api.getDoneQuestions(qs.stringify(data)).then((res) => {
        this.allques = res.data.all;
        this.attemp = res.data.done;
      });
      this.series = [
        {
          name: "Ability",
          data: [
            this.acnumber/this.totnumber * 100,this.attemp/this.allques *100, this.partyContest/this.totContest *100
          ],
        },
      ]
  },
  watch: {
    clientHeight(val) {
      this.clientHeight = val;
      this.tableHeight =
        (document.documentElement.clientHeight * 99) / 100 -
        document.getElementById("block").offsetHeight -
        230;
    },
    clientWidth(val) {
      this.clientWidth = val;
    },
    "$store.state.myUser": function (val) {
      this.getusersContest();
    },
  },
  methods: {
     mountedFunctions(){
        if (this._isMobile() !== null) {
      this.mobileFlag = true;
      this.pageSize = 10000;
      }
    this.tableHeight =
      (document.documentElement.clientHeight * 99) / 100 -
      document.getElementById("block").offsetHeight -
      230;
    window.addEventListener(
      "resize",
      () => (this.clientHeight = document.documentElement.clientHeight),
      false
    );
    window.addEventListener(
      "resize",
      () => (this.clientWidth = document.body.clientWidth),
      false
    );
     },
    _isMobile() {
      let flag = navigator.userAgent.match(
        /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
      );
      return flag;
    },
    changeNickName() {
      let data = {
        nickname: this.newNickName.newName,
        user_id: this.$store.state.myUser.id,
      };
      api.changeNickname(qs.stringify(data)).then((res) => {
        if (res.data === "Modify Success") {
          this.$store.state.myUser.nickname = this.newNickName.newName;
          this.$message({
            message: "Change Nick name Success!",
            type: "success",
          });
          this.changeNicknameV = false;
        } else {
          this.$message.error("Network Error.");
        }
      });
    },
    showChangeNickname() {
      this.changeNicknameV = true;
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
    getAllContest() {
     
    },
    accessAnswerPage(contestId) {
      this.$router.push({
        path: "/contestCoding/" + contestId,
      });
    },
    handleClick(row) {
      let data = {
        contest_id: row.contestId,
        user_id: this.$store.state.myUser.id,
      };
      api.joinContest(qs.stringify(data)).then((res) => {
        if (res.data === "Join contest successfully") {
          this.$message({
            message: "Join Success",
            type: "success",
          });
          this.getusersContest();
        } else {
          this.$message.erro("Join fail! Please check your network.");
        }
      });
    },
    getusersContest() {
      api.getusersContest(this.$store.state.myUser.id).then((res) => {
        this.participatedContestId = [];
        this.partyContest = res.data.length;
        for (let i = 0; i < res.data.length; i++) {
          if (res.data[i].enable === true) {
            this.participatedContestId[i] = res.data[i].id;
          }
        }
      });
    },
    getMySubmission() {
      this.SubmissionRecordVisible = true;
      this.tableLoad = true;
      api.getMySubmission(this.$store.state.myUser.id).then((res) => {
        if (res.status === 200) {
          this.record = [];
          for (let i = 0; i < res.data.length; i++) {
            let unit = {
              id: res.data[i].id,
              status: res.data[i].status,
              submitTime: res.data[i].submitTime,
              contest: res.data[i].contest.name,
              questionTitle: res.data[i].question.name,
              info: res.data[i].info,
              code: atob(res.data[i].code.replace(/-/g, "+").replace(/_/g, "/")),
            };
            this.record.push(unit);
          }
          this.tableLoad = false
        }
      });
    },
    initNotices() {
      this.annoucement = [];
      api.getAllNotice().then((res) => {
        for (let i = 0; i < res.data.length; i++) {
          this.annoucement.push(
            '<div style = "font-size: 35px;text-align: center; font-family: Georgia;">' +
              res.data[i].topic +
              "</div>" +
              '<div style = "font-family: Times;">' +
              res.data[i].content +
              "</div>" +
              '<div style = "text-align: right; color: gray;">' +
              res.data[i].author.name +
              "</div>"
          );
        }
      });
    },
    getUserData() {
      let data = {
        user_id: this.$store.state.myUser.id,
      };
      api.getDataAna(qs.stringify(data)).then((res) => {

        this.acnumber = res.data.Accept;
        this.totnumber = res.data.totSubmit;
      });
      api.getDoneQuestions(qs.stringify(data)).then((res) => {
        this.allques = res.data.all;
        this.attemp = res.data.done;
      });
    },
  },
  filters: {
    ellipsis(value) {
      if (!value) return "";
      if (value.length > 8) {
        return value.slice(0, 8) + "...";
      }
      return value;
    },
  },
};
</script>

<style>
.icon {
  width: 1.5em;
  height: 1.5em;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}

#ContestMenu {
  top: 60px;
  right: 0;
  height: 100%;
  width: 100%;
  position: fixed;
  overflow: auto;
  background-color: white;
}

.selfInfo {
  margin-top: -150px;
  margin-right: 10%;
  float: right;
  width: 15%;
  background-color: white;
  height: 600px;
  box-shadow: 0 10px 10px rgba(0, 0, 0, 0.1);
}

#Annoucement {
  width: 60%;
  margin-left: 7%;
  padding: 0;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
#Annoucement1 {
  padding: 2%;
  width: 100%;
  padding: 0;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
#table-content {
  float: left;
  width: 60%;
  height: 100%;
  margin-left: 7%;
  padding: 0;
  margin-bottom: 0;
  overflow: auto;
}

#table-content1 {
  float: left;
  width: 60%;
  height: 100%;
  padding: 0;
  margin-bottom: 0;
  overflow: auto;
}

.el-carousel__item {
  overflow: auto;
  background-color: rgb(250, 250, 250);
}

.el-carousel__item h3 {
  text-align: left;
  color: black;
  font-size: 18px;
  opacity: 0.75;
  line-height: 30px;
  margin: 0;
  /*overflow: auto;*/
}

.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}

.el-pagination {
  padding-bottom: 0;
}
</style>
