<template>
<div>
  <q-inner-loading
      :showing="showLoading"
      color: primary/>
    <div v-if="showUserPage" class="q-ma-sm">
        <div class="row">
          <q-avatar v-if="user.photoUrl !== undefined" class="q-px-sm q-pt-lg " size="75px">
            <img v-if="user.photoUrl !== undefined" :src="user.photoUrl" />
          </q-avatar>
          <h4 class="q-pl-lg"  style=" position: relative; top: -2px;">
              {{user.name}}
          </h4>
        </div>
        <div v-if="entries.length > 0">
          <div id="wrapper">
            <div id="bestTime">
              <p>Best Time: <br>{{bestTime}}</p>
            </div>
            <div id="avgTime">
              <p>Average Time: <br>{{averageTime}}</p>
            </div>
          </div>
        <q-card class="q-pa-sm">
          <CalendarChart :entries="entries" :min_time="minTime" :max_time="maxTime"/>
        </q-card>
      </div>
    </div>
</div>
</template>
<script lang="ts">

import { Entry } from 'src/models/Entries/entries';
import { User } from 'src/models/Users/users';
import {defineComponent} from 'vue'

import { getEntriesByUserId} from 'src/models/Entries/entries.api'
import { getUserByUserId} from 'src/models/Users/users.api'
import { getBestTimeByUserId} from 'src/models/Stats/stats.api'
import { getAverageTimeByUserId} from 'src/models/Stats/stats.api'

import {formatTime} from 'src/utilities/time'
import CalendarChart from 'components/charts/CalendarChart.vue'

export default defineComponent({
  name: 'UserPage',
  components: {
    CalendarChart
  },
  data() {
    return {
      user: {name: '', id: ''} as User,
      showUserPage: false,
      showLoading: true,
      entries: [] as Entry[],
      minTime: 0,
      maxTime: 0,
      bestTime: '',
      averageTime: '',
    };
  },
  computed: {
  },
  mounted() {
    this.getUserInfo();
    this.getAllEntries();
    this.getAllEntries();
    this.getStats();

  },
  methods: {
    async getUserInfo()
    {
        this.user = await getUserByUserId(this.$route.params.userID as string, true)
        this.showUserPage = true;
        this.showLoading = false;
    },
    async getAllEntries()
    {
      this.entries = await getEntriesByUserId(this.$route.params.userID as string)
      this.minTime = Math.min.apply(Math, this.entries.map(e => e.time))
      this.maxTime = Math.max.apply(Math, this.entries.map(e => e.time))
      this.getStats();
    },
    async getStats()
    {
      this.minTime = await getBestTimeByUserId(this.$route.params.userID as string)
      this.bestTime = formatTime(this.minTime)
      this.averageTime = formatTime(await getAverageTimeByUserId(this.$route.params.userID as string))
    }
  },
});
</script>
<style scoped>
    .capitalize {
        text-transform: capitalize;
    }
    #wrapper {
    display: table;
    table-layout: fixed;
    
    width:90%;
    height:20px;
    } 
    #wrapper div {
        display: table-cell;
        height: 10%;
    }

    #bestTime {
      text-align: center;
    }
    #avgTime {
      text-align: center;
    }
</style>