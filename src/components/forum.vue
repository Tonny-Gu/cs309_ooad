<template>
	<div>
		<!-- 表格数据 -->
		<div class="table-content">
			<el-table :data="pageList" stripe style="width: 100%;">
				<el-table-column prop="1" label="1"></el-table-column>
				<el-table-column prop="2" label="2"></el-table-column>
				<el-table-column prop="3" label="3"></el-table-column>
				<el-table-column prop="4" label="4"></el-table-column>
			</el-table>
			<div class="block">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage1"
				 :page-sizes="[10, 20, 30, 40]" :page-size="10" layout="total, sizes, prev, pager, next, jumper" :total="rawList.length"></el-pagination>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				rawList: [
					{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},{
						'1' : '1',
						'2' : '2',
						'3' : '3',
						'4' : '4'
					},
				],
				currentPage1: 1,
				pageSize: 10,
				pageList: []
			};
		},

		mounted() {
			this.axios.get("/api/xxx").then(response => {
				this.rawList = response.data;
				this.currentChangePage(this.rawList, 1);
			});
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
			}
		}
	};
</script>
