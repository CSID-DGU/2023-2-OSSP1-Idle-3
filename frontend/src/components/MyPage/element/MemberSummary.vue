<template>
  <v-sheet
    class="mx-5 mt-4 pa-5 d-flex flex-column detail-shadow light-font xs-font"
    rounded="lg"
  >
    <!-- text -->
    <!-- 이번달 모임개수 -->
    <div class="main-col-1 d-flex flex-row align-center">
      <v-icon small>$vuetify.icons.check_round</v-icon>
      <div class="ml-2">
        <span>이번달에 </span>
        <span class="bold-font">{{ this.thisMonthAttendMeetingCnt }}번</span>
        <span>의 모임을 잡으셨네요.</span>
      </div>
    </div>
    <!-- 누적 지각횟수 -->
    <div class="main-col-1 d-flex flex-row align-center">
      <v-icon small>$vuetify.icons.check_round</v-icon>
      <span class="ml-2">
        <span>누적 </span>
        <span class="bold-font">{{ this.attendMeetings.length }}번</span>
        <span>의 약속 중 </span>
        <span class="bold-font">{{ this.totalLateCnt }}번</span>
        <span> 지각하셨습니다.</span>
      </span>
    </div>
    <!-- 지난달 총 소비금액 -->
    <div class="main-col-1 d-flex flex-row align-center">
      <v-icon small>$vuetify.icons.check_round</v-icon>
      <span class="ml-2">
        <span>지난달 모임에 </span>
        <span class="bold-font"
          >{{ this.lastMonthTotalSpentMoney.toLocaleString("ko-KR") }}원</span
        >
        <span>을 소비했습니다.</span>
      </span>
    </div>
  </v-sheet>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "MemberSummary",
  data() {
    return {};
  },
  computed: {
    ...mapState("mypageStore", [
      "attendMeetings",
      "thisMonthAttendMeetingCnt",
      "totalLateCnt",
      "lastMonthTotalSpentMoney",
    ]),
  },
  methods: {},
};
</script>

<style scoped>
.image-container {
  position: absolute;
  margin-left: 45%;
  margin-top: -33%;
}
</style>
