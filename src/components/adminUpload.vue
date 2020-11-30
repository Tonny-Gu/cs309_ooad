<template>
	<div id="adminUpload">
    <el-form  :ref="form" :rules="rules" :model="form" label-width="110px" style="overflow: hidden;margin-left: 300px;margin-top: 80px">
      <el-col :span="12">
        <el-form-item label="题面描述: ">
          <el-upload class="upload-demo"
                     ref="upload"
                     :limit=1
                     action="this is fake action"
                     accept=".md"
                     :on-preview="handlePreviewF1"
                     :on-remove="handleRemoveF1"
                     :on-change="handleChangeF1"
                     :on-exceed="handleExceedF1"
                     :file-list="fileListF1"
                     :auto-upload="false">
                    <el-button slot="trigger" size="small" type="primary">Select File</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="拓展文件: " prop="apply_reason">
          <el-radio v-model="radio" label="1">有</el-radio>
          <el-radio v-model="radio" label="2">无</el-radio>
        </el-form-item>
        <el-form-item label="上传材料：" prop="files">

        </el-form-item>
        <el-form-item label="审批人：" prop="approve_person" >
          <el-select v-model="form.approve_person" placeholder="请选择" style="display: block">
            <el-option v-for="item in approveOptions" :key="item.PersonId" :label="item.Name" :value="item.PersonId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button class="submit-button"  @click="submitUpload(form)">提交</el-button>
        </el-form-item>
      </el-col>
    </el-form>

<!--  <div id='question_file' style="float:left;width: 10%">-->
<!--      <h1>Question Description</h1>-->
<!--      <el-upload-->
<!--          class="upload-demo"-->
<!--          ref="upload"-->
<!--          :limit=1-->
<!--          action="this is fake action"-->
<!--          accept=".md"-->
<!--          :on-preview="handlePreview"-->
<!--          :on-remove="handleRemove"-->
<!--          :on-change="handleChange"-->
<!--          :on-exceed="handleExceed"-->
<!--          :file-list="fileList"-->
<!--          :auto-upload="false">-->
<!--        <el-button slot="trigger" size="small" type="primary">Select File</el-button>-->
<!--     </el-upload>-->
<!--  </div>-->
<!--    <div id='extension_file' style="float:left;width: 10%">-->
<!--      <h1>Extension File</h1>-->
<!--      <el-upload-->
<!--          class="upload-demo"-->
<!--          ref="upload"-->
<!--          :limit=1-->
<!--          action="this is fake action"-->
<!--          accept=".md"-->
<!--          :on-preview="handlePreview"-->
<!--          :on-remove="handleRemove"-->
<!--          :on-change="handleChange"-->
<!--          :on-exceed="handleExceed"-->
<!--          :file-list="fileList"-->
<!--          :auto-upload="false">-->
<!--        <el-button slot="trigger" size="small" type="primary">Select File</el-button>-->
<!--      </el-upload>-->
<!--    </div>-->
<!--    <div id='ans_file' style="float:left;width: 10%">-->
<!--      <h1>Extension File</h1>-->
<!--      <el-upload-->
<!--          class="upload-demo"-->
<!--          ref="upload"-->
<!--          :limit=1-->
<!--          action="this is fake action"-->
<!--          accept=".md"-->
<!--          :on-preview="handlePreview"-->
<!--          :on-remove="handleRemove"-->
<!--          :on-change="handleChange"-->
<!--          :on-exceed="handleExceed"-->
<!--          :file-list="fileList"-->
<!--          :auto-upload="false">-->
<!--        <el-button slot="trigger" size="small" type="primary">Select File</el-button>-->
<!--      </el-upload>-->
<!--    </div>-->
<!--    <div id='testcase_file' style="float:left;width: 10%">-->
<!--      <h1>Extension File</h1>-->
<!--      <el-upload-->
<!--          class="upload-demo"-->
<!--          ref="upload"-->
<!--          :limit=1-->
<!--          action="this is fake action"-->
<!--          accept=".md"-->
<!--          :on-preview="handlePreview"-->
<!--          :on-remove="handleRemove"-->
<!--          :on-change="handleChange"-->
<!--          :on-exceed="handleExceed"-->
<!--          :file-list="fileList"-->
<!--          :auto-upload="false">-->
<!--        <el-button slot="trigger" size="small" type="primary">Select File</el-button>-->
<!--      </el-upload>-->
<!--    </div>-->
<!--		<div id='db_file'>-->
<!--      建库文件-->
<!--			<el-upload class="upload-demo" drag action="https://jsonplaceholder.typicode.com/posts/" multiple>-->
<!--				<i class="el-icon-upload"></i>-->
<!--				<div class="el-upload__text">将建库文件拖到此处，或<em>点击上传</em></div>-->
<!--				<div class="el-upload__tip" slot="tip">只能上传.sql文件</div>-->
<!--			</el-upload>-->
<!--			<el-table :data="pageList" height="92%" stripe>-->
<!--				<el-table-column prop="db_time" label="提交时间">-->
<!--				</el-table-column>-->
<!--				<el-table-column prop="db_name" label="文件名">-->
<!--				</el-table-column>-->
<!--				<el-table-column prop="db_result" label="提交结果">-->
<!--				</el-table-column>-->
<!--			</el-table>-->
<!--		</div>-->
<!--		<div id='answer_file'>-->
<!--      标准答案-->
<!--			<el-upload class="upload-demo" drag action="https://jsonplaceholder.typicode.com/posts/" multiple>-->
<!--				<i class="el-icon-upload"></i>-->
<!--				<div class="el-upload__text">将标准答案文件拖到此处，或<em>点击上传</em></div>-->
<!--				<div class="el-upload__tip" slot="tip">只能上传.sql文件</div>-->
<!--			</el-upload>-->
<!--			<el-table :data="pageList" height="92%" stripe>-->
<!--				<el-table-column prop="answer_time" label="提交时间">-->
<!--				</el-table-column>-->
<!--				<el-table-column prop="answer_name" label="文件名">-->
<!--				</el-table-column>-->
<!--				<el-table-column prop="answer_result" label="提交结果">-->
<!--				</el-table-column>-->
<!--			</el-table>-->
<!--		</div>-->
<!--    <div id="progress">-->
<!--      <el-steps :active="active" finish-status="success">-->
<!--        <el-step title="步骤 1"></el-step>-->
<!--        <el-step title="步骤 2"></el-step>-->
<!--        <el-step title="步骤 3"></el-step>-->
<!--      </el-steps>-->
<!--      <el-button  @click="next">下一步</el-button>-->
<!--    </div>-->


	</div>
</template>
<script>
import api from '@/views/api'
	export default {
		data() {
			return {
				fileListF1: [],
        pageList: [],
        input: '',
        active:0,
        dialogVisible: false,
        picUrl:'',
        radio:"",
        uploadForm: new FormData(),
        form: {
          files:'',
          seal_type:'',
          apply_reason:'',
          approve_person:'',
        }
			};
		},
		methods: {
		  next() {
        if (this.active++ > 2) this.active = 0;
      },
      // submitUpload() {
      //   let fileName = this.fileList[0].name
      //   let pos = fileName.lastIndexOf('.')
      //   let lastName = fileName.substring(pos, fileName.length)
      //   if (lastName.toLowerCase() !== '.md') {
      //     this.$message.error('文件必须为markdown')
      //     this.fileList = []
      //     // this.resetCompressData()
      //     return
      //   }else {
      //     let formdata = new FormData()
      //     formdata.append("questionFile", this.fileList[0].raw)
      //     formdata.append("author", "1")
      //     formdata.append("degree", "Mid")
      //     formdata.append("dbType", "ALL")
      //
      //     api.uploadQuestion(formdata).then(res => {
      //       console.log(res)
      //     })
      //   }
      // },
      handleRemoveF1(file, fileList) {
        console.log(file, fileList);
      },
      handlePreviewF1(file) {
        console.log(file);
      },
      handleChangeF1(file, fileList){
        this.fileList = fileList

      },
      handleExceedF1(){
        alert("file number exceed, limit 1")
      },
		}
	}
</script>

<style>
	#adminUpload {
    padding:1%;
		margin-top: 2px;
	}

	#question_file {
		margin-left: 20%;
		margin-top: 5%;
	}
  #extension_file {
    margin-left: 20%;
    margin-top: 5%;
  }
  #ans_file{
    margin-left: 20%;
    margin-top: 5%;
  }
  #testcase_file{
    margin-left: 20%;
    margin-top: 5%;
  }

  #progress{
    align-items: center;
    width: 60%;
    margin-left: 30%;
    margin-top: 30%;
  }



	#answer_file {
		float: left;
		margin-left: 2%;
		margin-top: 3%;
	}

	#db_file {
		float: left;
		margin-left: 2%;
		margin-top: 3%;
	}
</style>
