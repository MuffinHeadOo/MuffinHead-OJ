<template>
  <div>
    <input type="button" value="发送post请求" @click="handleSend" />
  </div>
</template>



<style>
  
</style>

<script>
import axios from '@/http';

export default {
  name: 'AboutComponent',
  props: {

  },
  data() {
    return {
      
    }
  },
  methods: {
    handleSendPost() {
      axios.post('/api/home/list',{page: 1,pageSize: 10,topic: null,userName: null},{headers:{token:'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAADWMQQrDIBBF7zLrCDOaRMkJuukhxjoDBloEFVpC715ddPf_f7x_wdkyHMAu7YLOGhuVzIpIhsODjEuoniIJRoUFMjc4yKMPdqNtXaD2OOz6qU2ek9c66r2r5tdNOI2Je5r_pYws7_LXg596Hozo-wPJ8RT8hgAAAA.umtWbyOLp6uEF0Il29nF1r5vJv4W6De7KwqsurLnW-5RYSrd0MZOgTXXQ_UlyphAGO3fbHPPZiUqmTzkrX52Ng'}}).then(res => {
      console.log(res.data)
      })
    },
    handleSend(){
      //使用axios提供的统一方式
      axios({
        url: '/api/home/list',
        method: 'post',
        data:{
          page: 1,
          pageSiz: 10,
          topic: null,
          userName: null
        },
        headers:{
          token:'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAADWMQQrDIBBF7zLrCDOaRMkJuukhxjoDBloEFVpC715ddPf_f7x_wdkyHMAu7YLOGhuVzIpIhsODjEuoniIJRoUFMjc4yKMPdqNtXaD2OOz6qU2ek9c66r2r5tdNOI2Je5r_pYws7_LXg596Hozo-wPJ8RT8hgAAAA.umtWbyOLp6uEF0Il29nF1r5vJv4W6De7KwqsurLnW-5RYSrd0MZOgTXXQ_UlyphAGO3fbHPPZiUqmTzkrX52Ng'
        }
      }).then(res => {
      console.log(res.data)
      })
    },
  },
}
</script>