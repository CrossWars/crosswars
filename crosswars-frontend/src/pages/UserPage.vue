<template lang="">
<div>
  <q-inner-loading
      :showing="showLoading"
      color: primary/>
  <div class="q-ma-sm">
      <h4 class=text-capitalize>
          {{user.name}}
      </h4>
    <CalendarChart :entries="entries"/>
  </div>

</div>
</template>
<script lang="ts">

import { Entry } from 'src/models/Entries/entries';
import { User } from 'src/models/Users/users';
import {defineComponent} from 'vue'

import { getEntriesByUserId} from 'src/models/Entries/entries.api'
import { getUserByUserId} from 'src/models/Users/users.api'

import CalendarChart from 'components/charts/CalendarChart.vue'

export default defineComponent({
  name: 'UserPage',
  components: {
    CalendarChart
  },
  data() {
    return {
      user: {name: 'PlaceholderUser', id: ''} as User,
      showUserPage: false,
      showLoading: true,
      entries: [] as Entry[]
    };
  },
  computed: {
  },
  mounted() {
    this.getUserInfo();
    this.getAllEntries();
  },
  methods: {
    async getUserInfo()
    {
        this.user = await getUserByUserId(this.$route.params.userID as string)
        this.getAllEntries()
        this.showUserPage = true;
        this.showLoading = false;
    },
    async getAllEntries()
    {
      this.entries = await getEntriesByUserId(this.$route.params.userID as string)
    }
  },
});
</script>
<style scoped>
    .capitalize {
        text-transform: capitalize;
    }
</style>