<template lang="">
<q-inner-loading
    :showing="showLoading"
    color: primary/>
<div class="q-ma-lg"
    v-show="showGroupPage">
    <h3 class=text-capitalize>
        {{groupName}}
    </h3>
</div>

</template>
<script>
import EntryList from "components/EntryList.vue";

export default {
  name: "Group",
  components: {
  },
  data() {
    return {
      groupName: new String,
      user_name: "testdude",
      groupIdToGroup: new Map(),
      userIdToUser: new Map(),
      groupToEntries: new Map(),
      userIdToLeaderboardEntry: new Map(),
      submitLoading: false,
      showLoading: true,
      showGroupPage: false,
    };
  },
  computed: {
  },
  mounted() {
    this.getGroupInfo();
  },
  methods: {
    async getGroupInfo()
    {
        this.$api.get(`/groups/ids?group_id=${this.$route.params.groupID}`).then((result) => {
            this.groupName = result.data.name;
            this.showGroupPage = true;
            this.showLoading = false;
            this.getGroupEntries();
        });
    },
    async getGroupEntries()
    {
        //pass
    }
  },
};
</script>
<style scoped>
    .capitalize {
        text-transform: capitalize;
    }
</style>