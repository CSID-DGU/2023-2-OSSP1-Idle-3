<template>
  <v-sheet
    height="100%"
    color="transparent"
    class="d-flex flex-column align-center justify-center"
  >
    <v-sheet
      max-height="700"
      height="80%"
      color="transparent"
      class="login narrow-font d-flex flex-column justify-space-between align-center hugelarge-font main-col-1"
    >
      <div class="logo-font d-flex flex-column">
        <span>Almost</span>
        <span>There!</span>
      </div>
      <div class="d-flex justify-center align-center login-modal">
        <div
          class="sm-font medium-font main-col-1 d-flex flex-column align-center justify-center"
          style="position: relative"
        >
          <div class="mb-4 d-flex flex-column align-center justify-center">
            <span class="mb-1">친구들과 약속을 잡고</span>
            <!-- <br /> -->
            <span>서로의 위치를 공유해 보세요!</span>
          </div>
          <div class="d-flex flex-column align-center justify-center">
            <span class="mb-1">만남 장소 추천부터 정산 기능까지</span>
            <!-- <br /> -->
            <span>더 편하게 모임을 즐겨보세요!</span>
          </div>
          <!-- <br /> -->
          <!-- <br /> -->

          <v-btn
            elevation="5"
            color="var(--main-col-1)"
            dark
            rounded
            block
            @click="kakaoLogin"
            >카카오로 시작하기</v-btn
          >
        </div>
      </div>
    </v-sheet>
  </v-sheet>
</template>

<script>
export default {
  name: "user_login",
  methods: {
    kakaoLogin() {
      // console.log(`${process.env.VUE_APP_KAKAO_LOGIN_URL}`);
      window.location.replace(`${process.env.VUE_APP_KAKAO_LOGIN_URL}`);
    },
  },
};
</script>

<style scoped>
.logo-font {
}
.login-modal {
  position: relative;
  height: 32vh;
  width: 80vw;
  max-width: 300px;
  background: radial-gradient(
    27.95% 27.95% at 46.91% 89.9%,
    rgba(255, 255, 255, 0.0495679) 0%,
    rgba(255, 255, 255, 0.3) 100%
  ) !important;
  box-shadow: 0px 4px 50px -10px rgba(0, 0, 0, 0.25);
  backdrop-filter: blur(50px);
  border-radius: 30px;
}
.login {
  background-image: url("@/assets/images/page/login.png");
  background-size: 30vh;
  background-position-x: center;
  background-position-y: 65%;
}

#text {
  position: absolute;
  text-align: center;
  top: 25%;
  width: auto;
}

#text > span {
  font-family: var(--regular-font);
  line-height: 18px;
}

.v-btn {
  margin-top: 10%;
  align-self: center;
  width: 213px;
}
.v-sheet {
  /* position: relative; */
}
.logo-font {
  animation: jump 1s infinite;
}

@keyframes jump {
  0% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
  100% {
    transform: translateY(0);
  }
}
</style>
