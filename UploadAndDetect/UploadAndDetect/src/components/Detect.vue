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
    <div style="clear: right" v-if="isGetResult">
      <el-row style="flex-direction: row; clear: right">
        <el-col :span="24">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>Detect Result</span>
            </div>
            <div
              v-loading="loading"
              element-loading-text="正在进行检测"
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
            >
            {{ fixedHiveql }}
            </div>
          </el-card>
        </el-col>
        <el-col :span="24" style="margin-top: 10px">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>Fix Suggestions</span>
            </div>
            <el-table
              v-loading="loading"
              element-loading-text="正在进行修复"
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
              :data="fixSuggestions"
              style="width: 100%"
            >
              <el-table-column prop="id" label="序号" width="80">
              </el-table-column>
              <el-table-column prop="suggestion" label="修复建议" width="720">
              </el-table-column>
            </el-table>
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
      fixedHiveql: "",
      fixSuggestions:[
        // {
        //   id: "-1",
        //   suggestion: "test",
        // }
       ],
      isGetResult: false,
      disableHiveQL: false,
      api1url: this.common.api1url,
      fixData: [
        {
          type: "test1",
          description: "description",
        },
        {
          type: "test2",
          description: "description",
        },
      ],
      loading: true,
    };
  },
  methods: {
    detect() {
      var _this = this; //指定this
      // _this.disableHiveQL = true;
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
          _this.isGetResult = true ;
          _this.loading = false ;
          console.log(response.data); //打印请求的数据
          _this.fixedHiveql = response.data.fixedHiveql;
          for(var i=0;i<response.data.fixedSuggestions.length;i++){
            console.log(response.data.fixedSuggestions[i]);
            _this.fixSuggestions.push({"id":i+1,"suggestion":response.data.fixedSuggestions[i]});
          }
          console.log(response.data.fixedHiveql); 
        });
    },
  },
};
</script>

<style scoped>
.detect {
  float: right;
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
