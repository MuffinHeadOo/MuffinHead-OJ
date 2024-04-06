import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '@/views/LoginView.vue'
import Home from '@/views/HomeView.vue'
import HomeComponent from '@/components/HomeComponent.vue'
import PersonalComponent from '@/components/PersonalComponent.vue'
import ShowPersonalComponent from '@/components/ShowPersonalComponent.vue'
import InfoComponent from '@/components/InfoComponent.vue'
import TopicComponent from '@/components/TopicComponent.vue'
import AboutComponent from '@/components/AboutComponent.vue'
import SubmitComponent from '@/components/SubmitComponent.vue'
import SubmitDetail from '@/components/SubmitDetail.vue'
import NotFound from '@/components/NotFound.vue'
import problemEdit from '@/components/problemEdit.vue'
import AdminView from '@/views/AdminView.vue'
import AdminProblem from '@/components/admin/adminProblem.vue'
import AdminInterduce from '@/components/admin/adminInterduce.vue'
import AdminUser from '@/components/admin/adminUser.vue'
import adminAnnouncement from '@/components/admin/adminAnnouncement.vue'
import AnnouncementDetail from '@/components/AnnouncementDetail.vue'
import TalkComponent from '@/components/TalkComponent.vue'

Vue.use(VueRouter)

const router = new VueRouter({
  mode: 'history',
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'Login',
      component: Login ,
    },
    {
      path: '/admin',
      name: 'AdminView',
      component: AdminView ,
      redirect:'/admin/interduce',
      children: [
        {
          path: '/admin/interduce',
          name: 'AdminInterduce',
          component: AdminInterduce ,
        },
        {
          path: '/admin/problem',
          name: 'AdminProblem',
          component: AdminProblem ,
        },
        {
          path: '/admin/user',
          name: 'AdminUser',
          component: AdminUser ,
        },
        {
          path: '/admin/Announcement',
          name: 'adminAnnouncement',
          component: adminAnnouncement ,
        }
      ]
    },
    {
      path: '/home',
      name: 'Home',
      component: Home ,
      redirect:'/home/main',
      children: [
        {
          path: '/home/TalkComponent',
          name: 'TalkComponent',
          component: TalkComponent ,
        },
        {
          path: '/home/announcement/:id',
          name: 'AnnouncementDetail',
          component: AnnouncementDetail ,
        },
        {
          path: '/home/main',
          name: 'HomeComponent',
          component: HomeComponent ,
        },
        {
          path: '/home/about',
          name: 'AboutComponent',
          component: AboutComponent ,
        },
        {
          path: '/home/topic',
          name: 'TopicComponent',
          component: TopicComponent ,
        },
        {
          path: '/home/submit',
          name: 'SubmitComponent',
          component: SubmitComponent ,
        },
        {
          path: '/home/submitDetail/:id',
          name: 'SubmitDetail',
          component: SubmitDetail ,
        },
        {
          path: '/home/user/:id',
          name: 'ShowPersonalComponent',
          component: ShowPersonalComponent ,
        },
        {
              path: '/home/topic/:id',
              name: 'problemEdit',
              component: problemEdit ,
        },
        {
          path: '/home/personal',
          name: 'PersonalComponent',
          component: PersonalComponent ,
          children: [
            {
              path: '/home/personal/info',
              name: 'InfoComponent',
              component: InfoComponent ,
            },
          ]
        },
      ]
    },
    {
      path:'*',
      redirect: '/404',
    },
    {
      path:'/404',
      component: NotFound
    },
  ]
})


const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

router.beforeEach((to,from,next) =>{
  if(to.path === '/login') {
    next();
  } else {
    const user = JSON.parse(window.localStorage.getItem('user'))
    if(!user) {
      return next("/login");
    }
  }
  next();
})

export default router
