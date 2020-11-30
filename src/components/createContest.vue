<template>
  <div id="createContest">
    <div id="contest_name" style="width: 20%;margin-left: 20%; float: left;" class="demo-input-suffix">
      <el-form label-width="130px">
        <el-form-item label="Contest's Name: ">
          <el-input v-model="contestData.name"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <div id="contest_start" style="margin-left:3%;float:left; width: 20%;">
      <el-form label-width="130px">
        <el-form-item label="Start Time: ">
          <el-date-picker v-model="contestData.beginTime" type="datetime" placeholder="Please choose the start time" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%;"></el-date-picker>
        </el-form-item>
      </el-form>
    </div>
    <div id="contest_end" style="margin-left: 3%;float:left; width: 20%;">
      <el-form label-width="130px">
        <el-form-item label="End Time: ">
          <el-date-picker v-model="contestData.endTime" type="datetime" placeholder="Please choose the end time" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%;"></el-date-picker>
        </el-form-item>
      </el-form>
    </div>
    <div id="addQuestion" style="float: left; margin-left: 20%; margin-top: 5%; width: 100%;">
      <el-form label-width="130px">
        <el-form-item label="Choose questions: ">
          <el-transfer filterable :filter-method="filterMethod" filter-placeholder="Please input the id and title" v-model="value" :data="data" :titles="['question bank', 'contest']" style="text-align: left"></el-transfer>
        </el-form-item>
      </el-form>
      <el-button @click="create">Create</el-button>
    </div>

  </div>
</template>

<script>
import api from '@/views/api'
import Qs from 'qs'
export default {
  data() {
    const generateData = _ => {
      let data = [];
      api.getAllQuestion('false').then(res =>{
        for(let i = 0; i < res.data.length;i++){
          let tmpdata = {
            questionId:res.data[i].id,
            questionTitle:res.data[i].name,
          }
          data.push({
            key: tmpdata.questionId,
            label: tmpdata.questionId+'. '+tmpdata.questionTitle,
            questions: tmpdata.questionId+'. '+tmpdata.questionTitle
          });
        }
      })
      return data;
    };
    return {
      contestData:{
        name:'',
        beginTime:'',
        endTime:''
      },
      data: generateData(),
      value: [],
      filterMethod(query, item) {
        return item.questions.indexOf(query) > -1;
      }
    };
  },
  methods: {
    create: function (){
      let data = {
        name:this.contestData.name,
        beginTime:this.contestData.beginTime,
        endTime:this.contestData.endTime
      }
      api.createContest(Qs.stringify(data)).then(res =>{
        for(let i = 0; i < this.value.length;i++){
          let data = {
            contest_id :res.data.id,
            question_id:this.value[i]
          }
          api.addQuestion(Qs.stringify(data))
        }
      })
    },
  },

}
</script>

<style>
#createContest{
  margin-top: 70px;
}

.el-transfer-panel{
  width: 20%;
}
</style>
