<template>
  <div>
    <h1>View Configurations</h1>
    <el-form ref="form"  :model="formInline" class="demo-form-inline" label-width="200px" style="width:800px">
      <el-form-item label="MetaBase Address">
        <el-input v-model="formInline.mysqlUrl"  :disabled="true" ></el-input>
      </el-form-item>
      <el-form-item label="MetaBase Username">
        <el-input v-model="formInline.mysqlUsername" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="MetaBase Password">
        <el-input v-model="formInline.mysqlPassword" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="Hive Address">
        <el-input v-model="formInline.url" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="Hive Username">
        <el-input v-model="formInline.username" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="Hive Password">
        <el-input v-model="formInline.password" :disabled="true"></el-input>
      </el-form-item>
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
      },
      api2url:this.common.api2url,
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
      _this.$axios({
        method: "get",
        url: _this.api2url+"/configGet",
      }).then(function(response){
        console.log("get data");
        console.log(response.data); // print data
        _this.formInline.mysqlUrl = response.data.mysqlUrl;
        _this.formInline.mysqlUsername = response.data.mysqlUsername;
        _this.formInline.mysqlPassword = response.data.mysqlPassword;
        _this.formInline.url = response.data.url;
        _this.formInline.username = response.data.username;
        _this.formInline.password = response.data.password;
      });
    }
  }
}
</script>
