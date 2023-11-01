<template>
  <v-dialog v-model="dialog" scrollable max-width="300px" rounded="xl">
    <v-card rounded="xl">
      <v-card-title class="d-flex flex-column">
        <div class="align-self-end">
          <close-button @closeDialog="closeDialog"></close-button>
        </div>
        <img src="@/assets/images/dialog/logout.png" width="60%" />
        <span class="point-font xxxxxxl-font main-col-1">로그아웃</span>
        <span class="extralight-font sm-font">로그아웃하시겠습니까?</span>
      </v-card-title>
      <v-card-text>
        <v-row>
          <v-col class="pr-1">
            <v-btn
              elevation="0"
              color="var(--main-col-1)"
              dark
              rounded
              block
              @click="logout"
              >로그아웃</v-btn
            >
          </v-col>
          <v-col class="pl-1">
            <v-btn
              elevation="0"
              color="var(--main-col-1)"
              outlined
              dark
              rounded
              @click="closeDialog"
              block
              >취소</v-btn
            >
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import CloseButton from "@/common/component/button/CloseButton.vue";
import { mapActions, mapState } from "vuex";

export default {
  name: "LogoutDialog",
  components: { CloseButton },
  data() {
    return {
      dialog: false,
    };
  },
  computed: {
    ...mapState("memberStore", ["member_id"]),
  },
  methods: {
    ...mapActions("memberStore", ["memberLogout"]),

    openDialog() {
      this.dialog = true;
    },
    closeDialog() {
      this.dialog = false;
    },

    logout() {
      this.memberLogout(this.member_id);
    },
  },
};
</script>

<style></style>
