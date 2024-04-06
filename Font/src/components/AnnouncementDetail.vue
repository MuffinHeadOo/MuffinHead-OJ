<template>
  <div style="text-align: center; margin: 0 auto; max-width: 1400px">
    <el-card class="box-card" shadow="hover">

        <!-- <el-table :data="problemList" height="96px" :header-cell-style="{ textAlign: 'center' } "
        :cell-style="{ textAlign: 'center' }" > -->
        <el-table :data="problemList" height="96px" :header-cell-style="{ textAlign: 'center' } "
        :cell-style="{ textAlign: 'center' }" v-loading="!finished">
        
          <el-table-column prop="id" label="#" width="100px" />

          <el-table-column prop="topic" label="标题" width="auto">
          </el-table-column>

          <el-table-column prop="userName" label="发布者" width="300px">
            <template #default="scope">
              <span class="rlink" @click="goToUser(scope.row.userId)">
                {{ scope.row.userName}}
              </span>
          </template>
          </el-table-column>

          <el-table-column prop="createdTime" label="发布时间" width="350px">
            <template #default="scope">
              <span> {{ formatDate(scope.row.createdTime) }}</span>
            </template>
          </el-table-column>

        

        </el-table>

          <v-md-editor v-model="problemList[0].content" height="600px" mode="preview" left-toolbar="undo redo clear" right-toolbar="sync-scroll fullscreen" style="margin-top: 10px;"></v-md-editor>
   
        
    </el-card>
  </div>
</template>



<script>
import axios from '@/http';


export default {
  name: 'TopicComponent',
  data() {
    return {
      problemList: [],
      total: 0,
      id: 1,
      finished: false,
    }
  },
  methods: {
    getResultClass(result) {  
      switch (result) {  
        case -2:  
          return 'compile-error';  
        case -4:  
          return 'run-error';  
        case -1:  
          return 'time-limit-exceeded';  
        case 0:  
          return 'red-percent';  
        case 100:  
          return 'green-percent';  
        default:  
          return 'yellow-percent';  
      }  
    },
    goToUser(userId) {  
      this.$router.push({ name: 'ShowPersonalComponent', params: { id: userId}});
    },
    all() {
      axios.get(`/api/home/announcement/${this.id}`,{  
        headers: {  
        'token': localStorage.getItem('token'),
        } 
      }).then(res => {
        this.problemList = res.data.data,
        this.finished = true
      })
    },
    formatDate(dateTimeString) {  
      const date = new Date(dateTimeString);  
      const year = date.getFullYear();  
      const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始，所以需要加1  
      const day = String(date.getDate()).padStart(2, '0');   
      return `${year}-${month}-${day}`;  
    },
    formatExecutionTime(executionTime) {  
      if (executionTime === null) {  
        return 'NULL';  
      }  
      return `${executionTime}ms`;  
    },
    formatMemory(memory) {
      if (memory === null) {  
        return 'NULL';  
      }  
      return `${memory}KB`;
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.pageQuery();
    },
    goToPage(topicId) {  
      this.$router.push({ name: 'problemEdit', params: { id: topicId}});
    },
  },
  mounted() {
    this.id = this.$route.params.id;
    this.all();
  }
}

</script>




<style scoped>
  .box-card {
  height: 760px;
  margin: 10px;
}

.card-header {
  display: flex;
  align-items: center;
  height: 20px;
}
.rlink {
  cursor: pointer;
  color: #558CDD;
}
.compile-error{
  color: rgb(189, 19, 189);
}

.run-error{
   color: rgb(255, 157, 0);
}
.time-limit-exceeded {  
  color: red; /* 编译错误和时间超限显示为红色 */  
}  
  
.red-percent {  
  color: red; /* 0% 显示为红色 */  
}  
  
.green-percent {  
  color: rgb(2, 253, 2); /* 100% 显示为绿色 */  
}  
  
.yellow-percent {  
  color: rgb(214, 214, 28); /* 其他数字显示为黄色 */  
}  
</style>
