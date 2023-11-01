<template>
  <v-sheet class="mx-3 my-2 d-flex flex-column main-col-1" color="transparent">
    <span class="point-font xxxxl-font">장소</span>
    <v-sheet
      class="my-1 py-3 px-4 d-flex flex-column justify-center detail-border main-col-1"
      rounded="lg"
    >
      <span class="sm-font">{{ meetingPlace }}</span>
      <span class="thin-font xxs-font">({{ meetingAddress }})</span>
    </v-sheet>
  </v-sheet>
</template>

<script>
export default {
  name: "MeetingPlace",
  props: {
    meetingPlace: String,
    meetingAddress: String,
  },
};
</script>

<style></style>
