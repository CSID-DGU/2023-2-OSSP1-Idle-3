<template>
  <v-sheet
    class="px-3 d-flex flex-row justify-space-between align-center xxxxl-font main-col-1"
    height="55"
    max-width="500"
    style="
      position: fixed;
      margin: 0 auto;
      left: 0;
      right: 0;
      top: 0;
      z-index: 2;
    "
  >
    <back-button color="var(--main-col-1)"></back-button>
    <span class="point-font">{{ name }}</span>
    <div>
      <v-btn
        id="square-btn"
        outlined
        color="var(--main-col-1)"
        @click.stop="openDrawer()"
        rounded
      >
        <v-icon color="var(--main-col-1)">$vuetify.icons.list</v-icon>
      </v-btn>
    </div>
  </v-sheet>
</template>

<script>
import BackButton from "@/common/component/button/BackButton.vue";

export default {
  name: "ChattingHeader",
  components: {
    BackButton,
  },
  props: {
    name: String,
  },
  methods: {
    openDrawer() {
      this.$emit("openDrawer");
    },
  },
};
</script>

<style></style>
