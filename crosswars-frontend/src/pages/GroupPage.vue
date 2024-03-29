<template lang="">
<div>
  <q-inner-loading
      :showing="showLoading"
      color: primary/>
  <div v-show="showGroupPage">
    <div class="q-pl-md">
      <div>
        <h4 class="text-capitalize" style="margin-right: 70px;">
          {{group.name}}
        </h4>
      </div>
      <div class="invite">
        <q-btn color="grey-7" round flat icon=person_add @click="inviteAlert = true">
          <q-dialog v-model="inviteAlert">
            <q-card>
              <q-card-section>
                <div class="text-h6 unselectable">Invite Others to {{group.name}}</div>
              </q-card-section>
              <q-card-section class="q-pt-none unselectable">
                Use the copy button below to get the invite link:
              </q-card-section>
              <q-card-section>
                <div class="input-group">
                  <div class="input-group-area"><div class="link-parent"><p class=link-child>{{inviteLink}}</p></div></div>
                  <div class="input-group-icon unselectable">
                    <q-btn class="icon-button unselectable" flat icon="content_copy" @click="copyInviteLink"/>
                  </div>
                </div>
              </q-card-section>
              <q-card-actions align="right">
                <q-btn flat label="OK" color="primary" v-close-popup />
              </q-card-actions>
            </q-card>
          </q-dialog>
        </q-btn>
      </div>
    </div>
    <GroupDailyBarChart :entries="dailyLeaderboardEntries"/>
    <div class="q-pa-md">
      <q-card>
        <q-expansion-item
        default-opened
        icon="leaderboard"
        label="Today's Leaderboard">
          <EntryList :entries="dailyLeaderboardEntries"/>
        </q-expansion-item>
      </q-card >
    </div>
    <div class="q-pa-md">
      <q-card>
        <q-expansion-item
        default-opened
        icon="emoji_events"
        label="Overall Wins">
          <WinsList :winCounts="winCounts" :users="users"/>
        </q-expansion-item>
      </q-card >
    </div>
    <div v-if="showWinCalendar">
      <WinsCalendarChart :wins="wins" :users="users" :min_date="minWinDate" :max_date="maxWinDate" :month_diff="winMonthDiff"/>
    </div>
  </div> 
</div>
</template>
<script lang="ts">

import { LeaderboardEntry } from 'src/models/Entries/entries';
import { Group } from 'src/models/Groups/groups';
import { LeaderboardWinCount, Win } from 'src/models/Wins/wins';
import {defineComponent} from 'vue';
import {copyToClipboard, useQuasar} from 'quasar';

import {createDailyLeaderboardEntries} from 'src/models/Entries/entries.factory'
import {getGroupByGroupId} from 'src/models/Groups/groups.api'
import {getWinsByGroupId} from 'src/models/Wins/wins.api'
import {createLeaderboardWinCounts} from 'src/models/Wins/wins.factory'

import GroupDailyBarChart from 'components/charts/GroupDailyBarChart.vue'
import WinsCalendarChart from 'components/charts/WinsCalendarChart.vue'
import WinsList from 'components/WinsList.vue'
import EntryList from 'components/EntryList.vue'
import { ref } from 'vue'
import { getUsersByGroupId } from 'src/models/Users/users.api';
import { User } from 'src/models/Users/users';
import {monthDiff} from 'src/utilities/dates';

export default defineComponent({
  name: 'GroupPage',
  components: {
    EntryList,
    WinsList,
    GroupDailyBarChart,
    WinsCalendarChart
  },
  setup() {
    const $q = useQuasar()

    return {
      showCopyNotif() {
        $q.notify({
          message: 'Link copied to clipboard.',
          position: 'top',
          icon: 'info'
        })
      },
      showCopyErrorNotif() {
        $q.notify({
          message: 'Error copying link to clipboard.',
          position: 'top',
          color: 'red',
          icon: 'error'
        })
      }
    }
  },
  data() {
    return {
      group: {name: 'poop', id: ''} as Group,
      showGroupPage: false,
      showLoading: true,
      inviteAlert: ref(false),
      dailyLeaderboardEntries: [] as LeaderboardEntry[],
      wins: [] as Win[],
      winCounts: [] as LeaderboardWinCount[],
      minWinDate: {} as Date,
      maxWinDate: {} as Date,
      winMonthDiff: {} as number,
      showWinCalendar: false,
      users: [] as User[],
    };
  },
  computed: {
    inviteLink: function(): string {
        return `${location.host}/#/group_invite/${this.group.id}`
    }
  },
  async mounted() {
    this.users = await getUsersByGroupId(this.$route.params.groupID as string, true)
    this.getGroupInfo();
    this.getDailyLeaderboardEntries();
    this.getWins();
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
      this.dailyLeaderboardEntries = await createDailyLeaderboardEntries(this.$route.params.groupID as string, this.users)
    },
    copyInviteLink()
    {
      copyToClipboard(this.inviteLink)
        .then(() => {
          this.showCopyNotif()
        }).catch(() => {
          this.showCopyErrorNotif()
        })
    },
    async getWins(){
      this.winCounts = await createLeaderboardWinCounts(this.$route.params.groupID as string)
      this.wins = await getWinsByGroupId(this.$route.params.groupID as string)
      const winDates = this.wins.map(w => new Date(w.date));
      this.minWinDate = winDates.reduce(function (a, b) { return a < b ? a : b; });
      this.maxWinDate = winDates.reduce(function (a, b) { return a > b ? a : b; });
      this.winMonthDiff = monthDiff(this.minWinDate, this.maxWinDate);
      if(this.wins.length > 0) {
        this.showWinCalendar = true;
      }
    }
  },
});
</script>
<style scoped>
    .capitalize {
        text-transform: capitalize;
    }
    .invite {
      position: absolute;
      top: 6.7em;
      right: 1.2em;
      
    }

  .input-group{
    display: table;
    border-collapse: collapse;
    width:100%;
    height: 10%;
    
  }
  .input-group > div{
    display: table-cell;
    height: inherit;
    
    vertical-align: middle;  /* needed for Safari */
  }
  .input-group-icon{
    background:rgb(234, 234, 234);
    color: #777;
    border-radius: 0px 5px 5px 0px;
    display: inline-block;
    overflow: auto;
  }
  .icon-button{
    border: 1px solid #ddd;
    border-radius: 0px 5px 5px 0px;
    border-left-width: 0px;
  }
  .input-group-area{
    width:100%;
    overflow: hidden;
    position:relative;
    
  }
  .link-parent {
    border: 1px solid #ddd;
    border-radius: 5px 0px 0px 5px;
    height:max-content;
    background:rgb(247, 247, 247);
    height: 100%;
    width: 100%;
    position: absolute;
    right: 0;
    top: 0;
  }
  .link-child {
    position: relative;
    color: rgb(88, 88, 88);
    top: 35%;
    left: 2%;
    width: 400%;
  }
  .unselectable {
      -webkit-touch-callout: none;
      -webkit-user-select: none;
      -khtml-user-select: none;
      -moz-user-select: none;
      -ms-user-select: none;
      user-select: none;
  }
</style>