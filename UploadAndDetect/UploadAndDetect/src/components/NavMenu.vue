<template>
  <div class="left-nav" style="height: 100%">
    <el-row class="tac" style="height: 100%">
      <el-col :span="24" style="height: 100%">
        <el-menu
          default-active="2"
          class="el-menu-vertical-demo"
          @open="handleOpen"
          @close="handleClose"
          unique-opened
          router
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
          style="height: 100%"
        >
        <el-submenu v-for="item in menu" :index="item.id" :key="item.id">
            <template slot="title">
                <span v-text="item.name"></span>
                <i class="item.class"></i>
            </template>
            <el-menu-item-group class="over-hide" v-for="sub in item.sub" :key="sub.componentName">
                <el-menu-item :index="sub.componentName" v-text="sub.name">
                </el-menu-item>
            </el-menu-item-group>
        </el-submenu>
        <!-- <el-menu-item index="1">
              <i class="el-icon-location"></i>
             <span slot="title">导航一</span>
          </el-menu-item>
          <el-menu-item index="2">
            <i class="el-icon-menu"></i>
            <span slot="title">导航二</span>
          </el-menu-item>
          <el-menu-item index="3">
            <i class="el-icon-document"></i>
            <span slot="title">导航三</span>
          </el-menu-item>
          <el-menu-item index="4">
            <i class="el-icon-setting"></i>
            <span slot="title">导航四</span>
          </el-menu-item> -->
        </el-menu>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import menu from '@/config/menu-config'
export default {
    data(){
        return {
            menu:menu
        }
    },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
  },
};
</script>