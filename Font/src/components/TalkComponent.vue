<template>
  <div align="center">
    <el-row style="width: 50%;margin-top: 10px;" >
      <!--      用户列表-->
      <el-col :span="6">
        <el-card style="width: 100%;height: 800px;margin-right: 10px;">
          <div style="text-align: center;font-size: 28px;margin-bottom: 10px">当前在线</div>
          <div style="height: 700px;overflow-y:auto;border:1px solid #000000;border-radius: 5px">
            <div v-for="(item,index) in userList" :key="index" style="padding: 10px;margin-top: 10px;font-size: 20px">
              {{item}}
            </div>
          </div>
        </el-card>
      </el-col>
      <!--      聊天室-->
      <el-col :span="18">
        <div style="width: 100%;margin-left: 10px;">
          <el-card style="width: 100%;height: 800px">
            <div style="text-align: center;font-size: 28px;margin-bottom: 10px">talk</div>
            <div style="width: 100%;height: 550px;border:1px solid #000000;border-radius: 5px;overflow-y:auto;margin-bottom: 10px">
              <div v-for="(item,index) in msgList" :key="index">
                <div align="right" v-if="item.from===user" style="color: dodgerblue">{{item.time}}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<el-tag style="margin-top: 15px;">{{item.from}}</el-tag><el-avatar shape="square" :size="30" :src="item.image" style="margin-right: 10px;" :key="item.image"></el-avatar>
                <div style="margin-right: 10px;">{{item.msg}}</div>
                </div>
                <div align="left" v-else style="color: coral"><el-avatar shape="square" :size="30" :src="item.image" style="margin-left: 10px;" :key="item.image"></el-avatar><el-tag type="danger">{{item.from}}</el-tag>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{item.time}}
                <div style="margin-left: 10px;">{{item.msg}}</div>
                </div>
              </div>
            </div>
            <el-input @keyup.enter.native="send" type="textarea" v-model="message.msg" :autosize="{ minRows: 2, maxRows: 4}" placeholder="请输入聊天内容"></el-input>
            <div align="right">
              <el-button type="primary" style="margin-top: 10px" @click="send">发送</el-button>
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import axios from '@/http';

  let socket;

  export default {
    name: 'TalkComponent',
    data() {
      return {
        // 登录用户
        user: '',
        // 消息记录列表
        msgList: [],
        allList:[],
        image:'',
        // 发送的消息
        message: {
          time:null,//时间
          to: '',//发给谁
          from: '',
          msg: '',
          image:'',
        },
        // 在线用户列表
        userList: []
      }
    },
    methods: {
      all() {
      axios.post('/api/websocket', {

      },{  
        headers: {  
        'token': localStorage.getItem('token'),
        } 
      }).then(res => {
        this.msgList = res.data.data
      })
    },
      init() {
        // 如果sessionStorage中没有用户信息，则跳转登录页面
        this.user = JSON.parse(localStorage.getItem('user')).name;
        this.image = JSON.parse(localStorage.getItem('user')).image;

        //初始化时加载数据库历史消息
        // this.msgList.push(data)

        // if (!this.user) {
        //   this.$router.push('/')
        // }
        let that = this;
        if (typeof (WebSocket) == "undefined") {
          console.log("您的浏览器不支持WebSocket");
        } else {
          console.log("您的浏览器支持WebSocket");
          let socketUrl = "ws://39.106.91.152:8080/socket/" + this.user;
          if (socket != null) {
            socket.close();
            socket = null;
          }
          // 开启一个websocket服务
          socket = new WebSocket(socketUrl);
          //打开事件
          socket.onopen = function () {
            console.log("websocket已打开");
          };
          //  浏览器端收消息，获得从服务端发送过来的文本消息
          socket.onmessage = function (msg) {
            console.log("收到数据====" + msg.data)
            let data = JSON.parse(msg.data)
            console.log(data);
            if (data.userNames) {
              // userNames存在则是有人登录，获取在线人员信息
              that.userList = data.userNames
            } else {
              // userNames不存在则是有人发消息
              that.msgList.push(data)
              // console.log(that.msgList)
            }
          };
          //关闭事件
          socket.onclose = function () {
            console.log("websocket已关闭");
          };
          //发生了错误事件
          socket.onerror = function () {
            console.log("websocket发生了错误");
          }
        }
      },
      send() {
        if (!this.message.msg) {
          this.$message({
            message: '大兄弟，请输入聊天消息！',
            type: 'warning'
          });
        } else {
          if (typeof (WebSocket) == "undefined") {
            console.log("您的浏览器不支持WebSocket");
          } else {
            console.log("您的浏览器支持WebSocket");
            this.message.from=this.user;
            this.message.time = (new Date()).toLocaleDateString() + ' ' + (new Date()).toLocaleTimeString();
            this.message.image=this.image;
            socket.send(JSON.stringify(this.message));
            this.message.msg = '';
          }
        }
      }
    },
    mounted() {
      this.all(),
      this.init()
    }
  }
</script>