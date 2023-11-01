<template>
  <v-dialog v-model="dialog" scrollable max-width="300px" rounded="xl">
    <template v-slot:activator="{ on, attrs }">
      <v-btn
        class="mr-1"
        id="square-big-btn"
        outlined
        color="var(--main-col-1)"
        rounded
        v-bind="attrs"
        v-on="on"
      >
        <v-icon>$vuetify.icons.user_invite_outline</v-icon>
      </v-btn>
    </template>
    <v-card rounded="xl" class="pb-3">
      <v-card-title class="d-flex flex-column">
        <div class="align-self-end">
          <close-button @closeDialog="closeDialog"></close-button>
        </div>
        <span class="point-font xl-font main-col-1 semi2narrow-font">
          {{ meetingName }}
        </span>
        <span class="point-font xxxxxxl-font main-col-1">초대 링크</span>
        <img src="@/assets/images/dialog/invite_link.png" width="60%" />
        <v-sheet
          class="pr-1 px-4 detail-border d-flex flex-row justify-space-between align-center"
          rounded="lg"
          width="100%"
        >
          <span class="xs-font light-font">{{
            link.length > 22 ? link.substr(0, 22) + "···" : link
          }}</span>
          <div>
            <v-btn icon @click="copyLink"
              ><v-icon>$vuetify.icons.copy_outline</v-icon></v-btn
            >
            <v-btn icon @click="sendkakao">
              <v-icon>$vuetify.icons.share_outline</v-icon>
              <!-- <v-icon>mdi-share-variant-outline</v-icon> -->
            </v-btn>
          </div>
        </v-sheet>
        <span class="extralight-font xxs-font main-col-1">
          초대 링크를 공유해 멤버를 추가해 보세요.
        </span>
      </v-card-title>
    </v-card>
    <v-snackbar
      v-model="snackbar"
      :timeout="timeout"
      text
      elevation="10"
      color="var(--main-col-1)"
      outlined
      id="link-copy"
    >
      <span class="bold-font">{{ text }}</span>
      <template v-slot:action="{ attrs }">
        <v-btn
          color="var(--main-col-1)"
          text
          v-bind="attrs"
          @click="snackbar = false"
        >
          닫기
        </v-btn>
      </template>
    </v-snackbar>
  </v-dialog>
</template>
<script
  src="https://t1.kakaocdn.net/kakao_js_sdk/2.1.0/kakao.min.js"
  integrity="sha384-dpu02ieKC6NUeKFoGMOKz6102CLEWi9+5RQjWSV0ikYSFFd8M3Wp2reIcquJOemx"
  crossorigin="anonymous"
></script>
<script>
import CloseButton from "@/common/component/button/CloseButton.vue";

export default {
  name: "MeetingInviteBtn",
  components: { CloseButton },
  data() {
    return {
      dialog: false,
      link: "https://k8a401.p.ssafy.io/invite",
      image: require("@/assets/images/banner/home.png"),
      multiLine: true,
      snackbar: false,
      text: `초대링크가 복사되었습니다.`,
      timeout: 2000,
    };
  },
  props: {
    meetingName: String,
    meetingDate: String,
    meetingTime: String,
    meetingPlace: String,
    roomCode: String,
  },
  methods: {
    copyLink: async function () {
      const roomCode = this.roomCode;
      const textToCopy = `https://almostthere.co.kr/entrance/${roomCode}`;
      try {
        await navigator.clipboard.writeText(textToCopy);
        // console.log("Text copied to clipboard");
        this.snackbar = true;
      } catch (err) {
        // console.error("Failed to copy text: ", err);
      }
    },

    sendkakao: function () {
      const filterMeetingDate = this.formatDate(this.meetingTime);
      const filterMeetingTime = this.formatTime(this.meetingTime);
      const roomCode = this.roomCode;

      if (!Kakao.isInitialized()) {
        Kakao.init(`${process.env.VUE_APP_KAKAO_API_KEY}`);
      }
      window.Kakao.Share.sendDefault({
        objectType: "feed",
        content: {
          title: "모임 이름: " + this.meetingName,
          description:
            "호스트가 모임에 초대합니다.\n" +
            "아래 버튼을 통해 초대를 수락하세요! ",
          imageUrl: "https://almostthere.co.kr/almostthere/images/home.png",
          link: {
            // mobileWebUrl: "http://localhost:8080",
            // webUrl: "http://localhost:8080",
          },
        },
        itemContent: {
          items: [
            {
              item: "장소:",
              itemOp: this.meetingPlace,
            },
            {
              item: "날짜:",
              itemOp: filterMeetingDate,
            },
            {
              item: "시간:",
              itemOp: filterMeetingTime,
            },
          ],
        },
        buttons: [
          {
            title: "초대 모임 참여하기",
            link: {
              // 룸코드 props? store로 받아와서 url에 추가하기
              mobileWebUrl: `https://almostthere.co.kr/entrance/${roomCode}`,
              webUrl: `https://almostthere.co.kr/entrance/${roomCode}`,
              // mobileWebUrl: `http://localhost:3000/entrance/${roomCode}`,
              // webUrl: `http://localhost:3000/entrance/${roomCode}`,
            },
          },
        ],
      });
    },
    openDialog() {
      this.dialog = true;
    },
    closeDialog() {
      this.dialog = false;
    },

    formatDate(value) {
      const date = new Date(value);
      const year = date.getFullYear();
      const month = date.getMonth();
      const day = date.getDate();

      const result = `${year}년 ${
        month + 1 < 10 ? `0${month + 1}` : month + 1
      }월 ${day < 10 ? `0${day}` : day}일`;
      return result;
    },
    formatTime(value) {
      const date = new Date(value);
      const hour = date.getHours();
      const min = date.getMinutes();
      const result = `${
        hour >= 12
          ? `오후 ${hour == 12 ? `${hour}` : hour - 12}`
          : `오전 ${hour}`
      }시 ${min < 10 ? `0${min}` : min}분`;
      return result;
    },
  },
};
</script>

<style>
#link-copy .v-snack__wrapper {
  margin-bottom: 40px !important;
}
</style>
