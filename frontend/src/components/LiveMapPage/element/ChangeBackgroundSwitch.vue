<template>
  <v-sheet color="transparent">
    <v-sheet
      max-width="500"
      color="transparent"
      width="100%"
      class="d-flex flex-column justify-end align-end"
      style="
        position: fixed;
        margin: 0 auto;
        left: 0;
        right: 0;
        top: 45px;
        padding: 0 15px 15px 0;
        z-index: 2;
      "
    >
      <v-switch
        v-model="mapType"
        color="var(--main-col-1)"
        @change="handleChange"
      ></v-switch>
    </v-sheet>
  </v-sheet>
</template>

<script>
export default {
  name: "ChangeBackgroundSwitch",
  props: {},
  data() {
    return {
      mapType: false,
    };
  },
  methods: {
    // [@Method] 변경된 switch 값 부모 컴포넌트로 전달
    handleChange() {
      this.$emit("map-type-change", this.mapType);
    },
  },
};
</script>

<style></style>
