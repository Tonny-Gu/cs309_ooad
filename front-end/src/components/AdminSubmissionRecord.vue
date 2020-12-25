<template>
<div id="AdminSubmission">
  <div style="width: 20%; float:left; margin-left: 5%;">
    Contest Id (Optional):
    <el-input
        placeholder="Please input Contest Id"
        v-model="ContestId"
        clearable
        style="width: 50%;"></el-input>
  </div>
  <div style="float: left; width: 20%;">
  Question Id (Optional):
  <el-input
    placeholder="Please input Question Id"
    v-model="QuestionId"
    clearable
    style="width: 50%;"></el-input>
  </div>
  <div style="float: left; width: 20%;">
    Student Id (Optional):
    <el-input
        placeholder="Please input student Id"
        v-model="StudentId"
        clearable
        style="width: 50%;">
    </el-input>
  </div>
  <el-button type="primary" style="float:left;" @click="searchSubmission">Search</el-button>
  <el-table
  v-loading = 'tableLoad'
      :data="allSubmissionRecord"
      style="width: 90%; margin-left: 5%;">
    <el-table-column
        prop="submissionId"
        label="Submission Id" sortable>
    </el-table-column>
    <el-table-column
        prop="sid"
        label="Student Id" sortable>
    </el-table-column>

    <el-table-column
        prop="subTime"
        label="Submission Time" sortable>
    </el-table-column>
    <el-table-column
        prop="contestTitle"
        label="Contest Name (Id)">
    </el-table-column>
    <el-table-column
        prop="questionTitle"
        label="Question Title (Id)">
    </el-table-column>
    <el-table-column
        prop="language"
        label="Language">
    </el-table-column>
    <el-table-column
        prop="status"
        label="Status">
      <template slot-scope="scope">
        <el-button type="primary" v-if="scope.row.status === 'Accepted'" style="background-color: #67C23A;width: 100px;border: black" size="small">{{ scope.row.status }}</el-button>
        <el-button type="primary" v-else-if="scope.row.status === 'Wrong Answer'" style="background-color: #F56C6C;width: 100px;border: black" size="small">Wrong</el-button>
        <el-button type="primary" v-else-if="scope.row.status === 'Time Limited'" style="background-color: #F56C6C;width: 100px;border: black" size="small">Time Limited Exceed</el-button>
        <el-button type="primary" size="small" v-else style="background-color: rgb(245,187,1);width: 100px; border: black">Waiting</el-button>
      </template>
    </el-table-column>
    <el-table-column
        label="Details">
      <template slot-scope="scope">
        <el-button type="primary" @click="detail(scope.row)">More Details</el-button>
      </template>
    </el-table-column>
  </el-table>
  <el-dialog :visible.sync="detailVisible" title="More Details">
    <span>{{detailCode}}</span>
    <el-divider></el-divider>
    <li v-for="info in detailInfo">
            Test Case{{ info["id"] }}-----Status:{{ info["status"] }}
          </li>
  </el-dialog>
</div>
</template>

<script>
import api from '@/views/api'
import qs from 'qs'
export default {
name: "AdminSubmissionRecord",
  data(){
  return{
    tableLoad: false,
    StudentId: '',
    QuestionId: '',
    ContestId: '',
    allSubmissionRecord:[],
    detailVisible: false,
    detailCode: '23',
    detailInfo: 'pas',
    sidforsearch:'',
  }
  },
  methods: {
    detail(row){
      this.detailCode = '';
      this.detailInfo = [];
      this.detailVisible = true;
      row.submissionId
      let data = {
        id:row.submissionId
      }
      api.getResult(qs.stringify(data)).then(res =>{
        this.detailCode = atob(res.data.code)
        this.detailInfo = JSON.parse(res.data.info)
      })
  },
    async searchSubmission(){
      this.tableLoad = true
      this.allSubmissionRecord = [];
      let data = new Object() ;
      if(this.ContestId !== ''){
          data.contest_id = this.ContestId
      }
      if(this.StudentId !== ''){
        let datatmp = {
          username:this.StudentId
        }
        await api.getUserbyusername(qs.stringify(datatmp)).then(res =>{
          this.sidforsearch = res.data.id
          data.user_id = this.sidforsearch
        })

      }
      if(this.QuestionId !== ''){
        data.question_id = this.QuestionId
      }
      data.withCode = false
      api.getSubmissionForTeacher(qs.stringify(data)).then(res=>{
        if(res.data.length>0){
        for (let i = 0; i < res.data.length;i++){
          let unit = {
            submissionId:res.data[i].id,
            sid:res.data[i].student.username,
            contestTitle: res.data[i].contest.name+" ("+res.data[i].contest.id+")",
            questionTitle: res.data[i].question.name+" ("+res.data[i].question.id+")",
            subTime:res.data[i].submitTime,
            language:res.data[i].question.dbType,
            status:res.data[i].status

          }
          this.allSubmissionRecord.push(unit)
          this.tableLoad = false
        }
        }else{
          this.$message.error("Something goes wrong")
        }
      })

    }
  }
}

</script>

<style scoped>
#AdminSubmission{
  margin-top: 70px;
}
</style>
