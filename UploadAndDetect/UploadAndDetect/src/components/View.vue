<template>
  <div>
    <h1>View Configurations</h1>
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item label="设置1">
        <el-input v-model="formInline.user" placeholder="审批人" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="设置2">
        <el-select v-model="formInline.region" placeholder="活动区域" :disabled="true">
          <el-option label="区域一" value="shanghai"></el-option>
          <el-option label="区域二" value="beijing"></el-option>
        </el-select>
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
        user: "test user",
        region: "test region",
      }
    }
  },
 methods: {
    onSubmit() {
      console.log("go to modify!");
        this.$router.push({
            path: `/set`
          })
    }
  }
}
</script>
