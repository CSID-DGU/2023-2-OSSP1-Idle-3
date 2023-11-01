<template>
  <v-btn id="square-btn" outlined :color="color" @click="goHome()">
    <v-icon :color="color">$vuetify.icons.home_outline</v-icon>
  </v-btn>
</template>

<script>
export default {
  name: "HomeButton",
  props: {
    color: String,
  },
  methods: {
    goHome() {
      this.$router.push("/");
    },
  },
};
</script>

<style></style>
