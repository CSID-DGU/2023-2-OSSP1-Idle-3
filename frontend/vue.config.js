const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  transpileDependencies: ["vuetify"],
  // devServer: {
  //   client: {
  //     // 웹소켓용 url 지정
  //     webSocketURL: "wss://k8a401.p.ssafy.io/ws",
  //   },
  // },
});
