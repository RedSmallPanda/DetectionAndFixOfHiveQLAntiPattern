<template>
  <div>
    <h1>Enter the SQL Below</h1>
    <el-input
      type="textarea"
      :autosize="{ minRows: 5, maxRows: 25 }"
      placeholder="请输入HiveQL"
      v-model="hiveQL"
    >
    </el-input>
    <div class="detect">
      <el-button type="primary" v-on:click="detect">Detect</el-button>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import NavMenu from "@/components/NavMenu.vue";
export default {
  name: "Detect",
  components: { Header, NavMenu },
  data() {
    return {
      hiveQL: "",
      api1url: this.common.api1url,
    };
  },
  methods: {
    detect() {
      var _this = this; //指定this
      this.$axios({
        //创建接口
        methods: "get", //类型为get请求
        url: _this.api1url+'/astCheck', //请求的接口地址
        params:{
          hiveql: this.hiveQL,
        }
      }).then(function (response) {
        //请求成功返回
        _this.focus = response.data.focus; //数据打包，打包在data中创建的数组 我这里是focus数组
        console.log(response.data); //打印请求的数据
        this.$router.push({
          path: `/result`,
        });
      });
    },
  },
};
</script>

<style scoped>
.detect {
  float: right;
  margin-top: 10px;
}
</style>
