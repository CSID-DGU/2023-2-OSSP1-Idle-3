<template>
  <v-dialog v-model="dialog" max-width="300" rounded="xl" persistent>
    <v-card rounded="xl" class="pa-2">
      <v-card-title class="d-flex flex-column">
        <v-progress-circular
          :size="70"
          :width="7"
          color="var(--main-col-1)"
          class="mb-5 loading"
          indeterminate
        ></v-progress-circular>
        <div
          class="point-font xxl-font d-flex flex-column align-center semi2narrow-font main-col-1"
        >
          <!-- <div class="find-place-text">중간 장소를 찾고있습니다.</div> -->
          <span>중간 장소를</span>
          <span>찾고 있습니다.</span>
        </div>
      </v-card-title>
    </v-card>
  </v-dialog>
</template>

<script>
// import CloseButton from "@/common/component/button/CloseButton.vue";
export default {
  name: "InternetError",
  //   components: { CloseButton },
  data() {
    return {
      dialog: true,
    };
  },
  methods: {
    openDialog() {
      this.dialog = true;
    },
    closeDialog() {
      this.dialog = false;
    },
    goBack() {
      this.$router.go(-1);
    },
  },
};
</script>

<style scoped>
/* .loading {
  margin-top: 10%;
}
.find-place-text {
  font-family: var(--extrabold-font);
  font-size: 20px;
  margin-top: 20%;
} */
</style>
