<template>
  <v-sheet class="mx-3 my-2 d-flex flex-column" color="transparent">
    <v-btn
      elevation="0"
      outlined
      color="var(--main-col-1)"
      rounded
      @click="open()"
    >
      <img class="mr-1" height="25" src="@/assets/images/icons/roulette.png" />
      <span class="medium-font">돌려돌려 돌림판</span>
    </v-btn>
    <vue-bottom-sheet ref="gameSheet" max-width="500px" :is-full-screen="true">
      <v-sheet class="px-5 d-flex flex-column pb-10">
        <v-btn text rounded v-on:click="spin">
          <span class="point-font xxxxl-font main-col-1 align-self-center">
            Click Here !
          </span>
        </v-btn>
        <section class="section d-flex flex-column align-center justify-center">
          <div class="container" id="app">
            <div class="columns">
              <div class="column">
                <div class="columns is-centered"></div>
                <div class="d-flex flex-column align-center justify-center">
                  <canvas id="canvas" width="300" height="300"> </canvas>
                </div>
              </div>
              <div class="d-flex flex-column align-center justify-center">
                <v-sheet
                  class="d-flex flex-column justify-center detail-border main-col-1"
                  height="50"
                  width="100%"
                  rounded="lg"
                  elevation="3"
                >
                  <div class="d-flex flex-row justify-space-between">
                    <img
                      class="ml-1"
                      src="@/assets/images/icons/congratuation.png"
                      height="30"
                    /><span
                      class="point-font xxxl-font main-col-1 align-self-center"
                      >{{ result }}</span
                    ><img
                      class="mr-1"
                      src="@/assets/images/icons/congratuation.png"
                      height="30"
                      style="transform: scaleX(-1)"
                    />
                  </div>
                </v-sheet>
              </div>
              <div class="mt-4">
                <!-- <v-sheet class="px-2"> -->
                <!-- <div class="columns is-multiline"> -->
                <!-- <div class="d-flex flex-row justify-space-between"> -->
                <!-- <v-responsive max-width="200"> -->
                <v-text-field
                  v-model="new_option"
                  v-on:keyup.enter="addOptions"
                  type="text"
                  append-icon="$vuetify.icons.add_circle"
                  @click:append="addOptions"
                  placeholder="항목"
                  outlined
                  hide-details
                  dense
                  maxlength="7"
                  block
                ></v-text-field>
                <v-row class="ma-0 pa-0 mt-2">
                  <v-col class="ma-0 pa-0">
                    <v-btn
                      color="var(--main-col-1)"
                      small
                      dark
                      width="98%"
                      elevation="0"
                      @click="pushMember()"
                    >
                      모임 멤버 추가하기
                    </v-btn>
                  </v-col>
                  <v-col class="ma-0 pa-0">
                    <v-btn
                      color="var(--main-col-1)"
                      small
                      dark
                      block
                      width="98%"
                      outlined
                      elevation="0"
                      @click="removeAllOptions()"
                    >
                      전체 항목 삭제하기
                    </v-btn>
                  </v-col>
                </v-row>

                <!-- </v-responsive> -->
                <!-- <v-btn
                        id="round-btn"
                        outlined
                        color="var(--main-col-1)"
                        v-on:click="addOptions"
                        icon
                      >
                        <v-icon color="var(--main-col-1)"
                          >$vuetify.icons.add</v-icon
                        >
                      </v-btn> -->
                <!-- </div> -->
                <div class="mt-2 d-flex flex-wrap justify-space-between">
                  <div
                    class="mt-2"
                    v-for="option in options"
                    :key="option"
                    style="width: 48%"
                  >
                    <v-btn
                      class="px-3 py-4 d-flex flex-row justify-space-between"
                      small
                      color="white"
                      v-on:click="removeOptions(option)"
                      width="100%"
                    >
                      <v-icon color="var(--main-col-1)">
                        mdi-minus-circle
                      </v-icon>
                      <span class="main-col-1"> {{ option }} </span>
                    </v-btn>
                  </div>
                </div>
                <!-- </div> -->
                <!-- </v-sheet> -->
              </div>
            </div>
          </div>
        </section>
      </v-sheet>
    </vue-bottom-sheet>
  </v-sheet>
</template>

<script>
export default {
  name: "MeetingGame",
  data() {
    return {
      options: ["꽝"],
      new_option: "",
      startAngle: 0,
      startAngleStart: 0,
      spinTimeout: null,
      spinArcStart: 10,
      spinTime: 0,
      spinTimeTotal: 0,
      result: "",
      ctx: "",
    };
  },
  props: {
    memberList: Array,
  },
  // mounted() {
  //   this.$refs.gameSheet.open();
  // },
  computed: {
    arc() {
      return Math.PI / (this.options.length / 2);
    },
  },
  methods: {
    open() {
      this.result = "";
      this.options = ["꽝"];
      // console.log("마운트");
      this.drawRouletteWheel();
      this.$refs.gameSheet.open();
    },
    byte2Hex(n) {
      var nybHexString = "0123456789ABCDEF";
      return (
        String(nybHexString.substr((n >> 4) & 0x0f, 1)) +
        nybHexString.substr(n & 0x0f, 1)
      );
    },

    RGB2Color(r, g, b) {
      return "#" + this.byte2Hex(r) + this.byte2Hex(g) + this.byte2Hex(b);
    },

    getColor(item, maxitem) {
      var phase = 0;
      var center = 128;
      var width = 127;
      var frequency = (Math.PI * 2) / maxitem;

      var red = Math.sin(frequency * item + 2 + phase) * width + center;
      var green = Math.sin(frequency * item + 0 + phase) * width + center;
      var blue = Math.sin(frequency * item + 3 + phase) * width + center;

      return this.RGB2Color(red, green, blue);
    },

    addOptions() {
      if (this.options.length == 10) {
        return;
      }
      this.options.push(this.new_option);
      this.new_option = "";
      this.drawRouletteWheel();
    },
    removeOptions(option) {
      let idx = this.options.indexOf(option) || 0;
      this.options.splice(idx, 1);
      this.drawRouletteWheel();
    },
    removeAllOptions() {
      this.options = [];
      this.drawRouletteWheel();
    },
    pushMember() {
      for (var member of this.memberList) {
        if (this.options.length == 10) break;
        if (!this.options.includes(member.memberNickname)) {
          this.options.push(member.memberNickname);
        }
      }
      this.drawRouletteWheel();
    },
    drawRouletteWheel() {
      var canvas = document.getElementById("canvas");
      if (canvas.getContext) {
        var outsideRadius = 120.5;
        var textRadius = 96;
        var insideRadius = 50;

        this.ctx = canvas.getContext("2d");
        this.ctx.clearRect(0, 0, 300, 300);

        this.ctx.strokeStyle = "#D9D9D9";
        this.ctx.lineWidth = 5;

        this.ctx.font = "16px Pretendard-Regular-Retina";

        for (var i = 0; i < this.options.length; i++) {
          var angle = this.startAngle + i * this.arc;
          this.ctx.fillStyle = this.getColor(i, this.options.length);

          this.ctx.beginPath();
          this.ctx.arc(150, 150, outsideRadius, angle, angle + this.arc, false);
          this.ctx.arc(150, 150, insideRadius, angle + this.arc, angle, true);
          this.ctx.stroke();
          this.ctx.fill();

          this.ctx.save();
          this.ctx.fillStyle = "white";
          this.ctx.translate(
            150 + Math.cos(angle + this.arc / 2) * textRadius,
            150 + Math.sin(angle + this.arc / 2) * textRadius
          );
          this.ctx.rotate(angle + this.arc / 2 + Math.PI / 2);
          var text = this.options[i];
          this.ctx.fillText(text, -this.ctx.measureText(text).width / 2, 0);
          this.ctx.restore();
        }

        //Arrow
        // console.log(outsideRadius);
        this.ctx.fillStyle = "#092a49";
        this.ctx.beginPath();
        this.ctx.moveTo(150 - 8, 150 - (outsideRadius + 4)); // 146, 25
        this.ctx.lineTo(150 + 16, 150 - (outsideRadius + 4)); // 154, 25
        this.ctx.lineTo(150 + 4, 150 - (outsideRadius - 8)); // 154, 35
        this.ctx.fill();
      }
    },

    spin() {
      this.spinAngleStart = Math.random() * 10 + 20;
      this.spinTime = 0;
      this.spinTimeTotal = Math.random() * 3 + 5 * 3000;
      this.rotateWheel();
    },

    rotateWheel() {
      this.spinTime += 30;
      if (this.spinTime >= this.spinTimeTotal) {
        this.stopRotateWheel();
        return;
      }
      var spinAngle =
        this.spinAngleStart -
        this.easeOut(this.spinTime, 0, this.spinAngleStart, this.spinTimeTotal);
      this.startAngle += (spinAngle * Math.PI) / 180;
      this.drawRouletteWheel();

      let _this = this;
      this.spinTimeout = setTimeout(function () {
        _this.rotateWheel();
      }, 10);
    },

    stopRotateWheel() {
      clearTimeout(this.spinTimeout);
      var degrees = (this.startAngle * 180) / Math.PI + 90;
      var arcd = (this.arc * 180) / Math.PI;
      var index = Math.floor((360 - (degrees % 360)) / arcd);
      this.ctx.save();
      // this.ctx.font = "bold 25px Helvetica, Arial";
      var text = this.options[index];
      this.result = text;
      this.ctx.restore();
    },

    easeOut(t, b, c, d) {
      var ts = (t /= d) * t;
      var tc = ts * t;
      return b + c * (tc + -3 * ts + 3 * t);
    },
  },
};
</script>

<style></style>
