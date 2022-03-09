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
              <p>Average Time: <br>{{averageTimeStr}}</p>
            </div>
          </div>
        <div class="q-pa-sm" v-if="showLineChart">
        <q-card class="q-pa-sm">
          <WeekLineChart :entries="entries" :min_date="minDate" :max_date="maxDate" :min_time_entry="minTimeEntry" :day_diff="entryDayDiff" :avg_time="averageTime"/>
        </q-card>
        </div>
        <div class="q-pa-sm">
        <q-card >
          <CalendarChart :entries="entries" :min_time="minTimeEntry.time" :max_time="maxTimeEntry.time" :min_date="minDate" :max_date="maxDate" :month_diff="entryMonthDiff"  />
        </q-card>
        </div>
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
import { getAverageTimeByUserId} from 'src/models/Stats/stats.api'

import {formatTime} from 'src/utilities/time'
import {monthDiff, dayDiff} from 'src/utilities/dates'
import CalendarChart from 'components/charts/CalendarChart.vue'
import WeekLineChart from 'components/charts/WeekLineChart.vue'

export default defineComponent({
  name: 'UserPage',
  components: {
    CalendarChart,
    WeekLineChart
  },
  data() {
    return {
      user: {name: '', id: ''} as User,
      showUserPage: false,
      showLoading: true,
      showLineChart: false,
      entries: [] as Entry[],
      bestTime: '',
      averageTime: 0,
      averageTimeStr: '',
      minDate: '',
      maxDate: '',
      minTimeEntry: {} as Entry,
      maxTimeEntry: {} as Entry,
      entryMonthDiff: 0,
      entryDayDiff: 0,
    };
  },
  mounted() {
    this.getUserInfo();
    this.getAllEntries();
    this.getAllEntries()

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
      const entryDates = this.entries.map(e => e.date)
      this.minDate = entryDates.reduce(function (a, b) { return a < b ? a : b; });
      this.maxDate = entryDates.reduce(function (a, b) { return a > b ? a : b; });
      this.entryMonthDiff = monthDiff(new Date(this.minDate), new Date(this.maxDate));
      this.entryDayDiff = dayDiff(new Date(this.minDate), new Date(this.maxDate))
      this.minTimeEntry = this.entries.reduce((prev, curr) => prev.time < curr.time ? prev : curr)
      this.maxTimeEntry = this.entries.reduce((prev, curr) => prev.time > curr.time ? prev : curr)
      this.getStats();
    },
    async getStats()
    {
      this.bestTime = formatTime(this.minTimeEntry.time)
      this.averageTime = await getAverageTimeByUserId(this.$route.params.userID as string)
      this.averageTimeStr = formatTime(this.averageTime);
      this.showLineChart = this.entries.length >= 2;
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