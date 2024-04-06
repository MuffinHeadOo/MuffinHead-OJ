<template>
  <div style="text-align: center; margin: 0 auto; max-width: 1400px">
    <el-card class="box-card" shadow="hover">

        <el-table :data="problemList" height="96px" :header-cell-style="{ textAlign: 'center' } "
        :cell-style="{ textAlign: 'center' }" v-loading="!finished">
        
          <el-table-column prop="id" label="#" width="100px" />

          <el-table-column prop="topic" label="题目" width="auto">
            <template #default="scope">
              <span class="rlink" @click="goToPage(scope.row.topicId)">
                {{ scope.row.topic}}
              </span>
            </template>
          </el-table-column>

          <el-table-column prop="userName" label="提交者" width="150px"></el-table-column>

          <el-table-column prop="result" label="提交结果" width="150px" >  
            <template v-slot="scope">  
              <span :class="getResultClass(scope.row.result)">  
                <span v-if="scope.row.result === -2">编译错误</span>  
                <span v-else-if="scope.row.result === -1">时间超限</span>  
                <span v-else-if="scope.row.result === -4">运行错误</span>
                <span v-else-if="scope.row.result == 0">0%</span>  
                <span v-else-if="scope.row.result === 100">  
                <span class="green">100%</span>  
              </span>  
              <span v-else>  
                <span class="yellow">{{ scope.row.result }}%</span>  
              </span>  
              </span>  
            </template>  
          </el-table-column>

          <el-table-column prop="executionTime" label="总耗时" width="150px">  
            <template v-slot="scope">  
              <span>{{ formatExecutionTime(scope.row.executionTime) }}</span>  
            </template>  
          </el-table-column>

          <el-table-column prop="memory" label="内存" width="150px">  
            <template v-slot="scope">  
              <span>{{ formatMemory(scope.row.memory) }}</span>  
            </template>  
          </el-table-column>

          <el-table-column prop="submitTime" label="提交时间" width="200px">
            <template #default="scope">
              <span> {{ formatDate(scope.row.submitTime) }}</span>
            </template>
          </el-table-column>

        

        </el-table>

          <v-md-editor v-model="problemList[0].code" height="600px" mode="edit" left-toolbar="undo redo clear" right-toolbar="sync-scroll fullscreen" style="margin-top: 10px;"></v-md-editor>
   
        
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
    all() {
      axios.get(`/api/home/list/submission/${this.id}`,{  
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
      const hours = String(date.getHours()).padStart(2, '0');  
      const minutes = String(date.getMinutes()).padStart(2, '0');  
      const seconds = String(date.getSeconds()).padStart(2, '0');  
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;  
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
