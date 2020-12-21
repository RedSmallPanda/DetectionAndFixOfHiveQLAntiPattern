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
    <div style="clear: right" v-if="isGetFixResult">
      <el-row style="flex-direction: row; clear: right">
        <el-col :span="24">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>Detect Result</span>
            </div>
            <div
              v-loading="fixLoading"
              element-loading-text="正在进行检测"
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
            >
            <br>
            HiveQL检测结果：{{ fixedHiveql }}
            <br>
            </div>
            <div
              v-if="isGetJoinResult"
              v-loading="joinLoading"
              element-loading-text="正在检查数据倾斜"
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
              style="margin-top:10px;margin-bottom:10px"
            >
            <br>
            数据倾斜结果：{{ dataImbalancedSuggest }}
            <br>
            </div>
          </el-card>
        </el-col>
        <el-col :span="24" style="margin-top: 10px">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>Fix Suggestions</span>
            </div>
            <el-table
              v-loading="fixLoading"
              element-loading-text="正在进行修复"
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
              :data="fixSuggestions"
              style="width: 100%"
            >
              <el-table-column prop="id" label="序号" width="80">
              </el-table-column>
              <el-table-column prop="suggestion" label="修复建议" >
              </el-table-column>
            </el-table>
            <el-table
              v-if="isGetJoinResult"
              v-loading="joinLoading"
              element-loading-text="正在推荐Reduce数量"
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
              :data="recommendReduceNum"
              style="width: 100%"
            >
              <el-table-column prop="id" label="序号" width="80">
              </el-table-column>
              <el-table-column prop="recommend" label="推荐配置" >
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
      dataImbalancedSuggest: "",
      recommendReduceNum:[],
      fixSuggestions:[
        // {
        //   id: "-1",
        //   suggestion: "test",
        // }
       ],
      isGetFixResult: false,
      disableHiveQL: false,
      isGetJoinResult: false,
      fixLoading: true,
      joinLoading: true,
      api1url: this.common.api1url,
      api2url: this.common.api2url,
      t1_name: " ",
      t1_key: " ",
      t2_name: " ",
      t2_key: " ",
    };
  },
  methods: {
    detect() {
      var _this = this; //savve this
      // clear all history detection
      _this.isGetFixResult = false;
      _this.isGetJoinResult = false;
      _this.fixLoading = true;
      _this.joinLoading = true;
      _this.fixedHiveql = "";
      _this.fixSuggestions = [];
      _this.dataImbalancedSuggest = "";
      _this.recommendReduceNum = [];
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
          _this.isGetFixResult = true ;
          _this.fixLoading = false ;
          console.log(response.data); //打印请求的数据
          _this.fixedHiveql = response.data.fixedHiveql;
          for(var i=0;i<response.data.fixedSuggestions.length;i++){
            console.log(response.data.fixedSuggestions[i]);
            _this.fixSuggestions.push({"id":i+1,"suggestion":response.data.fixedSuggestions[i]});
          }
          console.log(response.data.fixedHiveql);
          console.log(response.data.joinParams);
          if (response.data.joinParams){
            _this.t1_name = response.data.joinParams[0];
            _this.t1_key = response.data.joinParams[1];
            _this.t2_name = response.data.joinParams[2];
            _this.t2_key = response.data.joinParams[3];
            _this.join_detect();
          }
        });
    },
    join_detect(){
      var _this = this;
      _this.isGetJoinResult = true;
      _this
      .$axios({
        // create api
        methods: "get",
        url: _this.api2url+'/join_check',
        params:{
          t1_name:_this.t1_name,
          t1_key:_this.t1_key,
          t2_name:_this.t2_name,
          t2_key:_this.t2_key
        }
      }).then(function(response){    
        _this.joinLoading = false;
        console.log(response.data); //打印请求的数据
        _this.dataImbalancedSuggest = response.data.dataImbalancedSuggest;
        _this.recommendReduceNum = [{"id":1,"recommend":response.data.recommendReduceNum}];
      });
    }
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
