<template>
  <v-dialog v-model="dialog" scrollable max-width="300px" rounded="xl">
    <v-card rounded="xl">
      <v-card-title class="d-flex flex-column">
        <div class="align-self-end">
          <close-button @closeDialog="dialog = false"></close-button>
        </div>
        <div
          class="point-font xxxxxxl-font main-col-1 d-flex flex-column align-center"
        >
          <span>시간을</span>
          <span>다시 설정해주세요</span>
        </div>
        <span
          class="extralight-font xs-font d-flex flex-column align-center seminarrow-font"
        >
          <div>현재 일시 이후의</div>
          <div>날짜와 시간만 설정 가능합니다.</div>
        </span>
      </v-card-title>
      <v-card-text>
        <v-btn
          elevation="0"
          color="var(--main-col-1)"
          dark
          rounded
          block
          @click="dialog = false"
          >닫기</v-btn
        >
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import CloseButton from "@/common/component/button/CloseButton.vue";

export default {
  name: "TimeError",
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
  },
};
</script>

<style></style>
