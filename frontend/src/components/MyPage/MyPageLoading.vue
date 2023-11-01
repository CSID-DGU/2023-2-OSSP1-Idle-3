<template>
  <v-sheet id="mypage-loading" class="d-flex flex-column mx-5">
    <div class="d-flex flex-row">
      <v-skeleton-loader
        type="image"
        height="80"
        width="80"
      ></v-skeleton-loader>
      <div class="ml-4 d-flex flex-column">
        <v-skeleton-loader
          class="mb-1"
          type="heading"
          width="150"
        ></v-skeleton-loader>
        <v-skeleton-loader
          class="mb-1"
          type="heading"
          width="200"
        ></v-skeleton-loader>
        <v-skeleton-loader type="heading" width="80"></v-skeleton-loader>
      </div>
    </div>
    <v-sheet class="mt-4 px-5 pt-5 pb-4 detail-shadow" rounded="lg">
      <v-skeleton-loader type="text"></v-skeleton-loader>
      <v-skeleton-loader type="text"></v-skeleton-loader>
      <v-skeleton-loader type="text"></v-skeleton-loader>
    </v-sheet>
    <v-sheet class="mt-7">
      <v-skeleton-loader type="image"></v-skeleton-loader>
    </v-sheet>
  </v-sheet>
</template>

<script>
export default {
  name: "MyPageLoading",
};
</script>

<style>
#mypage-loading .v-skeleton-loader__heading {
  width: 100% !important;
}
</style>
