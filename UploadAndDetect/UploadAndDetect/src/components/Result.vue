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
          <el-col :span="24">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>Problem</span>
                <!-- <el-button style="float: right; padding: 3px 0" type="text" >操作按钮</el-button> -->
              </div>
              <div
                v-for="o in 4"
                :key="o"
                class="text item"
                v-loading="loading"
              >
                {{ "列表内容 " + o }}
              </div>
              <el-table
                v-loading="loading"
                element-loading-text="拼命加载中"
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
      loading: true,
    };
  },
  methods: {},
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
  width: 1200px;
}
</style>