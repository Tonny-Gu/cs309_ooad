import Vue from 'vue'
import axios from 'axios'
axios.defaults.baseURL = 'https://dboj.mylab.cc:443/dboj/api'
axios.defaults.withCredentials = true
Vue.prototype.$http = axios

export default {
    login (data) {
        return common_interact('login', 'post', {data})
    },
    register (data){
        return common_interact('register', 'post', {data})
    },
    uploadQuestion(data){
        return common_interact('admin/question/upload','post',{data})
    },
    createContest(data){
        return common_interact('admin/contest/create','post',{data})
    },
    getAllQuestion(data){
        return common_interact('question','post',{data})},

    getQuestionById(id){
        return common_interact('/question/id','get',{
            params: {
                id: id
            }
        })
    },
    addQuestion(data){
        return common_interact('admin/contest/addquestion','post',{data})
    },
    getAllContest(){
        return common_interact('user/contest','get')
    },
    getAdminContest(){
      return common_interact('admin/contest','get')
    },
    getQuestionByContest(id){
        return common_interact('/question/contest','get',{
            params: {
                contest_id: id
            }
        })
    },
    handleLogout() {
        return common_interact('/logout','get')
    },
    submitCode(data){
        return common_interact('user/submit','post',{data})
    },
    getResult(data){
        return common_interact('user/submission/byId','post',{data})
    },
    uploadTestcase(data){
        return common_interact('admin/testcase/upload','post',{data})
    },
    joinContest(data){
        return common_interact('user/joinContest','post',{data})
    },
    getusersContest(id){
        return common_interact('user/get/contests','get',{
            params: {
                id: id
            }
        })
    },
    getAllsubmission(){
        return common_interact('admin/submission/all','get')
    },
    getMySubmission(id) {
        return common_interact('user/get/submission','get',{
            params: {
                id: id,
            }
        })
    },


    getLatestSubmitCode(data){
        return common_interact('user/get/submission','post',{data})
    },
    getSubmissionCondi(data){
        return common_interact('user/submission','post',{data})
    },
    getSubmissionByContest(data){
        return common_interact('admin/submission/contest','post',{data})
    },
    getSubmissionByQuestion(data){
        return common_interact('admin/submission/question','post',{data})
    },

    uploadNotice(data){
        return common_interact('admin/notice/upload','post',{data})
    },
    getAllNotice(){
        return common_interact('notice/all','get')
    },
    deleteNotice(data){
        return common_interact('admin/notice/cancel','post',{data})
    },
    enableNotice(data){
        return common_interact('admin/notice/enable','post',{data})
    },
    getSubmissionForTeacher(data){
        return common_interact('admin/submission/all','post',{data})
    },
    getContestRank(id){
        return common_interact('user/contest/rank','get',{
            params: {
                contest_id: id
            }
        })
    },
    getRankForStudent(contest_id,question_id,user_id){
        return common_interact('user/score','get',{
            params: {
                contest_id: contest_id,
                question_id:question_id,
                user_id:user_id,
            }
        })
    },
    modifyContest(data){
        return common_interact('admin/contest/modify','post',{data})
    },
    modifyQuestion(data){
        return common_interact('admin/question/modify','post',{data})
    },
    getQuestionByDB(dbType){
        return common_interact('question/db','get',{
            params: {
                dbType:dbType,
            }
        })
    },
    getQuestionByName(name){
        return common_interact('/question/name','get',{
            params: {
                name: name
            }
        })
    },
    getUserbyusername(data){
        return common_interact('admin/getUser','post',{data})
    },
    sendCode(data){
        return common_interact('send/code','post',{data})
    },
    changeNickname(data){
        return common_interact('user/modify/nickname','post',{data})
    },
    changePassword(data){
        return common_interact('modify/password','post',{data})
    },
    getDataAna(data){
      return common_interact('user/data/submission','post',{data})
    },
    getDoneQuestions(data){
      return common_interact('user/data/question','post',{data})
    },
    changePermission(data){
      return common_interact('admin/modify/role','post',{data})
    },
    getHistoryData(data){
      return common_interact('data/time','post',{data})
    },
    getContestsById(id){
      return common_interact('contest/id','get',{
        params: {
          id:id,
        }
    })
    },
    delQuestion(data){
      return common_interact('admin/contest/delquestion','post',{data})
    },
    downloadgrade(data){
      return common_interact('super/score/download','post',{data})
    }

}

function common_interact(url, method, options){
    if (options !== undefined) {
        var {params = {}, data = {}} = options
    } else {
        params = data = {}
    }
    return new Promise((resolve, reject) => {
        axios({
            url,
            method,
            params,
            data,
        }).then(res => {
            if (res.data.error !== undefined) {             
            } else {
                resolve(res)
            }
        })
    })
}
