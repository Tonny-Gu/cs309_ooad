<template>
	<el-container id='homeBody'>
		<el-main style="overflow: hidden">
			<el-button style="float: right;margin-left: 5px;" v-on:click="search()" >search</el-button>
			<el-input style="width: 20%;float: right;" v-model="search_input" placeholder="please input the question id"></el-input>
			<el-table :data="pageList" height="92%" stripe style="width: 100%; text-align-all: center;">
				<el-table-column prop="questionId" label="题目号">
				</el-table-column>
				<el-table-column prop="questionTitle" label="题目标题">
					<template slot-scope="scope">
						<el-button type="text" @click="accessAnswerPage(scope.row.questionId)">{{scope.row.questionTitle | ellipsis}}</el-button>
					</template>
				</el-table-column>
				<el-table-column prop="dbType" label="类型">
				</el-table-column>
				<el-table-column prop="difficulty" label="难度">
				</el-table-column>
				<el-table-column prop="author" label="作者"></el-table-column>
			</el-table>
			<div class="block">
				<el-pagination id='PageControl' @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage1"
				 :page-sizes="[10, 20, 30, 40]" :page-size="10" layout="total, sizes, prev, pager, next, jumper" :total="rawList.length"></el-pagination>
			</div>
		</el-main>
		<el-footer>
			<susfoot></susfoot>
		</el-footer>
		<br />
	</el-container>
</template>
<script>
	import susfoot from "./susfoot.vue"
  import api from "@/views/api";
	export default {
		data() {
			return {
				search_input: '',
				rawList: [],
				currentPage1: 1,
				pageSize: 10,
				pageList: [],
			}
		},
		methods: {
			search: function(){
				alert(this.search_input);
				this.pageList = []
				let i = 0;
				for(; i < this.rawList.length; i++){
					if(this.rawList[i]['questionId'] === this.search_input){
						this.pageList.push(this.rawList[i]);
						break;
					}
				}
			},
			handleSizeChange: function(pageSize) {
				this.pageSize = pageSize;
				this.handleCurrentChange(this.currentPage1);
			},
			handleCurrentChange: function(currentPage) {
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
      getAllQuestion(){
        api.getAllQuestion(false).then(res =>{
          for (let i = 0 ; i < res.data.length; i++){
            let tmpdata = {
              questionId:res.data[i].id,
              questionTitle:res.data[i].name,
              dbType:res.data[i].dbType,
              difficulty:res.data[i].degree,
              author:res.data[i].author.username
            }
            this.rawList.push(tmpdata)
          }
          this.handleCurrentChange(1);
          console.log(res);
        })
      },
      accessAnswerPage(questionTitle){
			  this.$router.push({
          path: '/answer/' + questionTitle
        })
      }
		},
		components: {
			susfoot
		},
		mounted: function(){
      this.getAllQuestion();
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
