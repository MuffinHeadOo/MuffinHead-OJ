<template>
  <div >
    <!--顶部div-->
    <div class="PersonTop">
      <div class="PersonTop_img">
        <img v-image-preview :src="user.image" />
      </div>
      
      <div class="PersonTop_text">
        <div class="user_text">
          <div class="user_name">
            <span> {{ user.name}} </span>
          </div>

          <div class="user-v">
              <span> {{ user.authority === 0 ? '普通用户' : '管理员' }} </span>
          </div>

          <!-- <div class="user_qianming">
            <span> {{ user.name === 0 ? '普通用户' : '管理员' }} </span>
          </div> -->

          <div class="user_anniu">
            <!-- <el-button class="el-icon-edit"  type="primary" size="medium" plain @click="edit" style="margin-top: 30px;">编辑</el-button> -->
            <!-- <el-button v-else @click="follow" type="primary" size="medium" icon="el-icon-check" v-text="   isfollowid.indexOf(this.$route.params.id) > -1     ? '已关注'     : '关注' "></el-button> -->
          </div>
        </div>

        <div class="user_num">
          <span style="margin-right: 50px;"> {{ user.signature}} </span>
        </div>
        
      </div>

      <div class="person_body">
        <div class="person_body_left">
          <el-card class="box-card" :body-style="{ padding: '0px' }">
            <div slot="header" class="clearfix">
            <span class="person_body_list" style="border-bottom: none">
              个人中心
            </span>
          </div>

          

          <el-menu active-text-color="#00c3ff" class="el-menu-vertical-demo" default-active="a">

            <el-menu-item index="a">
              <i class="el-icon-user"></i>
              <span slot="title">个人简介</span>
            </el-menu-item> 


          </el-menu>
          
          </el-card>
        </div>

        <div class="person_body_right">
                  <!-- 1         -->
            <el-card>
              <el-descriptions class="margin-top" title="简介" :column="2" border >
                
                
                <el-descriptions-item>
                  <template slot="label">
                    <i class="el-icon-user"></i>
                    用户名
                  </template>
                  {{ user.name }}
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">
                    <i class="el-icon-user"></i>
                    AC数量
                  </template>
                  {{ user.accepted }}
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">
                    <i class="el-icon-user"></i>
                    权限
                  </template>
                  {{ user.authority === 0 ? '普通用户' : '管理员' }}
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">
                    <i class="el-icon-user"></i>
                    状态
                  </template>
                  {{ user.status === 0 ? '正常' : '已封禁' }}
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">
                    <i class="el-icon-message"></i>
                    邮箱Email
                  </template>
                  {{ user.email }}
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">
                    <i class="el-icon-mobile-phone"></i>
                    手机号码
                  </template>
                  {{ user.phone }}
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">
                    <i class="el-icon-date"></i>
                    注册日期
                  </template>
                  {{ formatDate(user.createdTime) }}
                </el-descriptions-item>
              </el-descriptions>
            </el-card>

      
          
          
        </div>
      </div>

    </div>
    
    


  </div>
</template>

<script>
import axios from '@/http';


export default {
  data() {
      return {
        DialogFlag: false,
        user:'',
        show: 1,
        form:[],
        oldPwd: '',
        newPwd: '',
        repPwd: '',
        id: '',
      };
  },
  methods: {
    all() {
      axios.get(`/api/home/user/${this.id}`,{  
        headers: {  
        'token': localStorage.getItem('token'),
        } 
      }).then(res => {
        this.user = res.data.data,
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
  },
  mounted() {
    this.id = this.$route.params.id;
    this.all();
  }

	}
</script>

<style scoped>
.header {
  font-size: 24px;
  font-weight: 800;
  margin-top: 20px;
  margin-left: 20px;
}
.save-button-wrapper {  
  position: absolute;  
  right: 0;  
  top: 0;  
  left: 40px;
  transform: translateY(-50%); /* 垂直居中 */  
}  
  .me-video-player {
  background-color: transparent;
  width: 100%;
  height: 100%;
  object-fit: fill;
  display: block;
  position: fixed;
  left: 0;
  z-index: 0;
  top: 0;
}
.PersonTop {
  width: 1000px;
  height: 140px;
  padding-top: 20px;
  background-color: white;
  margin-top: 30px;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  border-radius: 5px;
}

.PersonTop_img {
  width: 150px;
  height: 120px;
  background-color: #8c939d;
  margin-right: 24px;
  margin-left: 20px;
  overflow: hidden;
  border-radius: 20px;
}

.PersonTop_img img {
  width: 100%;
  height: 100%;
  border-radius: 20px;
}

.PersonTop_text {
  height: 120px;
  width: 880px;
  display: flex;
}

.user_text {
  width: 60%;
  height: 100%;
  line-height: 30px;
}

.user_name {
  font-weight: bold;
  font-size: large;
}
.user-v {
  margin-bottom: -5px;
}
.user-v-img {
  width: 15px;
  height: 15px;
}
.user-v-font {
  font-size: 15px;
  color: #00c3ff;
}
.user_qianming {
  font-size: 14px;
  color: #999;
}

.user_num {
  width: 180%;
  height: 100%;
  display: flex;
  align-items: center;

}

.user_num > div {
  text-align: center;
  border-right: 1px dotted #999;
  box-sizing: border-box;
  width: 80px;
  height: 40px;
  line-height: 20px;
}

.num_text {
  color: #999;
}

.num_number {
  font-size: 20px;
  color: #333;
}
.el-menu-item>span {
  font-size: 16px;
  color: #999;
}

/*下面部分样式*/
.person_body {
  width: 1000px;
  margin-top: 210px;
  display: flex;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  border-radius: 5px;
}

.person_body_left {
  width: 27%;
  height: 600px;
  border-radius: 5px;
  margin-right: 3%;
  text-align: center;
}

.person_body_list {
  width: 100%;
  height: 50px;
  margin-top: 25px;
  font-size: 22px;
  border-bottom: 1px solid #f0f0f0;
  background-image: -webkit-linear-gradient(
    left,
    rgb(42, 134, 141),
    #e9e625dc 20%,
    #3498db 40%,
    #e74c3c 60%,
    #09ff009a 80%,
    rgba(82, 196, 204, 0.281) 100%
  );
  -webkit-text-fill-color: transparent;
  -webkit-background-clip: text;
  -webkit-background-size: 200% 100%;
  -webkit-animation: masked-animation 4s linear infinite;
}

.el-menu-item {
  margin-top: 22px;
}

.person_body_right {
  width: 70%;
  height: 500px;
  border-radius: 5px;
  background-color: white;
}

.box-card {
  height: 500px;
}

/*ui样式*/
.el-button {
  width: 84px;
}

</style>
