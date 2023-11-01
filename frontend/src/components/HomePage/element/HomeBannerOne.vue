<template>
  <v-sheet
    id="banner-one"
    @click="goManual()"
    width="100%"
    height="100%"
    class="d-flex flex-column white-font align-center justify-center banner-font-1 hugexlarge-font narrow-font"
  >
    <span>Welcome</span>
    <span>To</span>
    <span>Almost There</span>
    <v-btn
      class="px-7 mt-3"
      elevation="0"
      color="white"
      rounded
      @click="goManual()"
    >
      <span class="light-font main-col-1 mr-2">사용설명서 보러가기</span>
      <v-icon small>$vuetify.icons.arrow_right_white</v-icon>
    </v-btn>
  </v-sheet>
</template>

<script>
export default {
  name: "HomeBannerOne",
  methods: {
    goManual() {
      this.$router.push(`/manual`);
    },
  },
};
</script>

<style scoped>
#banner-one {
  background-image: url("@/assets/images/banner/banner1.jpg");
  background-size: cover;
  background-position: center 60%;
}
</style>
