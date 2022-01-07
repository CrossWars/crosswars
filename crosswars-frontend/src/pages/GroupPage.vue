<template lang="">
<div>
  <q-inner-loading
      :showing="showLoading"
      color: primary/>
  <div v-show="showGroupPage">
    <div class="q-px-md">
      <div>
        <h4 class="text-capitalize">
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
        label="Today's leaderboard">
          <EntryList :entries="dailyLeaderboardEntries"/>
        </q-expansion-item>
      </q-card >
    </div>
  </div> 
</div>
</template>
<script lang="ts">

import { LeaderboardEntry } from 'src/models/Entries/entries';
import { Group } from 'src/models/Groups/groups';
import {defineComponent} from 'vue';
import {copyToClipboard, useQuasar} from 'quasar';

import {createDailyLeaderboardEntries} from 'src/models/Entries/entries.factory'
import {getGroupByGroupId} from 'src/models/Groups/groups.api'

import GroupDailyBarChart from 'components/charts/GroupDailyBarChart.vue'
import EntryList from 'components/EntryList.vue'
import { ref } from 'vue'

export default defineComponent({
  name: 'GroupPage',
  components: {
    EntryList,
    GroupDailyBarChart
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
      dailyLeaderboardEntries: [] as LeaderboardEntry[]
    };
  },
  computed: {
    inviteLink: function(): string {
        return `${location.host}/#/group_invite/${this.group.id}`
    }
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
    },
    copyInviteLink()
    {
      copyToClipboard(this.inviteLink)
        .then(() => {
          this.showCopyNotif()
        }).catch(() => {
          this.showCopyErrorNotif()
        })
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