// import meetingStore from "./meeting";st

const placeStore = {
  namespaced: true,
  state: {
    placeX: null,
    placeY: null,
    placeName: null,
    placeAddr: null,
  },
  getters: {},
  mutations: {
    UPDATE_PLACE(state, placeMap) {
      state.placeX = placeMap.get("x");
      state.placeY = placeMap.get("y");
      state.placeName = placeMap.get("name");
      state.placeAddr = placeMap.get("addr");
    },
    RESET_PLACE(state) {
      state.placeX = null;
      state.placeY = null;
      state.placeName = null;
      state.placeAddr = null;
    },
    SET_SELECT_PLACE(state, placeMap) {
      state.regist_placeX = placeMap.get("x");
      state.regist_placeY = placeMap.get("y");
      state.regist_placeName = placeMap.get("name");
      state.regist_placeAddr = placeMap.get("addr");
    },
  },
  actions: {
    updatePlace({ commit }, placeMap) {
      commit("UPDATE_PLACE", placeMap);

      // meetingStore.commit("SET_PLACE_NAME", placeMap.get("name"));
      // commit("meetingStore/SET_PLACE_NAME", placeMap.get("name"), {
      //   root: true,
      // });
      // commit("meetingStore/SET_PLACE_ADDR", placeMap.get("addr"), {
      //   root: true,
      // });
    },
    resetPlace({ commit }) {
      commit("RESET_PLACE");
    },
  },
};

export default placeStore;
