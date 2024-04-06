<template>
<el-container>
  <el-header>
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
      <el-menu-item index="1">管理中心</el-menu-item>    
    </el-menu>
  </el-header>

  <el-container>

    <el-menu default-active="1">
      
      <el-menu-item index="1" @click="goToPage('AdminInterduce')">
        <i class="el-icon-setting"></i>
        <span>功能介绍</span>
      </el-menu-item>

      <el-menu-item index="2" @click="goToPage('AdminProblem')">
        <i class="el-icon-document"></i>
        <span>题目管理</span>
      </el-menu-item>

      <el-menu-item index="3" @click="goToPage('AdminUser')">
        <i class="el-icon-menu"></i>
        <span>用户管理</span>
      </el-menu-item>

      <el-menu-item index="4" @click="goToPage('adminAnnouncement')">
        <i class="el-icon-setting"></i>
        <span>公告管理</span>
      </el-menu-item>

    </el-menu>
 
    
  
    
    <el-main><router-view></router-view></el-main>
  </el-container>
</el-container>
</template>


<script>
  export default {
  name: 'AdminView',
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

</style>

