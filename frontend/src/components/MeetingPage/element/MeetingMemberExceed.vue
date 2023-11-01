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
        <img src="@/assets/images/dialog/cant_out.png" width="60%" />
        <span class="logo-font xxxxxxl-font main-col-1">Error</span>
        <span
          class="extralight-font xs-font d-flex flex-column align-center seminarrow-font"
        >
          <div>모임 정원을 초과했습니다.</div>
          <div>호스트에게 문의해보세요.</div>
        </span>
      </v-card-title>
      <v-card-text>
        <v-btn
          elevation="0"
          color="var(--main-col-1)"
          dark
          rounded
          block
          @click="closeDialog"
          >확인</v-btn
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
      dialog: true,
    };
  },
  methods: {
    openDialog() {
      this.dialog = true;
    },
    closeDialog() {
      this.dialog = false;
      this.$router.push(`/home`);
    },
  },
};
</script>

<style></style>
