<template>
  <div id="createContest" style="align-items: center">
    <div style="float: left; margin-left: 10%; width: 15%">
      <el-select
        v-model="currentContest"
        placeholder="Select a contest to modify"
        @change="currentContestChanged"
        style="width: 100%"
        clearable
      >
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
          <span style="float: left">{{ item.label }}</span>
          <span style="float: right; color: #8492a6; font-size: 13px">{{
            item.value
          }}</span>
        </el-option>
      </el-select>
    </div>
    <div
      id="contest_name"
      style="width: 20%; float: left; margin-left: 3%"
      class="demo-input-suffix"
    >
      <el-form label-width="130px">
        <el-form-item label="Contest's Name: ">
          <el-input v-model="contestData.name"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <div id="contest_start" style="margin-left: 3%; float: left; width: 20%">
      <el-form label-width="130px">
        <el-form-item label="Start Time: ">
          <el-date-picker
            v-model="contestData.beginTime"
            type="datetime"
            placeholder="Please choose the start time"
            value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 100%"
          ></el-date-picker>
        </el-form-item>
      </el-form>
    </div>
    <div id="contest_end" style="margin-left: 3%; float: left; width: 20%">
      <el-form label-width="130px">
        <el-form-item label="End Time: ">
          <el-date-picker
            v-model="contestData.endTime"
            type="datetime"
            placeholder="Please choose the end time"
            value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 100%"
          ></el-date-picker>
        </el-form-item>
      </el-form>
    </div>
    <div
      id="transfer"
      style="width: 100%; margin-left: 25%; float: left; margin-top: 5%"
    >
      <el-transfer
        filterable
        :filter-method="filterMethod"
        filter-placeholder="Please input the id and title"
        v-model="value"
        :data="data"
        :titles="['question bank', 'contest']"
        style="text-align: left"
      ></el-transfer>
    </div>
    <div>
      <el-button
        v-if="!buttonDis"
        @click="create"
        type="success"
        style="
          margin-left: 5%;
          margin-right: 5%;
          margin-top: 5%;
          float: right;
          width: 10%;
        "
        >Create</el-button
      >
      <el-button
        v-else
        disabled
        @click="create"
        type="success"
        style="
          margin-left: 5%;
          margin-right: 5%;
          margin-top: 5%;
          float: right;
          width: 10%;
        "
        >Create</el-button
      >
      <el-button
        @click="ModifyContest"
        type="primary"
        style="margin-top: 5%; float: right; width: 10%"
        >Modify</el-button
      >
      <el-button
        @click="ExportGrade"
        type="info"
        style="margin-top: 5%; float: right; margin-right: 5%; width: 10%"
        >Export Grades</el-button
      >
    </div>
  </div>
</template>

<script>
import api from "@/views/api";
import Qs from "qs";
export default {
  data() {
    const generateData = (_) => {
      let data = [];
      let tmpdata = {
        withContent: false,
      };
      api.getAllQuestion(Qs.stringify(tmpdata)).then((res) => {
        for (let i = 0; i < res.data.length; i++) {
          let tmpdata = {
            questionId: res.data[i].id,
            questionTitle: res.data[i].name,
          };
          data.push({
            key: tmpdata.questionId,
            label: tmpdata.questionId + ". " + tmpdata.questionTitle,
            questions: tmpdata.questionId + ". " + tmpdata.questionTitle,
          });
        }
      });
      return data;
    };
    return {
      buttonDis: false,
      original_list: [],
      currentContest: "",
      options: [],
      contestData: {
        name: "",
        beginTime: "",
        endTime: "",
      },
      data: generateData(),
      value: [],
      filterMethod(query, item) {
        return item.questions.indexOf(query) > -1;
      },
    };
  },
  methods: {
    download(data) {
      if (!data) {
        return;
      }
      let url = window.URL.createObjectURL(new Blob([data]));
      let link = document.createElement("a");
      link.style.display = "none";
      link.href = url;
      link.download = "grade.csv"; //下载后文件名
      link.setAttribute("download", "grade.csv");
      document.body.appendChild(link);
      link.click();
    },

    ExportGrade() {
      let data = {
        contest_id: this.currentContest,
      };
      api.downloadgrade(Qs.stringify(data)).then((res) => {
        if (res.data === "Not logged in") {
          this.$message.error("Not logged in");
        } else {
          this.download(res.data);
        }
      });
    },
    currentContestChanged() {
      if (this.currentContest !== "") {
        this.buttonDis = true;
      } else {
        this.buttonDis = false;
        this.contestData = {};
        this.value = []
      }
      if (this.currentContest !== "") {
        api.getContestsById(this.currentContest).then((res) => {
          if (res.status == 200) {
            this.contestData = {
              name: res.data.name,
              beginTime: res.data.beginTime,
              endTime: res.data.endTime,
            };
          }
        });
        api.getQuestionByContest(this.currentContest).then((res) => {
          for (let i = 0; i < res.data.length; i++) {
            let tmpdata = {
              questionId: res.data[i].id,
              questionTitle: res.data[i].name,
            };
            this.original_list.push(res.data[i].id);
            this.value.push(res.data[i].id);
          }
        });
      }
    },
    create: function () {
      let data = {
        name: this.contestData.name,
        beginTime: this.contestData.beginTime,
        endTime: this.contestData.endTime,
      };

      if (data.name !== "" && data.beginTime !== "" && data.endTime !== "") {
        api.createContest(Qs.stringify(data)).then((res) => {
          if (res.data === "Not logged in") {
            this.$message.error("Not logged in");
          } else {
            let flag = 0;
            for (let i = 0; i < this.value.length; i++) {
              let data = {
                contest_id: res.data.id,
                question_id: this.value[i],
              };
              api.addQuestion(Qs.stringify(data)).then((res) => {
                if (res.data !== "question add successfully") {
                  flag = 1;
                }
              });
              if (flag === 1) {
                this.$message.error("Network Error!");
              } else {
                this.$message({
                  message: "Create contest success",
                  type: "success",
                });
                this.contestData = {
                  name: "",
                  beginTime: "",
                  endTime: "",
                };
              }
            }
          }
        });
      } else {
        this.$message.error("Title/BeginTime/EndTime must not be null");
      }
    },
    getCurrentContest() {
      api.getAllContest().then((res) => {
        this.options = [];
        for (let i = 0; i < res.data.length; i++) {
          let data = {
            label: res.data[i].name,
            value: res.data[i].id,
          };
          this.options.push(data);
        }
      });
    },
    ModifyContest() {
      let data = {
        contest_id: this.currentContest,
        name: this.contestData.name,
        beginTime: this.contestData.beginTime,
        endTime: this.contestData.endTime,
      };
      let flag = 0;
      for (let i = 0; i < this.value.length; i++) {
        if (this.original_list.indexOf(this.value[i]) < 0) {
          let tmpdata = {
            contest_id: this.currentContest,
            question_id: this.value[i],
          };
          api.addQuestion(Qs.stringify(tmpdata)).then((res) => {
            if (res.data.search("success") === -1) {
              flag = 1;
            }
          });
        }
      }
      for (let i = 0; i < this.original_list.length; i++) {
        if (this.value.indexOf(this.original_list[i]) < 0) {
          let tmpdata = {
            contest_id: this.currentContest,
            question_id: this.original_list[i],
          };
          api.delQuestion(Qs.stringify(tmpdata)).then((res) => {
            if (res.data.search("success") === -1) {
              flag = 1;
            }
          });
        }
      }
      api.modifyContest(Qs.stringify(data)).then((res) => {
        if (res.data.search("success") === -1) {
          flag = 1;
        }
        if (flag == 1) {
          this.$message.error("Something goes wrong");
        } else {
          this.$message({
            message: "Modify Success",
            type: "success",
          });
          this.contestData = {
            name: "",
            beginTime: "",
            endTime: "",
          };
          this.value = [];
        }
      });
    },
  },
  mounted() {
    this.getCurrentContest();
  },
};
</script>

<style>
#createContest {
  margin-top: 70px;
}

.el-transfer-panel {
  width: 20%;
  margin-right: 0;
  margin-left: 0;
  right: 0;
  left: 0;
  padding: 0;
}

.el-transfer__buttons {
  width: 10%;
  margin-left: 0;
  margin-right: 0;
  right: 0;
  left: 0;
  padding: 0;
  text-align: center;
}
</style>
