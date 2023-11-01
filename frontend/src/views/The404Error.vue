<template>
  <v-sheet
    class="d-flex flex-column align-center justify-center"
    height="100%"
    width="100%"
  >
    <img class="mb-2" src="@/assets/images/404.png" width="60%" />
    <v-btn
      class="mb-10 px-12"
      color="var(--main-col-1)"
      dark
      rounded
      @click="goHome()"
    >
      홈으로 돌아가기
    </v-btn>
  </v-sheet>
</template>

<script>
export default {
  name: "The404Error",
  methods: {
    goHome() {
      this.$router.push("/home");
    },
  },
};
</script>

<style></style>
