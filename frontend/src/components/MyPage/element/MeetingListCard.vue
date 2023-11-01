<template>
  <v-sheet class="d-flex align-center">
    <v-timeline id="my-page-timeline" align-top dense>
      <v-timeline-item
        fill-dot
        small
        v-for="(meeting, index) in onDayMeetings"
        :key="meeting.id"
        :color="colorSet[index % colorSet.length].color"
      >
        <v-card
          :style="{
            background:
              colorSet[index % colorSet.length].greColor + ' !important',
          }"
          class="white-font timeline-shadow"
        >
          <v-card-title
            class="py-1 md-font d-flex flex-row justify-space-between"
          >
            <span>{{ meeting.meetingName }}</span>
            <v-btn color="white" icon @click="goDetail(meeting.id)">
              <v-icon>mdi-dots-vertical</v-icon>
            </v-btn>
          </v-card-title>
          <v-card-text class="pa-3 light-font white xxs-font">
            <!-- 모임 장소-->
            <div class="mb-1 d-flex align-center">
              <img height="20" src="@/assets/images/page/pointer.png" />
              <div class="ml-2">{{ meeting.meetingPlace }}</div>
            </div>
            <!-- 모임 시간-->
            <div class="d-flex align-center">
              <img height="20" src="@/assets/images/icons/clock.png" />
              <div class="ml-2">
                {{ formattedTime(meeting.meetingTime) }}
              </div>
            </div>
          </v-card-text>
        </v-card>
      </v-timeline-item>
      <!-- 무한스크롤 감지 -->
      <infinite-loading @infinite="infiniteHandler" spinner="spiral">
        <div slot="no-more"></div>
        <div slot="no-results"></div>
      </infinite-loading>
    </v-timeline>
  </v-sheet>
</template>

<script>
import InfiniteLoading from "vue-infinite-loading";

export default {
  name: "MeetingListCard",
  props: {
    // 선택한 날짜
    date: {
      type: String,
      required: true,
    },
    // 선택한 날짜의 all 모임 list
    meetings: {
      type: Array,
      required: true,
    },
  },
  components: {
    InfiniteLoading,
  },
  data() {
    return {
      onDayMeetings: [], // 무한스크롤을 통해 보여줄 선택한 날짜의 모임 list (2개씩)
      infiniteIndex: 0,
      colorSet: [
        {
          color: "var(--red-col)",
          bgColor: "red lighten-1",
          greColor:
            "linear-gradient(180deg, rgba(242, 78, 85, 0.8) 0%, #F24E55 100%)",
        },
        {
          color: "var(--yellow-col)",
          bgColor: "yellow lighten-1",
          greColor:
            "linear-gradient(180deg, rgba(244, 164, 26, 0.8) 0%, #F4A41A 100%)",
        },
        {
          color: "var(--green-col)",
          bgColor: "green lighten-1",
          greColor:
            "linear-gradient(180deg, rgba(0, 163, 129, 0.8) 0%, #00A381 100%)",
        },
        {
          color: "var(--blue-col)",
          bgColor: "blue lighten-1",
          greColor:
            "linear-gradient(180deg, rgba(0, 95, 186, 0) 0%, rgba(0, 95, 186, 0.8) 0.01%, #005FBA 100%)",
        },
      ],
    };
  },
  watch: {
    date() {
      this.resetOnDayMeetings();
    },
    meetings() {
      this.resetOnDayMeetings();
    },
  },
  created() {
    this.resetOnDayMeetings();
  },
  methods: {
    goDetail(id) {
      this.$router.push(`/meeting/${id}`);
    },
    // [@Method] DateTime을 원하는 format으로 변경
    formattedTime(dateTime) {
      const date = new Date(dateTime);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");
      const hour = String(date.getHours() % 12 || 12).padStart(2, "0");
      const minute = String(date.getMinutes()).padStart(2, "0");
      const meridiem = date.getHours() >= 12 ? "오후" : "오전";

      return `${year}년 ${month}월 ${day}일 ${meridiem} ${hour}시 ${minute}분`;
    },
    // [@Method] 무한 스크롤 초기화 & 모임 List에 2개 push
    resetOnDayMeetings() {
      this.infiniteIndex = 0;

      const onDayMeeting = [];
      // 2개 넣기
      if (this.meetings.length > 1) {
        onDayMeeting.push(this.meetings[0]);
        onDayMeeting.push(this.meetings[1]);
        this.infiniteIndex = 1;
      }
      // 2개가 없는 경우 1개 넣기
      else if (this.meetings.length == 1) {
        onDayMeeting.push(this.meetings[0]);
        this.infiniteIndex = 0;
      }
      this.onDayMeetings = onDayMeeting;
    },
    // [@Method] 무한스크롤
    infiniteHandler($state) {
      // console.log(
      //   "#21# 무한 스크롤 감지 - onDayMeetings: ",
      //   this.onDayMeetings
      // );

      // meetings 마지막 index라면 다 가져왔다고 판단
      if (this.infiniteIndex == this.meetings.length - 1) {
        $state.complete();
      } else {
        // onDayMeetings 배열에 2개씩 추가
        for (var i = 0; i < 2; i++) {
          if (this.meetings[this.infiniteIndex + 1]) {
            this.infiniteIndex += 1;
            this.onDayMeetings.push(this.meetings[this.infiniteIndex]);
          }
        }
        $state.loaded();
      }
    },
  },
};
</script>

<style>
#my-page-timeline.v-timeline {
  padding-top: 0px !important;
  width: 100% !important;
}
#my-page-timeline .v-timeline-item__dot--small {
  height: 20px !important;
  width: 20px !important;
}
#my-page-timeline .v-card {
  max-width: 500px !important;
  width: 100% !important;
}
#my-page-timeline .v-card:before,
.v-card:after {
  visibility: hidden !important;
}
#my-page-timeline .v-timeline-item {
  width: 100% !important;
  justify-content: center;
}
#my-page-timeline .v-timeline-item__body {
  width: 100% !important;
}
#my-page-timeline .v-timeline-item__divider {
  position: relative;
  min-width: 30px !important;
  display: flex;
  align-items: center;
  justify-content: flex-start !important;
}
#my-page-timeline.v-timeline:before {
  left: calc(43px - 1px) !important;
}
</style>
