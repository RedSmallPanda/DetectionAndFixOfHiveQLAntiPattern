<template>
  <div>
    <h1>Set Configurations</h1>
    <el-form ref="form"  :model="formInline" class="demo-form-inline" label-width="200px" style="width:800px">
      <el-form-item label="Hive数据库地址">
        <el-input v-model="formInline.mysqlUrl"    ></el-input>
      </el-form-item>
      <el-form-item label="Hive数据库用户名">
        <el-input v-model="formInline.mysqlUsername"  ></el-input>
      </el-form-item>
      <el-form-item label="Hive数据库密码">
        <el-input v-model="formInline.mysqlPassword"  ></el-input>
      </el-form-item>
      <el-form-item label="数据库地址">
        <el-input v-model="formInline.url"  ></el-input>
      </el-form-item>
      <el-form-item label="数据库用户名">
        <el-input v-model="formInline.username"  ></el-input>
      </el-form-item>
      <el-form-item label="数据库密码">
        <el-input v-model="formInline.password"  ></el-input>
      </el-form-item>
      <!-- <el-form-item label="设置2">
        <el-select v-model="formInline.region" placeholder="活动区域"  >
          <el-option label="区域一" value="shanghai"></el-option>
          <el-option label="区域二" value="beijing"></el-option>
        </el-select>
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" @click="onSubmit">Save</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      formInline: {
        user: "",
        region: "",
      }
    }
  },
 methods: {
    onSubmit() {
      console.log("submit!");
      const _this = this;
      _this.$message({
          message: '保存成功',
          type: 'success'
        });
    }
  }
}
</script>
