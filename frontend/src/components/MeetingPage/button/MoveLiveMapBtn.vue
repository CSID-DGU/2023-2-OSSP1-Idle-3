<template>
  <v-btn
    color="var(--main-col-1)"
    @click="moveLivemap"
    text
    rounded
    icon
    x-large
  >
    <div class="pt-1 d-flex flex-column align-center">
      <v-icon
        large
        style="font-size: 37px; height: 37px; width: 37px; margin: 2px 0px"
      >
        $vuetify.icons.map_outline
      </v-icon>
      <span class="xxxxs-font">위치</span>
    </div>
  </v-btn>
</template>

<script>
export default {
  name: "MoveLiveMapBtn",
  props: {
    meetingTime: String,
  },
  methods: {
    moveLivemap() {
      this.$router.push(`/live-map/${this.$route.params.id}`);
    },
  },
};
</script>

<style scoped>
span.extralight-font.sm-font {
  line-height: 1.5;
}
</style>
