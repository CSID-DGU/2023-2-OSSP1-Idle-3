<template>
  <v-sheet
    max-width="500"
    color="transparent"
    width="100%"
    class="d-flex flex-row justify-end align-center"
    style="
      position: fixed;
      margin: 0 auto;
      left: 0;
      right: 0;
      bottom: 0;
      padding: 0 10px 65px 0;
    "
  >
    <v-btn
      v-show="fab"
      dark
      color="var(--main-col-1)"
      style="background-color: white"
      @click="goBottom"
      icon
      outlined
      small
    >
      <v-icon>mdi-arrow-down</v-icon>
    </v-btn>
  </v-sheet>
</template>

<script>
export default {
  name: "ScrollBottomButton",
  data() {
    return {
      fab: false,
      bottom: false,
    };
  },
  watch: {
    bottom(bottom) {
      if (bottom) this.fab = false;
      else this.fab = true;
    },
  },
  methods: {
    // onScroll() {
    //   if (typeof window === "undefined") return;
    //   const scrollY = window.scrollY;
    //   const visible = document.documentElement.clientHeight;
    //   const pageHeight = document.documentElement.scrollHeight;
    //   const bottomOfPage = visible + scrollY + 40 >= pageHeight;
    //   this.fab = !(bottomOfPage || pageHeight < visible);
    // },
    open() {
      this.fab = true;
    },
    close() {
      this.fab = false;
    },
    // [@Method] 맨 아래로 가기
    goBottom() {
      window.scrollTo(0, document.querySelector("body").scrollHeight);
      // document
      //   .getElementById("chattingMessages")
      //   .scrollTo(0, document.getElementById("chattingMessages").scrollHeight);
    },
    // bottomVisible() {
    //   const scrollY = window.scrollY;
    //   const visible = document.documentElement.clientHeight;
    //   const pageHeight = document.documentElement.scrollHeight;
    //   // + 90은 Footer의 높이
    //   const bottomOfPage = visible + scrollY + 90 >= pageHeight;
    //   return bottomOfPage || pageHeight < visible;
    // },
    // onTheBottom() {
    //   this.bottom = this.bottomVisible();
    // },
  },
  // created() {
  //   window.addEventListener("scroll", this.onTheBottom);
  // },
  // beforeDestroy() {
  //   window.removeEventListener("scroll", this.onTheBottom);
  // },
};
</script>

<style></style>
