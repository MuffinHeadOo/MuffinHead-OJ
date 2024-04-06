const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer:{
    port:7070,
    proxy: {
      '/api' :{
        target: 'http://39.106.91.152:8080',
        pathRewrite: {
          '^/api' : ''
        }
      }
    }
  }
})
