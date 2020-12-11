import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'
import api from '../views/api'
import types from "@/store/types";


Vue.use(Vuex)

const  rootState = {
  website:{},
  modalStatus:{
    mode:'login',
    visible:false
  },
  myUser:{}
}
const rootGetters = {
  'website' (state) {
    return state.website
  },
  'modalStatus' (state) {
    return state.modalStatus
  },
  'myUser' (state){
    return state.myUser
  }
}
const rootMutations = {
  [types.CHANGE_MODAL_STATUS] (state, {mode, visible}) {
    if (mode !== undefined) {
      state.modalStatus.mode = mode
    }
    if (visible !== undefined) {
      state.modalStatus.visible = visible
    }
  },
  [types.SHAREUSER] (state,user) {
    state.myUser = user
  }

}

const rootActions = {
  changeModalStatus ({commit}, payload) {
    commit(types.CHANGE_MODAL_STATUS, payload)
  },
  shareUser ({commit},user){
    commit(types.SHAREUSER, user)
  }
}

export default new Vuex.Store({
  modules: {
    user
  },
  state:sessionStorage.getItem('rootState') ? JSON.parse(sessionStorage.getItem('rootState')): {
    //id
    website:{},
    modalStatus:{
      mode:'login',
      visible:false
    },
    myUser:{}
  },
  getters: rootGetters,
  mutations: rootMutations,
  actions: rootActions,
})
export { types }
