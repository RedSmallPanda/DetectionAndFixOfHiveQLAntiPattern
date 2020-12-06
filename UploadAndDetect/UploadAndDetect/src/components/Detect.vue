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
           <el-button  type="primary" v-on:click="detect">Detect</el-button>
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
    };
  },
  methods: {
    detect() {
      this.$router.push({
            path: `/result`
          })
      // this.$axios
      // .post('/detect',{
      //   hiveQL: this.hiveQL
      // })
      // .then(successResponse => {
      //   if (successResponse.data.code === 200){
      //     this.$router.push({
      //       path: `/result`
      //     })
      //   }
      // })
    }

  }
};
</script>

<style scoped>
.detect{
  float: right;
  margin-top: 10px;
}
</style>
