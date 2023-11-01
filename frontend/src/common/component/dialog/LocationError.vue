<template>
  <v-dialog
    v-model="dialog"
    scrollable
    max-width="300px"
    rounded="xl"
    persistent
  >
    <v-card rounded="xl">
      <v-card-title class="d-flex flex-column">
        <div class="align-self-end">
          <close-button @closeDialog="closeDialog"></close-button>
        </div>
        <img
          class="mb-1"
          src="@/assets/images/dialog/location_error.png"
          width="40%"
        />
        <span class="logo-font xxxxxxl-font main-col-1">Location Error</span>
        <span
          class="mt-1 extralight-font xxs-font d-flex flex-column align-center semi2narrow-font main-col-1"
        >
          <div>현재 위치를 가져올 수 없어</div>
          <div>위치 공유 서비스를 이용할 수 없습니다.</div>
          <div class="medium-font">서비스에 다시 접속해주세요!</div>
        </span>
      </v-card-title>
      <v-card-text>
        <v-btn
          elevation="0"
          color="var(--main-col-1)"
          dark
          rounded
          small
          block
          @click="closeDialog"
          >닫기</v-btn
        >
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import CloseButton from "@/common/component/button/CloseButton.vue";

export default {
  name: "LocationError",
  components: { CloseButton },
  data() {
    return {
      dialog: false,
    };
  },
  methods: {
    openDialog() {
      this.dialog = true;
    },
    closeDialog() {
      this.dialog = false;
    },
  },
};
</script>

<style></style>
