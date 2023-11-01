<template>
  <v-sheet class="mx-3 my-2 d-flex flex-column" color="transparent">
    <v-sheet class="d-flex flex-row justify-space-between align-end">
      <span class="point-font xxxxl-font main-col-1">나의 출발지</span>
      <v-btn
        class="d-flex justify-center align-center"
        id="square-btn"
        outlined
        color="var(--main-col-1)"
        rounded
        small
        @click="moveStartPlace()"
      >
        <v-icon>$vuetify.icons.edit_outline</v-icon>
      </v-btn>
    </v-sheet>
    <v-sheet
      class="my-1 py-3 px-4 d-flex flex-column justify-center detail-border main-col-1"
      rounded="lg"
    >
      <span class="sm-font">{{ startPlace | formatPlace }}</span>
      <span class="thin-font xxs-font">{{ startAddress | formatAddress }}</span>
    </v-sheet>
  </v-sheet>
</template>

<script>
export default {
  name: "MeetingStartPoint",
  props: {
    startPlace: String,
    startAddress: String,
    startLat: Number,
    startLng: Number,
  },
  filters: {
    formatPlace(value) {
      // console.log("장소: " + value);
      if (value === null) return "출발지를 설정하지 않았습니다.";
      else return value;
    },
    formatAddress(value) {
      // console.log("주소: " + value);
      if (value === null) return "";
      else return value;
    },
  },
  methods: {
    // [@Method] 출발지 선택 페이지로 이동
    moveStartPlace() {
      this.$router.push({
        path: `/start-place/${this.$route.params.id}`,
        query: {
          startPlace: this.startPlace,
          startAddress: this.startAddress,
          startLat: this.startLat,
          startLng: this.startLng,
        },
      });
    },
  },
};
</script>

<style></style>
