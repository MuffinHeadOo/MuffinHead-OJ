<template>
<div>
  <div style="text-align: center; margin: 0 auto; max-width: 1400px">
    <el-card class="box-card" shadow="hover">
      <div class="card-header" slot="header">
          <p style="font-weight :bold">用户列表</p>
          <!-- <el-pagination :page-size="20" :pager-count="11" layout="total, prev, pager, next" :total="total" @current-change="handleCurrentChange" :current-page="currentPage"></el-pagination>
          <el-button type="primary"  icon="el-icon-refresh-left" @click="all">
              <el-icon>
                <Refresh />
              </el-icon>
              刷新
          </el-button> -->
          
           <p style="font-weight :bold;margin-left: 570px;margin-right: 10px;">用户名:</p>
           <el-input v-model="nameQuery" placeholder="请输入用户名" style="width: 150px;"/>

           <p style="font-weight :bold;margin-right: 10px;margin-left: 10px;">权限:</p>
           <el-select v-model="authority" placeholder="所有" style="width: 150px;">
               <el-option label="所有" value=null></el-option>
               <el-option label="普通用户" value=0></el-option>
               <el-option label="管理员" value=1></el-option>
           </el-select>

           <p style="font-weight :bold;margin-right: 10px;margin-left: 10px;">权限:</p>
           <el-select v-model="status" placeholder="所有" style="width: 150px;">
               <el-option label="所有" value=null></el-option>
               <el-option label="启用" value=0></el-option>
               <el-option label="禁用" value=1></el-option>
           </el-select>

          <el-button type="primary" style="margin-left: 20px;" @click="pageQuery()">查询</el-button>
      </div>

    
        <el-table :data="userList" height="700px" :header-cell-style="{ textAlign: 'center' } "
        :cell-style="{ textAlign: 'center' }" v-loading="!finished">
          <el-table-column prop="id" label="#" width="100px" />
          <el-table-column prop="name" label="用户名" width="auto">
          </el-table-column>
          <el-table-column prop="createdTime" label="创建时间" width="220px">
            <template #default="scope">
              <span> {{ formatDate(scope.row.createdTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="权限" width="150px">
            <template #default="scope">
              <el-select v-model="scope.row.authority" @change="selectChanged(scope.row.id,scope.row.authority)">
                <el-option label="管理员" :value="1"></el-option>
                <el-option label="普通用户" :value="0"></el-option>
              </el-select>
            </template>
            
          </el-table-column>
          <el-table-column label="状态" width="200px">
            <template #default="scope">
              <el-switch v-model="scope.row.status" :active-value="0" :inactive-value="1" active-color="#13ce66" inactive-color="#ff4949" active-text="启动" inactive-text="禁用" @change="switchReceiveStatus(scope.row.id,scope.row.status)"></el-switch>
            </template>
            
          </el-table-column>
          <el-table-column label="编辑" width="100px">
            <template #default="scope">
              <el-button type="primary" style="margin-left: 20px;" @click="dialogVisible = true,adminShowBack(scope.row.id)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="20"
            layout="total, prev, pager, next" :total="total"></el-pagination>
    </el-card>
  </div>

<el-dialog title="编辑信息" :visible.sync="dialogVisible" :before-close="handleClose" width="40%" show-overflow-tooltip>
        <el-form ref="form" :model="form" label-width="80px">
         <el-form-item label="头像:" style="font-weight :bold;margin-left: 100px;">
            <!-- <el-avatar shape="square" :size="100" :src="form.image" style="margin-left: 100px;" :key="form.image"></el-avatar> -->
            <el-upload  
              class="avatar-uploader"  
              action="/api/user/common/upload"
              :headers="uploadHeaders"
              :show-file-list="false"  
              :on-success="handleAvatarSuccess"  
              :before-upload="beforeAvatarUpload"  
            >  
              <img v-if="form.image" :src="form.image" class="avatar" style="margin-left: 100px;">  
              <i v-else class="el-icon-plus avatar-uploader-icon" style="margin-left: 100px;"></i>  
            </el-upload>
          </el-form-item>
          <el-form-item label="用户名:" style="font-weight :bold;width: 400px;margin-left: 100px;">
            <el-input v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="密码:" style="font-weight :bold;width: 400px;margin-left: 100px;">
            <el-input v-model="form.password" :disabled="disable"></el-input>
            <el-button type="primary" icon="el-icon-edit" size="mini" style="margin-top: 5px;" @click="changeDisabled()">修改</el-button>&nbsp;&nbsp;（不修改勿点，点错右下角取消重改）  
          </el-form-item>
          <el-form-item label="注册时间:" style="font-weight :bold;margin-left: 100px;width: 400px;">
            <el-date-picker v-model="form.createdTime" type="date" placeholder="选择日期" style="width: 320px;"></el-date-picker>
          </el-form-item>
          <el-form-item label="手机号:" style="font-weight :bold;width: 400px;margin-left: 100px;">
            <el-input v-model="form.phone"></el-input>
          </el-form-item>
          <el-form-item label="邮箱:" style="font-weight :bold;width: 400px;margin-left: 100px;">
            <el-input v-model="form.email"></el-input>
          </el-form-item>
          <el-form-item label="个人签名:" style="font-weight :bold;width: 400px;margin-left: 100px;height: 150px;">
            <el-input type="textarea" v-model="form.signature" maxlength="2000" show-word-limit style="font-weight :normal;" :rows="9"></el-input>
          </el-form-item>
          
          
        </el-form>

        <span slot="footer" class="dialog-footer">
          <el-button @click="changeDisabledTrue(),dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveAdminChange(),changeDisabledTrue(),dialogVisible = false,pageQuery();">确 定</el-button>
        </span>
      </el-dialog>

</div>
</template>


<script>
import axios from '@/http';
import { Message } from "element-ui";
  export default {
    data() {
      return {
        disable: true,
        total: 0,
        currentPage: 1,
        pageSize: 20,
        value2: true,
        dialogVisible: false,
        form: [],
        content: '请具体描述问题，并至少给出三组输入输出案例',
        userList: [],
        nameQuery: null,
        authority: '',
        status: '',
        beginDate: null,
        endDate: null,
        //scope.row.id
        pid: '',
        //判断是否修改过密码 0未修改 1修改
        p: 0,
        token:'',
      }
    },
    computed: {  
    uploadHeaders() {  
      // 返回一个包含 Authorization header 的对象  
      // 如果 token 存在，则添加到 headers 中，否则返回一个空对象或您需要的默认 headers  
      return { 'token': `${this.token}` };  
    }  
   }, 
    methods: {
      handleClose(done){
        this.$confirm('确定关闭吗').then(() => {
          // function(done)，done 用于关闭 Dialog
          this.disable = true,
          this.p = 0,
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
      axios.post('/api/admin/user', {
        page: this.currentPage,
        size: this.pageSize,
        name: this.nameQuery,
        authority: this.authority,
        status: this.status,
        beginDate: this.beginDate,
        endDate: this.endDate,
      },{  
        headers: {  
        'token': localStorage.getItem('token'),
        } 
      }).then(res => {
        this.userList = res.data.data,
        this.total = res.data.total,
        this.finished = true
      })
      },
      handleCurrentChange(val) {
      this.currentPage = val;
      this.pageQuery();
      },
      switchReceiveStatus(ID, rowData) {  
      axios.post('/api/admin/user/cs', {
        id: ID,
        status: rowData,
      },{  
        headers: {  
        'token': localStorage.getItem('token'),
        } 
      }).then(
        this.$message({
        message: '修改成功',
        type: 'success'
        })
      )
      },
      selectChanged(ID, rowData){
      axios.post('/api/admin/user/ca', {
        id: ID,
        authority: rowData,
      },{  
        headers: {  
        'token': localStorage.getItem('token'),
        } 
      }).then(
        this.$message({
        message: '修改成功',
        type: 'success'
        })
      )
      },
      adminShowBack(id) {
      axios.get(`/api/admin/user/show/${id}`,{  
        headers: {  
        'token': localStorage.getItem('token'),
        } 
      }).then(res => {
        this.form = res.data.data,
        this.pid = id,
        this.p=0
      })
      },
      saveAdminChange(){
      axios.post('/api/admin/user/show/save', {
        id: this.pid,
        image: this.form.image,
        name: this.form.name,
        password: this.form.password,
        createdTime: this.formatDate(this.form.createdTime),
        phone: this.form.phone,
        signature: this.form.signature,
        email: this.form.email,
        p: this.p,
      },{  
        headers: {  
        'token': localStorage.getItem('token'),
        } 
      }).then(
         setTimeout(() => {
      // 这里写需要执行的操作或者调用其他方法
        }, 200),
        this.$message({
        message: '修改成功',
        type: 'success'
        }),
      )
      },
      changeDisabled(){
        this.disable = false;
        this.p = 1,
        this.form.password=''
      },
      changeDisabledTrue(){
        this.disable = true,
        this.p = 0
      },
      handleAvatarSuccess(response) {  
      // 上传成功后的回调，response 是接口返回的数据  
      console.log(response);
      this.form.image = response.data; // 假设你的接口返回了一个包含url的字段  
      Message ({
            message: '头像上传成功！',
            type: 'success'
        });
    },  
    beforeAvatarUpload(file) {  
      const isJPG = file.type === 'image/jpeg';  
      const isPNG = file.type === 'image/png';  
      const isLt2M = file.size / 1024 / 1024 < 2;  
  
      if (!isJPG && !isPNG) {  
        this.$message.error('上传头像图片只能是 JPG/PNG 格式!');  
      }  
      if (!isLt2M) {  
        this.$message.error('上传头像图片大小不能超过 2MB!');  
      }  
      return isJPG || isPNG && isLt2M;  
    },
    },
    mounted() {
    this.pageQuery();
    this.token = localStorage.getItem('token');
    }
  };


</script>



<style scoped>
.PersonTop_img img {
  width: 100%;
  height: 100%;
  border-radius: 20px;
}
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
.avatar-uploader .el-upload {  
  border: 1px dashed #d9d9d9;  
  border-radius: 6px;  
  cursor: pointer;  
  position: relative;  
  width: 100px;  
  height: 100px;  
}  
.avatar-uploader .el-upload:hover {  
  border-color: #409EFF;  
}  
.avatar-uploader-icon {  
  font-size: 28px;  
  color: #8c939d;  
  width: 100px;  
  height: 100px;  
  line-height: 100px;  
  text-align: center;  
}  
.avatar {  
  width: 100px;  
  height: 100px;  
  display: block;  
} 
</style>
