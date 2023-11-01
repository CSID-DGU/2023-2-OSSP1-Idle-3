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
        <img src="@/assets/images/dialog/permission_error.jpg" width="60%" />
        <span class="logo-font xxxxxxl-font main-col-1">Permission Error</span>
        <span
          class="mt-2 main-col-1 extralight-font xxs-font d-flex flex-column align-center semi2narrow-font"
        >
          <div>위치 권한이 허용되어 있지 않아</div>
          <div>위치 공유 서비스를 이용할 수 없습니다.</div>
          <div class="medium-font">위치 권한을 허용해주세요!</div>
        </span>
      </v-card-title>
      <v-card-text>
        <v-btn
          elevation="0"
          color="var(--main-col-1)"
          dark
          rounded
          block
          small
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
  name: "LocationPermissionError",
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
