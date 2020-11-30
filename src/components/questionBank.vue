<template>
	<el-container>
		<router-view></router-view>
	</el-container>
</template>

<script>
	export default {
		mounted: function(){
			let h = document.getElementById('titlelist');
		}
	}
</script>

<style>
	#homeMenu {
		float: left;
		left: 0;
		top: 60px;
		height: 100%;
		width: 10%;
		position: fixed;
		overflow: auto;
		background-color: #545c64;
	}
</style>
