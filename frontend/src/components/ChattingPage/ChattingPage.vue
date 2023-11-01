<template>
  <v-sheet height="100%" style="padding-top: 55px">
    <!-- header -->
    <chatting-header
      @openDrawer="openDrawer"
      :name="meetingName"
    ></chatting-header>
    <!-- 채팅 멤버 목록 -->
    <v-navigation-drawer v-model="drawer" fixed temporary right>
      <v-sheet height="100%" class="d-flex flex-column justify-space-between">
        <div>
          <!-- 모임 이름 -->
          <div
            class="px-4 py-3 bold-font xxl-font main-col-1"
            style="word-break: break-all"
          >
            {{ meetingName }}
          </div>
          <v-divider></v-divider>
          <!-- 채팅 멤버 리스트 -->
          <v-list-item v-for="member in member_list" :key="member.memberId">
            <!-- 채팅 멤버 프로필 사진 -->
            <v-list-item-avatar rounded="lg" size="40">
              <v-img :src="member.profile"></v-img>
            </v-list-item-avatar>
            <!-- 채팅 멤버 닉네임-->
            <v-list-item-content>
              <v-list-item-title class="xs-font main-col-1">
                {{ member.nickname }}
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </div>
        <!-- 미팅 방 페이지로 가는 버튼 -->
        <detail-button :elevation="false" :isIcon="false"></detail-button>
      </v-sheet>
    </v-navigation-drawer>
    <!-- 새로 들어온 채팅 -->
    <v-snackbar
      id="chatting-snackbar"
      v-model="snackbar"
      color="var(--main-col-1)"
      style="margin: 0px 0px 58px 0px"
      min-width="250"
      width="95%"
      max-width="480"
      min-height="0"
      timeout="-1"
      outlined
      elevation="5"
    >
      <div class="d-flex flex-row justify-space-between align-center">
        <div class="d-flex flex-column">
          <span class="light-font" v-if="newMessageMemberId">
            {{ members[newMessageMemberId].nickname }}
          </span>
          <span class="medium-font">
            {{ newMessage }}
          </span>
        </div>
        <v-icon color="var(--main-col-1)">mdi-arrow-down</v-icon>
      </div>
    </v-snackbar>
    <!-- 채팅 정보를 불러올 수 없는 경우 -->
    <internet-error ref="error"></internet-error>
    <chatting-loading v-if="loading"></chatting-loading>
    <v-sheet v-else class="d-flex flex-column justify-end" min-height="100%">
      <!-- scroll 맨 아래로 내리는 버튼 -->
      <scroll-bottom-button ref="scrollDownBtn"></scroll-bottom-button>
      <!-- 채팅창 -->
      <v-sheet id="chattingMessages">
        <!-- 무한스크롤 -->
        <infinite-loading
          spinner="spiral"
          direction="top"
          @infinite="infiniteHandler"
        >
          <div slot="no-more"></div>
        </infinite-loading>

        <!-- 메세지 반복 -->
        <!-- <div> -->
        <!-- header 여백 -->
        <div v-for="(item, idx) in chatList" :key="idx">
          <!-- 날짜 : 년월일 (날짜가 같은 경우처음에만 노출) -->
          <div
            class="d-flex flex-row justify-center sm-font main-col-1 mt-3"
            v-if="
              idx == 0 ||
              item.chattingTime.split('T')[0] !=
                chatList[idx - 1].chattingTime.split('T')[0]
            "
          >
            {{ new Date(item.chattingTime).getFullYear() }}년
            {{ new Date(item.chattingTime).getMonth() + 1 }}월
            {{ new Date(item.chattingTime).getDate() }}일
          </div>
          <!-- 메세지 목록 -->
          <div
            class="mx-3 mt-1 d-flex flex-row"
            :class="item.memberId == memberId ? 'justify-end' : 'justify-start'"
          >
            <!-- 사용자 프로필 (시간이 같은 경우 처음에만 노출) -->
            <div v-if="item.memberId != memberId">
              <!-- 사진 -->
              <v-avatar
                class="mr-2"
                size="34"
                rounded="lg"
                v-if="
                  idx == 0 ||
                  item.memberId != chatList[idx - 1].memberId ||
                  item.chattingTime.split('T')[1].substr(0, 5) !=
                    chatList[idx - 1].chattingTime.split('T')[1].substr(0, 5)
                "
              >
                <v-img
                  :src="members[item.memberId].profile"
                  :alt="members[item.memberId].nickname"
                ></v-img>
              </v-avatar>
              <!-- 사진 없이 사진 크기와 동일한빈 공간 -->
              <v-sheet v-else class="mr-2" style="padding-left: 34px">
              </v-sheet>
            </div>

            <!-- 사용자 닉네임, 메세지 창, 등록시간 -->
            <v-sheet class="d-flex flex-column" width="100%">
              <!-- 사용자 닉네임 (시간이 같은 경우 처음에만 노출) -->
              <span
                class="xxxs-font"
                v-if="
                  item.memberId != memberId &&
                  (idx == 0 ||
                    item.memberId != chatList[idx - 1].memberId ||
                    item.chattingTime.split('T')[1].substr(0, 5) !=
                      chatList[idx - 1].chattingTime.split('T')[1].substr(0, 5))
                "
              >
                <span>{{ members[item.memberId].nickname }}</span>
              </span>

              <!-- 메세지 내용 및 시간 -->
              <v-sheet
                class="d-flex align-end"
                :class="
                  item.memberId == memberId ? 'flex-row-reverse' : 'flex-row'
                "
                width="100%"
              >
                <!-- 메세지 창 -->
                <v-sheet
                  class="pa-1 px-2 xs-font light-font d-flex flex-row"
                  max-width="70%"
                  :dark="item.memberId == memberId"
                  :outlined="item.memberId != memberId"
                  :color="item.memberId == memberId ? 'var(--main-col-1)' : ''"
                  elevation="0"
                  rounded="lg"
                  style="word-break: break-all"
                >
                  {{ item.message }}
                </v-sheet>
                <!-- 작성 날짜 -->
                <span
                  class="mx-1 xxxxs-font thin-font d-flex flex-row"
                  v-if="
                    idx == chatList.length - 1 ||
                    item.memberId != chatList[idx + 1].memberId ||
                    item.chattingTime.split('T')[1].substr(0, 5) !=
                      chatList[idx + 1].chattingTime.split('T')[1].substr(0, 5)
                  "
                >
                  <span
                    v-if="
                      new Date(item.chattingTime).getHours() == 0 ||
                      new Date(item.chattingTime).getHours() == 24
                    "
                  >
                    오전 12시
                    {{ new Date(item.chattingTime).getMinutes() }}분
                  </span>
                  <span
                    v-else-if="new Date(item.chattingTime).getHours() == 12"
                  >
                    오후
                    {{ new Date(item.chattingTime).getHours() }}시
                    {{ new Date(item.chattingTime).getMinutes() }}분
                  </span>
                  <span v-else-if="new Date(item.chattingTime).getHours() > 12">
                    오후
                    {{ new Date(item.chattingTime).getHours() - 12 }}시
                    {{ new Date(item.chattingTime).getMinutes() }}분
                  </span>
                  <span v-else>
                    오전
                    {{ new Date(item.chattingTime).getHours() }}시
                    {{ new Date(item.chattingTime).getMinutes() }}분
                  </span>
                </span>
              </v-sheet>
            </v-sheet>
          </div>
          <!-- footer 여백 -->
          <v-sheet height="72" v-if="idx == chatList.length - 1"></v-sheet>
        </div>
      </v-sheet>
      <!-- 메세지 입력창 -->
      <v-sheet
        class="px-3 pb-4"
        max-width="500"
        color="transparent"
        style="position: fixed; margin: 0 auto; left: 0; right: 0; bottom: 0"
      >
        <v-text-field
          style="background-color: white"
          v-model="message"
          @click:append-outer="sendMessage"
          @click:clear="message == ''"
          @keyup.enter="sendMessage"
          placeholder="메시지를 입력해주세요"
          outlined
          append-outer-icon="$vuetify.icons.send_outline"
          dense
          hide-details
          clearable
          maxlength="255"
        ></v-text-field>
      </v-sheet>
    </v-sheet>
  </v-sheet>
</template>

<script>
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import { getChatting, getChattingLog } from "@/api/modules/chatting.js";
import ChattingHeader from "@/views/Header/ChattingHeader.vue";
import InfiniteLoading from "vue-infinite-loading";
import ScrollBottomButton from "@/common/component/button/ScrollBottomButton.vue";
import InternetError from "@/common/component/dialog/InternetError.vue";
import ChattingLoading from "./ChattingLoading.vue";
import DetailButton from "@/common/component/button/DetailButton.vue";
import { mapActions, mapState } from "vuex";

export default {
  name: "ChattingPage",
  components: {
    ChattingHeader,
    InfiniteLoading,
    ScrollBottomButton,
    InternetError,
    ChattingLoading,
    DetailButton,
  },
  data() {
    return {
      memberId: null, // 현재 로그인 돼 있는 사용자 아이디
      message: "", // 작성한 메세지 내용
      chatList: [], // 채팅 리스트
      meetingName: null, // 미팅 제목
      members: {}, // 미팅에 참여하는 멤버
      last: -1, // 무한 스크롤 마지막에 불러온 Indexss
      page: 1, // 무한 스크롤 페이지
      loading: true, // 페이지 로딩 여부
      drawer: null, // 오른쪽 프로필 목록 창
      roomCode: null, // 모임 코드
      snackbar: false,
      newMessage: "",
      newMessageMemberId: null,
      bottom: false,
    };
  },
  watch: {
    bottom(bottom) {
      if (bottom) {
        this.$refs.scrollDownBtn.close();
        this.snackbar = false;
      } else this.$refs.scrollDownBtn.open();
    },
  },
  computed: {
    ...mapState("websocketStore", ["connected", "stompClient"]),
    member_list() {
      return Object.keys(this.members).map((item) => this.members[item]);
    },
  },
  async created() {
    this.loading = true;
    // 저장된 채팅 정보를 가져옵니다.
    await getChatting(this.$route.params.id).then(async (res) => {
      if (res && res.data.statusCode == 200) {
        const info = await res.data.data;
        // 룸 코드
        // this.roomCode = await info.roomCode;
        // 방 이름
        this.meetingName = await info.meetingName;
        // 멤버 정보
        this.members = await info.chattingMemberMap;
        // 로그인한 멤버 정보
        this.memberId = await info.memberId;
        // 요청 값을 받아오면 소켓 연결을 시도합니다.
        this.connect();
      } else {
        this.$refs.error.openDialog();
      }
    });
  },
  methods: {
    ...mapActions("websocketStore", ["updateStompClient", "updateConnected"]),
    bottomVisible() {
      const scrollY = window.scrollY;
      const visible = document.documentElement.clientHeight;
      const pageHeight = document.documentElement.scrollHeight;
      const bottomOfPage = visible + scrollY + 90 >= pageHeight;
      return bottomOfPage || pageHeight < visible;
    },
    onTheBottom() {
      this.bottom = this.bottomVisible();
    },
    // 오른쪽 멤버 프로필 목록 상태 변경
    watchNewMessage() {
      this.snackbar = false;
      this.goBottom();
    },
    openDrawer() {
      this.drawer = !this.drawer;
    },
    // 무한 스크롤 함수
    infiniteHandler($state) {
      // 마지막 Index가 0이나 음수면 값을 다 가져왔다고 판단
      if (this.last <= 0 && this.last != -1) $state.complete();
      // 그 외에 가져와야 할 값이 더 있는 경우
      else {
        // ChattingLog를 가져오는 API 요청
        getChattingLog(this.$route.params.id, this.last).then(async (res) => {
          // chat 정보 저장
          const chat = await res.data.data;
          // 무한 스크롤 페이지
          this.page += 1;
          // 채팅 기록 리스트에 넣기
          this.chatList.unshift(...chat.chattingDtoList.reverse());
          // 마지막 Index 업데이트
          this.last = await chat.lastNumber;
          // Rerendering
          $state.loaded();
        });
      }
    },
    // 맨 아래로 스크롤 이동
    goBottom() {
      window.scrollTo(0, document.querySelector("body").scrollHeight);
    },
    // 메세지 보내고, 입력 내용 초기화
    sendMessage() {
      if (this.memberId !== "" && this.message !== "") {
        this.send();
        this.message = "";
      }
    },
    // 메세지 전송
    send() {
      if (this.stompClient && this.stompClient.ws.readyState == 1) {
        this.stompClient.send(
          `/message/receive/${this.$route.params.id}`,
          JSON.stringify({ message: this.message, memberId: this.memberId }),
          {}
        );
      }
    },
    // 메세지 구독
    chatSubscribe() {
      // 일단 끊고
      this.stompClient.unsubscribe(
        `chatting-subscribe-${this.$route.params.id}`
      );
      this.stompClient.subscribe(
        `/send/${this.$route.params.id}`,
        async (res) => {
          const data = await JSON.parse(res.body);
          if (data.statusCode == 200) {
            // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
            await this.chatList.push(data.data);
            // 스크롤 맨 아래로 이동
            // 본인이 작성한 채팅 or 스크롤이 아래 있는 경우
            if (this.memberId == data.data.memberId || this.bottom) {
              await this.goBottom();
            } else {
              this.newMessage = await data.data.message;
              this.newMessageMemberId = await data.data.memberId;
              this.snackbar = await true;
            }
          }
        },
        { id: `chatting-subscribe-${this.$route.params.id}` }
      );
    },
    // 멤버 정보 가져오기 구독
    getMember() {
      this.stompClient.subscribe(
        `/enter/${this.$route.params.id}`,
        (res) => {
          const data = JSON.parse(res.body);
          if (data.statusCode == 200) {
            this.members[data.data.memberId] = data.data;
          }
        },
        { id: `member-subscribe-${this.$route.params.id}` }
      );
    },
    // 소켓 연결 후 동작
    async connectAction() {
      await this.chatSubscribe();
      await this.getMember();
      this.loading = await false;
      await document
        .querySelector(".v-snack__wrapper")
        .addEventListener("click", this.watchNewMessage);
      await window.addEventListener("scroll", this.onTheBottom);
      // await this.goBottom();
    },
    // 소켓 연결 기다리기
    waitConnect() {
      setTimeout(() => {
        if (this.stompClient.ws.readyState == 1) {
          this.connectAction();
        } else {
          this.waitConnect();
        }
      }, 1);
    },
    // Websocket 연결
    connect() {
      // 연결 시도 중거나 이미 연결됐거나
      if (
        this.connected ||
        (this.stompClient && this.stompClient.ws.readyState == 1)
      ) {
        this.waitConnect();
      }
      // 연결 시도
      else {
        this.updateConnected(true);
        const serverURL = `${process.env.VUE_APP_API_BASE_URL}/websocket`;
        let socket = new SockJS(serverURL);
        this.updateStompClient(Stomp.over(socket));

        // console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`);
        this.stompClient.connect(
          {},
          async (frame) => {
            frame;
            // console.log("소켓 연결 성공", frame);
            this.updateConnected(false);
            this.connectAction();
          },
          (error) => {
            // console.log("소켓 연결 실패", error);
            error;
            // this.$refs.error.openDialog();
            this.updateConnected(false);
            this.connect();
          }
        );
      }
    },
  },
  // 메시지 구독 끊기
  beforeDestroy() {
    this.stompClient.unsubscribe(`chatting-subscribe-${this.$route.params.id}`);
    this.stompClient.unsubscribe(`member-subscribe-${this.$route.params.id}`);

    if (this.loading) {
      document
        .querySelector(".v-snack__wrapper")
        .removeEventListener("click", this.watchNewMessage);
      window.removeEventListener("scroll", this.onTheBottom);
    }
  },
};
</script>

<style scoped></style>
