<template>
  <v-sheet class="mx-3 my-2 d-flex flex-column main-col-1" color="transparent">
    <span class="point-font xxxxl-font">일시</span>
    <v-sheet class="d-flex flex-row justify-space-between" color="transparent">
      <v-sheet
        width="49%"
        class="my-2 py-2 d-flex justify-center detail-border main-col-1 sm-font"
        rounded="lg"
      >
        {{ meetingTime | formatDate }}
      </v-sheet>
      <v-sheet
        width="49%"
        class="my-2 py-2 d-flex justify-center detail-border main-col-1 sm-font"
        rounded="lg"
      >
        {{ meetingTime | formatTime }}
      </v-sheet>
    </v-sheet>
  </v-sheet>
</template>

<script>
export default {
  name: "MeetingDatetime",
  props: {
    meetingTime: String,
    meetingDate: String,
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
  },
};
</script>

<style></style>
