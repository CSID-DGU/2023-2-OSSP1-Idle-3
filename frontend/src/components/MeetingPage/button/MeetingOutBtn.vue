<template>
  <v-dialog v-model="dialog" scrollable max-width="300px" rounded="xl">
    <template v-slot:activator="{ on, attrs }">
      <v-btn
        class="mr-1"
        id="square-big-btn"
        outlined
        color="var(--main-col-1)"
        rounded
        v-bind="attrs"
        v-on="on"
      >
        <v-icon>$vuetify.icons.out_outline</v-icon>
      </v-btn>
    </template>
    <v-card rounded="xl">
      <v-card-title class="d-flex flex-column">
        <div class="align-self-end">
          <close-button @closeDialog="closeDialog"></close-button>
        </div>
        <img src="@/assets/images/dialog/out_meeting.png" width="60%" />
        <span class="point-font xxxxxxl-font main-col-1"> 모임에서 나가기</span>
        <span class="extralight-font sm-font">모임에서 나가시겠습니까?</span>
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
              @click="clickOutMeeting"
              >나가기</v-btn
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
  name: "MeetingOutBtn",
  components: { CloseButton },
  data() {
    return {
      dialog: false,
    };
  },
  computed: {
    ...mapState("memberStore", ["member", "member_id"]),
  },
  methods: {
    ...mapActions("meetingStore", ["outMeeting"]),
    clickOutMeeting() {
      // console.log("모임 탈출 ~");
      this.outMeeting(this.member_id)
        .then(() => {
          // console.log("정상실행");
          this.$nextTick(() => {
            // this.$refs.modifySheet.close();
            this.$router.replace("/home");
            //reload를 해야 하나??
          });
        })
        .catch((error) => {
          error;
          // console.error(error);
          // alert("모임 탈출 실패 ~ ");
        });
    },

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
