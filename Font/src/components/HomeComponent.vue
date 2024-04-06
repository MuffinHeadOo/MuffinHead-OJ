<template>
<div>
  <el-row style="margin: auto;max-width: 1500px;min-width: 600px;">
  <el-col :span="16">
  <el-card class="box-card" shadow="hover">

   <div slot="header" class="clearfix">
    <span>公告栏</span>
  </div>

    <el-table :data="tableData" style="width: 100%" height="190px" class="box1" v-loading="!finished">
    
    <el-table-column prop="topic" label="标题" width=auto>
      <template #default="scope">
              <span class="rlink" @click="goToAnnouncement(scope.row.id)">
                {{ scope.row.topic}}
              </span>
          </template>
    </el-table-column>
    <el-table-column prop="userName" label="发布者" width="180px">
      <template #default="scope">
              <span class="rlink" @click="goToUser(scope.row.userId)">
                {{ scope.row.userName}}
              </span>
          </template>
    </el-table-column>
    <el-table-column prop="createdTime" label="发布时间" width="180px">
      <template #default="scope">
              <span> {{ formatDate(scope.row.createdTime) }}</span>
      </template>
    </el-table-column>

  </el-table>

  
</el-card>

<div style="text-align: center; max-width: 1200px">
    <el-card class="box-card" shadow="hover">
      <template #header>
        <div class="card-header">
          AC排名
        </div>
      </template>

      <el-table v-loading="!finished" :data="info" height="600px">
        <el-table-column label="#" type="index" width="80px" />
        <el-table-column prop="name" label="用户名" width="150px">
          <template #default="scope">
              <span class="rlink" @click="goToUser(scope.row.id)">
                {{ scope.row.name}}
              </span>
          </template>
        </el-table-column>
        <el-table-column prop="accepted" label="AC数量" width="130px" />
        <el-table-column prop="signature" label="个人主页" width="auto" class-name="cell-ellipsis">
          <template #default="scope">
            <span class="ellipsis-text"> {{ scope.row.signature }} </span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
  </el-col>
    

<el-col :span="6" style="min-width: 300px;">
<div style="text-align: center; margin: 0 auto; max-width: 300px">
    <el-card class="box-card2" shadow="hover">
        <div class="card-header">
          使用说明
        </div>
        <hr style="margin-top: 20px;" />

        <p style="margin-top: 20px;">本站目前只支持python和java程序</p>
        <hr style="margin-top: 20px;" />
        <p style="margin-top: 20px;">本站使用命令行环境进行判题（命令行参数)</p>
        <p>而非交互式环境（标准输入）</p>
        <hr style="margin-top: 20px;" />
        <p style="margin-top: 20px;">举例</p>
        <p style="margin-top: 20px;">java:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;输入类型 input = args[0];</p>
        <v-md-editor v-model="code" mode="preview"></v-md-editor>
        <p>python:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;input = sys.argv[1]</p>
        <v-md-editor v-model="code2" mode="preview"></v-md-editor>
  
    </el-card>
  </div>
</el-col>
  

  </el-row>

</div>


  
</template>

<script>
import axios from '@/http';


  export default {
    data() {
      return {
        tableData: [],
        info:[],
        code:'```java\npublic class Main\n{\n    public static void main(String[] args)\n    {\n        //接收题目所需输入\n        int a = Integer.parseInt(args[0]);\n        System.out.println(a + 1);\n    }\n}\n```\n',
        code2:"```python\nimport sys\n\ndef main():\n    # 接收题目所需输入\n    a = int(sys.argv[1])\n    result = a + 1\n    print(result)\n\nif __name__ == \"__main__\":\n    main()\n```"
      }
    },
    methods: {
      indexMethod(index) {
        return index * 1;
      },
      all() {
      this.finished = false;
      axios.post('/api/home/accepted', {

      },{  
        headers: {  
        'token': localStorage.getItem('token'),
        } 
      }).then(res => {
        this.info = res.data.data,
        this.finished = true
      })
      },
      allAnnouncement() {
      this.finished = false;
      axios.post('/api/home/announcement', {

      },{  
        headers: {  
        'token': localStorage.getItem('token'),
        } 
      }).then(res => {
        this.tableData = res.data.data,
        this.finished = true
      })
      },
    goToUser(userId) {  
      this.$router.push({ name: 'ShowPersonalComponent', params: { id: userId}});
    },
    goToAnnouncement(id) {  
      this.$router.push({ name: 'AnnouncementDetail', params: { id: id}});
    },
    formatDate(dateTimeString) {  
      const date = new Date(dateTimeString);  
      const year = date.getFullYear();  
      const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始，所以需要加1，并使用padStart确保总是两位数  
      const day = String(date.getDate()).padStart(2, '0'); // 使用padStart确保总是两位数  
      return `${year}-${month}-${day}`;  
      },
    },
    mounted() {
    this.all();
    this.allAnnouncement();
    }
  };
</script>

<style scoped>
.rlink {
  cursor: pointer;
  color: #558CDD;
}
.cell-ellipsis .ellipsis-text {  
  display: inline-block;  
  max-width: 100%; /* 或者你想要的任何宽度 */  
  overflow: hidden;  
  text-overflow: ellipsis;  
  white-space: nowrap; /* 防止内容换行 */  
}  
  .box-card {
    margin-left: 90px;
    width: 900px;
    margin-top: 20px;
  }
  .box-card2 {
    margin-left: 0px;
    width: 460px;
    margin-top: 20px;
  }
  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }
  
</style>
