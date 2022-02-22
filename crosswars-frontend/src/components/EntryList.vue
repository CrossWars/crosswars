<template>
  <div>
    <q-list separator>
    <div v-if="sortedEntries.length > 0">  
      <q-item v-for="entry in sortedEntries" :key="entry.user.id" class="q-my-sm">
      <q-item-section side>
        <div>
        <q-item-label style="font-weight: bold">{{ entry.position }}</q-item-label>
        </div>
      </q-item-section>
      <q-item-section avatar>
        <q-btn :to="getUserLink(entry.user_id)" round>
          <q-avatar v-if="entry.user.photoUrl !== undefined" >
            <img :src="entry.user.photoUrl"/>
          </q-avatar>
          <q-avatar v-else color="primary" text-color="white">
            {{ entry.user.name.charAt(0).toUpperCase()}}
          </q-avatar>
        </q-btn>
      </q-item-section>
      <q-item-section>
        <q-item-label><p font=text-weight-bold class="q-pt-xs" style="text-transform: capitalize; text-align: left;">{{ entry.user.name }}</p></q-item-label>
      </q-item-section>
      <q-item-section side>
        <q-item-label style="color: black; font-size: 15px;">{{ formatTime(entry.time) }}</q-item-label>
      </q-item-section>
      </q-item>
      </div>
    <div v-else>
        <q-item>
        <q-item-section>
            <q-item-label font=text-weight-bold>It's pretty empty here</q-item-label>
          </q-item-section>
        </q-item>
    </div>
    </q-list>
  </div>
</template>

<script lang="ts">
import { LeaderboardEntry } from 'src/models/Entries/entries';
import { defineComponent, PropType } from 'vue';

export default defineComponent({
  name: 'CombinedEntryList',
  props: {
    entries: 
    {
      type: Array as PropType<LeaderboardEntry[]>,
      default: () => []
    }
  },
  methods: {
    formatTime(timeInSeconds: number) : string{
      const mins = Math.floor(timeInSeconds / 60);
      const secs = timeInSeconds % 60;
      return `${mins}:${secs < 10 ? '0' : ''}${secs}`
    },
    groupRedirect(groupId: string) {
      void this.$router.push({path: `/group/${groupId}`})
    },
    getUserLink(userId: string): string
    {
      return `/user/${userId}`
    }
  },
  computed: {
    sortedEntries: function() : LeaderboardEntry[] {
        //this sorting function comes from .position possibly being undefined
        return [...this.entries].sort((a, b) => 
          (a.position || b.position) ? 
            (!a.position ? -1 : !b.position ? 1 : a.position -b.position) : 0
        );
      }
    }
});
</script>
