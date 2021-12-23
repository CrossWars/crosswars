<template lang="">
  <div>
    <div class="q-pa-sm">
      <EntryForm @add:entry="addEntry" :loading="submitLoading"/>
    </div>
    <div class="q-px-md">
      <q-card>
      <q-expansion-item
      default-opened
      icon="leaderboard"
      label="Today's leaderboard">
        <CombinedEntryList :entries="entries"/>
      </q-expansion-item>
      </q-card >
      <div class="q-pt-md">
      <q-card>
      <q-expansion-item
      default-opened
      icon="groups"
      label="Your Groups">
      </q-expansion-item>
      </q-card>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import CombinedEntryList from 'src/components/CombinedEntryList.vue';
import EntryForm from 'components/EntryForm.vue';
import {defineComponent} from 'vue'
import { createDailyCombinedLeaderboardEntries,  setLeaderboardEntryPositions } from '../models/Entries/entries.factory'
import { postEntry } from '../models/Entries/entries.api'
import { CombinedLeaderboardEntry } from 'src/models/Entries/entries';
import { User } from 'src/models/Users/users';
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
      submitLoading: false,
    };
  },
  mounted() {
    this.createEntries();
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
    }
  },
});
</script>
<style scoped></style>