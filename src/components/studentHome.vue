<template>
  <div>
    <NavHeader></NavHeader>
    <div style="padding: 0;margin: 0;" v-if="this.$route.fullPath === '/'">
      <div id="studentHeader" style="padding: 1px;"
           :style="'height: ' + clientHeight * 50 / 100 + 'px;width: ' + clientWidth + 'px;'">
        <div class="vintage" style="font-size: 50px;" :style="'margin-top:' + clientHeight * 20 / 100 + 'px;'">Welcome to DBOJ!</div>
      </div>
      <div style="position: absolute">
        <div id="zyq" class="teacher_card"
             :style="'margin-left: ' + clientWidth * 20 / 100+ 'px; margin-top: ' + clientHeight * 5 / 100 + 'px;'">
          <el-card class="box-card" :body-style="{padding: 0}">
            <div class="cardHeader">
              <el-image :src="require('../assets/zyq.jpg')" :style="{height: imageHeight + 'px' }" @load="setheight">
              </el-image>
              <div>张煜群</div>
            </div>
            <div class='identity'>任课老师</div>
            <el-divider class='divider'></el-divider>
            <div class='Email'>
              E-Mail: Zhangyq@sustech.edu.cn
            </div>
            <div class='qq'>
              QQ: secret
            </div>
            <div class='WeChat'>
              WeChat: secret
            </div>
          </el-card>
        </div>
        <div id="zym" class="teacher_card"
             :style="'margin-left: ' + clientWidth * 20 / 100+ 'px; margin-top: ' + clientHeight * 5 / 100 + 'px;'">
          <el-card class="box-card" :body-style="{padding: 0}">
            <div class="cardHeader">
              <el-image :src="require('../assets/zym.jpg')" :style="{height: imageHeight + 'px' }" @load="setheight">
              </el-image>
              <div>朱悦铭</div>
            </div>
            <div class='identity'>助理老师</div>
            <el-divider class='divider'></el-divider>
            <div class='Email'>
              E-Mail: Zhangyq@sustech.edu.cn
            </div>
            <div class='qq'>
              QQ: secret
            </div>
            <div class='WeChat'>
              WeChat: secret
            </div>
          </el-card>
        </div>
      </div>
    </div>
    <router-view v-else></router-view>
  </div>
</template>

<script>
import NavHeader from "./NavHeader"

export default {
  components: {
    NavHeader
  },
  data() {
    return {
      imageHeight: '',
      clientHeight: document.documentElement.clientHeight,
      clientWidth: document.body.clientWidth,
    }
  },
  methods: {
    setheight(event) {
      let image = event.target;
      this.imageHeight = image.width;
    },
  },
  mounted() {
    console.log(this.$route.fullPath)
    this.clientWidth = document.body.clientWidth;
    // 监听window的resize事件．在浏览器窗口变化时再设置下区域高度．
    const _this = this;
    window.onresize = function temp() {
      _this.clientWidth = document.body.clientWidth;
      _this.clientHeight = document.documentElement.clientHeight;
    };

  }
}
</script>

<style scoped>
.el-image {
  width: 34%;
  border-radius: 50%;
}

html, body {
  padding: 0;
  margin: 0;
  width: 100%;
  height: 100%;
}

.teacher_card {
  float: left;
  margin-left: 200px;
  margin-top: 60px;
}

.box-card {
  width: 200px;
}

.cardHeader {
  background-image: url(../assets/home.png);
  background-size: cover;
  margin: 0;
  padding: 10px;
}

.divider {
  margin-top: 10px;
}

.Email {
  margin-top: -10px;
  font-size: 1px;
}

.qq {
  margin-top: 10px;
  font-size: 1px;
}

.WeChat {
  margin-top: 10px;
  font-size: 1px;
}

.identity {
  margin-top: 10px;
  font-size: 1px;
}

#studentHeader {
  background-image: url(../assets/background.jpg);
  background-size: cover;
  margin: 0;
}

.vintage {
  color: transparent;
  background-color : black;
  text-shadow : rgba(255,255,255,0.5) 0 5px 6px, rgba(255,255,255,0.2) 1px 3px 3px;
  -webkit-background-clip : text;
}
</style>