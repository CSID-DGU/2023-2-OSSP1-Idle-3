<template>
  <v-sheet
    class="narrow-font d-flex flex-column justify-center align-center hugelarge-font main-col-1"
    height="100%"
    color="transparent"
  >
    <img src="@/assets/images/logo/logo.png" height="47" />
    <span class="logo-font">Almost</span>
    <span class="logo-font">There!</span>
  </v-sheet>
</template>

<script>
export default {
  name: "TheLanding",
  mounted() {
    setTimeout(() => {
      this.$router.push("/login");
    }, 2500);
  },
};
</script>

<style></style>
