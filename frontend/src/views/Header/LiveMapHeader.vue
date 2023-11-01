<template>
  <v-sheet
    class="px-3 d-flex flex-row justify-space-between align-center xxxxl-font main-col-1"
    height="55"
    max-width="500"
    width="100%"
    color="transparent"
    style="
      position: fixed;
      margin: 0 auto;
      left: 0;
      right: 0;
      top: 0;
      z-index: 2;
    "
  >
    <back-button :elevation="true" color="var(--main-col-1)"></back-button>
    <v-btn
      class="point-font main-col-1 xxl-font px-3"
      color="var(--main-col-1)"
      elevation="3"
      outlined
      style="
        background-color: white !important;
        border-radius: 10px;
        width: 70%;
      "
      @click="goDetail()"
    >
      <div class="pt-1">{{ meetingName }}</div>
    </v-btn>
    <detail-button :elevation="true" :isIcon="true"></detail-button>
  </v-sheet>
</template>

<script>
import BackButton from "@/common/component/button/BackButton.vue";
import DetailButton from "@/common/component/button/DetailButton.vue";

export default {
  name: "LiveMapHeader",
  components: {
    BackButton,
    DetailButton,
  },
  props: {
    meetingName: String,
  },
  methods: {
    goDetail() {
      this.$router.push(`/meeting/${this.$route.params.id}`);
    },
  },
};
</script>

<style scoped>
.title {
  text-shadow: -2px 0 var(--main-col-1), 0 2px var(--main-col-1),
    2px 0 var(--main-col-1), 0 -2px var(--main-col-1);
}
</style>
