<template>
  <div style="text-align: center; margin: 0 auto; max-width: 1400px">
    <el-card class="box-card" shadow="hover">
      <div class="card-header" slot="header">
          <p style="font-weight :bold">提交记录</p>
          <!-- <el-pagination :page-size="20" :pager-count="11" layout="total, prev, pager, next" :total="total" @current-change="handleCurrentChange" :current-page="currentPage"></el-pagination>
          <el-button type="primary"  icon="el-icon-refresh-left" @click="all">
              <el-icon>
                <Refresh />
              </el-icon>
              刷新
          </el-button> -->
          <label style="margin-left: 300px;margin-right: 10px;">
            <p style="font-weight :bold">题目名称:</p>
          </label>
          <el-input v-model="topic" placeholder="请输入题目名称" style="width: 10%;"/>

          <!-- todo -->
          <label style="margin-left: 25px;margin-right: 10px;">
           <p style="font-weight :bold">提交者:</p>
          </label>
          <el-input v-model="userName" placeholder="请输入提交者" style="width: 10%;"/>
          <!-- todo -->
          <label style="margin-left: 25px;margin-right: 10px;">
           <p style="font-weight :bold">结果:</p>
          </label>
          <el-select v-model="result" placeholder="请选择结果" style="width: 10%;">  <el-option  v-for="item in resultOptions"  :key="item.value"  :label="item.label"  :value="item.value" ></el-option>  
    </el-select> 

          <el-button type="primary" style="margin-left: 20px;" @click="pageQuery();showPagination1()">查询</el-button>
          <el-button type="success" style="margin-left: 20px;" @click="myPageQuery();showPagination2()">我的提交</el-button>
      </div>

        <el-table :data="problemList" height="640px" :header-cell-style="{ textAlign: 'center' } "
        :cell-style="{ textAlign: 'center' }" v-loading="!finished">
          <el-table-column prop="id" label="#" width="100px" />

          <el-table-column prop="topic" label="题目" width="auto">
            <template #default="scope">
              <span class="rlink" @click="goToPage(scope.row.topicId)">
                {{ scope.row.topic}}
              </span>
            </template>
          </el-table-column>

          <el-table-column prop="userName" label="提交者" width="150px">
            <template #default="scope">
              <span class="rlink" @click="goToUser(scope.row.userId)">
                {{ scope.row.userName}}
              </span>
            </template>
          </el-table-column>

          <el-table-column prop="result" label="提交结果" width="150px" >  
            <template v-slot="scope">  
              <span :class="getResultClass(scope.row.result)" class="hand-cursor" @click="goToPageDetail(scope.row.id)">  
                <span v-if="scope.row.result === -2">编译错误</span>  
                <span v-else-if="scope.row.result === -1">时间超限</span>  
                <span v-else-if="scope.row.result === -4">运行错误</span>
                <span v-else-if="scope.row.result === 0">0%</span>  
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

        <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="20"
            layout="total, prev, pager, next" :total="total" v-if="currentPagination === 'pagination1'"></el-pagination>

        <el-pagination @current-change="myHandleCurrentChange" :current-page="currentPage1" :page-size="20"
            layout="total, prev, pager, next" :total="total1" v-if="currentPagination === 'pagination2'"></el-pagination>



    </el-card>
  </div>
</template>



<script>
import axios from '@/http';


export default {
  name: 'TopicComponent',
  data() {
    return {
      currentPagination: 'pagination1', // 默认显示第一个导航栏
      problemList: [],
      total: 0,
      id: 1,
      currentPage: 1,
      pageSize: 20,
      currentPage1: 1,
      pageSize1: 20,
      finished: false,
      topic: null,
      userName: null,
      //选择框
      result: '', // 初始选中的结果，可以是 '正确'、'错误'、'编译错误' 或 '时间超限' 中的任意一个  
      resultOptions: [ // 可选的结果列表  
        { value: '', label: '全部' }, 
        { value: 100, label: '回答正确' },  
        { value: -3, label: '回答错误' },  
        { value: -2, label: '编译错误' },  
        { value: -1, label: '时间超限' },
        { value: -4, label: '运行错误' }
      ]
    }
  },
  methods: {
    showPagination1() {  
    this.currentPagination = 'pagination1';  
  },  
  showPagination2() {  
    this.currentPagination = 'pagination2';  
  },
    getResultClass(result) {  
      switch (result) {  
        case -2:  
          return 'compile-error';  
        case -1:  
          return 'time-limit-exceeded';
        case -4:  
          return 'run-error';   
        case 0:  
          return 'red-percent';  
        case 100:  
          return 'green-percent';  
        default:  
          return 'yellow-percent';  
      }  
    },
    pageQuery() {
      this.finished = false;
      axios.post('/api/home/list/submission', {
        page: this.currentPage,
        pageSize: this.pageSize,
        topic: this.topic,
        userName: this.userName,
        result: this.result,
      },{  
        headers: {  
        'token': localStorage.getItem('token'),
        } 
      }).then(res => {
        this.problemList = res.data.data.records,
        this.total = res.data.data.total,
        this.finished = true
      })
    },
    myPageQuery() {
      this.finished = false;
      axios.post('/api/home/list/mySubmission', {
        page: this.currentPage1,
        pageSize: this.pageSize1,
      },{  
        headers: {  
        'token': localStorage.getItem('token'),
        } 
      }).then(res => {
        this.problemList = res.data.data.records,
        this.total1 = res.data.data.total,
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
    myHandleCurrentChange(val) {
      this.currentPage1 = val;
      this.myPageQuery();
    },
    goToPage(topicId) {  
      this.$router.push({ name: 'problemEdit', params: { id: topicId}});
    },
    goToPageDetail(id) {  
      this.$router.push({ name: 'SubmitDetail', params: { id: id}});
    },
    goToUser(userId) {  
      this.$router.push({ name: 'ShowPersonalComponent', params: { id: userId}});
    },
  },
  mounted() {
    this.pageQuery();
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
.hand-cursor {  
        cursor: pointer;  
    }  
</style>
