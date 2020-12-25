<template>
  <el-container id="homeBody">
    <el-main style="overflow: hidden">
      <el-button style="float: right; margin-left: 5px" v-on:click="search()"
        >search</el-button
      >
      <el-input
        style="width: 20%; float: right"
        v-model="search_input"
        placeholder="please input the question id"
      ></el-input>
      <div :style="'height:' + this.tableHeight + 'px'">
        <el-table
        v-loading='tableLoad'
          :data="pageList"
          height="92%"
          stripe
          style="width: 100%; text-align-all: center"
        >
          <el-table-column prop="questionId" label="Question Id" align="center">
          </el-table-column>
          <el-table-column prop="questionTitle" label="Title" align="center">
            <template slot-scope="scope">
              <el-button
                type="text"
                @click="accessAnswerPage(scope.row.questionId)"
                >{{ scope.row.questionTitle | ellipsis }}</el-button
              >
            </template>
          </el-table-column>
          <el-table-column prop="dbType" label="Type" align="center">
          </el-table-column>
          <el-table-column prop="difficulty" label="Difficulty" align="center">
          </el-table-column>
          <el-table-column
            prop="author"
            label="Author"
            align="center"
          ></el-table-column>
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
    </el-main>
    <br />
  </el-container>
</template>
<script>
import api from "@/views/api";
import Qs from "qs";
export default {
  data() {
    return {
      tableLoad: false,
      clientHeight: document.documentElement.clientHeight,
      tableHeight: 0,
      search_input: "",
      rawList: [],
      currentPage1: 1,
      pageSize: 10,
      pageList: [],
    };
  },
  watch: {
    clientHeight(val) {
      this.clientHeight = val;
      this.tableHeight =
        (document.documentElement.clientHeight * 99) / 100 -
        document.getElementById("block").offsetHeight -
        10;
    },
  },
  methods: {
    search: function () {
      this.pageList = [];
      let i = 0;
      for (; i < this.rawList.length; i++) {
        if (this.rawList[i]["questionId"] == this.search_input) {
          this.pageList.push(this.rawList[i]);
          break;
        }
      }
      if(this.search_input == ''){
        this.getAllQuestion();
      }
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
    getAllQuestion() {
      let data = {
        withContent: false,
      };
      this.tableLoad = true
      api.getQuestionByContest(1).then((res) => {
        for (let i = 0; i < res.data.length; i++) {
          let tmpdata = {
            questionId: res.data[i].id,
            questionTitle: res.data[i].name,
            dbType: res.data[i].dbType,
            difficulty: res.data[i].degree,
            author: res.data[i].author.username,
          };
          this.rawList.push(tmpdata);
          this.tableLoad = false;
        }
        this.handleCurrentChange(1);
      });
    },
    accessAnswerPage(questionTitle) {
      this.$router.push({
        path: "/answer/" + questionTitle,
      });
    },
  },
  components: {},
  mounted: function () {
    this.clientHeight = document.documentElement.clientHeight;
    this.tableHeight =
      (document.documentElement.clientHeight * 99) / 100 -
      document.getElementById("block").offsetHeight -
      90;
    this.getAllQuestion();
    window.addEventListener(
      "resize",
      () => (this.clientHeight = document.documentElement.clientHeight),
      false
    );
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
<style scoped>
#homeBody {
  left: 7%;
  top: 60px;
  height: 100%;
  width: 85%;
  position: fixed;
  height: 100%;
  overflow: hidden;
}
</style>
