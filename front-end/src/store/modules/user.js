import api from '@/views/api'
import storage from "@/utils/storage";
import types from "@/store/types";

const state = {
    profile:{}
}

const getters = {
    user: state => state.profile.user || {},
    profile: state => state.profile,
}
const mutations = {
    [types.CHANGE_PROFILE](state,{profile}){
        state.profile = profile
    }
}

const actions = {

}
export default {
    state,
    getters,
    actions,
    mutations
}
