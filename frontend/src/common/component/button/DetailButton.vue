<template>
  <div>
    <v-btn
      v-if="isIcon"
      outlined
      id="square-btn"
      color="var(--main-col-1)"
      @click="goDetail()"
      rounded
      :elevation="elevation ? '3' : '0'"
      style="background-color: white !important"
    >
      <v-icon color="var(--main-col-1)">$vuetify.icons.detail_outline</v-icon>
    </v-btn>
    <v-btn
      v-else
      class="ma-4"
      outlined
      color="var(--main-col-1)"
      @click="goDetail()"
      rounded
    >
      <span>모임 페이지로 이동</span>
    </v-btn>
  </div>
</template>

<script>
export default {
  name: "DetailButton",
  props: {
    isIcon: Boolean,
    elevation: Boolean,
  },
  methods: {
    goDetail() {
      this.$router.push(`/meeting/${this.$route.params.id}`);
    },
  },
};
</script>

<style></style>
