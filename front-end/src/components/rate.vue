<template>
	<el-container id='RateBody'>
		<el-main>
			<el-table :data="pageList" height="92%" stripe style="width: 100%; text-align-all: center;">
				<el-table-column prop="studentNo" label="No">
				</el-table-column>
				<el-table-column prop="studentId" label="User">
				</el-table-column>
				<el-table-column prop="passNumber" label="Pass Number">
				</el-table-column>
				<el-table-column prop="submitNumber" label="Submit Number">
				</el-table-column>
				<el-table-column prop="passRate" label="Pass Rate"></el-table-column>
			</el-table>
			<div class="block">
				<el-pagination id='PageControl' @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage1"
				 :page-sizes="[10, 20, 30, 40]" :page-size="10" layout="total, sizes, prev, pager, next, jumper" :total="rawList.length"></el-pagination>
			</div>
		</el-main>
		<el-footer>
		</el-footer>
		<br />
	</el-container>
</template>

<script>
	export default {
		data() {
			return {
				rawList: [{
					studentNo: 1,
					studentId: 'a123',
					passNumber: 100,
					submitNumber: 100,
					passRate: '100%'
				}],
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
		},
		mounted: function() {
			this.handleCurrentChange(1);
		},
		components: {
		}
	}
</script>

<style>
	#RateBody {
		float: right;
		top: 60px;
		right: 0px;
		height: 100%;
		width: 85%;
		position: fixed;
		height: 100%;
		overflow: auto;
	}
</style>
