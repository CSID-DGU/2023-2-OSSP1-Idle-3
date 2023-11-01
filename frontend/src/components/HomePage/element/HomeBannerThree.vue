<template>
  <v-sheet
    id="banner-three"
    width="100%"
    height="100%"
    class="pr-3 d-flex flex-column white-font align-center justify-center banner-font-3 huge-font seminarrow-font"
  >
    <span>Special Event</span>
    <span>Coming Soon</span>
  </v-sheet>
</template>

<script>
export default {
  name: "HomeBannerThree",
};
</script>

<style scoped>
#banner-three {
  background-image: url("@/assets/images/banner/banner3.jpg");
  background-size: cover;
  background-position: center center;
}
</style>
