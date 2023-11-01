<template>
  <div style="width: 100%; height: 100%">
    <v-sheet
      class="px-3 main-col-1 d-flex flex-row justify-space-between align-center"
      max-width="500"
      style="
        position: fixed;
        margin: 0 auto;
        left: 0;
        right: 0;
        top: 0;
        z-index: 2;
      "
      height="55"
      color="transparent"
    >
      <!-- 뒤로가기 & 검색 -->
      <v-btn
        id="square-btn"
        class="back-btn"
        outlined
        @click="$router.go(-1)"
        rounded
        elevation="3"
      >
        <v-icon>$vuetify.icons.arrow_left</v-icon>
      </v-btn>
      <v-sheet class="ml-2 search-place" color="transparent" width="100%">
        <input
          class="search-box"
          placeholder=" 출발장소를 검색하세요"
          v-on:click="goSearchPage('/search')"
        />
        <v-icon class="mr-2 search-icon" v-on:click="goToPage('/search')">
          $vuetify.icons.search
        </v-icon>
      </v-sheet>
    </v-sheet>

    <!-- kakao-map -->
    <div id="map"></div>
    <!-- 장소 info -->
    <div @click="saveStartPlace">
      <place-info
        class="place-info"
        v-show="this.startPlace != null"
        :startPlace="this.startPlace"
        :startAddress="startAddress"
      ></place-info>
    </div>
  </div>
</template>

<script>
import placeInfo from "@/components/PlacePage/placeInfo.vue";
import { saveMemberStartPlace } from "@/api/modules/meeting";

export default {
  name: "StartPlace",
  components: { placeInfo },
  data() {
    return {
      meetingRoomId: null,
      startPlace: null,
      startAddress: null,
      startLatLng: [],
    };
  },
  mounted() {
    // Kakao Map Script import
    if (window.kakao && window.kakao.maps) {
      this.initMap();
    } else {
      const script = document.createElement("script");
      //   /* global kakao */
      script.src =
        "//dapi.kakao.com/v2/maps/sdk.js?appkey=4a440970d2ed6adb820352f0223f931f&autoload=false&libraries=services"; // &autoload=false api를 로드한 후 맵을 그리는 함수가 실행되도록 구현
      script.onload = () => window.kakao.maps.load(this.initMap); // 스크립트 로드가 끝나면 지도를 실행될 준비가 되어 있다면 지도가 실행되도록 구현
      document.head.appendChild(script);
    }
  },
  created() {
    // 출발지 장소, 주소, 좌표, 모임ID 저장
    this.startPlace = this.$route.query.startPlace;
    this.startAddress = this.$route.query.startAddress;
    this.startLatLng.push(this.$route.query.startLat);
    this.startLatLng.push(this.$route.query.startLng);
    this.meetingRoomId = this.$route.params.id;
  },
  methods: {
    // ...mapActions("placeStore", ["excuteSaveStartPlace"]),
    // [@Method] Kakao Map 생성
    initMap() {
      const container = document.getElementById("map");
      if (container) {
        const options = {
          center: new window.kakao.maps.LatLng(37.5296, 127.0698),
          level: 4,
        };
        // 지도 객체 등록
        this.map = new window.kakao.maps.Map(container, options);

        // i) 기존 출발지 marker 표시
        if (this.startPlace != null) {
          this.createMarker();
          this.resizeMapBound();
        }
      }
    },
    // [@Method] 출발지 marker 생성
    createMarker() {
      const marker = new window.kakao.maps.Marker({
        position: new window.kakao.maps.LatLng(
          this.startLatLng[0],
          this.startLatLng[1]
        ),
      });
      // marker 표시
      marker.setMap(this.map);
    },
    // [@Method] 검색 페이지로 이동
    goSearchPage(url) {
      this.$router.push({
        path: url,
        query: { type: "start-place", id: this.meetingRoomId },
      });
    },
    // [@Method] marker에 따른 지도 범위 조정
    resizeMapBound() {
      // LatLngBounds 객체에 marker 좌표 추가
      const bounds = new window.kakao.maps.LatLngBounds();
      bounds.extend(
        new window.kakao.maps.LatLng(this.startLatLng[0], this.startLatLng[1])
      );

      // 지도 범위 재설정
      this.map.setBounds(bounds);
    },
    // [@Method] 출발장소 저장
    saveStartPlace() {
      const startPlaceInfo = {
        meetingId: this.meetingRoomId,
        startPlace: this.startPlace,
        startAddress: this.startAddress,
        startLat: this.startLatLng[0],
        startLng: this.startLatLng[1],
      };

      saveMemberStartPlace(startPlaceInfo).then(async (res) => {
        if (res.data.statusCode == 200) {
          // console.log("#21# 출발지 저장 성공 data: ", res.data);
          window.location.href = `${window.location.origin}/meeting/${this.meetingRoomId}`;
        }
      });
    },
  },
};
</script>

<style>
#map {
  width: 100%;
  height: 100%;
}

/* .place-info {
  z-index: 2;
  position: absolute;
  bottom: 5%;
  left: 8.5%;
} */
</style>
