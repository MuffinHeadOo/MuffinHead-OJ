<template>
  <div>
    <el-card>
      <el-descriptions class="margin-top" title="简介" :column="2" border>
        
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-picture-outline"></i>
            头像
          </template>
          <img class="img" :src="avatar" alt="" />
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            账户名
          </template>
          {{ account }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-s-custom"></i>
            昵称
          </template>
          {{ nickname }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-odometer"></i>
            年龄
          </template>
          {{ age }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-male"></i>
            <i class="el-icon-female"></i>
            性别
          </template>
          <el-tag size="small">{{ sex }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-message"></i>
            邮箱Email
          </template>
          {{ email }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            手机号码
          </template>
          {{ mobilePhoneNumber }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-location-outline"></i>
            地区
          </template>
          {{ area }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-office-building"></i>
            职业
          </template>
          {{ work }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-basketball"></i>
            兴趣爱好
          </template>
          {{ hobby }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-magic-stick"></i>
            个性签名
          </template>
          {{ design }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-date"></i>
            注册日期
          </template>
          {{ createDate | formatDate }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script>
// export default {
//   name: "Info",
//   data() {
//     return {
//       avatar: String,
//       account: String,
//       age: Number,
//       email: String,
//       mobilePhoneNumber: String,
//       area: String,
//       createDate: Number,
//       nickname: String,
//       sex: String,
//       work: String,
//       hobby: String,
//       design: String,
//     };
//   },
//   mounted() {
//     this.load();
//   },
//   methods: {
//     load() {
//       userInfo(this.$route.params.id)
//         .then((res) => {
//           this.avatar = res.data.avatar;
//           this.account = res.data.account;
//           this.age = res.data.age;
//           this.email = res.data.email;
//           this.mobilePhoneNumber = res.data.mobilePhoneNumber;
//           this.area = res.data.area;
//           this.createDate = res.data.createDate;
//           this.nickname = res.data.nickname;
//           this.sex = res.data.sex == 1 ? "男" : "女";
//           this.work = res.data.work;
//           this.design = res.data.design;
//           this.hobby = res.data.hobby;
//         })
//         .catch((err) => {
//           console.log(err);
//         });
//     },
//     load2() {
//       userInfo(this.$route.params.id)
//         .then((res) => {
//           this.avatar = res.data.avatar;
//           this.account = res.data.account;
//           this.age = res.data.age;
//           this.email = res.data.email;
//           this.mobilePhoneNumber = res.data.mobilePhoneNumber;
//           this.area = res.data.area;
//           this.createDate = res.data.createDate;
//           this.nickname = res.data.nickname;
//           this.sex = res.data.sex == 1 ? "男" : "女";
//           this.work = res.data.work;
//           this.design = res.data.design;
//           this.hobby = res.data.hobby;
//         })
//         .catch((err) => {
//           console.log(err);
//         });
//     },
//   },
// };

</script>

<style scoped>
.img {
  width: 80px;
  height: 80px;
}
</style>
