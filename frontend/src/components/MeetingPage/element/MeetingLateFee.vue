<template>
  <v-sheet class="mx-3 my-2 d-flex flex-column main-col-1" color="transparent">
    <span class="point-font xxxxl-font">지각비</span>
    <v-sheet
      class="my-1 py-3 px-4 d-flex flex-column justify-center detail-border"
      rounded="lg"
    >
      <span class="sm-font main-col-1">{{ lateAmount | formatPrice }}</span>
    </v-sheet>
  </v-sheet>
</template>

<script>
export default {
  name: "MeetingLateFee",
  props: {
    lateAmount: Number,
  },
  filters: {
    formatPrice(value) {
      if (value === null) return "지각비를 설정하지 않았습니다";
      else return String(value).replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원";
    },
  },
};
</script>

<style></style>
