<template>
  <v-sheet
    min-height="100%"
    class="d-flex flex-column justify-end mx-3"
    style="padding: 55px 0px 20px 0px"
  >
    <div class="align-self-end d-flex flex-column align-end">
      <v-skeleton-loader
        type="image"
        class="mb-1"
        width="230"
        height="30"
      ></v-skeleton-loader>
      <v-skeleton-loader
        type="image"
        class="mb-1"
        width="100"
        height="30"
      ></v-skeleton-loader>
    </div>
    <div class="d-flex flex-row">
      <v-skeleton-loader type="avatar" class="mb-1 mr-2"></v-skeleton-loader>
      <div>
        <v-skeleton-loader
          type="image"
          class="mb-1"
          width="100"
          height="30"
        ></v-skeleton-loader>
        <v-skeleton-loader
          type="image"
          class="mb-1"
          width="200"
          height="30"
        ></v-skeleton-loader>
      </div>
    </div>
    <v-skeleton-loader
      type="image"
      class="align-self-end mb-1"
      width="70"
      height="30"
    ></v-skeleton-loader>
    <div class="d-flex flex-row">
      <v-skeleton-loader type="avatar" class="mb-1 mr-2"></v-skeleton-loader>
      <div>
        <v-skeleton-loader
          type="image"
          class="mb-1"
          width="180"
          height="30"
        ></v-skeleton-loader>
        <v-skeleton-loader
          type="image"
          class="mb-1"
          width="250"
          height="30"
        ></v-skeleton-loader>
      </div>
    </div>
  </v-sheet>
</template>

<script>
export default {};
</script>

<style></style>
