<template>
  <div>
    <internet-error ref="error"></internet-error>
    <meeting-loading v-if="loading"></meeting-loading>
    <div v-else>
      <meeting-title
        v-if="meeting.meetingName !== null"
        :meetingName="meeting.meetingName"
        :meetingDate="meeting.meetingDate"
        :meetingTime="meeting.meetingTime"
        :meetingPlace="meeting.meetingPlace"
        :meetingAddress="meeting.meetingAddress"
        :meetingLat="meeting.meetingLat"
        :meetingLng="meeting.meetingLng"
        :lateAmount="meeting.lateAmount"
        :roomCode="meeting.roomCode"
        :hostId="meeting.hostId"
        :meetingId="meeting.meetingId"
      ></meeting-title>
      <meeting-datetime :meetingTime="meeting.meetingTime"></meeting-datetime>
      <meeting-place
        :meetingPlace="meeting.meetingPlace"
        :meetingAddress="meeting.meetingAddress"
      ></meeting-place>
      <meeting-start-point
        :startPlace="startPlace"
        :startAddress="startAddress"
        :startLat="startLat"
        :startLng="startLng"
      ></meeting-start-point>
      <meeting-member :memberList="meeting.meetingMembers"></meeting-member>
      <meeting-game :memberList="meeting.meetingMembers"></meeting-game>
      <meeting-late-fee :lateAmount="meeting.lateAmount"></meeting-late-fee>
      <meeting-cost
        :meetingId="meeting.meetingId"
        :meetingName="meeting.meetingName"
        :calculateDetails="meeting.calculateDetails"
        :spentMoney="spentMoney"
        :lateTotal="lateTotal"
        :total="total"
        :remain="meeting.remain"
      ></meeting-cost>
    </div>
  </div>
</template>

<script>
import MeetingDatetime from "./element/MeetingDatetime.vue";
import MeetingLateFee from "./element/MeetingLateFee.vue";
import MeetingMember from "./element/MeetingMember.vue";
import MeetingPlace from "./element/MeetingPlace.vue";
import MeetingStartPoint from "./element/MeetingStartPoint.vue";
import MeetingTitle from "./element/MeetingTitle.vue";
import MeetingCost from "./element/MeetingCost.vue";
import MeetingGame from "./element/MeetingGame.vue";
import InternetError from "@/common/component/dialog/InternetError.vue";
import MeetingLoading from "./MeetingLoading.vue";
import { getMeeting } from "@/api/modules/meeting.js";
import { mapActions, mapState } from "vuex";

export default {
  name: "MeetingPage",
  components: {
    MeetingTitle,
    MeetingDatetime,
    MeetingPlace,
    MeetingStartPoint,
    MeetingMember,
    MeetingLateFee,
    MeetingCost,
    MeetingGame,
    InternetError,
    MeetingLoading,
  },
  data() {
    return {
      loading: true,
      meeting: {
        hostId: null,
        meetingId: null,
        meetingName: null,
        meetingPlace: null,
        meetingTime: null,
        meetingAddress: null,
        meetingLat: null,
        meetingLng: null,
        lateAmount: null,
        roomCode: null,
        remain: null,
        meetingMembers: [],
        calculateDetails: [],
      },
      startPlace: null,
      startAddress: null,
      startLat: null,
      startLng: null,
      spentMoney: null,
      lateTotal: null,
      total: null,
    };
  },
  computed: {
    ...mapState("meetingStore", [
      "place_name",
      "place_addr",
      "meeting_lat",
      "meeting_lng",
    ]),
    ...mapState("placeStoremet", ["placeName", "placeAddr"]),
    ...mapState("memberStore", ["member", "member_id"]),
  },
  watch: {
    place_name() {
      this.meeting.meetingPlace = this.place_name;
    },
    place_addr() {
      this.meeting.meetingAddress = this.place_addr;
    },
  },
  async created() {
    this.loading = true;
    await getMeeting(this.$route.params.id).then(async (res) => {
      if (res === null) {
        this.loading = await true;
        this.$refs.error.openDialog();
      } else {
        this.loading = await false;
        this.meeting = await res;
        this.setting(this.meeting.meetingMembers);
        this.SET_MEETING_INFO(res);
      }
    });
  },
  methods: {
    ...mapActions("meetingStore", ["SET_MEETING_INFO"]),

    setting(meetingMembers) {
      const member = meetingMembers.filter(
        (member) => member.memberId == this.member_id
      )[0];

      this.startPlace = member.startPlace;
      this.startAddress = member.startAddress;
      this.startLat = member.startLat;
      this.startLng = member.startLng;
      this.spentMoney = member.spentMoney === null ? 0 : member.spentMoney;
      this.lateTotal =
        member.state == "LATE"
          ? this.meeting.lateAmount === null
            ? 0
            : this.meeting.lateAmount
          : 0;
      this.total = this.meeting.calculateDetails.reduce(
        (accumulator, current) => accumulator + current.price,
        0
      );
    },
  },
};
</script>

<style></style>
