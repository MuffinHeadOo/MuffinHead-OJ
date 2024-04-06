<template>
  <div style="text-align: center; margin: 0 auto; max-width: 1400px">
    <el-card class="box-card" shadow="hover">
      <div class="card-header" slot="header">
          <p style="font-weight :bold">公告列表</p>

          <label style="margin-left: 280px;margin-right: 10px;">
            <p style="font-weight :bold">公告名称:</p>
          </label>
          <el-input v-model="topic" placeholder="请输入公告名称" style="width: 15%;"/>
          <label style="margin-left: 25px;margin-right: 10px;">
           <p style="font-weight :bold">发布者:</p>
          </label>
          <el-input v-model="userName" placeholder="请输入发布者" style="width: 15%;"/>
          <el-button type="primary" style="margin-left: 20px;" @click="pageQuery()">查询</el-button>
          <el-button type="success" style="margin-left: 220px;" @click="dialogVisible = true">添加公告</el-button>
      </div>
      <!-- 新增题目 -->
      <el-dialog title="新增公告" :visible.sync="dialogVisible" :before-close="dialoghandleClose" width="70%" show-overflow-tooltip>
        
        <el-descriptions direction="vertical" :column="1" border>
          <el-descriptions-item label="请输入公告名称">
           <el-input v-model="topicName"></el-input>
          </el-descriptions-item>
          <el-descriptions-item label="请输入内容">
            <v-md-editor v-model="content" height="600px" mode="edit"></v-md-editor>
          </el-descriptions-item>
        </el-descriptions>
        
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="dialogVisible = false,addProblem()" >确 定</el-button>
        </span>
     </el-dialog>
      <!-- 编辑公告 -->
        <el-dialog title="编辑题目" :visible.sync="dialogVisible2" :before-close="dialoghandleClose" width="70%" show-overflow-tooltip>
        
        <el-descriptions direction="vertical" :column="1" border>
          <el-descriptions-item label="请输入公告名称">
           <el-input v-model="form.topic"></el-input>
          </el-descriptions-item>
          <el-descriptions-item label="请输入内容">
            <v-md-editor v-model="form.content" height="400px" mode="edit"></v-md-editor>
          </el-descriptions-item>

        </el-descriptions>
        
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible2 = false">取 消</el-button>
          <el-button type="primary" @click="dialogVisible2 = false,changeProblem()" >确 定</el-button>
        </span>
     </el-dialog>


        <el-table :data="problemList" height="700px" :header-cell-style="{ textAlign: 'center' } "
        :cell-style="{ textAlign: 'center' }" v-loading="!finished">
          <el-table-column prop="id" label="#" width="100px" />

          <el-table-column prop="topic" label="标题" width="auto">
          </el-table-column>
          
          <el-table-column prop="createdTime" label="发布时间" width="200px">
            <template #default="scope">
              <span> {{ formatDate(scope.row.createdTime) }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="userName" label="出题人" width="120px">
          </el-table-column>

          <el-table-column label="编辑" width="100px">
            <template #default="scope">
              <el-button type="primary" style="margin-left: 20px;" @click="dialogVisible2 = true,adminShowBack(scope.row.id)">编辑</el-button>
            </template>
          </el-table-column>
          
          <el-table-column label="删除" width="100px">
            <template #default="scope">
            <el-button v-model="scope.row.id" type="primary" style="margin-left: 20px;" @click="handleDelete(scope.row.id)">删除</el-button>
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
    data() {
      return {
        dynamicTags: [],
        dialogVisible: false,
        dialogVisible2: false,
        content: '',
        problemList: [],
        currentPage: 1,
        pageSize: 20,
        topic: null,
        topicName: '',
        input: '',
        output:'',
        userName: null,
        inputVisible: false,
        inputValue: '',
        //编辑
        form: [],
        inputValue2:'',
        inputVisible2: false,
      }
    },
    methods: {
      dialoghandleClose(done){
        this.$confirm('确定关闭吗').then(() => {
          // function(done)，done 用于关闭 Dialog
          done();
 
          // console.info("点击右上角 'X' ，取消按钮或遮罩层时触发");
        }).catch(() => {
 
          // console.log("点击确定时触发");
        });
      },
      formatDate(dateTimeString) {  
      const date = new Date(dateTimeString);  
      const year = date.getFullYear();  
      const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始，所以需要加1，并使用padStart确保总是两位数  
      const day = String(date.getDate()).padStart(2, '0'); // 使用padStart确保总是两位数  
      return `${year}-${month}-${day}`;  
      },
      pageQuery() {
      this.finished = false;
      axios.post('/api/admin/announcement', {
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
      handleCurrentChange(val) {
      this.currentPage = val;
      this.pageQuery();
      },
      handleDelete(id) {  
      this.$confirm('此操作将永久删除该公告，是否继续?', '提示', {  
        confirmButtonText: '确定',  
        cancelButtonText: '取消',  
        type: 'warning'  
      }).then(() => {  
        // 用户点击了确定按钮，执行删除操作  
        this.delProblem(id);  
      }).catch(() => {  
        // 用户点击了取消按钮，不执行任何操作  
        console.log('取消删除');  
      });  
      },
      delProblem(ID){
      axios.post('/api/admin/announcement/del', {
        id: ID,
      },{  
        headers: {  
        'token': localStorage.getItem('token'),
        } 
      }).then(
         setTimeout(() => {
      // 这里写需要执行的操作或者调用其他方法
        }, 500),
        this.$message({
        message: '删除成功',
        type: 'success'
        }),
        this.pageQuery(),
      )
      },
      addProblem(){
      axios.post('/api/admin/announcement/add', {
        topic: this.topicName,
        content: this.content,
      },{  
        headers: {  
        'token': localStorage.getItem('token'),
        } 
      }).then(
         setTimeout(() => {
      // 这里写需要执行的操作或者调用其他方法
        }, 200),
        this.$message({
        message: '添加成功',
        type: 'success'
        }),
        this.pageQuery(),
      )
      },
      changeProblem(){
      axios.post('/api/admin/announcement/change', {
        id: this.form.id,
        topic: this.form.topic,
        content: this.form.content,
      },{  
        headers: {  
        'token': localStorage.getItem('token'),
        } 
      }).then(
         setTimeout(() => {
        }, 200),
        this.$message({
        message: '修改成功',
        type: 'success'
        }),
        this.pageQuery(),
      )
      },
      handleClose(tag) {
        this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
      },
      handleClose2(tag) {
        this.form.label.splice(this.form.label.indexOf(tag), 1);
      },

      showInput() {
        this.inputVisible = true;
        this.$nextTick(
          this.$refs.saveTagInput.$refs.input.focus()
        );
      },
      showInput2() {
        this.inputVisible2 = true;
        this.$nextTick(
          this.$refs.saveTagInput.$refs.input.focus()
        );
      },
      handleInputConfirm() {
        let inputValue = this.inputValue;
        if (inputValue) {
          this.dynamicTags.push(inputValue);
        }
        this.inputVisible = false;
        this.inputValue = '';
      },
      handleInputConfirm2() {
        let inputValue2 = this.inputValue2;
        if (inputValue2) {
          this.form.label.push(inputValue2);
        }
        this.inputVisible2 = false;
        this.inputValue2 = '';
      },
      adminShowBack(id) {
      axios.get(`/api/admin/announcement/show/${id}`,{  
        headers: {  
        'token': localStorage.getItem('token'),
        } 
      }).then(res => {
        this.form = res.data.data
      })
      },
    },
    mounted() {
      this.pageQuery();
    }
  };


</script>



<style scoped>
.box-card {
  height: 810px;
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
 .el-tag + .el-tag {
    margin-left: 10px;
  }
  .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }
  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }
</style>
