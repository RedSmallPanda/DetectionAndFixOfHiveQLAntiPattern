<template>
  <div>
    <h1>Enter the SQL Below</h1>
    <el-input
      type="textarea"
      :autosize="{ minRows: 5, maxRows: 25 }"
      placeholder="请输入HiveQL"
      v-model="hiveQL"
      :disabled="disableHiveQL"
    >
    </el-input>

    <div class="detect">
      <el-button type="primary" v-on:click="detect">Detect</el-button>
    </div>
    <div style="clear:right"> 
    <el-row style="flex-direction: row; clear:right">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>Problem</span>
            <!-- <el-button style="float: right; padding: 3px 0" type="text" >操作按钮</el-button> -->
          </div>
          <div v-for="o in 4" :key="o" class="text item" v-loading="loading">
            {{ "列表内容 " + o }}
          </div>
          <el-table
            v-loading="loading"
            element-loading-text="正在进行检测"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :data="tableData"
            style="width: 100%"
          >
            <el-table-column prop="date" label="日期" width="180">
            </el-table-column>
            <el-table-column prop="name" label="姓名" width="180">
            </el-table-column>
            <el-table-column prop="address" label="地址"> </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="24" style="margin-top: 10px">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>Fix Advice</span>
            <!-- <el-button style="float: right; padding: 3px 0" type="text" >操作按钮</el-button> -->
          </div>
          <el-table
            v-loading="loading"
            element-loading-text="正在进行修复"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :data="fixData"
            style="width: 100%"
          >
            <el-table-column prop="type" label="种类" width="180">
            </el-table-column>
            <el-table-column prop="description" label="描述" width="180">
            </el-table-column>
          </el-table>
          <div v-for="o in 4" :key="o" class="text item">
            {{ "列表内容 " + o }}
          </div>
        </el-card>
      </el-col>
    </el-row>
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
      disableHiveQL: false,
      api1url: this.common.api1url,
      fixData: [
        {
          type: "test1",
          description: "description"
        },
        {
          type: "test2",
          description: "description"
        }
      ],
      tableData: [
        {
          date: "2016-05-03",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄",
        },
        {
          date: "2016-05-02",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄",
        },
        {
          date: "2016-05-04",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄",
        },
      ],
      loading: false,
    };
  },
  methods: {
    detect() {
      var _this = this; //指定this
      _this.disableHiveQL = true;
      //  _this.$router.push({
      //     path: `/result`,
      //   });
      _this
        .$axios({
          //创建接口
          methods: "get", //类型为get请求
          url: _this.api1url + "/astCheck", //请求的接口地址
          params: {
            hiveql: this.hiveQL,
          },
        })
        .then(function (response) {
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
  float:right;
  margin-top: 10px;
  margin-bottom: 10px;
}
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}

.box-card {
  width: 1200px;
}
</style>
