<template>
	<div id='adminMain'>
		<NavHeader></NavHeader>
		<div id='adminLeft'>
			<el-menu default-active="1" class="el-menu-vertical-demo" background-color="#545c64" text-color="#fff"
			 active-text-color="#ffd04b" style="width: 10%;">
				<router-link to="/adminMain/adminUpload">
					<el-menu-item index="1" id='titlelist'>
						<i class="el-icon-menu"></i>
						<template slot="title">
							Upload Question
						</template>
					</el-menu-item>
				</router-link>
				<router-link to="/adminMain/createContest">
					<el-menu-item index="2" id='titlelist'>
						<i class="el-icon-menu"></i>
						<template slot="title">
							Create Contest
						</template>
					</el-menu-item>
				</router-link>
				<router-link to="/adminMain/adminUpload">
					<el-menu-item index="3" id='titlelist'>
						<i class="el-icon-menu"></i>
						<template slot="title">
							Students' Submission Record
						</template>
					</el-menu-item>
				</router-link>
			</el-menu>
		</div>
		<router-view></router-view>
	</div>
</template>

<script>
	import NavHeader from '../components/NavHeader.vue'
	export default {
		components: {
			NavHeader
		}
	}
</script>

<style>
	#adminLeft {
		float: left;
		left: 0;
		top: 60px;
		height: 100%;
		width: 10%;
		position: fixed;
		height: 100%;
		overflow: auto;
		background-color: #545c64;
	}
</style>
