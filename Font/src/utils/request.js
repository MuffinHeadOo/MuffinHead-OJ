import axios from 'axios' 
// 创建axios实例
const request = axios.create({
    // 这里可以放一下公用属性等。
    baseURL: '/api',
    timeout: 5000,
    withCredentials: true
 
})
 
// 请求拦截器
request.interceptors.request.use((config) => {
//token名称以自己的为定，我的是‘satoken’，如果不需要if这里就可以直接删掉
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    const user = localStorage.getItem("user")
    const token = localStorage.getItem("token")
    if(user){
        config.headers['token'] = JSON.parse(token);
    }
    return config;
}, (error) => {
    return Promise.reject(error)
})
 
request.interceptors.response.use((response) => {
//返回码以自己的为定，如果没有安装elementui就换成自己的提示
    let res = response.data;
    if(typeof res == 'string') {
        res = res ? JSON.parse(res) : res
    }
    return res;
}, (error) => {
    return Promise.reject(error)
})
 
export default request;//记得暴露出去