<template>
  <v-card
    height="175"
    width="300"
    elevation="0"
    @click="goDetail(meeting.id)"
    class="mb-6 d-flex flex-column"
    style="
      border: 1px solid var(--main-col-1);
      border-radius: 15px;
      box-shadow: 0px 5px 20px -10px var(--main-col-1) !important;
    "
  >
    <v-sheet
      width="150"
      class="align-self-end"
      color="var(--main-col-1)"
      style="border-radius: 0px 13px"
    >
      <span
        class="py-1 d-flex justify-center align-center white-font xxxxs-font"
      >
        약 {{ meeting.meetingTime | remainTime }} 후 예정
      </span>
    </v-sheet>
    <v-sheet class="px-6 pt-1" color="transparent">
      <span class="lg-font bold-font main-col-1">
        {{ meeting.meetingName }}
      </span>
      <v-divider
        class="my-2"
        width="30"
        style="border-color: var(--main-col-1)"
      ></v-divider>
      <div class="xxxs-font main-col-1">
        <span class="medium-font">장소</span>
        <span class="ml-1 light-font">
          {{ meeting.meetingPlace }}
        </span>
      </div>
      <div class="xxxs-font main-col-1">
        <span class="medium-font">일시</span>
        <span class="ml-1 light-font">
          {{ meeting.meetingTime | formatDate }}
        </span>
      </div>
      <div class="d-flex flex-row justify-space-between">
        <section class="avatars-group stacked">
          <div
            v-for="avatar in avatarsStackedLimited"
            :key="`avatar-id-${avatar.id}`"
            class="avatars-group__item"
          >
            <v-avatar size="27px">
              <v-img
                :src="avatar.member.memberProfileImg"
                alt="Profile"
              ></v-img>
            </v-avatar>
          </div>
          <div v-if="this.meeting.meetingMembers.length > 6">
            <div class="avatars-group__item more">
              <v-avatar color="white" size="27px">
                <v-menu
                  v-model="stackedMenu"
                  left
                  :max-height="menuMaxHeight"
                  nudge-bottom="8"
                  offset-y
                >
                </v-menu>
                <span class="xxxs-font light-font main-col-1"
                  >+{{ meeting.meetingMembers.length - 6 }}</span
                >
              </v-avatar>
            </div>
          </div>
        </section>
        <v-btn
          class="ml-1 mt-4"
          id="square-btn"
          outlined
          color="var(--main-col-1)"
          rounded
          small
          @click="goLiveMap(meeting.id)"
        >
          <v-icon>$vuetify.icons.map_outline</v-icon>
        </v-btn>
      </div>
    </v-sheet>
  </v-card>
</template>

<script>
import moment from "moment";

export default {
  name: "MeetingCard",
  props: {
    meeting: Object,
  },
  data() {
    return {
      menuMaxHeight: `${60 * 5 + 4}px`,
      stackedLimit: 6,
      stackedMenu: false,
    };
  },
  filters: {
    formatDate(value) {
      const date = new Date(value);
      const year = date.getFullYear();
      const month = date.getMonth();
      const day = date.getDate();
      const hour = date.getHours();
      const min = date.getMinutes();
      const result = `${year}년 ${
        month + 1 < 10 ? `0${month + 1}` : month + 1
      }월 ${day < 10 ? `0${day}` : day}일
      ${
        hour >= 12
          ? `오후 ${hour == 12 ? `${hour}` : hour - 12}`
          : `오전 ${hour}`
      }시 ${min < 10 ? `0${min}` : min}분`;
      return result;
    },
    remainTime(value) {
      const now = moment();
      const time = moment(value, "YYYY-MM-DD hh:mm:ss");
      const hour = moment.duration(time.diff(now)).hours();
      const min = moment.duration(time.diff(now)).minutes();
      const remain = hour == 0 ? `${min}분` : `${hour}시간 ${min}분`;
      return remain;
    },
  },
  computed: {
    avatarsStackedLimited() {
      return this.meeting.meetingMembers &&
        this.meeting.meetingMembers.length > 0
        ? this.meeting.meetingMembers.slice(0, this.stackedLimit)
        : null;
    },
  },
  methods: {
    goLiveMap(id) {
      this.$router.push(`/live-map/${id}`);
    },
    goDetail(id) {
      this.$router.push(`/meeting/${id}`);
    },
  },
};
</script>

<style lang="stylus" scoped>
$avatar-size = 40px
$base-unit = 5px
$border-width = $base-unit / 4

.avatars-group
  &.grid
    display grid
    grid-gap $base-unit
    grid-template-columns repeat(auto-fit, minmax(($avatar-size + $base-unit), 1fr))
  &.stacked
    display flex
    flex-direction row
    direction ltr
    max-width 100%
    overflow hidden
    white-space nowrap
    padding-top 15px
    > *
      margin-right -($base-unit)
      &:last-of-type
        padding-right ($base-unit * 2)
  &__item
    cursor default
    transition all .1s ease-out
    &.more
      align-items center
      border-radius 50%
      border: 2px solid var(--main-col-1)
      display flex
      &:hover
        transform none
    &:hover
      transform translateY(-($base-unit / 2))
      z-index 1
  .v-avatar
    border: 2px solid var(--main-col-1)
    img
      padding $border-width
    span
      align-items center
      display flex
      height 100%
      justify-content center
      letter-spacing 0.1rem
      width inherit

.v-avatar.bordered
  box-shadow 0px 0px 0px $border-width #fff inset
  img
    padding $border-width
.v-avatar.bordered.small
    box-shadow 0px 0px 0px ($border-width - 1) #fff inset
    img
      padding ($border-width - 1)

.presence
  box-shadow 0px 0px 0px ($border-width) #fff inset
  border-radius 50%
  bottom 0px
  display block
  height ($base-unit * 1.75)
  position absolute
  width ($base-unit * 1.75)
.v-avatar.bordered.small
  .presence
    box-shadow 0px 0px 0px ($border-width - 1) #fff inset
    display block
    height ($base-unit * 1)
    position absolute
    width ($base-unit * 1)
</style>
