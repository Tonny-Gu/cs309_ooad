<template>
  <div>
    <NavHeader></NavHeader>
    <div style="padding: 0;margin: 0;" v-if="this.$route.fullPath === '/'">
      <div id="studentHeader" style="padding: 1px;"
           :style="'height: ' + clientHeight * 50 / 100 + 'px;width: ' + clientWidth + 'px;'">
        <div id="login" :style="'height: ' + clientHeight * 50 / 100 + 'px;width: ' + clientWidth + 'px;'">
        <vue-particles
          class="login-bg"
          color="#39AFFD"
          :particleOpacity="0.7"
          :particlesNumber="100"
          shapeType="circle"
          :particleSize="4"
          linesColor="#8DD1FE"
          :linesWidth="1"
          :lineLinked="true"
          :lineOpacity="0.4"
          :linesDistance="150"
          :moveSpeed="3"
          :hoverEffect="true"
          hoverMode="grab"
          :clickEffect="true"
          clickMode="push"
          :style="'height: ' + clientHeight * 50 / 100 + 'px;width: ' + clientWidth + 'px;'"
      >
      </vue-particles>
        </div>
        <div class="vintage" style="font-size: 50px;" :style="'margin-top:-' + clientHeight * 20 / 100 + 'px;'">Sustech SQLitz</div>
      </div>
      <div v-if="!mobileFlag">
        <li v-for="item in cards_data" v-bind:key="item.id" style="float: left; list-style:none; " >
          <div id="zyq" class="teacher" :style="'margin-left: ' + ( firstClientWidth - 5 * 200 ) / 6 + 'px;'"
               >
            <el-card class="box-card" :body-style="{padding: 0}">
              <div class="cardHeader">
                <el-image :src=item.pic_src :style="{height: imageHeight + 'px' }" @load="setheight">
                </el-image>
                <div>{{item.name}}</div>
              </div>
              <div class='identity'>{{item.identity}}</div>
              <el-divider class='divider'></el-divider>
              <div class='Email'>
                E-Mail: {{item.email}}
              </div>
              <div class='qq'>
                QQ: {{item.qq}}
              </div>
              <div class='WeChat'>
                WeChat: {{item.wechat}}
              </div>
            </el-card>
          </div>
        </li>
      </div>
    </div>
    <router-view v-else></router-view>
  </div>
</template>

<script>
import NavHeader from "./NavHeader"
import api from "@/views/api";
import qs from "qs";
import VueApexCharts from "vue-apexcharts";
export default {
  components: {
    NavHeader
  },
  data() {
    return {
      mobileFlag: false,
      imageHeight: '',
      firstClientWidth: document.body.clientWidth,
      clientHeight: document.documentElement.clientHeight,
      clientWidth: document.body.clientWidth,
      cards_data:[
        {
          id: 1,
          name: '姚星河',
          pic_src: require('../assets/yxh.jpg'),
          identity: '学生助理',
          email: '11712406@mail.sustech.edu.cn',
          qq: 'secret',
          wechat: 'secret'
        },
        {
          id: 2,
          name: '朱悦铭',
          pic_src: require('../assets/zym.jpg'),
          identity: '实验课老师',
          email: 'Zhuym@sustech.edu.cn',
          qq: 'secret',
          wechat: 'secret'
        },
        {
          id: 3,
          name: '张煜群',
          pic_src: require('../assets/zyq.jpg'),
          identity: '任课老师',
          email: 'Zhangyq@sustech.edu.cn',
          qq: 'secret',
          wechat: 'secret'
        },
        {
          id: 4,
          name: '朱悦铭',
          pic_src: require('../assets/zym.jpg'),
          identity: '实验课老师',
          email: 'Zhuym@sustech.edu.cn',
          qq: 'secret',
          wechat: 'secret'
        },
        {
          id: 5,
          name: '胡青翘',
          pic_src: require('../assets/hqq.jpg'),
          identity: '学生助理',
          email: '11711807@sustech.edu.cn',
          qq: 'secret',
          wechat: 'secret'
        },
      ]
    }
  },
  methods: {
    _isMobile() {
      let flag = navigator.userAgent.match(
        /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
      );
      return flag;
    },
    setheight(event) {
      let image = event.target;
      this.imageHeight = image.width;
    },
  },
  mounted() {
    if(this._isMobile() !== null){
      this.mobileFlag = true;
    }
    this.clientWidth = document.body.clientWidth;
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

.teacher {
  float: left;
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
  font-size: 10px;
}

.qq {
  margin-top: 10px;
  font-size: 10px;
}

.WeChat {
  margin-top: 10px;
  font-size: 10px;
}

.identity {
  margin-top: 10px;
  font-size: 10px;
}

#studentHeader {
  /*background-image: url(../assets/background.jpg);*/
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
