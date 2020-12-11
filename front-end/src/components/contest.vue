<template>
  <div id='ContestMenu'>
    <div id='Annoucement' :style="'margin-top: ' + (clientHeight / 100) + 'px'">
      <el-carousel :interval="6000" height="150px" id='annoucementText'>
        <el-carousel-item v-for="item in annoucement" :key="item">
          <h3 class="medium" style="word-wrap:break-word;" v-html="item"></h3>
        </el-carousel-item>
      </el-carousel>
    </div>
    <div id="table-content" :style="'height: ' + (clientHeight * 99 / 100 - 230) + 'px;padding:0;margin-bottom: 0; margin-top: 0; top: 0; bottom: 0; overflow: auto;'">
      <div :style="'height: ' + (tableHeight) + 'px; padding: 0; margin: 0;'">
      <el-table :data="pageList" stripe style="width: 100%;top: 20px;"  >
        <el-table-column prop="contestName" label="Contest" align="center">
          <template slot-scope="scope">
            <div style="font-size: 20px">{{scope.row.contestTitle}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="StartTime" label="Start Time" ></el-table-column>
        <el-table-column prop="EndTime" label="End Time"></el-table-column>
        <el-table-column label="Access" align="center"><template slot-scope="scope">
          <el-button @click="accessAnswerPage(scope.row.contestId)" type="primary" v-if="participatedContestId.indexOf(scope.row.contestId) > -1 && Object.keys($store.state.myUser).length !== 0" style="background-color: #67C23A;width: 100px" size="small">Enter</el-button>
          <el-button @click="handleClick(scope.row)" type="primary" size="small" v-else style="width: 100px">Join</el-button>
        </template></el-table-column>
      </el-table>
      </div>
      <div id="block">
        <el-pagination id='PageControl' @size-change="handleSizeChange" @current-change="handleCurrentChange"
                       :current-page="currentPage1"
                       :page-sizes="[10, 20, 30, 40]" :page-size="10" layout="total, sizes, prev, pager, next, jumper"
                       :total="rawList.length"></el-pagination>
      </div>
    </div>
    <div class="selfInfo">
      <br/>
      <div style="text-align: left; margin-left: 5%; font-family: 'Microsoft JhengHei';">User Name: {{ this.$store.state.myUser.nickname }}</div>
      <br/>
      <div style="text-align: left; margin-left: 5%; font-family: 'Microsoft JhengHei';">Account Number: {{ this.$store.state.myUser.username }}</div>
      <br/>
      <div style="text-align: left; margin-left: 5%; font-family: 'Microsoft JhengHei';">Role: {{ this.$store.state.myUser.role }}</div>
      <br/>
      <el-button type="primary" style="background-color: green;" @click="getMySubmission">Submission Record</el-button>
      <br/>
      <br/>
      <div style="text-align: left; margin-left: 5%; font-family: 'Microsoft JhengHei';">Submit Rate:</div>
      <div id='susRate' style="width: 100%;height: 60%;"></div>
      <!--			<div id='SubmitTime' style="width: 100%;height: 40%;"></div>-->
    </div>

    <el-dialog title="Submit Record" :visible.sync="SubmissionRecordVisible" :append-to-body= true >
      <el-table :data="record" tooltip-effect="dark"
                style="width: 100%;overflow: auto;" :style="'height:' + clientHeight / 2 + 'px'">
        <el-table-column property="id" label="Submit Id" align="center" sortable></el-table-column>
        <el-table-column property="status" label="Statue" align="center" sortable>
          <template slot-scope="scope">
              <el-button type="primary" v-if="scope.row.status === 'Accept'" style="background-color: #67C23A;width: 100px;border: black" size="small">{{ scope.row.status }}</el-button>
            <el-button type="primary" v-else-if="scope.row.status === 'Wrong Answer'" style="background-color: #F56C6C;width: 100px;border: black" size="small">Wrong</el-button>
              <el-button type="primary" size="small" v-else style="background-color: rgb(245,187,1);width: 100px; border: black">Waiting</el-button>
          </template>
        </el-table-column>
        <el-table-column property="submitTime" label="Submit Time" align="center" sortable></el-table-column>
        <el-table-column property="contest" label="Contest" align="center"></el-table-column>
        <el-table-column property="questionTitle" label="Question" align="center"></el-table-column>
        <el-table-column label="Code/Info" align="center">
          <template slot-scope="scope">
            <el-button type="primary" @click="showDetail(scope.row)">Detail</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog title="Detail" :visible.sync="DetailVisible" :append-to-body="true">
        <div>{{this.Detail.info}}</div>
        <el-divider></el-divider>
        <div>{{this.Detail.code}}</div>
      </el-dialog>
    </el-dialog>

  </div>
</template>

<script>
import susfoot from "./susfoot.vue"
import echarts from 'echarts'
import api from '@/views/api'
import qs from 'qs'

export default {
  data() {
    return {
      DetailVisible: false,
      SubmissionRecordVisible: false,
      Detail:{
        info: '',
        code: ''
      },
      record: [

      ],
      clientHeight: document.documentElement.clientHeight,
      annoucement: [
        'Here is the first announcement.',
        'Here is the second announcement.',
        'Here is the third announcement',
      ],
      rawList: [],
      currentPage1: 1,
      pageSize: 10,
      pageList: [],
      user_name: '123',
      Account_Number: '118118xx',
      nick_name: 'test',
      belong_class: 'class1',
      resultcharts: '',
      Results: ['Pass', 'TLE', 'False'],
      ResultsData: [{
        value: 335,
        name: 'Pass'
      },
        {
          value: 310,
          name: 'TLE'
        },
        {
          value: 120,
          name: 'False'
        }
      ],
      timecharts: '',
      SubmitTime: ['00:00 - 02:00', '02:00 - 04:00', '04:00 - 06:00', '06:00 - 08:00', '08:00 - 10:00',
        '10:00 - 12:00', '12:00 - 14:00', '14:00 - 16:00', '16:00 - 18:00', '18:00 - 20:00', '20:00 - 22:00',
        '22:00 - 00:00'
      ],
      SubmitData: [{
        value: 10,
        name: '00:00 - 02:00'
      },
        {
          value: 10,
          name: '02:00 - 04:00'
        },
        {
          value: 10,
          name: '04:00 - 06:00'
        },
        {
          value: 10,
          name: '06:00 - 08:00'
        },
        {
          value: 10,
          name: '08:00 - 10:00'
        },
        {
          value: 10,
          name: '10:00 - 12:00'
        },
        {
          value: 10,
          name: '12:00 - 14:00'
        },
        {
          value: 10,
          name: '14:00 - 16:00'
        },
        {
          value: 10,
          name: '16:00 - 18:00'
        },
        {
          value: 10,
          name: '18:00 - 20:00'
        },
        {
          value: 10,
          name: '20:00 - 22:00'
        },
        {
          value: 10,
          name: '22:00 - 00:00'
        }
      ],
      participatedContestId: [],
      tableHeight: 0
    }
  },
  components: {
    susfoot
  },
  beforeCreate() {

  },
  mounted: function () {
    this.tableHeight = document.documentElement.clientHeight * 99 / 100 - document.getElementById('block').offsetHeight - 230;
    window.addEventListener('resize', () => this.clientHeight = document.documentElement.clientHeight, false)
    this.getAllContest();
    this.initNotices();
    this.getusersContest();
    let table = document.getElementById('table-content')
    table.style.height = (window.innerHeight - 210) + 'px'
    this.$nextTick(function () {
      this.drawPassRate('susRate');
      // this.drawSubmitTime('SubmitTime');
    });
  },
  watch: {
    clientHeight(val) {
      this.clientHeight = val;
      this.tableHeight = document.documentElement.clientHeight * 99 / 100 - document.getElementById('block').offsetHeight - 230;
    },
    '$store.state.myUser': function (val) {
      this.getusersContest()
    }
  },
  methods: {
    showDetail(row){
      this.Detail = {}
      this.Detail['code'] = row.code;
      this.Detail['info'] = row.info;
      console.log(row)
      this.DetailVisible = true
    },
    drawPassRate(id) {
      let colors = ['rgb(156,212,125)', 'rgb(205,205,205)', 'rgb(245,145,142)'];
      let i = 0;
      this.resultcharts = echarts.init(document.getElementById(id))
      this.resultcharts.setOption({
        tooltip: {
          trigger: 'item',
        },
        series: [{
          name: 'Results',
          type: 'pie',
          itemStyle: {
            normal: {
              color: function () {
                return colors[i++];
              },
            }
          },
          radius: ['50%', '70%'],
          avoidLabelOverlap: false,
          label: {
            normal: {
              show: false,
              position: 'center'
            },
            emphasis: {
              show: true,
              textStyle: {
                fontSize: '50',
                fontWeight: 'blod'
              }
            }
          },
          labelLine: {
            normal: {
              show: false
            }
          },
          data: this.ResultsData
        }]
      })
    },
    drawSubmitTime(id) {
      let i = 0;
      let colors = ['red', 'orange', 'yellow', 'green', 'blue', 'purple', 'brown', 'greenyellow', 'black', 'gray', 'pink',
        'cyan'
      ];
      this.timecharts = echarts.init(document.getElementById(id));
      this.timecharts.setOption({
        tooltip: {
          trigger: 'item',
        },
        title: {
          text: 'submit time'
        },
        series: [{
          name: 'Results',
          type: 'pie',
          radius: ['50%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            normal: {
              color: function () {
                return colors[i++];
              },
            }
          },
          label: {
            normal: {
              show: false,
              position: 'center'
            },
            emphasis: {
              show: true,
              textStyle: {
                fontSize: '30',
                fontWeight: 'blod'
              }
            }
          },
          labelLine: {
            normal: {
              show: false
            }
          },
          data: this.SubmitData
        }]
      })
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
      api.getAllContest().then(res => {
        for (let i = 1; i < res.data.length; i++) {
          let tmpdata = {
            contestId: res.data[i].id,
            contestTitle: res.data[i].name,
            StartTime: res.data[i].beginTime,
            EndTime: res.data[i].endTime,
          }
          this.rawList.push(tmpdata);
        }
        this.handleCurrentChange(1);
      })
    },
    accessAnswerPage(contestId) {
      this.$router.push({
        path: '/contestCoding/' + contestId
      })
    },
    handleClick(row) {
      let data = {
        contest_id: row.contestId,
        user_id: this.$store.state.myUser.id
      }
      api.joinContest(qs.stringify(data)).then(res => {
      })
      this.getusersContest()
    },
    getusersContest() {
      api.getusersContest(this.$store.state.myUser.id).then(res => {
        this.participatedContestId = []
        for (let i = 0; i < res.data.length; i++) {
          this.participatedContestId[i] = res.data[i].id
        }
      })


    },
    getMySubmission() {
      this.SubmissionRecordVisible = true
      api.getMySubmission(this.$store.state.myUser.id).then(res => {
        if(res.status === 200){
          console.log(res)
          this.record = []
          for(let i = 0;i<res.data.length;i++){
            let unit = {
                id : res.data[i].id,
                status: res.data[i].status,
                submitTime: res.data[i].submitTime,
                contest: res.data[i].contest.name,
                questionTitle: res.data[i].question.name,
                info:res.data[i].info,
                code:atob(res.data[i].code),
            }
            this.record.push(unit)
          }
        }
      })
    },
    initNotices() {
      this.annoucement = []
      api.getAllNotice().then(res=>{
        for (let i = 0; i < res.data.length; i++) {
          this.annoucement.push(res.data[i].content)
        }
      })
    },
  },
  filters: {
    ellipsis(value) {
      if (!value) return ''
      if (value.length > 8) {
        return value.slice(0, 8) + '...'
      }
      return value
    }
  }
}
</script>

<style>
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

#table-content {
  float: left;
  width: 60%;
  height: 100%;
  margin-left: 7%;
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

.el-pagination{
  padding-bottom: 0;
}
</style>
