<template>
	<div class="center">
		<h1>松饼脑袋的OJ</h1>
		<div class="logon">
			<div :class="overlaylong">
				<div class="overlaylong-Signin" v-if="disfiex == 0">
					<h2 class="overlaylongH2" style= "position: relative; top: -20px;">登录</h2>

          <el-form :model="user">
            <el-form-item>
              <el-input placeholder="请输入账号" v-model="user.name" clearable></el-input>
            </el-form-item>
            <el-form-item>
              <el-input placeholder="请输入密码" v-model="user.password" show-password></el-input>
            </el-form-item>
              
            <el-form-item>
              <el-button class="inupbutton" style="margin-left: 20px;" @click="Login()">登录</el-button>
            </el-form-item>

			<h3 style="margin-left: 20px;" @click="dialogVisible = true">Forgot your password?(下辈子记着点)</h3>
			<el-form-item>
              <el-button class="inupbutton" style="position: absolute; right: 25px; top: 35px;" @click="LoginAdmin()">管理员登录</el-button>
            </el-form-item>
			
          </el-form>


			</div>
				<div class="overlaylong-Signup" v-if="disfiex == 1">
					<h2 class="overlaylongH2" style="margin-top: -15px;">注册账号</h2>
					<el-form :model="register">
					<el-form-item>
						<el-input placeholder="请输入账号" v-model="register.name" clearable></el-input>
					</el-form-item>
					<el-form-item>
						<el-input placeholder="请输入密码" v-model="register.password" show-password></el-input>
					</el-form-item>
					<el-form-item>
						<el-button class="inupbutton" style="margin-left: 20px;" @click="Register()">注册</el-button>
					</el-form-item>
					</el-form>
				</div>
			</div>
			
			<div :class="overlaytitle">
				<div class="overlaytitle-Signin" v-if="disfiex == 0">
					<h2 class="overlaytitleH2">Hello,Friend!</h2>
					<p class="overlaytitleP">
						Register here and start your OJ journey
					</p>
					<div class="buttongohs" @click="Signin()">Register</div>
				</div>
				<div class="overlaytitle-Signup" v-if="disfiex == 1">
					<h2 class="overlaytitleH2">Welcome Back!</h2>
					<p class="overlaytitleP">Log in here and start your OJ journey</p>
					<div class="buttongohs" @click="Signup()">Login</div>
				</div>
			</div>

		</div>

		<el-dialog  
      :visible.sync="dialogVisible"  
      width="30%"  
      @close="dialogVisible = false"  
    >  
	<div>
		<p style="font-size: 20px;">&nbsp;&nbsp;&nbsp;请联系管理员</p>  
      <p style="font-size: 20px;">&nbsp;&nbsp;&nbsp;vx:&nbsp;&nbsp;&nbsp;MuffinHead0_0</p>  
      <p style="font-size: 20px;">&nbsp;&nbsp;&nbsp;qq:&nbsp;&nbsp;1608175635</p>  
	</div>
      
    </el-dialog>  

	</div>
</template>
 


<script>
import request from "@/utils/request"
import { Message } from "element-ui";
import { Dialog } from 'element-ui';  

export default {
	components: {  
    'el-dialog': Dialog  
  },
	data() {
		return {
			overlaylong: 'overlaylong',
			overlaytitle: 'overlaytitle',
			disfiex: 0,
			user : {},
			register: {},
			dialogVisible: false,
		}
	},

    created (){

	},
	methods: { 
		Signin() {
			this.overlaylong = "overlaylongleft"
			this.overlaytitle = "overlaytitleright"
			setTimeout(() => {
				this.disfiex = 1
			}, 200)
		},
		Signup() {
			this.overlaylong = "overlaylongright"
			this.overlaytitle = "overlaytitleleft"

			setTimeout(() => {
				this.disfiex = 0
			}, 200)
		},
  Login() {
    request.post("/user/login",this.user).then(res => {
		console.log(res.data)
      if(res.code === 200){
        Message ({
          message: '登录成功',
          type: 'success'
        });
		localStorage.setItem('user',JSON.stringify(res.data['user']))
		localStorage.setItem('token',JSON.stringify(res.data['token']))
        this.$router.push("/home");
      } else if(res.code === 1002){
        Message ({
          message: res.errorMessage,
          type: 'error'
        });
      } else if(res.code === 500){
        Message ({
          message: res.errorMessage,
          type: 'error'
        });
      } else if(res.code === 2){
        Message ({
          message: res.errorMessage,
          type: 'error'
        });
      } 
    })
  },
  Register(){
	request.post("/user/register",this.register).then(res => {
		if(res.code === 500){
			Message ({
            message: res.errorMessage,
            type: 'error'
        });
		} else if(res.code === 1002){
			Message ({
            message: res.errorMessage,
            type: 'error'
        });
		} else if(res.code === 200){
			Message ({
            message: '注册成功！',
            type: 'error'
        });
		}
	})
  },
  LoginAdmin() {
    request.post("/user/loginAdmin",this.user).then(res => {
      if(res.code === 200){
        Message ({
          message: '登录成功',
          type: 'success'
        });
		localStorage.setItem('user',JSON.stringify(res.data['user']))
		localStorage.setItem('token',JSON.stringify(res.data['token']))
        this.$router.push("/admin");
      } else if(res.code === 1002){
        Message ({
          message: res.errorMessage,
          type: 'error'
        });
      } else if(res.code === 500){
        Message ({
          message: res.errorMessage,
          type: 'error'
        });
      } else if(res.code === 2){
        Message ({
          message: res.errorMessage,
          type: 'error'
        });
      } 
    })
  },
	
		}
	}
</script>

<style scoped>

	.center {
    position : absolute;
		width: 100%;
		height: 100%;
		background-image: url('https://pic.52112.com/2019/05/27/JPG-190527_12/n5ErYQu6y9_small.jpg');
		background-size: 100% 100%;
		background-repeat: no-repeat;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
	}
 
	h1 {
		font-size: 30px;
		color: black;
	}
 
	.logon {
		background-color: #fff;
		border-radius: 10px;
		box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22);
		/* position: relative;
		overflow: hidden; */
		width: 768px;
		max-width: 100%;
		min-height: 480px;
		margin-top: 20px;
		display: flex;
		background: -webkit-linear-gradient(right, #4284db, #29eac4);
	}
 
	.overlaylong {
		border-radius: 10px 0 0 10px;
		width: 50%;
		height: 100%;
		background-color: #fff;
		display: flex;
		align-items: center;
		justify-content: center;
	}
 
	.overlaylongleft {
		border-radius: 0px 10px 10px 0px;
		width: 50%;
		height: 100%;
		background-color: #fff;
		transform: translateX(100%);
		transition: transform 0.6s ease-in-out;
		display: flex;
		align-items: center;
		justify-content: center;
	}
 
	.overlaylongright {
		border-radius: 10px 0 0 10px;
		width: 50%;
		height: 100%;
		background-color: #fff;
		transform: translateX(0%);
		transition: transform 0.6s ease-in-out;
		display: flex;
		align-items: center;
		justify-content: center;
	}
 
	.overlaytitle {
		border-radius: 0px 10px 10px 0px;
		width: 50%;
		height: 100%;
		background-color: rgba(0, 0, 0, 0);
		display: flex;
		align-items: center;
		justify-content: center;
	}
 
 
	.overlaytitleH2 {
		font-size: 30px;
		color: #fff;
		margin-top: 20px;
	}
 
	.overlaytitleP {
		font-size: 15px;
		color: #fff;
		margin-top: 20px;
	}
 
	.overlaytitleleft {
		border-radius: 0px 10px 10px 0px;
		width: 50%;
		height: 100%;
		background-color: rgba(0, 0, 0, 0);
		display: flex;
		align-items: center;
		justify-content: center;
		transform: translateX(0%);
		transition: transform 0.6s ease-in-out;
	}
 
	.overlaytitleright {
		border-radius: 0px 10px 10px 0px;
		width: 50%;
		height: 100%;
		background-color: rgba(0, 0, 0, 0);
		display: flex;
		align-items: center;
		justify-content: center;
		transform: translateX(-100%);
		transition: transform 0.6s ease-in-out;
	}
 
	.overlaytitle-Signin {
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
	}
 
	.overlaytitle-Signup {
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
	}
 
	.buttongohs {
		width: 180px;
		height: 40px;
		border-radius: 50px;
		border: 1px solid #fff;
		color: #fff;
		font-size: 15px;
		text-align: center;
		line-height: 40px;
		margin-top: 40px;
		cursor: pointer; /* 设置鼠标悬停时的光标类型为小手 */  
	}
 
	.overlaylongH2 {
		font-size: 25px;
		color: black;
		/* width: 250px; */
	}
 
	.overlaylong-Signin {
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
	}
 
	.overlaylong-Signup {
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
	}
 
	input {
		background-color: #eee;
		border: none;
		padding: 12px 15px;
		margin: 10px 0;
		width: 240px;
	}
	h3{
		font-size: 10px;
		margin-top: 10px;
		cursor: pointer;
	}
	.inupbutton{
		background-color: #29eac4;
		border: none;
		width: 180px;
		height: 40px;
		border-radius: 50px;
		font-size: 15px;
		color: #fff;	
		text-align: center;
	}
</style>
 