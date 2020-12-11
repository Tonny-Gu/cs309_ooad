<template>
  <div id="createContest" style="align-items: center">
    <div id="contest_name" style="width: 20%;float: left; margin-left: 17%;" class="demo-input-suffix">
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
    <div id="transfer" style="width: 100%; margin-left: 25%; float: left; margin-top: 5%;">
      <el-transfer filterable :filter-method="filterMethod" filter-placeholder="Please input the id and title" v-model="value" :data="data" :titles="['question bank', 'contest']" style="text-align: left;"></el-transfer>
    </div>
    <el-button @click="create" type="primary" style="margin-left: 50%; margin-top: 5%;">create</el-button>

  </div>
</template>

<script>
import api from '@/views/api'
import Qs from 'qs'
export default {
  data() {
    const generateData = _ => {
      let data = [];
      let tmpdata = {
        withContent:false
      }
      api.getAllQuestion(Qs.stringify(tmpdata)).then(res =>{
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
  margin-right: 0;
  margin-left: 0;
  right: 0;
  left: 0;
  padding: 0;
}

.el-transfer__buttons{
  width: 10%;
  margin-left: 0;
  margin-right: 0;
  right: 0;
  left: 0;
  padding: 0;
  text-align: center;
}
</style>
