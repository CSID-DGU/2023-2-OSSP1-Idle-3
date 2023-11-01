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
        <v-icon color="var(--main-col-1)">$vuetify.icons.delete_outline</v-icon>
      </v-btn>
    </template>
    <v-card rounded="xl">
      <v-card-title class="d-flex flex-column">
        <div class="align-self-end">
          <close-button @closeDialog="closeDialog"></close-button>
        </div>
        <img
          class="mb-1"
          src="@/assets/images/dialog/delete_meeting.png"
          width="60%"
        />
        <span class="point-font xxxxxxl-font main-col-1">모임 삭제하기</span>
        <span class="extralight-font sm-font">모임을 삭제하시겠습니까?</span>
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
              @click="delMeeting()"
              >삭제</v-btn
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
import { deleteMeeting } from "@/api/modules/meeting.js";
import { mapState } from "vuex";
export default {
  name: "MeetingDeleteBtn",
  components: { CloseButton },
  props: {
    meetingId: Number,
  },
  data() {
    return {
      dialog: false,
    };
  },
  computed: {
    ...mapState("memberStore", ["member", "member_id"]),
  },
  methods: {
    async delMeeting() {
      await deleteMeeting(this.meetingId).then(async () => {
        this.$router.replace("/home");
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
