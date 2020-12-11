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
      :data="allSubmissionRecord"
      style="width: 90%; margin-left: 5%;">
    <el-table-column
        prop=""
        label="Submission Id" sortable>
    </el-table-column>
    <el-table-column
        prop=""
        label="Student Id">
    </el-table-column>
    <el-table-column
        prop=""
        label="Contest Name">
    </el-table-column>
    <el-table-column
        prop=""
        label="Question Title">
    </el-table-column>
    <el-table-column
        prop=""
        label="Language">
    </el-table-column>
    <el-table-column
        prop=""
        label="Submission Time">
    </el-table-column>
    <el-table-column
        prop=""
        label="Statue">
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
    <span>{{detailInfo}}</span>
  </el-dialog>
</div>
</template>

<script>
export default {
name: "AdminSubmissionRecord",
  data(){
  return{
    StudentId: '',
    QuestionId: '',
    ContestId: '',
    allSubmissionRecord:[{
      code: '123',
      info: 'pass'
    }],
    detailVisible: false,
    detailCode: '23',
    detailInfo: 'pas'
  }
  },
  methods: {
detail(row){
  this.detailVisible = true;
  this.detailCode = row.code;
  this.detailInfo = row.info;
},
    searchSubmission(){

    }
  }
}

</script>

<style scoped>
#AdminSubmission{
  margin-top: 70px;
}
</style>
