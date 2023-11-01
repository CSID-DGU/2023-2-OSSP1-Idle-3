<template>
  <v-sheet class="my-2 d-flex flex-column" color="transparent">
    <v-sheet class="ml-3 d-flex flex-row justify-space-between align-center">
      <span class="point-font xxxxl-font main-col-1">멤버</span>
      <div class="mr-3 d-flex flex-row xxxxs-font thin-font">
        <div class="mx-1">
          <v-avatar
            class="mr-1"
            color="var(--roulette-col-1)"
            size="14"
          ></v-avatar>
          <span>지각</span>
        </div>
        <div class="mx-1">
          <v-avatar
            class="mr-1"
            color="var(--roulette-col-5)"
            size="14"
          ></v-avatar>
          <span>도착</span>
        </div>
      </div>
    </v-sheet>
    <swiper class="swiper pt-1 px-3" :options="swiperOption">
      <swiper-slide v-for="(member, index) in memberList" :key="index">
        <v-badge
          overlap
          dot
          :color="
            member.state == 'LATE'
              ? 'var(--roulette-col-1)'
              : member.state == 'ARRIVE'
              ? 'var(--roulette-col-5)'
              : ''
          "
          offset-x="13"
          offset-y="13"
        >
          <v-card style="border-radius: 18px" elevation="0">
            <v-img
              class="align-end"
              height="80"
              width="80"
              gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
              :src="member.memberProfileImg"
            >
              <div class="white-font extralight-font xxxs-font pa-2">
                {{ member.memberNickname }}
              </div>
            </v-img>
          </v-card>
        </v-badge>
      </swiper-slide>
    </swiper>
  </v-sheet>
</template>

<script>
export default {
  name: "MeetingMember",
  data() {
    return {
      swiperOption: {
        spaceBetween: 7,
        freeMode: true,
        slidesPerView: "auto",
      },
    };
  },
  props: {
    memberList: Array,
  },
};
</script>

<style scoped>
.swiper-container {
  width: 100% !important;
}

.swiper-slide {
  width: auto;
}
</style>
