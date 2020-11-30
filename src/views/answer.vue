<template>
	<div class="answer">
		<NavHeader></NavHeader>
		<div id='Main'>
			<div id='question_block'>
				<el-menu class="el-menu-demo" mode="horizontal" >
					<router-link to='/answer/:id' tag="el-menu-item" v-on:click.native="Show=true">Question Description</router-link>
					<router-link to='/answer/submissions' tag="el-menu-item" v-on:click.native="Show=false">Submission Record</router-link>
				</el-menu>
        <div id='question_description' v-html="questionContent" v-if="Show" style="text-align: left;margin-left: 2%;">

        </div>
				<router-view v-else></router-view>
			</div>
			<div id='edit_block' style="text-align: -webkit-auto;">
				<div id="edit_header">
					<div id="language">
						<el-select v-model="language" filterable placeholder="Please choose the language you use" style="width: 130px;margin-top: 10px;margin-right: 10px;"
						 size="large">
							<el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</div>
					<div id='format_div'>
						<el-tooltip class="item" effect="dark" content="Format you code" placement="top-start" style="margin-top: 10px;">
							<el-button plain v-on:click='format()'>Format</el-button>
						</el-tooltip>
					</div>
          <div id='upload' style="margin-top: 1%;">
            <el-tooltip class="item" effect="dark" content="Submit you Code" placement="top-start">
              <el-button style="width: 90px;" @click="submitCode">Submit<i class="el-icon-upload el-icon-right"></i></el-button>
              <!--              <el-link style='font-size: 20px; margin-top: 20px;margin-left: 8px;color: greenyellow;'><i class='el-icon-upload'></i>Submit</el-link>-->
            </el-tooltip>
          </div>
          <div id='load_history' style="margin-top: 1%;">
            <el-tooltip class="item" effect="dark" content="Reload the last code" placement="top-start">
              <el-button icon="el-icon-edit"></el-button>
              <!--              <el-link style='font-size: 20px; margin-top: 20px;margin-left: 8px;color: blue;'><i class='el-icon-info'></i>Reload</el-link>-->
            </el-tooltip>
          </div>
          <div id='clear' style="margin-top: 1%;">
            <el-tooltip class="item" effect="dark" content="Clear the code" placement="top-start">
              <el-button icon="el-icon-delete"></el-button>
              <!--              <el-link style='font-size: 20px; margin-top: 20px; color: red;'><i class='el-icon-delete'></i>Clear</el-link>-->
            </el-tooltip>
          </div>
				</div>
				<div>
					<textarea ref="mycode" class="codesql" v-model="value" autoRefresh="true">
			    </textarea>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	// @ is an alias to /src
	import NavHeader from '../components/NavHeader.vue'
	import questionDescription from '../components/questionDescr.vue'
	import sqlFormatter from "sql-formatter";
	import "codemirror/theme/ambiance.css";
	import "codemirror/lib/codemirror.css";
	import "codemirror/addon/hint/show-hint.css";
	import api from '@/views/api'
	let CodeMirror = require("codemirror/lib/codemirror");
	require("codemirror/addon/edit/matchbrackets");
	require("codemirror/addon/selection/active-line");
	require("codemirror/mode/sql/sql");
	require("codemirror/addon/hint/show-hint");
	require("codemirror/addon/hint/sql-hint");

	export default {

		name: 'answer',
		methods: {
			setVal() {
				if (this.editor) {
					if (this.value === '') {
						this.editor.setValue('')
					} else {
						this.editor.setValue(this.value)
					}

				}
			},
			//代码格式化方法
			format() {
				/*获取文本编辑器内容*/
				let sqlContent = "";
				sqlContent = this.editor.getValue();
				/*将sql内容进行格式后放入编辑器中*/
				this.editor.setValue(sqlFormatter.format(sqlContent));
			},
			getQuestionById(){
			  api.getQuestionById(this.$route.params.id).then(res =>{
			    this.questionContent = atob(res.data.content)
        })

      }
		},
		components: {
			NavHeader,
			questionDescription
		},
		data() {
			return {
				Show: true,
				editor: null,
				options: [{
					value: '选项1',
					label: 'PostgreSQL'
				}, {
					value: '选项2',
					label: 'MySQL'
				}, {
					value: '选项3',
					label: 'SQLite'
				}],
				language: 'MySQL',
        questionContent: '',
			}
		},
		props: {
			value: {
				type: String,
				default: ''
			},
			sqlStyle: {
				type: String,
				default: 'default'
			},
			readOnly: {
				type: [Boolean, String]
			}
		},
		watch: {
			newVal(newV, oldV) {
				if (this.editor) {
					this.$emit('changeTextarea', this.editor.getValue())
				}
			}
		},
		computed: {
			newVal() {
				if (this.editor) {
					return this.editor.getValue()
				}
			}
		},

		mounted() {
		  this.getQuestionById();
			let edit_block = document.getElementById('edit_block');
			edit_block.style.height = (window.innerHeight - 60) + "px";
			let question_block = document.getElementById('question_block');
			question_block.style.height = (window.innerHeight - 60) + "px";
			let mime = 'text/x-mariadb'
			//let theme = 'ambiance'//设置主题，不设置的会使用默认主题
			this.editor = CodeMirror.fromTextArea(this.$refs.mycode, {
				value: this.value,
				mode: mime, //选择对应代码编辑器的语言，我这边选的是数据库，根据个人情况自行设置即可
				indentWithTabs: true,
				indentUnit: 2,
				smartIndent: true,
				lineNumbers: true,
				matchBrackets: true,
				cursorHeight: 1,
				lineWrapping: true,
				readOnly: this.readOnly,
				//theme: '',
				// autofocus: true,
				extraKeys: {
					'Ctrl': 'autocomplete'
				}, //自定义快捷键
				hintOptions: { //自定义提示选项
					// 当匹配只有一项的时候是否自动补全
					completeSingle: false,
					// tables: {
					//     users: ['name', 'score', 'birthDate'],
					//     countries: ['name', 'population', 'size']
					// }
				}
			})
			//代码自动提示功能，记住使用cursorActivity事件不要使用change事件，这是一个坑，那样页面直接会卡死
			this.editor.on('inputRead', () => {
				this.editor.showHint()
			})
		}
	}
</script>
<style>
	#format_div {
		float: left;
	}

	#user_choice {
		float: right;
		font-size: 40px;
	}

	.el-dropdown-link {
		cursor: pointer;
		color: #409EFF;
	}

	.el-icon-arrow-down {
		font-size: 12px;
	}

	#Main {
		overflow: hidden;
	}

	#edit_block {
		top: 60px;
		right: 0.5%;
		width: 49%;
		position: fixed;
		float: left;
		overflow: auto;
		box-shadow: 0 15px 10px rgba(0, 0, 0, 0.7);
	}

	#question_block {
		top: 60px;
		left: 0.5%;
		height: 100%;
		width: 49%;
		position: fixed;
		background-size: cover;
		float: left;
		overflow: auto;
		box-shadow: 0 15px 10px rgba(0, 0, 0, 0.7);
	}


	#edit_header {
		top: 50px;
		width: 100%;
		height: 60px;
	}

	#language {
		float: left;
	}

	#upload {
		float: right;
		margin-left: 5px;
		margin-right: 1%;
	}

	#clear {
		float: right;
		margin-left: 5px;
	}

	#load_history {
		float: right;
		margin-left: 5px;
	}

	.CodeMirror {
		border: 1px solid #eee;
		height: calc(100vh - 90px);
		font-size: 20px;
	}
</style>
