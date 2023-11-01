<template>
  <v-btn
    id="square-btn"
    outlined
    :color="color"
    @click="goBack()"
    :elevation="elevation ? '3' : '0'"
    rounded
    :style="{
      'background-color':
        color == 'white' ? 'var(--main-col-1)' : 'white' + ' !important',
    }"
  >
    <v-icon v-if="color == 'white'">$vuetify.icons.arrow_left_white</v-icon>
    <v-icon v-else>$vuetify.icons.arrow_left</v-icon>
  </v-btn>
</template>

<script>
export default {
  name: "BackButton",
  props: {
    color: String,
    elevation: Boolean,
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
  },
};
</script>

<style></style>
