import { getMemberInfo } from "@/api/modules/member";

const mypageStore = {
  namespaced: true,
  state: {
    attendMeetings: [], // 해당 member가 참여한 모든 meeting
    thisMonthAttendMeetingCnt: null, // 이번달 모임개수
    totalLateCnt: null, // 총 지각횟수
    lastMonthTotalSpentMoney: null, // 지난달 총 소비금액
  },
  getters: {},
  mutations: {
    SET_ATTEND_MEETINGS(state, attendMeetings) {
      state.attendMeetings = attendMeetings;
    },
    SET_THIS_MONTH_ATTEND_MEETING_CNT(state, thisMonthAttendMeetingCnt) {
      state.thisMonthAttendMeetingCnt = thisMonthAttendMeetingCnt;
    },
    SET_TOTAL_LATE_CNT(state, totalLateCnt) {
      state.totalLateCnt = totalLateCnt;
    },
    SET_LAST_MONTH_TOTAL_SPENT_MONEY(state, lastMonthTotalSpentMoney) {
      state.lastMonthTotalSpentMoney = lastMonthTotalSpentMoney;
    },
  },
  actions: {
    // [@Method] my-page 회원정보 조회
    async excuteGetMemberInfo({ commit }) {
      // console.log("#21# 회원정보 조회 동작");
      var result = false;
      await getMemberInfo(
        async ({ data }) => {
          if (data.statusCode == 200) {
            // console.log("#21# data 확인: ", data);

            commit("SET_ATTEND_MEETINGS", data.data.attendMeetings);
            commit(
              "SET_THIS_MONTH_ATTEND_MEETING_CNT",
              data.data.thisMonthAttendMeetingCnt
            );
            commit("SET_TOTAL_LATE_CNT", data.data.totalLateCnt);
            commit(
              "SET_LAST_MONTH_TOTAL_SPENT_MONEY",
              data.data.lastMonthTotalSpentMoney
            );
            result = true;
          }
        },
        (error) => {
          error;
          // console.log(error);
        }
      );
      return await Promise.resolve(result);
    },
  },
};

export default mypageStore;
