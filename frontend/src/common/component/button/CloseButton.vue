<template>
  <v-btn
    id="round-btn"
    outlined
    color="var(--main-col-1)"
    @click="closeDialog()"
    icon
  >
    <v-icon color="var(--main-col-1)">$vuetify.icons.close</v-icon>
  </v-btn>
</template>

<script>
export default {
  name: "CloseButton",
  methods: {
    closeDialog() {
      this.$emit("closeDialog");
    },
  },
};
</script>

<style></style>
