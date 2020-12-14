<template>
  <div id="app">
    <el-container>
      <el-header class="header">
        <vheader/>
      </el-header>
      <el-container :style="defaultHeight">
        <el-aside width="200px">
          <navmenu></navmenu>
          </el-aside>
      <el-main>
        <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import NavMenu from './components/NavMenu.vue'
import Header from './components/Header'
import api from './api/backend-api'

export default {
  name: 'app',
  components:{
    'navmenu': NavMenu,
    'vheader': Header
  },
  data() {
    return {
        defaultHeight: {
            height: ""
        }
    };
},
methods: {
    //定义方法，获取高度减去头尾
    getHeight() {
        this.defaultHeight.height = window.innerHeight - 90 + "px";
    }
},
created() {
    //页面创建时执行一次getHeight进行赋值，顺道绑定resize事件
    window.addEventListener("resize", this.getHeight);
    this.getHeight();
}

}

</script>


<style >
.html{
  height: 100%;
}
.header {
  background-color: #2970b6;
  color: #fff;
  line-height: 60px;
}
</style>
