<template>
  <v-sheet class="mt-4">
    <div v-if="meetings.length == 0">
      <no-meeting />
    </div>
    <div v-else>
      <meeting-list-card :key="date" :date="date" :meetings="meetings" />
    </div>
  </v-sheet>
</template>

<script>
import { mapState } from "vuex";
import NoMeeting from "./NoMeeting.vue";
import MeetingListCard from "./MeetingListCard.vue";

export default {
  name: "MeetingListDay",
  props: {
    date: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      meetings: [],
    };
  },
  components: { NoMeeting, MeetingListCard },
  computed: {
    ...mapState("mypageStore", ["attendMeetings"]),
  },
  watch: {
    date() {
      this.getOnDayMeetings();
    },
  },
  created() {
    this.getOnDayMeetings();
  },
  methods: {
    getOnDayMeetings() {
      const onDayMeeting = [];
      this.meetings = this.attendMeetings.map((meeting) => {
        const meetingDate = meeting.meetingDto.meetingTime.substring(0, 10);
        // console.log("#21# 모임날짜 vs 선택한 날짜: ", meetingDate, this.date);

        // 현재 선택한 날짜의 모임 조회
        if (meetingDate == this.date) {
          onDayMeeting.push(meeting.meetingDto);
        }
      });
      this.meetings = onDayMeeting;
    },
  },
};
</script>

<style></style>
