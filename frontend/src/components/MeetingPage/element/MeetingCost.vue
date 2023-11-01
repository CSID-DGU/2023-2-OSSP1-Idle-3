<template>
  <v-sheet class="mx-3 my-2 d-flex flex-column main-col-1" color="transparent">
    <div class="d-flex flex-row justify-space-between">
      <span class="point-font xxxxl-font">정산</span>
      <v-btn
        class="ml-2 justify-space-between"
        depressed
        small
        color="var(--main-col-1)"
        dark
        rounded
        @click="sendkakao"
      >
        <v-icon class="mr-2" color="white" small
          >mdi-share-variant-outline</v-icon
        >
        <span class="xxxs-font">카카오톡 공유하기</span>
      </v-btn>
    </div>
    <v-simple-table dense>
      <template v-slot:default>
        <tbody>
          <tr
            v-for="(calculate, index) in calculateDetails"
            :key="index"
            class="d-flex flex-row justify-space-between align-center"
            @click="showDetailModel(calculate)"
            style="cursor: pointer"
          >
            <td
              class="px-1 d-flex align-center light-font main-col-1"
              style="border-bottom: none"
            >
              {{ calculate.storeName }}
            </td>
            <td
              class="px-1 d-flex align-center light-font main-col-1"
              style="border-bottom: none"
            >
              {{
                calculate.price
                  .toString()
                  .replace(/\B(?=(\d{3})+(?!\d))/g, ",")
              }}원
            </td>
          </tr>
        </tbody>
      </template>
    </v-simple-table>
    <meeting-cost-detail ref="detail"></meeting-cost-detail>
    <v-divider class="mb-1"></v-divider>
    <div class="d-flex flex-row justify-space-between">
      <span class="px-1 pb-1 medium-font sm-font">합계</span>
      <span class="px-1 pb-1 medium-font sm-font"
        >{{ String(total).replace(/\B(?=(\d{3})+(?!\d))/g, ",") }}원</span
      >
    </div>
    <div class="d-flex flex-row justify-space-between">
      <span class="px-1 pb-1 medium-font sm-font">잔액</span>
      <span class="px-1 pb-1 medium-font sm-font"
        >{{ String(remain).replace(/\B(?=(\d{3})+(?!\d))/g, ",") }}원</span
      >
    </div>
    <div class="d-flex flex-row justify-space-between">
      <span class="px-1 light-font sm-font">지각비</span>
      <span class="px-1 light-font sm-font"
        >{{ String(lateTotal).replace(/\B(?=(\d{3})+(?!\d))/g, ",") }}원</span
      >
    </div>
    <v-divider class="my-1"></v-divider>
    <div class="d-flex flex-row justify-space-between">
      <span class="px-1 bold-font sm-font">내가 내야 하는 금액</span>
      <span class="px-1 bold-font sm-font"
        >{{ String(spentMoney).replace(/\B(?=(\d{3})+(?!\d))/g, ",") }}원</span
      >
    </div>
    <v-btn class="my-3" color="var(--main-col-1)" dark rounded @click="open()">
      영수증 추가
    </v-btn>

    <!-- max-height="90% !important"
      height="fit-content !important" -->
    <vue-bottom-sheet ref="costSheet" max-width="500px" :is-full-screen="true">
      <v-sheet class="px-5 d-flex flex-column pb-10">
        <span class="point-font xxxxl-font main-col-1 align-self-center">
          영수증 등록
        </span>
        <v-form v-model="valid" ref="forms" lazy-validation>
          <v-file-input
            v-model="receipt"
            class="pt-5"
            accept="image/png, image/jpeg, image/jpg"
            placeholder="영수증 사진을 첨부해 주세요."
            prepend-icon="mdi-camera"
            :rules="[
              (v) =>
                (!!v && v.size <= 10000000) ||
                '첨부 파일 크기는 최대 10MB까지만 가능합니다.',
              (v) =>
                (!!v &&
                  ['image/png', 'image/jpeg', 'image/jpeg'].includes(v.type)) ||
                'png, jpg, jpeg 파일만 첨부 가능합니다.',
            ]"
          ></v-file-input>
        </v-form>

        <v-sheet
          v-if="imageLoading"
          class="mt-5 mb-5 d-flex flex-column justify-center align-center"
          height="300px"
        >
          <v-progress-circular
            :size="40"
            color="var(--main-col-1)"
            indeterminate
            class="mb-4"
          ></v-progress-circular>
          <span class="point-font main-col-1 lg-font">영수증 정보 읽는 중</span>
        </v-sheet>
        <div v-else>
          <div
            v-if="
              !!receipt &&
              receipt.size <= 10000000 &&
              ['image/png', 'image/jpeg', 'image/jpeg'].includes(receipt.type)
            "
            class="d-flex justify-center align-center"
          >
            <img :src="imageUrl" width="80%" max-height="10%" />
          </div>
          <div
            v-if="!ocrSuccess"
            class="my-3 d-flex flex-column justify-center align-center"
          >
            <span class="point-font main-col-1 lg-font"
              >사진 정보 읽기에 실패하였습니다.</span
            >
            <span class="point-font main-col-1 lg-font"
              >다시 시도하거나 아래 정보를 입력해 주세요!</span
            >
          </div>
          <div>
            <v-row class="ma-0 d-flex align-center">
              <v-col
                cols="2"
                class="d-flex flex-row justify-space-between medium-font main-col-1"
              >
                <span>상</span>
                <span>호</span>
                <span>명</span>
              </v-col>
              <v-col>
                <v-text-field
                  v-model="storeName"
                  type="text"
                  placeholder="상호명"
                  outlined
                  hide-details
                  dense
                ></v-text-field>
              </v-col>
            </v-row>
            <v-row class="mb-1 ma-0 d-flex align-center">
              <v-col
                cols="2"
                class="d-flex flex-row justify-space-between medium-font main-col-1"
              >
                <span>총</span>
                <span>액</span>
              </v-col>
              <v-col>
                <v-text-field
                  v-model="totalPrice"
                  type="number"
                  placeholder="총액"
                  outlined
                  hide-details
                  dense
                ></v-text-field>
              </v-col>
            </v-row>
            <div class="d-flex flex-column">
              <v-btn
                class="mb-5"
                color="var(--main-col-1)"
                rounded
                dark
                @click="addCalculateDetail()"
                >등록</v-btn
              >
            </div>
          </div>
        </div>
      </v-sheet>
    </vue-bottom-sheet>
  </v-sheet>
</template>
<script
  src="https://t1.kakaocdn.net/kakao_js_sdk/2.1.0/kakao.min.js"
  integrity="sha384-dpu02ieKC6NUeKFoGMOKz6102CLEWi9+5RQjWSV0ikYSFFd8M3Wp2reIcquJOemx"
  crossorigin="anonymous"
></script>

<script>
import MeetingCostDetail from "./MeetingCostDetail.vue";
import { postReceiptInfo, saveCalculateDetail } from "@/api/modules/meeting.js";

export default {
  name: "MeetingCost",
  props: {
    meetingId: Number,
    meetingName: String,
    calculateDetails: Array,
    lateTotal: Number,
    spentMoney: Number,
    total: Number,
    remain: Number,
  },
  data() {
    return {
      valid: true,
      imageLoading: false,
      ocrSuccess: true,
      receipt: null,
      storeName: null,
      totalPrice: 0,
    };
  },
  methods: {
    open() {
      this.$refs.costSheet.open();
    },
    showDetailModel(calculate) {
      this.$refs.detail.changeCalculate(calculate);
      this.$refs.detail.openDialog();
    },
    async addCalculateDetail() {
      // if (this.ocrSuccess && this.receipt) {
      // const { valid } = await this.$refs.forms.validate();
      await this.$refs.forms.validate();
      if (this.valid && this.receipt) {
        saveCalculateDetail(
          this.meetingId,
          this.receipt,
          this.storeName,
          this.totalPrice
        ).then((res) => {
          if (res) this.$router.go(this.$router.currentRoute);
        });
      }
    },
    sendkakao() {
      if (!Kakao.isInitialized()) {
        Kakao.init(`${process.env.VUE_APP_KAKAO_API_KEY}`);
      }
      Kakao.Share.sendDefault({
        objectType: "feed",
        content: {
          title: "정산 내역을 확인하여 정산을 완료해주세요!",
          description:
            "지각 여부에 따라 금액이 달라질 수 있습니다.\n자세한 정산 내역은 링크를 통해 확인해주세요.",
          imageUrl:
            "https://almostthere.co.kr/almostthere/images/calculate.jpg",
          link: {},
        },
        itemContent: {
          profileText: "모임 정산 내역을 안내드립니다.",
          items: [
            {
              item: "모임 이름",
              itemOp: this.meetingName,
            },
            {
              item: "합계",
              itemOp:
                String(this.total).replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원",
            },
            {
              item: "잔액",
              itemOp:
                String(this.remain).replace(/\B(?=(\d{3})+(?!\d))/g, ",") +
                "원",
            },
            {
              item: "지각비",
              itemOp:
                String(this.lateTotal).replace(/\B(?=(\d{3})+(?!\d))/g, ",") +
                "원",
            },
          ],
          sum: "나의 정산 금액",
          sumOp:
            String(this.spentMoney).replace(/\B(?=(\d{3})+(?!\d))/g, ",") +
            "원",
        },
        buttons: [
          {
            title: "정산내역 확인하러 가기",
            link: {
              mobileWebUrl: `https://almostthere.co.kr/meeting/${this.meetingId}`,
              webUrl: `https://almostthere.co.kr/meeting/${this.meetingId}`,
            },
          },
        ],
      });
    },
  },
  components: {
    MeetingCostDetail,
  },
  computed: {
    imageUrl() {
      if (this.receipt != null) {
        return URL.createObjectURL(this.receipt);
      } else {
        return null;
      }
    },
  },
  watch: {
    async receipt() {
      await this.$refs.forms.validate();
      if (this.valid && this.receipt != null) {
        this.imageLoading = true;
        this.ocrSuccess = await true;
        if (this.receipt != null) {
          postReceiptInfo(this.receipt).then(async (res) => {
            if (res != null) {
              this.storeName = await res.storeName;
              this.totalPrice = await res.totalPrice;
              this.imageLoading = await false;
              this.ocrSuccess = await true;
            } else {
              this.imageLoading = await false;
              this.ocrSuccess = await false;
            }
          });
        }
      } else {
        this.ocrSuccess = true;
      }
    },
  },
};
</script>

<style></style>
