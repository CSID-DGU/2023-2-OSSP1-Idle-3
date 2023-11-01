import ManualPage from "@/components/ManualPage/ManualPage.vue";
// import NavHeader from "@/views/Header/NavHeader.vue";

const banner = [
  {
    path: "/manual",
    name: "manual",
    components: {
      // header: NavHeader,
      default: ManualPage,
    },
  },
];

export default banner;
