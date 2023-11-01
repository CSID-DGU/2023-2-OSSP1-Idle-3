<template>
  <v-sheet
    class="pa-3"
    style="background-image: linear-gradient(to bottom, #092a49 75%, white 25%)"
  >
    <v-sheet
      class="pa-6 d-flex flex-column justify-space-between detail-shadow detail-border"
      height="190"
      outlined
      rounded="xl"
    >
      <div class="d-flex flex-row align-self-end">
        <!-- 수정 버튼 -->
        <meeting-modify-btn
          v-if="member_id === hostId"
          :meetingName="meetingName"
          :meetingDate="meetingDate"
          :meetingTime="meetingTime"
          :meetingPlace="meetingPlace"
          :meetingAddress="meetingAddress"
          :meetingLat="meetingLat"
          :meetingLng="meetingLng"
          :lateAmount="lateAmount"
        ></meeting-modify-btn>
        <!-- 초대 버튼 -->
        <meeting-invite-btn
          v-if="member_id === hostId"
          :meetingName="meetingName"
          :meetingDate="meetingDate"
          :meetingTime="meetingTime"
          :meetingPlace="meetingPlace"
          :roomCode="roomCode"
        ></meeting-invite-btn>
        <!-- 모임 삭제 버튼 -->
        <meeting-delete-btn
          v-if="member_id === hostId"
          :meetingId="meetingId"
        ></meeting-delete-btn>
        <!-- 모임 나가기 버튼 -->
        <meeting-out-btn v-if="member_id !== hostId"></meeting-out-btn>
      </div>
      <div class="d-flex flex-row justify-space-between align-end">
        <div class="d-flex flex-column">
          <img height="35" width="35" src="@/assets/images/logo/logo.png" />
          <span class="point-font main-col-1 xxxxxl-font">{{
            meetingName
          }}</span>
        </div>
        <div class="d-flex flex-row align-end justify-center">
          <move-chatting-btn></move-chatting-btn>
          <move-live-map-btn :meetingTime="meetingTime"></move-live-map-btn>
        </div>
      </div>
    </v-sheet>
  </v-sheet>
</template>

<script>
import MeetingModifyBtn from "../button/MeetingModifyBtn.vue";
import MeetingInviteBtn from "../button/MeetingInviteBtn.vue";
import MeetingDeleteBtn from "../button/MeetingDeleteBtn.vue";
import MoveChattingBtn from "../button/MoveChattingBtn.vue";
import MoveLiveMapBtn from "../button/MoveLiveMapBtn.vue";
import MeetingOutBtn from "../button/MeetingOutBtn.vue";
import { mapState } from "vuex";
export default {
  components: {
    MeetingModifyBtn,
    MeetingInviteBtn,
    MeetingDeleteBtn,
    MoveChattingBtn,
    MoveLiveMapBtn,
    MeetingOutBtn,
  },
  name: "MeetingPage",
  props: {
    meetingName: String,
    meetingDate: String,
    meetingTime: String,
    meetingPlace: String,
    meetingAddress: String,
    meetingLat: Number,
    meetingLng: Number,
    lateAmount: Number,
    roomCode: String,
    hostId: Number,
    meetingId: Number,
  },
  computed: {
    ...mapState("memberStore", ["member", "member_id"]),
  },
};
</script>
<style></style>
