<template>
  <div>
    <q-list separator>
      <q-item v-for="entry in sortedEntries" :key="entry.user.id">
      <q-item-section side>
        <div>
        <q-item-label style="font-weight: bold">{{ entry.position }}</q-item-label>
        </div>
      </q-item-section>

      <q-item-section avatar>
        <q-btn :to="getUserLink(entry.user_id)" round>
          <q-avatar color="primary" text-color="white">
            {{ entry.user.name.charAt(0).toUpperCase()}}
          </q-avatar>
        </q-btn>
         <q-item-label><p font=text-weight-bold class="q-pt-sm" style="text-transform: capitalize; text-align: center;">{{ entry.user.name }}</p></q-item-label>
      </q-item-section>
      <q-item-section name>
        <div>
          <q-chip clickable @click="groupRedirect(group.id)" size="s" v-for="group in entry.groups" :key="group.id">
            {{ group.name}}
          </q-chip>
        </div>
      </q-item-section>
      <q-item-section side>
        <q-item-label style="color: black; font-size: 15px;">{{ formatTime(entry.time) }}</q-item-label>
      </q-item-section>
      </q-item>
    </q-list>
  </div>
</template>

<script lang="ts">
import { CombinedLeaderboardEntry } from 'src/models/Entries/entries';
import { defineComponent, PropType } from 'vue';

export default defineComponent({
  name: 'CombinedEntryList',
  props: {
    entries: 
    {
      type: Array as PropType<CombinedLeaderboardEntry[]>,
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
    sortedEntries: function() : CombinedLeaderboardEntry[] {
        //this sorting function comes from .position possibly being undefined
        return [...this.entries].sort((a, b) => 
          (a.position || b.position) ? 
            (!a.position ? -1 : !b.position ? 1 : a.position -b.position) : 0
        );
      }
    }
});
</script>
