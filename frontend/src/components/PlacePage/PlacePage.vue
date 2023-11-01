<template>
  <div class="map-area">
    <!-- 장소 찾기 헤더 -->
    <v-sheet
      class="px-3 main-col-1"
      max-width="500"
      style="
        position: fixed;
        margin: 0 auto;
        left: 0;
        right: 0;
        top: 0;
        z-index: 2;
      "
      color="transparent"
    >
      <v-sheet
        height="55"
        color="transparent"
        class="d-flex flex-row justify-space-between align-center"
      >
        <v-btn
          id="square-btn"
          class="back-btn"
          outlined
          @click="goBack()"
          elevation="3"
          rounded
          color="var(--main-col-1)"
        >
          <v-icon color="var(--main-col-1)">$vuetify.icons.arrow_left</v-icon>
        </v-btn>
        <v-sheet class="ml-2 search-place" color="transparent" width="100%">
          <input
            class="search-box"
            placeholder=" 모임장소를 검색하세요"
            v-on:click="goToPage('/search')"
          />
          <v-icon class="mr-2 search-icon" v-on:click="goToPage('/search')">
            $vuetify.icons.search
          </v-icon>
        </v-sheet>
      </v-sheet>
      <v-sheet
        class="d-flex flex-row justify-space-between"
        color="transparent"
      >
        <div>
          <v-btn-toggle
            elevation="3"
            dense
            rounded
            v-show="isRecommend"
            mandatory
            color="var(--main-col-1)"
            id="category"
          >
            <v-btn id="SW8" @click="onClickCategory" style="min-width: 20px">
              <v-icon color="var(--main-col-1)" id="SW8">
                mdi mdi-subway-variant
              </v-icon>
            </v-btn>
            <v-btn id="FD6" @click="onClickCategory" style="min-width: 20px">
              <v-icon id="FD6" color="var(--main-col-1)">
                mdi mdi-silverware-fork-knife
              </v-icon>
            </v-btn>
            <v-btn id="CE7" @click="onClickCategory" style="min-width: 20px">
              <v-icon color="var(--main-col-1)" id="CE7">mdi mdi-coffee</v-icon>
            </v-btn>
            <v-btn id="CT1" @click="onClickCategory" style="min-width: 20px">
              <v-icon color="var(--main-col-1)" id="CT1">
                mdi mdi-movie-play
              </v-icon>
            </v-btn>
          </v-btn-toggle>
        </div>
        <v-btn class="halfway-btn" @click="findHalfway()" outlined rounded>
          <v-icon class="mr-1">$vuetify.icons.location_outline</v-icon>
          <span>중간 위치 찾기</span>
        </v-btn>
      </v-sheet>
      <!-- <ul v-show="isRecommend" id="category">
        <li id="SW8" @click="onClickCategory">
          <v-icon class="category_icon" id="SW8">mdi mdi-subway-variant</v-icon>
        </li>
        <li id="FD6" @click="onClickCategory">
          <v-icon class="category_icon" id="FD6"
            >mdi mdi-silverware-fork-knife</v-icon
          >
        </li>
        <li id="CE7" @click="onClickCategory">
          <v-icon class="category_icon" id="CE7">mdi mdi-coffee</v-icon>
        </li>
        <li id="CT1" @click="onClickCategory">
          <v-icon class="category_icon" id="CT1">mdi mdi-movie-play</v-icon>
        </li>
      </ul> -->
      <!-- <div class="find-middle-place-btn">
        <v-btn class="find-place-btn" 
          ><i class="fa-light fa-location-dot"></i>
          <v-icon class="marker-icon">$vuetify.icons.location_outline</v-icon>
          <span class="find-middle-place-title">중간 위치 찾기</span></v-btn
        >
      </div> -->
    </v-sheet>

    <halfway-modal ref="halfway"></halfway-modal>

    <div id="map" class="maps"></div>
    <div v-show="isSelect && placeX != null" @click="moveRegisterPage">
      <place-info class="place-info"></place-info>
    </div>
    <v-sheet width="100%" v-if="placeSelect">
      <!-- <middle-place-info
        class="middle-place-info"
        v-if="minTimes.length == startPlaces.length"
        :minTimes="minTimes"
        :stateTraffic="stateTraffic"
        :placeName="currentRecommendPlaceName"
        :addressName="currentRecommendPlaceAddress"
        :placeUrl="curRecommendPlaceUrl"
        :placeX="curRecommendX"
        :placeY="curRecommendY"
      ></middle-place-info> -->
      <middle-place-info
        v-if="minTimes.length == startPlaces.length"
        :minTimes="minTimes"
        :stateTraffic="stateTraffic"
        :placeName="currentRecommendPlaceName"
        :addressName="currentRecommendPlaceAddress"
        :placeUrl="curRecommendPlaceUrl"
        :placeX="curRecommendX"
        :placeY="curRecommendY"
      ></middle-place-info>
    </v-sheet>
    <loading-modal v-if="loading"></loading-modal>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import placeInfo from "./placeInfo.vue";
import middlePlaceInfo from "./middlePlaceInfo.vue";
import HalfwayModal from "./placeModal/HalfwayModal.vue";
import axios from "axios";
import LoadingModal from "./placeModal/LoadingModal.vue";

export default {
  components: { placeInfo, HalfwayModal, middlePlaceInfo, LoadingModal },
  name: "PlacePage",
  data() {
    return {
      map: null,
      ps: null,
      current: { lat: 37.5, lng: 127.039 },
      geocoder: null,
      isSelect: false,
      placeSelect: false,
      startMarker: null,
      recommendMarker: null,
      curIntroduceMarker: null,
      currCategory: "",
      markers: [],
      placeOverlay: null,
      isOveray: false,
      contentNode: null,
      isRecommend: false,
      currentMarker: null,
      polylines: [],
      minTimes: [],
      currentRecommendPlaceName: null,
      currentRecommendPlaceAddress: null,
      curRecommendX: null,
      stateTraffic: null,
      curRecommendY: null,
      curRecommendPlaceUrl: null,
      startMarkerImage: [
        require(`@/assets/images/icons/marker1.png`),
        require(`@/assets/images/icons/marker2.png`),
        require(`@/assets/images/icons/marker3.png`),
        require(`@/assets/images/icons/marker4.png`),
        require(`@/assets/images/icons/marker5.png`),
        require(`@/assets/images/icons/marker6.png`),
        require(`@/assets/images/icons/marker7.png`),
        require(`@/assets/images/icons/marker8.png`),
        require(`@/assets/images/icons/marker9.png`),
        require(`@/assets/images/icons/marker10.png`),
      ],
      loading: false,
    };
  },

  computed: {
    ...mapState("placeStore", ["placeX", "placeY", "placeName", "placeAddr"]),
    ...mapState("halfwayStore", ["startPlaces", "middlePlace"]),
    ...mapState("meetingStore", ["meeting_members"]),
  },

  watch: {
    minTimes() {
      if (this.minTimes.length == this.startPlaces.length) {
        this.loading = false;
      }
    },
    // 중간 위치 찾기 후 출발지 마커
    // 나중에 장소 추천지 있으면 여기에 추천장소 마커 추가해야함.
    middlePlace() {
      if (this.middlePlace != null) {
        this.resetPolylines();

        // 1. 검색한게 있으면 false 처리
        this.isSelect = false;
        this.isRecommend = true;
        // 2. 출발지 좌표 찍기
        var positions = [];
        var bounds = new window.kakao.maps.LatLngBounds();
        for (const startPlace of this.startPlaces) {
          var sp = new Map();
          sp.set("title", startPlace.get("name"));
          sp.set(
            "latlng",
            new window.kakao.maps.LatLng(
              startPlace.get("y"),
              startPlace.get("x")
            )
          );
          positions.push(sp);
          bounds.extend(
            new window.kakao.maps.LatLng(
              startPlace.get("y"),
              startPlace.get("x")
            )
          );
        }

        if (this.startMarker) this.startMarker.setMap(null);
        if (this.curIntroduceMarker) this.curIntroduceMarker.setMap(null);
        for (var i = 0; i < positions.length; i++) {
          const imageSrc = this.startMarkerImage[i], // 마커이미지의 주소입니다
            imageSize = new window.kakao.maps.Size(43, 48); // 마커이미지의 크기입니다

          const markerImage = new window.kakao.maps.MarkerImage(
            imageSrc,
            imageSize
          );

          this.startMarker = new window.kakao.maps.Marker({
            map: this.map, // 마커를 표시할 지도
            position: positions[i].get("latlng"), // 마커를 표시할 위치
            title: positions[i].get("title"), // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
            image: markerImage,
          });
        }
        this.map.setBounds(bounds);

        this.placeOverlay = new window.kakao.maps.CustomOverlay({ zIndex: 1 });
        this.contentNode = document.createElement("div"); // 커스텀 오버레이의 컨텐츠 엘리먼트 입니다
        this.contentNode.className = "placeinfo_wrap";
        this.placeOverlay.setContent(this.contentNode);

        this.ps = new window.kakao.maps.services.Places();

        this.placeOverlay.setMap(null);
        this.resetPolylines();
        this.currCategory = "SW8";
        // this.changeCategoryClass(e);
        this.searchPlaces();
      }
    },
  },

  mounted() {
    this.resetPolylines();

    if (window.kakao && window.kakao.maps.services) {
      // 카카오 객체가 있고, 카카오 맵 그릴 준비가 되어 있다면 맵 실행
      // console.log("loadMap", window.kakao.maps);
      this.loadMap();
    } else {
      // 없다면 카카오 스크립트 추가 후 맵 실행
      this.loadScript();
    }

    if (sessionStorage.getItem("findHalfwayModal") !== null) {
      this.$refs.halfway.openDialog();
    }
  },

  methods: {
    ...mapActions("placeStore", ["updatePlace"]),
    // 카테고리별 함수
    mapReload() {
      var bounds = new window.kakao.maps.LatLngBounds();
      for (var i = 0; i < this.startPlaces.length; i++) {
        bounds.extend(
          new window.kakao.maps.LatLng(
            this.startPlaces[i].get("y"),
            this.startPlaces[i].get("x")
          )
        );
      }
      this.map.setBounds(bounds);
      // var level = this.map.getLevel();
      // this.map.setLevel(level + 1);
      var center = this.map.getCenter();
      var projection = this.map.getProjection();
      var point = projection.pointFromCoords(center);
      point.y += 100;
      var newCenter = projection.coordsFromPoint(point);
      this.map.panTo(newCenter);
    },
    changeCategoryClass(el) {
      var category = document.getElementById("category"),
        children = category.children,
        i;
      for (i = 0; i < children.length; i++) {
        children[i].className = "";
      }
      if (el) {
        el.target.className = "on";
      }
    },
    onClickCategory(e) {
      if (e.target.id != this.currCategory) {
        var id = e.target.id;

        this.placeOverlay.setMap(null);
        this.resetPolylines();
        this.currCategory = id;
        this.searchPlaces();
      }
    },
    displayPlaceInfo(place) {
      var content =
        '<div class="placeinfo">' +
        '   <a class="title" href="' +
        place.place_url +
        '" target="_blank" title="' +
        place.place_name +
        '">' +
        place.place_name +
        "</a>" +
        `<div class="click">`;
      if (place.road_address_name) {
        content +=
          '    <span title="' +
          place.road_address_name +
          '">' +
          place.road_address_name +
          "</span>" +
          '  <span class="jibun" title="' +
          place.address_name +
          '">(지번 : ' +
          place.address_name +
          ")</span>";
      } else {
        content +=
          '    <span title="' +
          place.address_name +
          '">' +
          place.address_name +
          "</span>";
      }

      content +=
        '    <span class="tel">' +
        place.phone +
        "</span>" +
        "</div>" +
        `<div class="howto">
          <img id="bus-icon" src="${require("@/assets/images/icons/bus-icon.png")}"/>
          <img id="car-icon" src="${require("@/assets/images/icons/car-icon.png")}"/>
        </div> ` +
        "</div>" +
        '<div class="after"></div>';

      var self = this;
      this.contentNode.innerHTML = content;

      this.contentNode
        .querySelector("#bus-icon")
        .addEventListener("click", function (e) {
          e.preventDefault();
          self.findBusWay(place);
        });
      this.contentNode
        .querySelector("#car-icon")
        .addEventListener("click", function (e) {
          e.preventDefault();
          self.findCarWay(place);
        });
      this.placeOverlay.setContent(this.contentNode);

      this.placeOverlay.setPosition(
        new window.kakao.maps.LatLng(place.y, place.x)
      );
      this.placeOverlay.setMap(this.map);
    },

    recommendData(place) {
      const placeMap = new Map();
      placeMap.set("x", place.x);
      placeMap.set("y", place.y);
      placeMap.set("name", place.place_name);
      placeMap.set("addr", place.address_name);
      this.updatePlace(placeMap);
      this.$router.replace("/register");
    },

    removeMarker() {
      for (var i = 0; i < this.markers.length; i++) {
        this.markers[i].setMap(null);
      }
      this.markers = [];
    },

    addMarker(position) {
      var imageSrc =
          "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png", // 마커 이미지 url, 스프라이트 이미지를 씁니다
        imageSize = new window.kakao.maps.Size(24, 35), // 마커 이미지의 크기
        markerImage = new window.kakao.maps.MarkerImage(imageSrc, imageSize),
        marker = new window.kakao.maps.Marker({
          map: this.map,
          position: position, // 마커의 위치
          image: markerImage,
        });

      marker.setMap(this.map); // 지도 위에 마커를 표출합니다
      this.markers.push(marker); // 배열에 생성된 마커를 추가합니다

      return marker;
    },

    displayRecommendMarker(place) {
      let size = 0;

      if (place.length >= 3) {
        size = 3;
      } else {
        size = place.length;
      }
      var self = this;
      for (var i = 0; i < size; i++) {
        // 마커를 생성하고 지도에 표시합니다
        var marker = this.addMarker(
          new window.kakao.maps.LatLng(place[i].y, place[i].x)
        );

        // 마커와 검색결과 항목을 클릭 했을 때
        // 장소정보를 표출하도록 클릭 이벤트를 등록합니다
        (function (marker, p) {
          window.kakao.maps.event.addListener(marker, "click", function () {
            if (marker == self.currentMarker) {
              self.closeOveray();
            } else {
              self.isOveray = true;
              self.currentMarker = marker;
              self.displayPlaceInfo(p);
            }
          });
        })(marker, place[i]);
      }
    },

    closeOveray() {
      this.currentMarker = null;
      if (this.isOveray === true) {
        this.placeOverlay.setMap(null);
        this.isOveray = false;
      }
    },

    placesSearchCB(data, status) {
      if (status === window.kakao.maps.services.Status.OK) {
        // for (var i = 0; i < data.length; i++) {
        this.displayRecommendMarker(data);
        // }
      } else if (status === window.kakao.maps.services.Status.ZERO_RESULT) {
        // 검색결과가 없는경우 해야할 처리가 있다면 이곳에 작성해 주세요
      } else if (status === window.kakao.maps.services.Status.ERROR) {
        // 에러로 인해 검색결과가 나오지 않은 경우 해야할 처리가 있다면 이곳에 작성해 주세요
      }
    },

    searchPlaces() {
      if (!this.currCategory) {
        return;
      }
      // 커스텀 오버레이를 숨깁니다
      this.placeOverlay.setMap(null);
      // 지도에 표시되고 있는 마커를 제거합니다
      this.removeMarker();
      var center = new window.kakao.maps.LatLng(
        this.middlePlace.middleAvergeY,
        this.middlePlace.middleAvergeX
      );
      var radius = 5000;
      this.ps.categorySearch(this.currCategory, this.placesSearchCB, {
        location: center,
        radius: radius,
        useMapBounds: false,
      });
    },

    //[@method] 이동시간 및 경로 저장
    findCarWay(place) {
      this.stateTraffic = "car";
      this.resetPolylines();
      this.loading = true;
      this.currentRecommendPlaceName = place.place_name;
      this.currentRecommendPlaceAddress = place.road_address_name;
      this.curRecommendX = place.x;
      this.curRecommendY = place.y;
      this.curRecommendPlaceUrl = place.place_url;

      const REST_API_KEY = `${process.env.VUE_APP_KAKAO_CAR_WAY_KEY}`;
      const destination = `${place.x},${place.y}`; // 목적지

      this.startPlaces[0].get("x");
      const waypoints = "";
      const priority = "RECOMMEND";
      const car_fuel = "GASOLINE";
      const car_hipass = false;
      const alternatives = false;
      const road_details = false;

      // const placeCnt = this.startPlaces.length;

      // const car_routes = [];
      this.minTimes = [];
      Promise.all(
        this.startPlaces.map((element) => {
          const origin = element.get("x") + "," + element.get("y");
          return axios
            .get(`https://apis-navi.kakaomobility.com/v1/directions`, {
              params: {
                origin,
                destination,
                waypoints,
                priority,
                car_fuel,
                car_hipass,
                alternatives,
                road_details,
              },
              headers: {
                Authorization: `KakaoAK ${REST_API_KEY}`,
              },
            })
            .then((response) => {
              if (response.data.routes[0].result_code === 104) {
                return { car_route: [], minTime: 0 };
              }
              if (response.data.routes[0].result_code !== 0) {
                return { car_route: [], minTime: 1000 };
              }
              const guides = response.data.routes[0].sections[0].guides;
              const car_route = guides.map((element) => {
                return new window.kakao.maps.LatLng(
                  Number(element.y),
                  Number(element.x)
                );
              });
              const item = {
                car_route,
                minTime: Math.round(
                  response.data.routes[0].summary.duration / 60
                ),
                color: element.get("color"),
              };
              return item;
            })
            .catch((error) => {
              error;
              // console.error(error);
              return { car_route: [], minTime: 0 };
            });
        })
      ).then((results) => {
        const path_color = [
          "#32CD32",
          "#7B68EE",
          "#FFD700",
          "#4B0082",
          "#00CED1",
          "#FF00FF",
          "#87CEFA",
          "#6B8E23",
          "#9400D3",
          "#8B0000",
        ];
        const car_routes = [];
        const minTimes = [];
        for (let i = 0; i < results.length; i++) {
          const result = results[i];
          car_routes.push(result.car_route);
          minTimes.push(result.minTime);
          const polyline = new window.kakao.maps.Polyline({
            path: result.car_route,
            strokeWeight: 5,
            strokeColor: path_color[i],
            strokeOpacity: 1,
            strokeStyle: "solid",
          });
          this.polylines.push(polyline);
          polyline.setMap(this.map);
        }
        this.minTimes = minTimes;
      });

      this.closeOveray();
      this.mapReload();
      this.placeSelect = true;
    },

    async findBusWay(place) {
      this.resetPolylines();
      this.stateTraffic = "bus";
      this.loading = true;

      this.currentRecommendPlaceName = place.place_name;
      this.currentRecommendPlaceAddress = place.road_address_name;
      this.curRecommendX = place.x;
      this.curRecommendY = place.y;
      this.curRecommendPlaceUrl = place.place_url;

      this.minTimes = [];
      let maxTime = 99999999;
      let maxPayment = 99999999;
      let totalChange = 99999999;
      let idx = 0;

      for (var i = 0; i < this.startPlaces.length; i++) {
        const x = this.startPlaces[i].get("x");
        const y = this.startPlaces[i].get("y");
        const url = `https://api.odsay.com/v1/api/searchPubTransPathT?SX=${x}&SY=${y}&EX=${place.x}&EY=${place.y}&OPT=0&apiKey=${process.env.VUE_APP_ODSAY_KEY}`;
        idx = 0;

        await new Promise((resolve) => setTimeout(resolve, i * 40));

        try {
          const response = await axios.get(url);

          if (response.data["error"] != undefined) {
            if (response.data["error"]["code"] == -98) {
              this.minTimes.push(2);
              continue;
            } else {
              this.minTimes.push(2000);
              continue;
            }
          }

          if (response.data["result"]["searchType"] != 0) {
            let moveTime =
              response.data["result"]["path"][0]["info"]["totalTime"];
            let pathLen = response.data["result"]["path"][0]["subPath"].length;
            let sx = response.data["result"]["path"][0]["subPath"][0]["startX"];
            let sy = response.data["result"]["path"][0]["subPath"][0]["startY"];
            let ex =
              response.data["result"]["path"][0]["subPath"][pathLen - 1][
                "endX"
              ];
            let ey =
              response.data["result"]["path"][0]["subPath"][pathLen - 1][
                "endY"
              ];

            let linePath = [];
            for (let i = 0; i < pathLen; i++) {
              linePath.push(
                new window.kakao.maps.LatLng(
                  response.data["result"]["path"][0]["subPath"][i]["startY"],
                  response.data["result"]["path"][0]["subPath"][i]["startX"]
                )
              );
              linePath.push(
                new window.kakao.maps.LatLng(
                  response.data["result"]["path"][0]["subPath"][i]["endY"],
                  response.data["result"]["path"][0]["subPath"][i]["endX"]
                )
              );
            }
            // 지도에 도시간 이동 경로 생성
            let polyline = new window.kakao.maps.Polyline({
              map: this.map,
              path: linePath,
              strokeWeight: 5,
              strokeColor: "#ff0000 ",
              strokeOpacity: 1,
            });
            this.polylines.push(polyline);

            const startUrl = `https://api.odsay.com/v1/api/searchPubTransPathT?SX=${x}&SY=${y}&EX=${sx}&EY=${sy}&OPT=0&apiKey=${process.env.VUE_APP_ODSAY_KEY}`;
            const endUrl = `https://api.odsay.com/v1/api/searchPubTransPathT?SX=${ex}&SY=${ey}&EX=${place.x}&EY=${place.y}&OPT=0&apiKey=${process.env.VUE_APP_ODSAY_KEY}`;

            //출발지에서 출발 터미널 까지
            const startResponse = await axios.get(startUrl);
            if (
              startResponse.data["error"] == undefined ||
              (startResponse.data["error"]["code"] != -98 &&
                startResponse.data["error"]["code"] != 3)
            ) {
              moveTime +=
                startResponse.data["result"]["path"][0]["info"]["totalTime"];

              this.callMapObjApiAJAX(
                startResponse.data["result"]["path"][0].info.mapObj
              );
            } else {
              this.minTimes.push(2000);
              continue;
            }

            await new Promise((resolve) => setTimeout(resolve, 500));
            //도착 터미널에서 목적지 까지
            const endResponse = await axios.get(endUrl);
            if (
              endResponse.data["error"] == undefined ||
              (endResponse.data["error"]["code"] != -98 &&
                endResponse.data["error"]["code"] != 4)
            ) {
              moveTime +=
                endResponse.data["result"]["path"][0]["info"]["totalTime"];

              this.callMapObjApiAJAX(
                endResponse.data["result"]["path"][0].info.mapObj
              );
            } else {
              this.minTimes.push(2000);
              continue;
            }
            this.minTimes.push(moveTime);
          } else {
            maxTime = 99999999;
            maxPayment = 99999999;
            totalChange = 99999999;

            for (var j = 0; j < response.data["result"]["path"].length; j++) {
              if (
                maxTime >
                response.data["result"]["path"][j]["info"]["totalTime"]
              ) {
                idx = j;
                maxTime =
                  response.data["result"]["path"][j]["info"]["totalTime"];
                maxPayment =
                  response.data["result"]["path"][j]["info"]["payment"];
                totalChange =
                  response.data["result"]["path"][j]["info"][
                    "busTransitCount"
                  ] +
                  response.data["result"]["path"][j]["info"][
                    "subwayTransitCount"
                  ];
              } else if (
                maxTime ==
                response.data["result"]["path"][j]["info"]["totalTime"]
              ) {
                if (
                  maxPayment >
                  response.data["result"]["path"][j]["info"]["payment"]
                ) {
                  idx = j;
                  maxTime =
                    response.data["result"]["path"][j]["info"]["totalTime"];
                  maxPayment =
                    response.data["result"]["path"][j]["info"]["payment"];
                  totalChange =
                    response.data["result"]["path"][j]["info"][
                      "busTransitCount"
                    ] +
                    response.data["result"]["path"][j]["info"][
                      "subwayTransitCount"
                    ];
                } else if (
                  maxPayment ==
                  response.data["result"]["path"][j]["info"]["payment"]
                ) {
                  if (
                    totalChange >
                    response.data["result"]["path"][j]["info"][
                      "busTransitCount"
                    ] +
                      response.data["result"]["path"][j]["info"][
                        "subwayTransitCount"
                      ]
                  ) {
                    idx = j;
                    maxTime =
                      response.data["result"]["path"][j]["info"]["totalTime"];
                    maxPayment =
                      response.data["result"]["path"][j]["info"]["payment"];
                    totalChange =
                      response.data["result"]["path"][j]["info"][
                        "busTransitCount"
                      ] +
                      response.data["result"]["path"][j]["info"][
                        "subwayTransitCount"
                      ];
                  }
                }
              }
            }

            this.minTimes.push(maxTime);

            this.callMapObjApiAJAX(
              response.data["result"]["path"][idx].info.mapObj
            );
          }
        } catch (error) {
          error;
          // console.error(error);
        }
      }

      this.mapReload();
      this.placeSelect = true;
    },

    callMapObjApiAJAX(mabObj) {
      axios
        .get(
          `https://api.odsay.com/v1/api/loadLane?mapObject=0:0@` +
            mabObj +
            `&apiKey=${process.env.VUE_APP_ODSAY_KEY}`
        )
        .then((response) => {
          this.drawPolyLine(response.data);
        })
        .catch((error) => {
          error;
          // console.error(error);
        });
    },

    drawPolyLine(data) {
      let lineArray;
      this.closeOveray();
      for (let i = 0; i < data.result.lane.length; i++) {
        for (let j = 0; j < data.result.lane[i].section.length; j++) {
          lineArray = null;
          lineArray = new Array();
          for (
            let k = 0;
            k < data.result.lane[i].section[j].graphPos.length;
            k++
          ) {
            lineArray.push(
              new window.kakao.maps.LatLng(
                data.result.lane[i].section[j].graphPos[k].y,
                data.result.lane[i].section[j].graphPos[k].x
              )
            );
          }
          let polyline;
          //지하철결과의 경우 노선에 따른 라인색상 지정하는 부분 (1,2호선의 경우만 예로 들음)
          if (data.result.lane[i].type == 1) {
            polyline = new window.kakao.maps.Polyline({
              map: this.map,
              path: lineArray,
              strokeWeight: 8,
              storkeOpacity: 1,
              strokeColor: "#0D347F",
            });
          } else if (data.result.lane[i].type == 2) {
            polyline = new window.kakao.maps.Polyline({
              map: this.map,
              path: lineArray,
              strokeWeight: 8,
              storkeOpacity: 1,
              strokeColor: "#379206",
            });
          } else if (data.result.lane[i].type == 3) {
            polyline = new window.kakao.maps.Polyline({
              map: this.map,
              path: lineArray,
              strokeWeight: 8,
              storkeOpacity: 1,
              strokeColor: "#EC6C27",
            });
          } else if (data.result.lane[i].type == 4) {
            polyline = new window.kakao.maps.Polyline({
              map: this.map,
              path: lineArray,
              strokeWeight: 8,
              storkeOpacity: 1,
              strokeColor: "#3165A8",
            });
          } else if (data.result.lane[i].type == 5) {
            polyline = new window.kakao.maps.Polyline({
              map: this.map,
              path: lineArray,
              strokeWeight: 8,
              storkeOpacity: 1,
              strokeColor: "#703E8C",
            });
          } else if (data.result.lane[i].type == 6) {
            polyline = new window.kakao.maps.Polyline({
              map: this.map,
              path: lineArray,
              strokeWeight: 8,
              storkeOpacity: 1,
              strokeColor: "#904D23",
            });
          } else if (data.result.lane[i].type == 7) {
            polyline = new window.kakao.maps.Polyline({
              map: this.map,
              path: lineArray,
              strokeWeight: 8,
              storkeOpacity: 1,
              strokeColor: "#5B692E",
            });
          } else if (data.result.lane[i].type == 8) {
            polyline = new window.kakao.maps.Polyline({
              map: this.map,
              path: lineArray,
              strokeWeight: 8,
              storkeOpacity: 1,
              strokeColor: "#C82363",
            });
          } else if (data.result.lane[i].type == 9) {
            polyline = new window.kakao.maps.Polyline({
              map: this.map,
              path: lineArray,
              strokeWeight: 8,
              storkeOpacity: 1,
              strokeColor: "#B39627",
            });
          } else {
            polyline = new window.kakao.maps.Polyline({
              map: this.map,
              path: lineArray,
              strokeWeight: 8,
              storkeOpacity: 1,
            });
          }
          this.polylines.push(polyline);
        }
      }
    },

    resetPolylines() {
      for (let i = 0; i < this.polylines.length; i++) {
        this.polylines[i].setMap(null);
      }
      this.polylines = [];
    },
    //----------------------------------------------------------------------

    findHalfway() {
      sessionStorage.setItem("findHalfwayModal", true);
      this.closeOveray();
      this.$refs.halfway.openDialog();
    },

    moveRegisterPage() {
      const retrievedObject = sessionStorage.getItem("from");
      if (retrievedObject !== null) {
        const from = JSON.parse(retrievedObject);
        // this.$router.push("/Place");
        this.$router.replace(`/meeting/${from.id}`);
      } else {
        this.$router.replace("/register"); // register페이지
      }
    },

    goToPage(url) {
      this.$router.replace(url);
    },
    goBack() {
      this.$router.go(-1);
    },
    loadScript() {
      const script = document.createElement("script");
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&libraries=services&appkey=${process.env.VUE_APP_KAKAO_API_KEY}`;

      script.onload = () => window.kakao.maps.load(this.loadMap); // 스크립트 로드가 끝나면 지도를 실행될 준비가 되어 있다면 지도가 실행되도록 구현

      document.head.appendChild(script); // html>head 안에 스크립트 소스를 추가
    },
    loadMap() {
      const container = document.getElementById("map"); // 지도를 담을 DOM 영역
      if (container) {
        const options = {
          // 지도를 생성할 때 필요한 기본 옵션
          center: new window.kakao.maps.LatLng(
            this.current.lat,
            this.current.lng
          ), // 지도의 중심좌표
          level: 4, // 지도의 레벨(확대, 축소 정도)
        };

        this.map = new window.kakao.maps.Map(container, options); // 지도 생성 및 객체 리턴
        this.ps = new window.kakao.maps.services.Places();
        this.geocoder = new window.kakao.maps.services.Geocoder();

        if (this.placeX !== null) {
          this.isSelect = true;
          this.isRecommend = false;
        } else this.isSelect = false;
        if (this.placeX !== null) {
          // map의 marker를 다 지운다, 아래 displayMarker에서 뺴옴
          if (this.startMarker) this.startMarker.setMap(null);
          if (this.curIntroduceMarker) this.curIntroduceMarker.setMap(null);

          var bounds = new window.kakao.maps.LatLngBounds();
          bounds.extend(new window.kakao.maps.LatLng(this.placeY, this.placeX));
          this.current.lng = this.placeX;
          this.current.lat = this.placeY;
          this.displayMarker(this.placeY, this.placeX);
          // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
          this.map.setBounds(bounds);
          this.map.relayout();
          this.map.setCenter(
            new window.kakao.maps.LatLng(this.current.lat, this.current.lng)
          );
        }
      }
    },
    displayMarker(y, x) {
      if (this.curIntroduceMarker) this.curIntroduceMarker.setMap(null);
      // 마커를 생성하고 지도에 표시합니다
      this.curIntroduceMarker = new window.kakao.maps.Marker({
        map: this.map,
        position: new window.kakao.maps.LatLng(y, x),
      });
    },
  },
};
</script>

<style>
/* #category {
  position: absolute;

  top: 7.3%;
  left: 3%;
  border-radius: 20px;
  border: 1.6px solid #092a49;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.4);
  background: #fff;
  overflow: hidden;
  z-index: 2;
  list-style: none;
  padding: 0;
  margin: 0;
}
#category li {
  float: left;
  list-style: none;
  width: 40px;
  border-left: 1.6px solid #092a49;
  padding: 3.1%;
  text-align: center;
  margin-left: -1px;
  cursor: pointer;
}

#category li:hover {
  background: #2eccfa;
  border-left: 1px solid #092a49;
  margin-left: -1px;
} */

.placeinfo_wrap {
  position: absolute;
  bottom: 28px;
  left: -150px;
  width: 300px;
}
.placeinfo {
  position: relative;
  width: 100%;
  border-radius: 6px;
  border: 1px solid #ccc;
  border-bottom: 2px solid #ddd;
  /* padding-bottom: 10px; */
  background: #fff;
  min-height: 120px;
}
.placeinfo:nth-of-type(n) {
  border: 0;
  box-shadow: 0px 1px 2px #888;
}
.placeinfo_wrap .after {
  content: "";
  position: relative;
  margin-left: -12px;
  left: 50%;
  width: 22px;
  height: 12px;
  background: url("https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white.png");
}
.placeinfo a,
.placeinfo a:hover,
.placeinfo a:active {
  color: #fff;
  text-decoration: none;
}
.placeinfo a,
.placeinfo span {
  display: block;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}
.placeinfo .click {
  margin: 8px 10px;
}
.placeinfo span {
  /* margin: 7px 8px 0 10px; */
  cursor: default;
  font-size: 13px;
}
.placeinfo .title {
  /* font-weight: bold; */
  font-family: var(--bold-font) !important;
  font-size: 14px;
  border-radius: 6px 6px 0 0;
  margin: -1px -1px 0 -1px;
  padding: 8px 12px;
  color: white;
  background: var(--main-col-1);
  background: var(--main-col-1)
    url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/arrow_white.png)
    no-repeat right 14px center;
}
.placeinfo .tel {
  color: #0f7833;
}
.placeinfo .jibun {
  color: #999;
  font-size: 11px;
  margin-top: 0;
}
.howto {
  position: absolute;
  left: 71%;
  bottom: 0;
}
.howto #car-icon {
  width: 35px;
  margin-left: 10%;
  /* margin-top: 8%; */
  /* border: 4px solid var(--main-col-1); */
  border-radius: 50%;
  /* box-shadow: 0px 0px 5px 0px var(--main-col-1); */
}
.howto #bus-icon {
  width: 35px;
  /* margin-top: 8%; */
  /* border: 2px solid var(--main-col-1); */
  border-radius: 50%;
  /* box-shadow: 0px 0px 5px 0px var(--main-col-1); */
}
</style>
<style scoped>
/* .marker-icon {
  font-size: 25px;
  margin-right: 4%;
  margin-left: -8%;
  color: #092a49;
} */
.category_icon {
  z-index: 100;
  color: #092a49;
}
/* .place-info {
  z-index: 2;
  position: absolute;
  bottom: 5%;
  left: 8.5%;
} */
.middle-place-info {
  /* z-index: 50;
  position: absolute;
  bottom: 10%;
  left: 8.5%; */
}
/* input {
  padding-left: 10px;
  font-family: var(--medium-font);
} */
/* .find-place-btn {
  box-sizing: border-box;
  position: absolute;
  z-index: 2;
  right: 5%;
  top: 7.5%;
  font-family: var(--extrabold-font);
  background: #ffffff;
  border: 1.6px solid #092a49;
  box-shadow: 0px 4px 10px rgba(9, 42, 73, 0.25);
  border-radius: 18px;
} */
.map-area {
  width: 100%;
  height: 100%;
  display: flex; /* 추가 */
  flex-direction: column; /* 추가 */
  justify-content: center; /* 추가 */
  align-items: center; /* 추가 */
}
.maps {
  width: 100%;
  height: 100%;
}
/* .search-box {
  box-sizing: border-box;
  z-index: 2;
  display: flex;
  position: absolute;
  margin-left: 5%;
  width: 80%;
  height: 37px;
  left: 10%;
  right: 10%;
  top: 1.7%;
  bottom: unset;

  background: #ffffff;
  border: 1.6px solid #092a49;
  box-shadow: 0px 4px 10px rgba(9, 42, 73, 0.25);
  border-radius: 10px;
} */
/* .find-middle-place-btn {
  color: #092a49;
} */
/* .find-middle-place-title {
  color: #092a49;
  font-family: var(--bold-font);
} */
</style>
