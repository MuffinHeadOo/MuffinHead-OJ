<template>

<div class="major">


  <div class="body-bg">

    <el-header>
      <div class="wrapper">
    <nav>
        <input type="radio" name="tab" id="home" checked>
        <input type="radio" name="tab" id="comment" >
        <input type="radio" name="tab" id="envelope" >
        <input type="radio" name="tab" id="heart" >
        <input type="radio" name="tab" id="user" >
         
        <label for="home" class="home" @click="goToPage('HomeComponent')">
          <a href="#" >
            <i class="fa fa-home" aria-hidden="true"></i>
            首页
          </a>
        </label>
        <label for="comment" class="comment" @click="goToPage('TopicComponent')">
          <a href="#">
            <i class="fa fa-comment" aria-hidden="true"></i>
            题库
          </a>
        </label>
        <label for="envelope" class="envelope" @click="goToPage('SubmitComponent')">
          <a href="#">
            <i class="fa fa-envelope" aria-hidden="true"></i>
            提交记录
          </a>
        </label>
        <label for="heart" class="heart" @click="goToPage('TalkComponent')">
          <a href="#">
            <i class="fa fa-heart" aria-hidden="true"></i>
            在线讨论
          </a>
        </label>
        <label for="user" class="user" @click="goToPage('PersonalComponent')">
          <a href="#">
            <i class="fa fa-user" aria-hidden="true"></i>
            个人信息
          </a>
        </label>
        <div class="tab"></div>
    </nav>
    </div>
    </el-header>

  </div>
  
    

  
  <router-view></router-view>

  




</div>
</template>


<script>

export default {
  name: 'menuChange',
	data() {
		return {
      currentView: 'Home', // 当前视图
		}
	},

  created (){

	},
  methods: {
   goToPage(pageName) {  
      this.$router.push({ name: pageName });
      
    }
  },
	}
</script>


<style scoped>


.body-bg {
    /* 弹性盒子布局 垂直居中 文字居中 */
    display: flex;
    justify-content: center;
    text-align: center;
}
.major {
    /* 设置body高度为100%窗口高度 */
    height: 100vh;
    width: 100vw;  
    background: linear-gradient(200deg,#a8edea,#fed6e3);
}
.wrapper{
    /* 设置宽度为60%窗口宽度 */
    width: 60vw;
    height: 60px;
    line-height: 60px;
    background-color: #fff;
    /* 盒子阴影 */
    box-shadow: 0px 5px 15px rgba(0,0,0,0.25);
    border-radius: 50px;
}
.wrapper nav{
    display: flex;
    position: relative;
}
.wrapper nav label{
    flex:1;
    width:100%;
    position: relative;
    z-index: 1;
    cursor: pointer;
}
.wrapper nav label a{
    position: relative;
    z-index: -1;
    color: #333;
    /* font-size: 20px;
    font-weight: 500; */
    text-decoration: none;
}
.wrapper nav label a i{
    font-size: 25px;
    margin: 0px 7px;
}
.wrapper nav input{
    display: none;
}
.wrapper nav .tab{
    position: absolute;
    height: 100%;
    width: 20%;
    left: 0px;
    bottom: 0px;
    /* 渐变背景 自左向右 */
    background: linear-gradient(to right, #f09819, #ff5858);
    border-radius: 50px;
    /* 添加动画过渡 贝塞尔曲线 */
    transition: 0.6s cubic-bezier(0.68,-0.55,0.265,1.55);
}
.wrapper nav #home:checked ~ label.home a,
.wrapper nav #comment:checked ~ label.comment a,
.wrapper nav #envelope:checked ~ label.envelope a,
.wrapper nav #heart:checked ~ label.heart a,
.wrapper nav #user:checked ~ label.user a{
    color: #fff;
    /* 这里字体颜色改变也需要加个动画过渡 */
    transition: 0.6s;
}
.wrapper nav #comment:checked ~ .tab{
    left: 20%;
}
.wrapper nav #envelope:checked ~ .tab{
    left: 40%;
}
.wrapper nav #heart:checked ~ .tab{
    left: 60%;
}
.wrapper nav #user:checked ~ .tab{
    left: 80%;
}
</style>
