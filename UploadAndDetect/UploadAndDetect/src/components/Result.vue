<template>
  <div>
    <h1>The SQL Detected and Fix Suggestions</h1>
    <el-input
      type="textarea"
      :autosize="{ minRows: 5, maxRows: 25 }"
      placeholder="请输入HiveQL"
      v-model="hiveQL"
      :disabled="true"
    >
    </el-input>
    <div style="margin-top: 10px">
      <div style="flex-direction: row"> 
        <el-row>
        <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>Problem</span>
            <el-button style="float: right; padding: 3px 0" type="text" >操作按钮</el-button>
          </div>
          <div v-for="o in 4" :key="o" class="text item">
            {{ "列表内容 " + o }}
          </div>
        </el-card>
        </el-col>
        <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>Fix Advice</span>
            <el-button style="float: right; padding: 3px 0" type="text" >操作按钮</el-button>
          </div>
          <div v-for="o in 4" :key="o" class="text item">
            {{ "列表内容 " + o }}
          </div>
        </el-card>
        </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      hiveQL: "heloo",
    };
  },
};
</script>

<style scoped>
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
  width: 360px;
}
</style>