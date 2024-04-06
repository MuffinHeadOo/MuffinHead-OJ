<template>
    <el-row style="margin: auto;max-width: 1300px;min-width: 800px;">

    <el-dialog  
      :visible.sync="dialogVisible"  
      width="30%"  
      :before-close="handleClose"  
    >  
      <span style="font-size: 20px;color: rgb(205, 132, 24);">正在判题中，请勿离开</span>  
      <span slot="footer" class="dialog-footer">  
        <!-- 这里可以添加取消或确认按钮，但在这个场景下我们不需要它们 -->  
      </span>  
    </el-dialog>  

        <el-col :span="18">
            <el-card class="box-card" shadow="hover">
              <template #header>
                  <div class="card-header">
                    <p class="title">#{{ id }}、{{records.topic}}</p>
                  </div>
                </template>

            <el-select v-model="language" placeholder="请选择语言" v-if="showContent2">  
              <el-option  v-for="item in languageOptions"  :key="item.value"  :label="item.label"  :value="item.value"> </el-option>  
            </el-select>
            <v-md-editor v-model="records.content" height="600px" mode="preview"  v-if="showContent1"></v-md-editor>
            <v-md-editor v-model="code" height="600px" mode="edit"  v-if="showContent2" left-toolbar="undo redo clear" right-toolbar="sync-scroll fullscreen" style="margin-top: 10px;"></v-md-editor>
            
            <div class="center-button">
              <el-button type="primary" icon="el-icon-upload2" v-if="showContent2" @click="handleSubmit()">确认提交</el-button>
            </div>
             
            </el-card>
        </el-col>

        <el-col :span="6">
            <el-card class="box-card" shadow="hover">

                <template #header>
                  <div class="card-header" >
                    <p style="font-weight :bold">题目信息 (提交:{{ records.submit }} AC: {{ records.accepted }})</p>
                  </div>
                </template>

                <el-descriptions direction="vertical" :column="1" border>
                  <el-descriptions-item label="时间限制">
                    1000ms
                  </el-descriptions-item>
                
                  <el-descriptions-item label="空间限制">
                      128MB
                  </el-descriptions-item>

                  <el-descriptions-item label="题目标签">
                    <el-tag :key="tag" v-for="tag in tags">  
                      {{ tag }}  
                    </el-tag>
                  </el-descriptions-item>
  
                  <el-descriptions-item label="出题人">
                      {{records.userName}}
                  </el-descriptions-item>

                  <el-descriptions-item label="发布时间">
                      {{ formatDate(records.createdTime)}}
                  </el-descriptions-item>

                </el-descriptions>

                <div class="center-button">  
                  <el-button type="primary" icon="el-icon-upload2" v-if="showContent1" @click="changePage1()">提交代码</el-button>
                  <el-button type="success" icon="el-icon-refresh-left" v-if="showContent2" @click="changePage2()">返回题目</el-button> 
                </div>
      </el-card>
    </el-col>

    </el-row>

    
</template>



<script>
import axios from '@/http';


export default {
  name: "problemEdit",
  data() {
    return {
      dialogVisible: false,
      id: 1,
      runid: 1,
      records: [],
      tags: [],
      showContent1: true,
      showContent2: false,
      //代码参数
      code: '',
      language: '', // 初始选中的语言，可以是 'python' 或 'java'  
      languageOptions: [ // 可选的语言列表  
        { value: 'python', label: 'Python' },  
        { value: 'java', label: 'Java' }  
      ],
    }
  },
  methods: {
    all() {
      axios.get(`/api/home/list/${this.id}`,{  
        headers: {  
        'token': localStorage.getItem('token'),
        } 
      }).then(res => {
        this.records = res.data.data;
        this.tags = this.records.label.split(",");
      })
    },
    goToPageDetail(id) {  
      this.$router.push({ name: 'SubmitDetail', params: { id: id}});
    },
    runCode() {
      this.dialogVisible = true; // 显示对话框
      axios.post(`/api/codesandbox/run`, {
        code: this.code,
        language: this.language,
        topicId: this.records.id,
        input: this.records.input,
        output: this.records.output,
      },{  
        headers: {  
        'token': localStorage.getItem('token'),
        } 
      }).then(res => {
        this.runid = res.data.id;
        this.$router.push({ name: 'SubmitDetail', params: { id: this.runid}})
      }).finally(() => {  
      this.dialogVisible = false; // 无论成功或失败，都隐藏对话框  
    })
    },
    handleSubmit() {  
      if (!this.language) {  
        this.$message({  
          message: '请选择一门语言',  
          type: 'warning'  
        });  
        return; // 阻止表单提交或执行其他逻辑  
      } else {
        this.runCode();
      }
    },
    changePage1() {
      this.showContent1 = false;
      this.showContent2= true;
    },
    changePage2() {
      this.showContent1 = true;
      this.showContent2= false;
    },
    formatDate(isoString) {  
      const date = new Date(isoString);  
      const year = date.getFullYear();  
      const month = String(date.getMonth() + 1).padStart(2, '0');  
      const day = String(date.getDate()).padStart(2, '0');  
      return `${year}-${month}-${day}`;  
    },
  },
  mounted() {
    this.id = this.$route.params.id;
    this.all();
  }
}

</script>




<style scoped>
.center-button {  
  margin-top: 20px;
  display: flex;  
  justify-content: center;  
  align-items: center;  
}  
.box-card {
  margin: 10px;
  text-align: left;
}

.box-card {
  margin: 10px;
}

.title {
  text-align: center;
  margin: 0;
  font-size: 25px;
}

.el-tag {
  margin-right: 5px;
  margin-bottom: 5px;
}
</style>
