// src/http.js  
  
import axios from 'axios';  
import router from '@/router'; // 确保你的路由文件被正确导入  
import { Message } from "element-ui";
// 创建 Axios 实例  
const service = axios.create({  
  baseURL: process.env.VUE_APP_BASE_API, // 使用环境变量配置基础 URL  
});  
  
  
// 响应拦截器  
service.interceptors.response.use(  
  response => {  
    // 对响应数据做点什么  
    return response; // 返回实际的数据，而非整个响应对象  
  },  
  error => {  
    const { response } = error;  
    if (response && response.status === 401) {  
      // 401 错误处理逻辑  
      // 比如跳转到登录页面或者执行其他全局操作  
      Message ({
        message: '用户信息已过期，请重新登录',
        type: 'error'
      });
      router.push('/login'); // 假设你有一个登录路由  
      // 返回一个已解析的 Promise 来阻止错误冒泡  
      return Promise.resolve(error.response); 
    }  
    // 返回错误信息，可以在组件中捕获并处理  
    return Promise.reject(error);  
  }  
);  
  
// 导出配置好的 Axios 实例  
export default service;