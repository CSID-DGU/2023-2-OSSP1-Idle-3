import { getLoginMember, logout } from "@/api/modules/member";
// import router from "@/router";

const memberStore = {
  namespaced: true,
  state: {
    member_id: null,
    member: null, // member 객체
  },
  getters: {},
  mutations: {
    SET_MEMBER(state, member) {
      state.member = member;
    },
    SET_MEMBER_ID(state, id) {
      state.member_id = id;
    },
  },
  actions: {
    // [@Method] 로그인 회원 확인
    async isLogin({ commit, state }) {
      // console.log("2. getUserInfo() decodeToken :: ");
      await getLoginMember(
        state.member_id,
        ({ data }) => {
          if (data.message === "SUCCESS") {
            // console.log()
            // console.log("3. getUserInfo data >> ", data);
            commit("SET_MEMBER", data.data);
          } else {
            // console.log("유저 정보 없음");
          }
        },
        async (error) => {
          error;
          // console.log(
          //   "getUserInfo() error code [토큰 만료되어 사용 불가능.] ::: ",
          //   error
          // );
          // router.push("/");
          // commit("SET_IS_VALID_TOKEN", false);
          // await dispatch("tokenRegeneration");
        }
      );
    },

    async SET_MEMBER_ID({ commit }, id) {
      await commit("SET_MEMBER_ID", id);
    },

    async memberLogout({ commit }, id) {
      commit,
        await logout(id, () => {
          localStorage.clear();
          window.location.href = "/";
        }),
        (error) => {
          error
          // console.log("로그인 실패", error);
        };
    },
  },
};

export default memberStore;
