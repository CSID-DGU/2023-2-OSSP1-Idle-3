<template>
  <div>
    <div v-if="loading">
      <my-page-loading></my-page-loading>
    </div>
    <div v-else>
      <img
        class="my-page-image"
        src="@/assets/images/page/mypage.png"
        width="30%"
      />
      <member-profile
        :memberProfileImg="memberProfileImg"
        :memberNickname="memberNickname"
      />
      <member-summary />
      <member-calendar />
      <!-- nav 자리 -->
      <v-sheet height="56px"></v-sheet>
    </div>
  </div>
</template>

<script>
import { mapActions, mapState } from "vuex";
import MemberProfile from "./element/MemberProfile.vue";
import MemberSummary from "./element/MemberSummary.vue";
import MemberCalendar from "./element/MemberCalendar.vue";
import MyPageLoading from "./MyPageLoading.vue";
import { getLoginMember } from "@/api/modules/member";

export default {
  name: "MyPage",
  components: { MemberProfile, MemberSummary, MemberCalendar, MyPageLoading },
  data() {
    return {
      loading: true,
      memberNickname: null,
      memberProfileImg: null,
    };
  },
  created() {
    this.loading = true;
    // [@Method] 회원정보 조회
    getLoginMember(
      this.member_id,
      (res) => {
        // console.log("#마이페이지 정보 조회", res);
        if (res.data.statusCode == 200) {
          this.memberProfileImg = res.data.data.memberProfileImg;
          this.memberNickname = res.data.data.memberNickname;
          this.excuteGetMemberInfo().then((res) => {
            if (res) {
              this.loading = false;
            }
          });
        }
      },
      () => {}
    );
  },
  computed: {
    ...mapState("memberStore", ["member_id"]),
  },
  methods: {
    ...mapActions("mypageStore", ["excuteGetMemberInfo"]),
    ...mapActions("memberStore", ["isLogin"]),
  },
};
</script>

<style scoped>
.my-page-image {
  position: absolute;
  z-index: 2;
  right: 5%;
}
</style>
