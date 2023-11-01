<template>
  <v-sheet
    class="px-3 d-flex flex-row justify-space-between align-center xxxxl-font main-col-1"
    height="55"
    color="transparent"
  >
    <back-button :elevation="false" color="var(--main-col-1)"></back-button>
    <span class="logo-font" @click="goHome()" style="cursor: pointer">
      Almost There
    </span>
    <home-button color="var(--main-col-1)"></home-button>
  </v-sheet>
</template>

<script>
import BackButton from "@/common/component/button/BackButton.vue";
import HomeButton from "@/common/component/button/HomeButton.vue";

export default {
  name: "NavHeader",
  components: {
    BackButton,
    HomeButton,
  },
  methods: {
    goHome() {
      if (this.$route.name != "home") {
        this.$router.push(`/home`);
      }
    },
  },
};
</script>

<style></style>
