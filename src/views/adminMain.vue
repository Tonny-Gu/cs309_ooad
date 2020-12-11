<template>
  <div id='adminMain'>
    <NavHeader></NavHeader>
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
