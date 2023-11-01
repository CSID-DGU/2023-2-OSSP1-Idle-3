import Vue from "vue";
import App from "./App.vue";
import store from "./store";
import router from "./router";
import vuetify from "./plugins/vuetify";
import VueAwesomeSwiper from "vue-awesome-swiper";
import "swiper/css/swiper.min.css";
import VueBottomSheet from "@webzlodimir/vue-bottom-sheet";

Vue.config.productionTip = false;
Vue.use(VueAwesomeSwiper);
Vue.use(VueBottomSheet);

function kakaoInitHandler() {
  const KAKAO_API_KEY = `${process.env.VUE_APP_KAKAO_API_KEY}`;
  if (window.Kakao === undefined) {
    setTimeout(kakaoInitHandler, 500);
  } else if (!window.Kakao.isInitialized()) {
    window.Kakao.init(KAKAO_API_KEY);
    kakaoInitHandler();
  }
}

kakaoInitHandler();

new Vue({
  store,
  router,
  vuetify,
  VueAwesomeSwiper,
  VueBottomSheet,
  render: (h) => h(App),
}).$mount("#app");
