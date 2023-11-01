<template>
  <v-bottom-navigation
    id="nav"
    fixed
    style="margin: 0 auto; left: 0; right: 0"
    v-model="value"
    shift
    max-width="500"
  >
    <v-btn
      v-for="(item, index) in items"
      :key="index"
      :to="item.link"
      :value="item.link"
    >
      <span class="main-col-1 medium-font">{{ item.text }}</span>
      <v-icon v-if="value == item.link" color="var(--main-col-1)">{{
        item.icon1
      }}</v-icon>
      <v-icon v-else color="var(--main-col-2)">{{ item.icon2 }}</v-icon>
    </v-btn>
  </v-bottom-navigation>
</template>

<script>
export default {
  name: "TheNavigation",
  computed: {
    routerName() {
      return this.$route.name;
    },
  },
  watch: {
    routerName() {
      this.value = window.location.pathname;
    },
  },
  data() {
    return {
      value: null,
      items: [
        {
          link: "/register",
          text: "등록",
          icon1: `$vuetify.icons.register`,
          icon2: `$vuetify.icons.register_outline`,
        },
        {
          link: "/home",
          text: "홈",
          icon1: `$vuetify.icons.home`,
          icon2: `$vuetify.icons.home_outline`,
        },
        {
          link: "/mypage",
          text: "마이",
          icon1: `$vuetify.icons.mypage`,
          icon2: `$vuetify.icons.mypage_outline`,
        },
      ],
    };
  },
  // created() {
  //   this.value = window.location.pathname;
  // },
};
</script>

<style></style>
