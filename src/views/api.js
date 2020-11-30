import Vue from 'vue'
import axios from 'axios'
axios.defaults.baseURL = 'http://glab2.mylab.cc:11081/dboj/api'
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
    userGetContest(userID){
        return common_interact('/user/get/contests','get',{
            params: {
                id: userID
            }
        })
    },
    createContest(data){
        return common_interact('admin/contest/create','post',{data})
    },
    getAllQuestion(withContent){
        return common_interact('question','get',{
            params: {
                withContent: withContent
            }
    })},
    getQuestionByName(name){
        return common_interact('/question/name','get',{
            params: {
                name: name
            }
        })
    },
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
        return common_interact('/contest','get')
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
    getResult(id){
        return common_interact('user/submission/byId','get',{
            params: {
                id: id
            }
        })
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
            // API正常返回(status=20x), 是否错误通过有无error判断
            if (res.data.error !== undefined) {
                // 若后端返回为登录，则为session失效，应退出当前登录用户
                // if (res.data.data.startsWith('Please login')) {
                //     store.dispatch('changeModalStatus', {'mode': 'login', 'visible': true})
                // }
            } else {
                resolve(res)
            }
        })
    })
}
