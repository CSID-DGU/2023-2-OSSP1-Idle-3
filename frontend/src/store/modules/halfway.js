// import meetingStore from "./meeting";

// 중간 지점 찾는 store

const halfwayStore = {
  namespaced: true,
  state: {
    startPlaces: [null, null],
    middlePlace: [],
  },
  getters: {},
  mutations: {
    ADD_MIDDLE_PLACE(state, middlePlace) {
      state.middlePlace = middlePlace;
    },
    UPDATE_START_PLACE(state, startPlace) {
      state.startPlaces[localStorage.getItem("listIndex") - 1] = startPlace;
    },
    ADD_LIST(state) {
      state.startPlaces.push(null);
    },
    REMOVE_START_PLACE(state, index) {
      state.startPlaces.splice(index, 1);
    },
    RESET_START_PLACE(state) {
      state.startPlaces = [null, null];
    },
    SET_START_PLACE(state, startMeetingPlaces) {
      state.startPlaces = startMeetingPlaces;
    },
  },

  actions: {
    resetStartPlace({ commit }) {
      commit("RESET_START_PLACE");
    },
    addMiddlePlace({ commit }, middlePlace) {
      commit("ADD_MIDDLE_PLACE", middlePlace);
    },
    updateHalfway({ commit }, startPlace) {
      commit("UPDATE_START_PLACE", startPlace);
    },
    addPlaceList({ commit }) {
      commit("ADD_LIST");
    },
    removePlaceList({ commit }, index) {
      commit("REMOVE_START_PLACE", index);
    },
    setStartPlace({ commit }, startMeetingPlaces) {
      commit("SET_START_PLACE", startMeetingPlaces);
    },
  },
};

export default halfwayStore;
