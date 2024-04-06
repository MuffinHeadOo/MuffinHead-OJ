<template>
  <div style="text-align: center; margin: 0 auto; max-width: 1200px">
    <el-card class="box-card" shadow="hover">
      <div class="card-header" slot="header">
          <p style="font-weight :bold">题目列表</p>
          <!-- <el-pagination :page-size="20" :pager-count="11" layout="total, prev, pager, next" :total="total" @current-change="handleCurrentChange" :current-page="currentPage"></el-pagination>
          <el-button type="primary"  icon="el-icon-refresh-left" @click="all">
              <el-icon>
                <Refresh />
              </el-icon>
              刷新
          </el-button> -->
          <label style="margin-left: 400px;margin-right: 10px;">
            <p style="font-weight :bold">题目名称:</p>
          </label>
          <el-input v-model="topic" placeholder="请输入题目名称" style="width: 15%;"/>
          <label style="margin-left: 25px;margin-right: 10px;">
           <p style="font-weight :bold">出题人:</p>
          </label>
          <el-input v-model="userName" placeholder="请输入出题人" style="width: 15%;"/>
          <el-button type="primary" style="margin-left: 20px;" @click="pageQuery()">查询</el-button>
      </div>

        <el-table :data="problemList" height="600px" :header-cell-style="{ textAlign: 'center' } "
        :cell-style="{ textAlign: 'center' }" v-loading="!finished">
          <el-table-column prop="id" label="#" width="100px" />
          <el-table-column prop="topic" label="题目" width="auto">
            <template #default="scope">
              <span class="rlink" @click="goToPage(scope.row.id)">
                {{ scope.row.topic}}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="accepted" label="AC/提交" width="150px">
            <template #default="scope">
              <span> {{ scope.row.accepted }} / {{ scope.row.submit }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="createdTime" label="发布时间" width="200px">
            <template #default="scope">
              <span> {{ formatDate(scope.row.createdTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="userName" label="出题人" width="120px">
            <template #default="scope">
              <span class="rlink" @click="goToUser(scope.row.userId)">
                {{ scope.row.userName}}
              </span>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="20"
            layout="total, prev, pager, next" :total="total"></el-pagination>
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
      currentPage: 1,
      pageSize: 20,
      finished: false,
      topic: null,
      userName: null,
    }
  },
  methods: {
    pageQuery() {
      this.finished = false;
      axios.post('/api/home/list', {
        page: this.currentPage,
        pageSize: this.pageSize,
        topic: this.topic,
        userName: this.userName,
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
    goToUser(userId) {  
      this.$router.push({ name: 'ShowPersonalComponent', params: { id: userId}});
    },
    formatDate(dateTimeString) {  
      const date = new Date(dateTimeString);  
      const year = date.getFullYear();  
      const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始，所以需要加1，并使用padStart确保总是两位数  
      const day = String(date.getDate()).padStart(2, '0'); // 使用padStart确保总是两位数  
      return `${year}-${month}-${day}`;  
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
    this.pageQuery();
  }
}

</script>




<style scoped>
  .box-card {
  height: 720px;
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
</style>
