<template>
  <v-sheet class="py-1" color="transparent">
    <span class="ml-4 point-font xxxxl-font main-col-1">오늘의 모임</span>
    <v-btn class="ml-1" icon x-small @click="show = !show">
      <v-icon>$vuetify.icons.info_outline</v-icon>
    </v-btn>
    <v-tooltip location="right" v-model="show">
      <span class="main-col-1 xxxs-font">
        지금으로부터 24시간 내의<br />
        나의 모임을 보여줍니다.
      </span>
    </v-tooltip>
    <v-sheet v-if="loading" height="175" class="mx-4 mb-6 mt-2">
      <v-skeleton-loader
        type="image"
        class="mb-1"
        width="100%"
        height="175"
      ></v-skeleton-loader>
    </v-sheet>
    <div v-else>
      <swiper
        v-if="meetings.length > 0"
        class="swiper mt-2 px-2"
        :options="swiperOption"
      >
        <swiper-slide
          class="px-2"
          v-for="(meeting, idx) in meetings"
          :key="idx"
        >
          <meeting-card :meeting="meeting"></meeting-card>
        </swiper-slide>
      </swiper>
      <v-card
        v-else
        height="175px"
        class="mb-6 mx-4 mt-2 d-flex flex-row justify-center align-center"
        elevation="0"
        style="
          border: 1px solid var(--main-col-1);
          border-radius: 15px;
          box-shadow: 0px 5px 20px -10px var(--main-col-1) !important;
        "
      >
        <v-sheet
          class="mx-7 empty d-flex flex-row justify-space-between align-center"
          width="100%"
          height="100%"
          max-width="330"
        >
          <v-sheet
            color="transparent"
            style="
              text-shadow: -1px 0 white, 0 1px white, 1px 0 white, 0 -1px white;
            "
          >
            <span class="my-2 semibold-font main-col-1 xl-font">
              오늘 모임이 없습니다.
            </span>
            <div
              class="d-flex flex-column my-1 regular-font xxs-font main-col-1"
            >
              <span>남는 시간이 있다면</span>
              <span>친구들과 번개 약속을</span>
              <span>잡아보는 건 어떠신가요?</span>
            </div>
            <v-btn
              class="mt-1"
              small
              color="var(--main-col-1)"
              dark
              rounded
              @click="goRegister()"
            >
              <span class="px-5 regular-font xxxs-font"> 모임 방 만들기 </span>
            </v-btn>
          </v-sheet>
        </v-sheet>
      </v-card>
    </div>
  </v-sheet>
</template>

<script>
import { getTodayMeetings } from "@/api/modules/meeting.js";
import MeetingCard from "./element/MeetingCard.vue";

export default {
  name: "TodayMeeting",
  components: {
    MeetingCard,
  },
  data() {
    return {
      swiperOption: {
        slidesPerView: "auto",
        spaceBetween: 0,
      },
      meetings: [],
      show: false,
      loading: true,
    };
  },
  async created() {
    this.loading = true;
    getTodayMeetings().then((res) => {
      if (res) {
        this.meetings = res;
        this.loading = false;
      }
    });
  },
  methods: {
    goRegister() {
      this.$router.push("/register");
    },
  },
};
</script>
<style scoped>
.empty {
  background-image: url("@/assets/images/component/calendar.png");
  background-size: 175px;
  background-position-x: right;
  background-position-y: center;
}
.swiper-slide {
  width: 300px;
}
.v-tooltip__content {
  position: absolute;
  width: 160px;
  height: 70px;
  left: 84px !important;
  top: 350px !important;
  padding: 12px;
  background: white;
  -webkit-border-radius: 10px;
  -moz-border-radius: 10px;
  border-radius: 10px;
  border: var(--main-col-1) solid 1px;
  display: flex;
  justify-content: center;
  align-content: center;
}

.v-tooltip__content:after {
  content: "";
  position: absolute;
  border-style: solid;
  border-width: 0 8px 9px;
  border-color: white transparent;
  display: block;
  width: 0;
  z-index: 1;
  top: -9px;
  left: 35px;
}

.v-tooltip__content:before {
  content: "";
  position: absolute;
  border-style: solid;
  border-width: 0 8px 9px;
  border-color: var(--main-col-1) transparent;
  display: block;
  width: 0;
  z-index: 0;
  top: -10px;
  left: 35px;
}
</style>
