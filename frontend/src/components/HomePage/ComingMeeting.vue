<template>
  <v-sheet color="transparent">
    <span class="pl-4 point-font xxxxl-font main-col-1">다가올 모임</span>
    <v-btn class="ml-1" icon x-small @click="show = !show">
      <v-icon>$vuetify.icons.info_outline</v-icon>
    </v-btn>
    <v-tooltip location="right" v-model="show">
      <span class="main-col-1 xxxs-font">
        지금으로부터 한 달 내의<br />
        나의 모임을 보여줍니다.
      </span>
    </v-tooltip>
    <v-sheet v-if="loading" height="185" class="mx-4 mb-6 mt-2">
      <v-skeleton-loader
        type="image"
        class="mb-1"
        width="100%"
        height="185"
      ></v-skeleton-loader>
    </v-sheet>
    <div v-else>
      <swiper
        v-if="meetings && meetings.length > 0"
        class="swiper mt-2 px-3"
        :options="swiperOption"
      >
        <swiper-slide
          class="px-1"
          v-for="(meeting, idx) in meetings"
          :key="idx"
        >
          <v-card
            @click="goDetail(meeting.id)"
            elevation="0"
            class="mb-6 px-5 d-flex flex-column align-center justify-center"
            :class="{
              'upcoming-ellipse': idx % 4 === 0,
              'upcoming-polygon': idx % 4 === 1,
              'upcoming-rectangle': idx % 4 === 2,
              'upcoming-star': idx % 4 === 3,
            }"
          >
            <v-sheet color="transparent" width="150">
              <v-sheet
                class="d-flex flex-row justify-space-between align-center"
                color="transparent"
              >
                <v-sheet color="transparent">
                  <span
                    class="extralight-font md-font white-font d-flex flex-row justify-space-between"
                  >
                    {{ meeting.meetingTime | remainDay }}
                  </span>
                  <v-divider width="40" style="border-color: white"></v-divider>
                </v-sheet>
                <v-sheet
                  class="d-flex flex-row justify-end"
                  color="transparent"
                >
                  <div
                    :class="{
                      'mt-3 ellipse rounded-circle': idx % 4 === 0,
                      'triangle-first': idx % 4 === 1,
                      'rectangle-first': idx % 4 === 2,
                      'star-first': idx % 4 === 3,
                    }"
                  ></div>
                  <div
                    :class="{
                      'mt-6 mr-3 ellipse rounded-circle': idx % 4 === 0,
                      'triangle-second': idx % 4 === 1,
                      'rectangle-second': idx % 4 === 2,
                      'mr-3 star-second': idx % 4 === 3,
                    }"
                  ></div>
                </v-sheet>
              </v-sheet>
              <v-sheet color="transparent">
                <div class="py-3 bold-font md-font white-font">
                  {{ meeting.meetingName }}
                </div>
                <v-sheet
                  class="d-flex flex-column align-end"
                  color="transparent"
                >
                  <span class="xxxxs-font extralight-font white-font">
                    {{ meeting.meetingPlace }}
                  </span>
                  <span class="xxxxs-font extralight-font white-font">
                    {{ meeting.meetingTime | formatDate }}
                  </span>
                  <span class="xxxxs-font extralight-font white-font">
                    {{ meeting.meetingTime | formatTime }}
                  </span>
                </v-sheet>
              </v-sheet>
            </v-sheet>
          </v-card>
        </swiper-slide>
      </swiper>
      <div
        v-else
        class="mb-6 none pa-3 mx-4 mt-2 d-flex justify-center align-center"
      >
        <v-sheet
          width="100%"
          height="100%"
          class="empty white-col-1 pa-4 d-flex justify-end align-center"
          color="transparent"
          max-width="330"
        >
          <v-sheet
            class="d-flex flex-row justify-end align-center"
            color="transparent"
            height="100%"
          >
            <div class="d-flex flex-column align-start">
              <span class="semibold-font xl-font white-font">
                다가올 모임이 없습니다.
              </span>
              <div class="d-flex flex-column regular-font xxs-font white-font">
                <span>지금 바로 스터디, 식사 약속 등</span>

                <span>다양한 모임을 잡아보세요!</span>
              </div>
              <v-btn class="mt-1" small rounded @click="goRegister()">
                <span class="px-5 regular-font xxxs-font main-col-1">
                  모임 방 만들기
                </span>
              </v-btn>
            </div>
          </v-sheet>
        </v-sheet>
      </div>
    </div>
  </v-sheet>
</template>

<script>
import { getUpcomingMeetings } from "@/api/modules/meeting.js";
import moment from "moment";
export default {
  name: "ComingMeeting",
  data() {
    return {
      meetings: [],
      swiperOption: {
        slidesPerView: "auto",
        spaceBetween: 0,
      },
      show: false,
      loading: true,
    };
  },
  created() {
    this.loading = true;
    getUpcomingMeetings().then((res) => {
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
    goDetail(id) {
      this.$router.push(`/meeting/${id}`);
    },
  },
  filters: {
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
    remainDay(value) {
      const now = moment();
      const time = moment(value, "YYYY-MM-DD hh:mm:ss");
      const hour = moment.duration(time.diff(now)).days();
      return `D-${hour}`;
    },
  },
};
</script>

<style scoped>
.empty {
  background-image: url("@/assets/images/component/cards.png");
  background-size: 120px;
  background-position-x: left;
  background-position-y: center;
}
.swiper-slide {
  width: 185px;
}
.upcoming-ellipse {
  width: 145px;
  height: 150px;

  background: linear-gradient(
    180deg,
    rgba(243, 122, 127, 0.9) 0%,
    var(--red-col) 100%
  );
  box-shadow: 0px 5px 20px -10px #f37a7f !important;
  border-radius: 15px !important;
}
.upcoming-polygon {
  width: 145px;
  height: 150px;

  background: linear-gradient(
    180deg,
    rgba(245, 186, 85, 0.9) 0%,
    var(--yellow-col) 100%
  );
  box-shadow: 0px 5px 20px -10px var(--yellow-col) !important;
  border-radius: 15px !important;
}
.upcoming-rectangle {
  width: 145px;
  height: 150px;

  background: linear-gradient(
    180deg,
    rgba(43, 192, 161, 0.9) 0%,
    var(--green-col) 100%
  );
  box-shadow: 0px 5px 20px -10px #2bc0a1 !important;
  border-radius: 15px !important;
}
.upcoming-star {
  width: 145px;
  height: 150px;

  background: linear-gradient(
    180deg,
    rgba(28, 128, 223, 0.9) 0%,
    var(--blue-col) 100%
  );
  box-shadow: 0px 5px 20px -10px var(--blue-col) !important;
  border-radius: 15px !important;
}
.none {
  height: 150px;
  /* width: 353px; */
  /* height: 150px; */
  background: linear-gradient(
    180deg,
    rgba(9, 42, 73, 0.8) 0%,
    var(--main-col-1) 100%
  );
  box-shadow: 0px 5px 20px -10px #000000;
  border-radius: 15px;
}
.ellipse {
  position: absolute;
  top: 0;
  width: 35px;
  height: 35px;
  background: rgba(255, 255, 255, 0.3);
}
.rectangle-first {
  position: absolute;
  top: 0;
  margin: 15px 0px 0px 0px;
  width: 31px;
  height: 31px;
  background: rgba(255, 255, 255, 0.3);
}
.rectangle-second {
  position: absolute;
  top: 0;
  margin: 25px 10px 0px 0px;
  width: 31px;
  height: 31px;
  background: rgba(217, 255, 247, 0.3);
}
.triangle-second {
  position: absolute;
  width: 0;
  height: 0;
  top: 0;
  margin: 25px 15px 0px 0px;
  border-style: solid;
  border-width: 0 17.5px 35px 17.5px;
  border-color: transparent transparent rgba(255, 255, 255, 0.3) transparent;
  transform: rotate(-14.43deg);
}
.triangle-first {
  position: absolute;
  width: 0;
  height: 0;
  top: 0;
  margin: 15px 0px 0px 0px;
  border-style: solid;
  border-width: 0 17.5px 35px 17.5px;
  border-color: transparent transparent rgba(250, 235, 211, 0.3) transparent;
  transform: rotate(9.24deg);
}
.star-second {
  position: absolute;
  width: 35px;
  height: 35px;
  top: 0;
  margin: 20px 0px 0px 0px;
  clip-path: polygon(
    50% 5%,
    61% 40%,
    98% 40%,
    68% 62%,
    79% 96%,
    50% 75%,
    21% 96%,
    32% 62%,
    2% 40%,
    39% 40%
  );
  background: rgba(255, 255, 255, 0.3);
  transform: rotate(-8.97deg);
}
.star-first {
  position: absolute;
  width: 35px;
  height: 35px;
  top: 0;
  margin: 10px 0px 0px 0px;
  clip-path: polygon(
    50% 5%,
    61% 40%,
    98% 40%,
    68% 62%,
    79% 96%,
    50% 75%,
    21% 96%,
    32% 62%,
    2% 40%,
    39% 40%
  );
  background: rgba(168, 213, 255, 0.3);
  transform: rotate(3.72deg);
}
/* .line {
  width: 30%;
  height: 0.5px;
  background: #fff;
} */

.v-tooltip__content {
  position: absolute;
  width: 160px;
  height: 70px;
  left: 86px !important;
  top: 590px !important;
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
