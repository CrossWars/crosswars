<template lang="">
<div>
  <q-inner-loading
      :showing="showLoading"
      color: primary/>
  <div class="q-ma-sm"
      v-show="showGroupPage">
      <h4 class=text-capitalize>
          {{group.name}}
      </h4>
  </div>
    <GroupDailyBarChart :entries="dailyLeaderboardEntries"/>
  
  <div class="q-pa-md">
      <q-card>
        <q-expansion-item
        default-opened
        icon="leaderboard"
        label="Today's leaderboard">
          <EntryList :entries="dailyLeaderboardEntries"/>
        </q-expansion-item>
      </q-card >
  </div>
</div>
</template>
<script lang="ts">

import { LeaderboardEntry } from 'src/models/Entries/entries';
import { Group } from 'src/models/Groups/groups';
import {defineComponent} from 'vue'

import {createDailyLeaderboardEntries} from 'src/models/Entries/entries.factory'
import {getGroupByGroupId} from 'src/models/Groups/groups.api'

import GroupDailyBarChart from 'components/charts/GroupDailyBarChart.vue'
import EntryList from 'components/EntryList.vue'

export default defineComponent({
  name: 'GroupPage',
  components: {
    EntryList,
    GroupDailyBarChart
  },
  data() {
    return {
      group: {name: 'poop', id: ''} as Group,
      showGroupPage: false,
      showLoading: true,
      dailyLeaderboardEntries: [] as LeaderboardEntry[]
    };
  },
  computed: {
  },
  mounted() {
    this.getGroupInfo();
    this.getDailyLeaderboardEntries();
  },
  methods: {
    async getGroupInfo()
    {
        this.group = await getGroupByGroupId(this.$route.params.groupID as string)
        this.showGroupPage = true;
        this.showLoading = false;
    },
    async getDailyLeaderboardEntries()
    {
      this.dailyLeaderboardEntries = await createDailyLeaderboardEntries(this.$route.params.groupID as string)
    }
  },
});
</script>
<style scoped>
    .capitalize {
        text-transform: capitalize;
    }
</style>