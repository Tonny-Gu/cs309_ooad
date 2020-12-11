<template>
	<div id='question_description' v-html="question">
		
	</div>
</template>

<script>
	export default{
		data(){
			return{
				question: '<div>Question</div>'
			}
		}
	}
</script>

<style>
	#question_description{
	}
</style>
