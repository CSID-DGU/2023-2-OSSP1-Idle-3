<template>
  <div style="margin-bottom: 76px" class="mx-5 d-flex flex-column">
    <v-sheet
      elevation="2"
      class="px-5 pt-4 pb-5 d-flex flex-column justify-center align-center"
      rounded
    >
      <img width="60%" src="@/assets/images/page/register.png" />
      <div
        class="my-2 d-flex flex-column align-center narrow-font point-font xxxxxxl-font main-col-1"
      >
        <span>지금 바로 모임을</span>
        <span>등록해 보세요!</span>
      </div>
      <div
        class="d-flex flex-column align-center light-font xxs-font main-col-1"
      >
        <span>함께하고 싶은 멤버들에게 초대 링크를 보낼 수 있고</span>
        <span>멤버들과 실시간 채팅, 위치 공유, 정산 기능까지!</span>
      </div>
    </v-sheet>
    <v-sheet elevation="0" class="mt-3">
      <!-- 제목 -->
      <v-sheet width="100%" class="d-flex flex-row" elevation="2" rounded>
        <v-sheet
          width="80"
          color="var(--red-col)"
          class="rounded-l pt-1 d-flex flex-row align-center justify-center point-font white-font xl-font main-col-1"
        >
          <span>제목</span>
        </v-sheet>
        <v-text-field
          v-model="meetingname"
          @keyup="setMeetingName"
          maxlength="9"
          hide-details
          placeholder="모임 제목을 입력해 주세요."
          dense
          solo
          flat
          full-width
          style="width: 100%"
        ></v-text-field>
      </v-sheet>

      <!-- 날짜 -->
      <v-sheet width="100%" class="mt-3 d-flex flex-row" elevation="2" rounded>
        <v-sheet
          width="80"
          color="var(--yellow-col)"
          class="rounded-l pt-1 d-flex flex-row align-center justify-center point-font white-font xl-font main-col-1"
        >
          <span>날짜</span>
        </v-sheet>
        <v-dialog
          ref="menu"
          v-model="menu"
          :return-value.sync="date"
          width="290px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              v-model="date"
              readonly
              v-bind="attrs"
              v-on="on"
              hide-details
              dense
              solo
              flat
              full-width
              style="width: 100%"
            ></v-text-field>
          </template>
          <v-date-picker
            class="regular-font"
            locale="ko"
            v-model="date"
            no-title
            scrollable
            color="var(--main-col-1)"
            :day-format="(date) => new Date(date).getDate()"
            :allowed-dates="disablePastDates"
          >
            <v-spacer></v-spacer>
            <v-btn
              text
              class="bold-font main-col-1"
              @click="menu = false"
              rounded
            >
              닫기
            </v-btn>
            <v-btn
              text
              class="bold-font main-col-1"
              @click="$refs.menu.save(date)"
              rounded
            >
              확인
            </v-btn>
          </v-date-picker>
        </v-dialog>
      </v-sheet>

      <!-- 시간 -->
      <v-sheet width="100%" class="mt-3 d-flex flex-row" elevation="2" rounded>
        <v-sheet
          width="80"
          color="var(--green-col)"
          class="rounded-l pt-1 d-flex flex-row align-center justify-center point-font white-font xl-font main-col-1"
        >
          시간
        </v-sheet>
        <v-dialog
          ref="timeDialog"
          v-model="timeDialog"
          :return-value.sync="time"
          width="290px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              v-model="time"
              readonly
              v-bind="attrs"
              v-on="on"
              hide-details
              solo
              flat
              full-width
              dense
              style="width: 100%"
            ></v-text-field>
          </template>
          <v-time-picker v-model="time" full-width color="var(--main-col-1)">
            <v-spacer></v-spacer>
            <v-btn text color="var(--main-col-1)" @click="timeDialog = false">
              닫기
            </v-btn>
            <v-btn
              text
              color="var(--main-col-1)"
              @click="$refs.timeDialog.save(time)"
            >
              확인
            </v-btn>
          </v-time-picker>
        </v-dialog>
      </v-sheet>

      <!-- 장소 -->
      <v-sheet width="100%" class="mt-3 d-flex flex-row" elevation="2" rounded>
        <v-sheet
          width="80"
          color="var(--blue-col)"
          class="rounded-l pt-1 d-flex flex-row align-center justify-center point-font white-font xl-font main-col-1"
        >
          장소
        </v-sheet>
        <v-text-field
          v-model="meeting_place"
          @click="movePlacePage"
          placeholder="장소를 선택해 주세요."
          solo
          readonly
          flat
          full-width
          style="width: 100%"
          hide-details
          dense
        ></v-text-field>
      </v-sheet>
      <v-btn
        elevation="3"
        color="var(--main-col-1)"
        dark
        block
        class="mt-3 md-font"
        @click="regist_meeting"
      >
        모임 등록하기
      </v-btn>
    </v-sheet>
    <no-image-default
      ref="time"
      title="<span>시간을</span>
          <span>다시 설정해주세요</span>"
      message="<div>현재 일시 이후의</div>
          <div>날짜와 시간만 설정 가능합니다.</div>"
    ></no-image-default>
    <no-image-default
      ref="input"
      title="<span>모든 정보를</span>
          <span>입력해주세요</span>"
      message="<div>제목, 날짜, 시간, 장소를</div>
          <div>모두 입력해야 등록이 가능합니다.</div>"
    ></no-image-default>
  </div>
</template>

<script>
// 추가해야할 부분 -> 현재 시간 이전을 설정하면 불가능하게
import { mapActions, mapState } from "vuex";
import { getMostRecentMeeting } from "@/api/modules/meeting.js";
import NoImageDefault from "@/common/component/dialog/NoImageDefault.vue";
// const meetingStore = "meetingStore";

export default {
  components: { NoImageDefault },
  name: "RegisterPage",
  data() {
    return {
      meetingname: null,
      date: new Date().toISOString().substring(0, 10),
      menu: null,
      time: null,
      timeDialog: null,
      meeting_place: "",
      curDate: null,
      curTime: null,
    };
  },

  computed: {
    ...mapState("meetingStore", ["regist"]),
    ...mapState("memberStore", ["member_id"]),
  },
  watch: {
    date: {
      handler: function () {
        this.setMeetingDate();
      },
      deep: true,
    },
    time: {
      handler: function () {
        this.setMeetingDate();
      },
      deep: true,
    },
  },
  methods: {
    ...mapActions("meetingStore", [
      "register",
      "setMeeting",
      "setRegistMeeting",
      "resetRegist",
    ]),
    ...mapActions("placeStore", ["resetPlace"]),
    ...mapActions("halfwayStore", ["resetStartPlace"]),

    setMeetingName() {
      this.regist.name = this.meetingname;
      // console.log(this.regist);
      this.setRegistMeeting(this.regist);
    },
    setMeetingDate() {
      this.getCurTime();
      this.regist.date = this.date;
      this.regist.time = this.time;
      this.setRegistMeeting(this.regist);
    },

    movePlacePage() {
      this.$router.push("/place");
    },

    regist_meeting() {
      this.getCurTime();

      if (this.curDate >= this.date && this.curTime >= this.time) {
        // alert("시간을 다시 설정해주세요!");
        this.$refs.time.openDialog();
      } else if (
        this.meetingname == null ||
        this.meeting_place == null ||
        this.meetingname.replace(/\s/g, "") == ""
      ) {
        // alert("모든 정보를 입력해주세요!");
        this.$refs.input.openDialog();
      } else {
        const date_time = this.date + " " + this.time; //LocalDate 타입에 맞게 변환
        const meeting_name = this.meetingname;
        const place_name = this.regist.place_name;
        const place_addr = this.regist.place_addr;
        const meeting_lat = this.regist.lat;
        const meeting_lng = this.regist.lng;

        const member_id = this.member_id;

        // console.log(
        //   meeting_name,
        //   date_time,
        //   place_name,
        //   place_addr,
        //   meeting_lat,
        //   meeting_lng
        // );

        this.register({
          member_id,
          meeting_name,
          date_time,
          place_name,
          place_addr,
          meeting_lat,
          meeting_lng,
        }).then(() => {
          getMostRecentMeeting().then((res) => {
            const newRecentMeeting = res;
            const savedRecentMeeting = this.recent_meeting;

            if (
              savedRecentMeeting == null ||
              savedRecentMeeting.meetingTime > newRecentMeeting.meetingTime
            ) {
              this.setMeeting(newRecentMeeting);
            }
          });
        });
        this.resetPlace();
        this.resetStartPlace();
        this.resetRegist();
      }
    },

    getCurTime() {
      const currentDate = new Date();
      const options = {
        timeZone: "Asia/Seoul",
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
      };
      const dateArray = currentDate
        .toLocaleDateString("en-GB", options)
        .split("/");
      this.curDate = `${dateArray[2]}-${dateArray[1]}-${dateArray[0]}`;
      this.curTime = currentDate.toLocaleTimeString("en-GB", {
        timeZone: "Asia/Seoul",
        hour12: false,
        hour: "2-digit",
        minute: "2-digit",
      });
    },
    disablePastDates(val) {
      const date = new Date();
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");
      const formattedDate = `${year}-${month}-${day}`;
      return val >= formattedDate;
    },
  },

  mounted() {
    // this.date = new Date().toLocaleDateString();
    if (this.regist.place_name !== null && this.regist.place_addr !== null) {
      this.meeting_place =
        this.regist.place_name + "(" + this.regist.place_addr + ")";
    }
    // console.log(this.member_id);

    this.meetingname = this.regist.name;
    this.date = this.regist.date;
    this.time = this.regist.time;

    // console.log(this.name, " ", this.date, " ", this.time);
    if (this.date === null || this.time === null) {
      this.getCurTime();
      this.date = this.curDate;
      this.time = this.curTime;
    }
  },
};
</script>

<style scoped></style>
