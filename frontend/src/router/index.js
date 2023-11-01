import Vue from "vue";
import VueRouter from "vue-router";
import Vuex from "vuex";

import main from "@/router/modules/main";
import meeting from "@/router/modules/meeting";
import error from "@/router/modules/error";
import banner from "@/router/modules/banner";

import store from "@/store";
import jwt_decode from "jwt-decode";

import { apiInstance } from "@/api/index";
const api = apiInstance();

async function checkMeetingMember(roomCode, router) {
  var result = null;
  await api
    .get(`/meeting/meeting-member/${roomCode}`)
    .then((res) => {
      console.log(res.data);
      if (res.data.statusCode == "200") {
        result = res.data.data;
      }
    })
    .catch(() => {
      router.push("/entrance-permission-error");
      throw error;
    });
  return await Promise.resolve(result); // meetingId return
}

Vue.use(VueRouter);
Vue.use(Vuex); //

const routes = [...main, ...meeting, ...banner, ...error];
const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
  scrollBehavior() {
    return { x: 0, y: 0 };
  },
});

router.beforeEach(async (to, from, next) => {
  const access_token = localStorage.getItem("Authorization");

  // console.log("Before", from, to);
  if (access_token && to.name === "entrance") {
    store.dispatch(
      "memberStore/SET_MEMBER_ID",
      jwt_decode(access_token.substring(7)).id
    );
    // # 초대 링크 클릭 시 로그인 되어 있을 때

    // 파람 룸코드 받기
    const roomCode = to.params.roomCode;

    // 가입되어 있는지, 아닌지 체크하고 가입시킨 다음 meetingId 받기

    const meetingId = await checkMeetingMember(roomCode, router);

    // if (meetingId == "NotFound") {
    //   next({
    //     name: "entrance-entrance-permission-error",
    //   });
    // } else
    if (meetingId) {
      next({ name: "meeting", params: { id: meetingId } });
    } else {
      // console.log("entrance-denied?");
      next({
        name: "entrance-denied",
      });
    }

    return;
  } else if (to.name === "entrance") {
    // # 초대 링크 클릭 시 로그인 안 되어 있을 때
    const roomCode = to.params.roomCode;
    // 룸코드 store에 저장
    // store.dispatch("meetingStore/setInvitedMeeting", roomCode);
    sessionStorage.setItem("roomCode", roomCode);
    // login으로 보냄
    next({
      name: "login",
    });
    // login 성공하면 store에 roomcode 저장되어있는지 확인하는 로직 실행
    return;
  }

  if (access_token) {
    store.dispatch(
      "memberStore/SET_MEMBER_ID",
      jwt_decode(access_token.substring(7)).id
    );
    //router 이동할 때 로그인된 member 정보 가져오기
  }

  if (to.name === "landing" || to.name === "login") {
    //login page를 가거나 login이 성공 됐을 때는 다음으로 넘어감
    next();
  } else if (access_token) {
    next();
  } else {
    //그 외에 모든 경로는 login으로

    next({
      name: "login",
    });
  }
});

export default router;
