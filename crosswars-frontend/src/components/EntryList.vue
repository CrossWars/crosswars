<template>
  <div>
    <q-list separator>
      <q-item v-for="entry in sortedEntries" :key="entry.user.id" class="q-my-sm">
      <q-item-section side>
        <div>
        <q-item-label style="font-weight: bold">{{ entry.position }}</q-item-label>
        </div>
      </q-item-section>
      <q-item-section avatar>
        <q-avatar color="primary" text-color="white">
          {{ entry.user.name.charAt(0)}}
        </q-avatar>
      </q-item-section>
      <q-item-section>
        <q-item-label font=text-weight-bold class="q-py-s">{{ entry.user.name }}</q-item-label>
      </q-item-section>
      <q-item-section side>
        <q-item-label>{{ formatTime(entry.time) }}</q-item-label>
      </q-item-section>
      </q-item>
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
