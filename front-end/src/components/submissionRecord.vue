<template>
	<div>
		<el-table :data="pageList" stripe style="width: 100%; text-align-all: center;">
			<el-table-column prop="QuestionId" label="题目号">
			</el-table-column>
			<el-table-column prop="StudentId" label="用户名">
			</el-table-column>
			<el-table-column prop="submitTime" label="提交时间">
			</el-table-column>
			<el-table-column prop="runTime" label="运行时间">
			</el-table-column>
			<el-table-column prop="statusName" label="结果">
				<template slot-scope="scope">
					<span v-html="formateStatus( scope.row.statusName )"></span>
				</template>
			</el-table-column>
		</el-table>
		<div class="block">
			<el-pagination id='PageControl' @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage1"
			 :page-sizes="[10, 20, 30, 40]" :page-size="10" layout="total, sizes, prev, pager, next, jumper" :total="rawList.length"></el-pagination>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				rawList: [{
						QuestionId: '321',
						StudentId: '123',
						submitTime: '2020-11-19 10:30',
						runTime: '10ms',
						statusName: "Pass"
					},
				],
				currentPage1: 1,
				pageSize: 10,
				pageList: [],
			}
		},
		methods: {
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
			formateStatus(statusName) {
				if (statusName === "Pass")
					return (`<span style="color: #4bfe20">${statusName}</span>`)
				return (`<span style="color: #f30112">${statusName}</span>`)
			}
		},
		mounted: function(){
			this.handleCurrentChange(1);
		}
	}
</script>

<style>
</style>
