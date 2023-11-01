<template>
  <v-btn
    class="mr-1"
    color="var(--main-col-1)"
    @click="moveChatting"
    text
    rounded
    icon
    large
  >
    <div class="d-flex flex-column align-center">
      <v-icon large style="font-size: 33px; height: 33px; width: 33px">
        $vuetify.icons.chatting_outline
      </v-icon>
      <span class="mt-1 xxxxs-font">채팅</span>
    </div>
  </v-btn>
</template>

<script>
export default {
  name: "MoveChattingBtn",
  methods: {
    moveChatting() {
      this.$router.push(`/chat/${this.$route.params.id}`);
    },
  },
};
</script>

<style></style>
