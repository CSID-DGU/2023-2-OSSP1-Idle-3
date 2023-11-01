<template>
  <v-sheet class="mt-7" width="100%">
    <v-date-picker
      id="my-page-calendar"
      class="regular-font"
      locale="ko"
      v-model="selectedDate"
      scrollable
      no-title
      full-width
      color="var(--main-col-1)"
      :day-format="(date) => new Date(date).getDate()"
      :events="highlightedDates"
      :event-color="
        (date) => (date == selectedDate ? 'white' : 'var(--main-col-1)')
      "
    >
    </v-date-picker>
    <meeting-list-on-day :date="selectedDate"></meeting-list-on-day>
  </v-sheet>
</template>

<script>
import { mapState } from "vuex";
import MeetingListOnDay from "./MeetingListOnDay.vue";

export default {
  name: "MemberCalendar",
  data() {
    return {
      selectedDate: new Date(
        Date.now() - new Date().getTimezoneOffset() * 60000
      )
        .toISOString()
        .substr(0, 10), // date-picker로 선택한 date
      highlightedDates: null, // highlight 처리할 날짜 배열
    };
  },
  components: { MeetingListOnDay },
  computed: {
    ...mapState("mypageStore", ["attendMeetings"]),
  },
  mounted() {
    const dates = [];
    this.highlightedDates = this.attendMeetings.map((meeting) => {
      const date = meeting.meetingDto.meetingTime.substring(0, 10);

      // 중복 check
      if (!dates.includes(date)) {
        const year = meeting.meetingDto.meetingTime.substring(0, 4);
        const month = meeting.meetingDto.meetingTime.substring(5, 7) - 1;
        const day = Number(meeting.meetingDto.meetingTime.substring(8, 10));

        dates.push(
          new Date(Date.UTC(year, month, day)).toISOString().substring(0, 10)
        );
      }
    });
    this.highlightedDates = dates;
    // console.log("#21# highlightedDates 확인: ", this.highlightedDates);
  },
  methods: {},
};
</script>

<style>
#my-page-calendar .v-btn--rounded {
  border-radius: 10px !important;
  height: 32px !important;
  width: 32px !important;
}
#my-page-calendar .v-date-picker-table--date .v-date-picker-table__events {
  bottom: 9px !important;
}
#my-page-calendar .v-date-picker-table__events > div {
  width: 18px !important;
  border-radius: 5px !important;
  height: 5px !important;
}
/* date-picker calendar 부분 */
/* .v-picker {
  flex-direction: row !important;
  width: 100%;
  font-size: large;
} */

/* date-picker highlight 표시 */
/* ::v-deep .v-date-picker-table__events > div {
  border-radius: 25%;
  display: inline-block;
  height: 5.5px;
  margin: 0 1px;
  width: 15px;
} */
</style>
