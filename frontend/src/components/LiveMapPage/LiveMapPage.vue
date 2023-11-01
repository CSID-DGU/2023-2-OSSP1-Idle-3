<template>
  <div class="live-map">
    <live-map-header :meetingName="meetingName"></live-map-header>
    <v-sheet
      v-if="loading"
      height="100%"
      color="transparnet"
      class="d-flex flex-column align-center justify-center"
    >
      <v-progress-circular
        :size="60"
        :width="6"
        color="var(--main-col-1)"
        indeterminate
      ></v-progress-circular>
      <span class="mt-2 xxxxxl-font logo-font main-col-1">Loading</span>
    </v-sheet>
    <!-- kakao map -->
    <div v-else class="live-map">
      <change-background-switch @map-type-change="handleMapTypeChange" />
      <!-- <div class="live-map" v-if="enterTimeCheckFlag"> -->
      <arrive-and-chat-btn
        @resizeMapLevel="resizeMapLevel"
      ></arrive-and-chat-btn>
      <live-map
        ref="livemap"
        :key="backgroundType"
        :background_type="backgroundType"
        :member_id="memberId"
        :meeting_lat="meetingLat"
        :meeting_lng="meetingLng"
        :chatting_map="chattingMap"
      ></live-map>
    </div>
    <!-- 시간 진입 불가 -->
    <no-enter-time-dialog ref="timeout"></no-enter-time-dialog>
    <!-- 그외 진입 불가 -->
    <internet-error ref="error"></internet-error>
  </div>
</template>

<script>
import LiveMap from "./element/LiveMap.vue";
import ArriveAndChatBtn from "./element/ArriveAndChatBtn.vue";
import ChangeBackgroundSwitch from "./element/ChangeBackgroundSwitch.vue";
import NoEnterTimeDialog from "./element/NoEnterTimeDialog.vue";
import InternetError from "@/common/component/dialog/InternetError.vue";
import { liveMapInfo } from "@/api/modules/livemap.js";
import LiveMapHeader from "@/views/Header/LiveMapHeader.vue";

export default {
  name: "LiveMapPage",
  components: {
    LiveMap,
    ArriveAndChatBtn,
    ChangeBackgroundSwitch,
    NoEnterTimeDialog,
    InternetError,
    LiveMapHeader,
  },
  data() {
    return {
      meetingTime: null,
      meetingName: null,
      memberId: null,
      chattingMap: null,
      meetingLat: null,
      meetingLng: null,
      backgroundType: false, // kakao-map 배경화면 type (flase = 타일, true = 지도)
      loading: true,
    };
  },
  async created() {
    this.loading = true;

    await liveMapInfo(this.$route.params.id).then(async (res) => {
      if (res) {
        // console.log(">>> ", res);
        this.memberId = await res.memberId;
        this.meetingName = await res.meetingName;
        this.meetingTime = await res.meetingTime;
        // console.log("1/ ", res);
        this.meetingLat = await res.meetingLat;
        this.meetingLng = await res.meetingLng;
        this.chattingMap = await res.chattingDtoMap;
        await this.checkMeetingTime();
      } else {
        this.$refs.error.openDialog();
      }
    });
  },
  methods: {
    resizeMapLevel() {
      this.$refs.livemap.resizeMapLevel();
    },
    // [@Method] 모임시간 3시간 전/후 check
    checkMeetingTime() {
      // console.log(this.meetingTime);
      const meetingTime = new Date(this.meetingTime);

      const threeHoursAgoTime = new Date(
        meetingTime.getTime() - 3 * 60 * 60 * 1000
      );
      const threeHoursAfterTime = new Date(
        meetingTime.getTime() + 3 * 60 * 60 * 1000
      );
      const currentTime = new Date();
      // console.log("#21# 3시간 전: ", threeHoursAgoTime);
      // console.log("#21# 3시간 후: ", threeHoursAfterTime);
      // i) 현재 시각이 모임 시간의 3시간 전/후 이내
      if (
        threeHoursAgoTime.getTime() <= currentTime.getTime() &&
        currentTime.getTime() <= threeHoursAfterTime.getTime()
      ) {
        this.loading = false;
      }
      // ii) 그 외 시간
      else {
        this.$refs.timeout.openDialog();
      }
    },
    // [@Method] 뒤로 돌아가기
    closeDialog() {
      this.$router.go(-1);
    },
    // [@Method] 변경된 mapType값 자식 컴포넌트로부터 받기
    handleMapTypeChange(mapType) {
      this.backgroundType = mapType;
    },
  },
};
</script>

<style scoped>
.live-map {
  width: 100%;
  height: 100%;
}
.live-map > div:not(.v-sheet) {
  height: 100%;
}

span.extralight-font.sm-font {
  line-height: 1.5;
}
</style>
