<template lang="">
  <div>
    <div>
      <EntryForm @add:entry="addEntry" :loading="submitLoading"/>
    </div>
    <div v-if="groups.length==0" class="q-px-md q-pb-md" >
      <q-card flat bordered class="my-card bg-grey-1">
        <q-card-section>
          <div class="row items-center no-wrap">
            <div class="col">
              <div class="text-h6">Looks like you're not in any groups</div>
              <div class="text-subtitle3">Create a group to compete with others. Or, get an invite link from a friend</div>
            </div>
          </div>
        </q-card-section>
        <q-separator />
        <q-card-actions>
          <q-btn to="/createGroup" flat>Create a Group</q-btn>
        </q-card-actions>
      </q-card>
    </div>
    <div v-if="entries.length > 0" class="q-px-md">
      <q-card>
      <q-expansion-item
      default-opened
      icon="leaderboard"
      label="Today's leaderboard">
        <CombinedEntryList :entries="entries"/>
      </q-expansion-item>
      </q-card >
    </div>
    
    </div>
</template>
<script lang="ts">
import CombinedEntryList from 'src/components/CombinedEntryList.vue';
import EntryForm from 'components/EntryForm.vue';
import {defineComponent} from 'vue'
import { createDailyCombinedLeaderboardEntries,  setLeaderboardEntryPositions } from '../models/Entries/entries.factory'
import { postEntry } from '../models/Entries/entries.api'
import { getGroupsByUserId } from '../models/Groups/groups.api'
import { CombinedLeaderboardEntry } from 'src/models/Entries/entries';
import { User } from 'src/models/Users/users';
import { Group } from 'src/models/Groups/groups';
export default defineComponent({
  name: 'HomePage',
  components: {
    CombinedEntryList,
    EntryForm,
  },
  data() {
    return {
      user: {id: '123456789', name: 'testdude'} as User,
      entries: [] as CombinedLeaderboardEntry[],
      groups: [] as Group[],
      submitLoading: false,
    };
  },
  mounted() {
    this.createEntries();
    this.getGroups();
  },
  methods: {
    async addEntry(time: number) {
      let oldEntry = this.entries.find((e)=> e.user.id == this.user.id)
      this.submitLoading = true;
      postEntry({time: time, user_id: this.user.id}).then(() => {
        this.submitLoading = false;
      })
      if(oldEntry)
      {
        oldEntry.time = time
      }
      else 
      {
        //don't care about current user's date or groups
        this.entries.push({time: time, user_id: this.user.id, date: '',  groups: [], position: 0, user: this.user})
      }
      setLeaderboardEntryPositions(this.entries)
    },
    async createEntries(){
      this.entries = await createDailyCombinedLeaderboardEntries(this.user.id);
    },
    async getGroups()
    {
      this.groups = await getGroupsByUserId(this.user.id)
    }
  },
});
</script>
<style scoped>
</style>