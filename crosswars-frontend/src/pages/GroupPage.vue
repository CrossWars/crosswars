<template lang="">
<div>
  <q-inner-loading
      :showing="showLoading"
      color: primary/>
  <div class="q-ma-lg"
      v-show="showGroupPage">
      <h3 class=text-capitalize>
          {{groupName}}
      </h3>
  </div>
</div>
</template>
<script lang="ts">
import {defineComponent} from 'vue'

import {Group} from '../models/Groups/groups'

export default defineComponent({
  name: 'GroupPage',
  components: {
  },
  data() {
    return {
      groupName: new String,
      user_name: 'testdude',
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
    void this.getGroupInfo();
  },
  methods: {
    async getGroupInfo()
    {
        await this.$api.get(`/groups/ids?group_id=${this.$route.params.groupID as string}`).then((result) => {
            this.groupName = (result.data as Group).name
            this.showGroupPage = true;
            this.showLoading = false;
            void this.getGroupEntries();
        });
    },
    async getGroupEntries()
    {
        //pass
    }
  },
});
</script>
<style scoped>
    .capitalize {
        text-transform: capitalize;
    }
</style>