const websocketStore = {
  namespaced: true,
  state: {
    connected: false,
    stompClient: null,
  },
  getters: {
    getStompClient(state) {
      return state.stompClient
    }
  },
  mutations: {
    UPDATE_STOMP_CLIENT(state, payload) {
      state.stompClient = payload;
    },
    UPDATE_CONNECTED(state, payload) {
      state.connected = payload;
    },
  },
  actions: {
    updateStompClient({ commit }, stomp) {
      commit("UPDATE_STOMP_CLIENT", stomp);
    },
    updateConnected({ commit }, isConnect) {
      commit("UPDATE_CONNECTED", isConnect);
    },
  }
}

export default websocketStore;