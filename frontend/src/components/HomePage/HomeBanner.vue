<template>
  <swiper class="mb-3 swiper" :options="swiperOption">
    <swiper-slide>
      <v-sheet width="100%" height="250px" style="cursor: pointer">
        <home-banner-one></home-banner-one>
      </v-sheet>
    </swiper-slide>
    <swiper-slide>
      <v-sheet width="100%" height="250px" style="cursor: pointer">
        <home-banner-two></home-banner-two>
      </v-sheet>
    </swiper-slide>
    <swiper-slide>
      <v-sheet width="100%" height="250px" style="cursor: pointer">
        <home-banner-three></home-banner-three>
      </v-sheet>
    </swiper-slide>
    <div class="swiper-pagination" slot="pagination"></div>
  </swiper>
</template>

<script>
import HomeBannerOne from "./element/HomeBannerOne.vue";
import HomeBannerTwo from "./element/HomeBannerTwo.vue";
import HomeBannerThree from "./element/HomeBannerThree.vue";

export default {
  name: "HomeBanner",
  components: {
    HomeBannerOne,
    HomeBannerTwo,
    HomeBannerThree,
  },
  data() {
    return {
      swiperOption: {
        slidesPerView: "auto",
        spaceBetween: 25,
        pagination: {
          el: ".swiper-pagination",
          clickable: true,
        },
        loop: true,
        autoplay: {
          delay: 3000,
          disableOnInteraction: false,
        },
      },
      // banners: [
      //   {
      //     title: ["VERSION 1.0", "RELEASE"],
      //     contents: ["Almost There", "서비스 OPEN"],
      //     image: require("@/assets/images/banner/home.png"),
      //   },
      //   {
      //     title: ["보다 편하게", "모임을 잡고 관리하세요"],
      //     contents: [
      //       "영수증을 인식해 N/1 정산 기능",
      //       "멤버 간 실시간 위치 공유",
      //       "출발지를 기반으로 한 중간 위치 추천",
      //     ],
      //     image: require("@/assets/images/banner/calendar.png"),
      //   },
      //   {
      //     title: ["개발팀 '어디야?'", "A401 Team"],
      //     contents: ["엄희정 지강훈", "신도연 안승태", "이종민 서지윤"],
      //     image: require("@/assets/images/banner/group.png"),
      //   },
      // ],
    };
  },
};
</script>

<style scoped>
.swiper-slide {
  width: 100%;
}
.swiper-container {
  width: 100% !important;
}
</style>
