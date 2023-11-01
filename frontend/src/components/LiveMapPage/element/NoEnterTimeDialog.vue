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
          <close-button @closeDialog="goBack()"></close-button>
        </div>
        <img src="@/assets/images/dialog/dont_enter.png" width="60%" />
        <span class="point-font xxxxxxl-font main-col-1"> 위치 공유 불가</span>
        <span class="extralight-font xs-font mt-2 seminarrow-font">
          멤버들과 실시간 위치 공유는
        </span>
        <span class="extralight-font xs-font mb-2 seminarrow-font">
          모임 시간 전후 3시간 동안만 지원됩니다.
        </span>
      </v-card-title>
      <v-card-text>
        <v-row>
          <v-col class="pl-1">
            <v-btn
              elevation="0"
              color="white"
              outlined
              dark
              rounded
              @click="goBack()"
              block
              style="background-color: var(--main-col-1)"
              >뒤로가기</v-btn
            >
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import CloseButton from "@/common/component/button/CloseButton.vue";

export default {
  name: "NoEnterTimeDialog",
  components: {
    CloseButton,
  },
  data() {
    return {
      dialog: false,
    };
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    closeDialog() {
      this.dialog = false;
    },
    openDialog() {
      this.dialog = true;
    },
  },
};
</script>

<style></style>
