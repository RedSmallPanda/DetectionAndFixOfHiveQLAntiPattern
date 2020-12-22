<template>
  <div>
    <h1>View Configurations</h1>
    <el-form ref="form"  :model="formInline" class="demo-form-inline" label-width="200px" style="width:800px">
      <el-form-item label="Hive数据库地址">
        <el-input v-model="formInline.mysqlUrl"  :disabled="true" ></el-input>
      </el-form-item>
      <el-form-item label="Hive数据库用户名">
        <el-input v-model="formInline.mysqlUsername" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="Hive数据库密码">
        <el-input v-model="formInline.mysqlPassword" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="数据库地址">
        <el-input v-model="formInline.url" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="数据库用户名">
        <el-input v-model="formInline.username" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="数据库密码">
        <el-input v-model="formInline.password" :disabled="true"></el-input>
      </el-form-item>
      <!-- <el-form-item label="设置2">
        <el-select v-model="formInline.region" placeholder="活动区域" :disabled="true">
          <el-option label="区域一" value="shanghai"></el-option>
          <el-option label="区域二" value="beijing"></el-option>
        </el-select>
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" @click="onSubmit">Modify</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      formInline: {
        mysqlUrl:" ",
        mysqlUsername: " ",
        mysqlPassword: " ",
        url:" ",
        username: " ",
        password: "",
      }
    }
  },
  mounted:function(){
      this.getConfig();//需要触发的函数
    },
 methods: {
    onSubmit() {
      console.log("go to modify!");
        this.$router.push({
            path: `/set`
          })
    },
    getConfig(){
      console.log("View Config, ready to get configures");
      const _this = this;
      // _this.$axios({
      //   methods:"get",
      //   url:"",
      // }).then(function(response){
      //   console.log(response.data); // print data
      //   _this.mysqlUrl = response.data.mysqlUrl;
      //   _this.mysqlUsername = response.data.mysqlUsername;
      //   _this.mysqlPassword = response.data.mysqlPassword;
      //   _this.url = response.data.url;
      //   _this.username = response.data.username;
      //   _this.password = response.data.password;
      // });
    }
  }
}
</script>
