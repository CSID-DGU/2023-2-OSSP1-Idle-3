import The404Error from "@/views/The404Error.vue";
import NavHeader from "@/views/Header/NavHeader.vue"

const error = [
  {
    path: "/:pathMatch(.*)*",
    name: "the404Error",
    components: {
      header: NavHeader,
      default: The404Error,
    },
  },
];

export default error;