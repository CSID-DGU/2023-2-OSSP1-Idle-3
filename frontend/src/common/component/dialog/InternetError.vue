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
          <close-button @closeDialog="goBack"></close-button>
        </div>
        <img src="@/assets/images/dialog/internet_error.png" width="60%" />
        <span class="logo-font xxxxxxl-font main-col-1">Server Error</span>
        <span
          class="extralight-font xxs-font d-flex flex-column align-center semi2narrow-font"
        >
          <div>인터넷 연결이 되지 않았거나</div>
          <div>접속 불가능한 링크입니다.</div>
        </span>
      </v-card-title>
      <v-card-text>
        <v-btn
          elevation="0"
          color="var(--main-col-1)"
          dark
          rounded
          block
          @click="goHome"
          >홈으로 돌아가기</v-btn
        >
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import CloseButton from "@/common/component/button/CloseButton.vue";
export default {
  name: "InternetError",
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
    goBack() {
      this.$router.go(-1);
    },
    goHome() {
      this.$router.replace(`/home`);
    },
  },
};
</script>

<style></style>
