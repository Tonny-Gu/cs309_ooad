<template>
	<div id='ContestMenu'>
		<div id='Annoucement'>
			<el-carousel :interval="6000" height="150px" id='annoucementText'>
				<el-carousel-item v-for="item in annoucement" :key="item">
					<h3 class="medium" style="word-wrap:break-word;" v-html="item"></h3>
				</el-carousel-item>
			</el-carousel>
		</div>
		<div id="table-content"  style="overflow: hidden">
			<el-table :data="pageList" stripe style="width: 100%;top: 20px; height: 90%; ">
				<el-table-column prop="contestName" label="Contest">
					<template slot-scope="scope">
						<el-button type="text" @click="accessAnswerPage(scope.row.contestId)">{{scope.row.contestTitle}}</el-button>
					</template>
				</el-table-column>
				<el-table-column prop="StartTime" label="DeadLine"></el-table-column>
				<el-table-column prop="EndTime" label="Start time"></el-table-column>
			</el-table>
			<br />
			<div class="block">
				<el-pagination id='PageControl' @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage1"
				 :page-sizes="[10, 20, 30, 40]" :page-size="10" layout="total, sizes, prev, pager, next, jumper" :total="rawList.length"></el-pagination>
			</div>
			<br />
		</div>

		<div class="selfInfo">
			<br />
			<div style="text-align: left; margin-left: 5%;">User Name: {{this.$store.state.myUser.nickname}}</div>
      <br/>
			<div style="text-align: left; margin-left: 5%;">Account Number: {{this.$store.state.myUser.username}}</div>
      <br/>
			<div style="text-align: left; margin-left: 5%;">Role: {{this.$store.state.myUser.role}}</div>
			<br />
			<br />
			<div id='susRate' style="width: 100%;height: 60%;margin-top: 10%;"></div>
<!--			<div id='SubmitTime' style="width: 100%;height: 40%;"></div>-->
		</div>

	</div>
</template>

<script>
	import susfoot from "./susfoot.vue"
	import echarts from 'echarts'
  import api from '@/views/api'
	export default {
		data() {
			return {
				annoucement: [
					'Here is the first announcement.',
					'Here is the second announcement.',
					'Here is the third announcement',
				],
				rawList: [
        ],
				currentPage1: 1,
				pageSize: 10,
				pageList: [
        ],
				user_name: '123',
				Account_Number: '118118xx',
				nick_name: 'test',
				belong_class: 'class1',
				resultcharts: '',
				Results: ['Pass', 'TLE', 'False'],
				ResultsData: [{
						value: 335,
						name: 'Pass'
					},
					{
						value: 310,
						name: 'TLE'
					},
					{
						value: 120,
						name: 'False'
					}
				],
				timecharts: '',
				SubmitTime: ['00:00 - 02:00', '02:00 - 04:00', '04:00 - 06:00', '06:00 - 08:00', '08:00 - 10:00',
					'10:00 - 12:00', '12:00 - 14:00', '14:00 - 16:00', '16:00 - 18:00', '18:00 - 20:00', '20:00 - 22:00',
					'22:00 - 00:00'
				],
				SubmitData: [{
						value: 10,
						name: '00:00 - 02:00'
					},
					{
						value: 10,
						name: '02:00 - 04:00'
					},
					{
						value: 10,
						name: '04:00 - 06:00'
					},
					{
						value: 10,
						name: '06:00 - 08:00'
					},
					{
						value: 10,
						name: '08:00 - 10:00'
					},
					{
						value: 10,
						name: '10:00 - 12:00'
					},
					{
						value: 10,
						name: '12:00 - 14:00'
					},
					{
						value: 10,
						name: '14:00 - 16:00'
					},
					{
						value: 10,
						name: '16:00 - 18:00'
					},
					{
						value: 10,
						name: '18:00 - 20:00'
					},
					{
						value: 10,
						name: '20:00 - 22:00'
					},
					{
						value: 10,
						name: '22:00 - 00:00'
					}
				]
			}
		},
		components: {
			susfoot
		},
		mounted: function() {
		  this.getAllContest();
		  let table = document.getElementById('table-content')
      table.style.height = (window.innerHeight - 210) + 'px'
			this.$nextTick(function() {
				this.drawPassRate('susRate');
				// this.drawSubmitTime('SubmitTime');
			});
		},
		methods: {
			drawPassRate(id) {
				let colors = ['rgb(156,212,125)', 'rgb(205,205,205)', 'rgb(245,145,142)'];
				let i = 0;
				this.resultcharts = echarts.init(document.getElementById(id))
				this.resultcharts.setOption({
					tooltip: {
						trigger: 'item',
					},
					title: {
						text: 'submit result'
					},
					series: [{
						name: 'Results',
						type: 'pie',
						itemStyle: {
							normal: {
								color: function() {
									return colors[i++];
								},
							}
						},
						radius: ['50%', '70%'],
						avoidLabelOverlap: false,
						label: {
							normal: {
								show: false,
								position: 'center'
							},
							emphasis: {
								show: true,
								textStyle: {
									fontSize: '50',
									fontWeight: 'blod'
								}
							}
						},
						labelLine: {
							normal: {
								show: false
							}
						},
						data: this.ResultsData
					}]
				})
			},
			drawSubmitTime(id) {
				let i = 0;
				let colors = ['red', 'orange', 'yellow', 'green', 'blue', 'purple', 'brown', 'greenyellow', 'black', 'gray', 'pink',
					'cyan'
				];
				this.timecharts = echarts.init(document.getElementById(id));
				this.timecharts.setOption({
					tooltip: {
						trigger: 'item',
					},
					title: {
						text: 'submit time'
					},
					series: [{
						name: 'Results',
						type: 'pie',
						radius: ['50%', '70%'],
						avoidLabelOverlap: false,
						itemStyle: {
							normal: {
								color: function() {
									return colors[i++];
								},
							}
						},
						label: {
							normal: {
								show: false,
								position: 'center'
							},
							emphasis: {
								show: true,
								textStyle: {
									fontSize: '30',
									fontWeight: 'blod'
								}
							}
						},
						labelLine: {
							normal: {
								show: false
							}
						},
						data: this.SubmitData
					}]
				})
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
      getAllContest(){
        api.getAllContest().then(res =>{
          console.log(res)
          for (let i = 1 ; i < res.data.length; i++){
            let tmpdata = {
              contestId:res.data[i].id,
              contestTitle:res.data[i].name,
              StartTime:res.data[i].beginTime,
              EndTime:res.data[i].endTime,
            }
            this.rawList.push(tmpdata);
          }
          this.handleCurrentChange(1);
          console.log(this.rawList);
        })
      },
      accessAnswerPage(contestId){
			  this.$router.push({
          path: '/contestCoding/' + contestId
        })
      }
		},
		filters: {
			ellipsis(value) {
				if (!value) return ''
				if (value.length > 8) {
					return value.slice(0, 8) + '...'
				}
				return value
			}
		},

	}
</script>

<style>
	#ContestMenu {
		top: 60px;
		right: 0px;
		height: 100%;
		width: 100%;
		position: fixed;
		overflow: auto;
		background-color: white;
	}

	.selfInfo {
		margin-top: -150px;
		margin-right: 10%;
		float: right;
		width: 15%;
		background-color: white;
		height: 600px;
		box-shadow: 0 10px 10px rgba(0, 0, 0, 0.1);
	}

	#Annoucement {
		width: 60%;
		margin-left: 7%;
    margin-top: 1%;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
	}

	#table-content {
		float: left;
		width: 60%;
    height: 100%;
    margin-left: 7%;
    overflow: auto;
	}

	#contestPagination {}

	.el-carousel__item {
		overflow: auto;
    background-color: rgb(250,250,250);
	}

	.el-carousel__item h3 {
		text-align: left;
		color: black;
		font-size: 18px;
		opacity: 0.75;
		line-height: 30px;
		margin: 0;
		/*overflow: auto;*/
	}

	.el-table .warning-row {
		background: oldlace;
	}

	.el-table .success-row {
		background: #f0f9eb;
	}
</style>
