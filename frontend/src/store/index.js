import Vue from "vue";
import Vuex from "vuex";

// !store 값 유지: npm i --save vuex-persistedstate 설치 필요
// import createPersistedState from "vuex-persistedstate";

import placeStore from "./modules/place";
import meetingStore from "./modules/meeting";
import halfwayStore from "./modules/halfway";
import memberStore from "./modules/member";
import mypageStore from "./modules/mypage";
import websocketStore from "./modules/websocket";

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    placeStore: placeStore,
    meetingStore: meetingStore,
    halfwayStore: halfwayStore,
    memberStore: memberStore,
    mypageStore: mypageStore,
    websocketStore: websocketStore,
  },
  // plugins: [
  //   createPersistedState({
  //     // ! localStorage에 저장할 store만을 path에 등록
  //     paths: ["memberStore"],
  //   }),
  // ],
});
