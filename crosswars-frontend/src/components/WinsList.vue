<template>
  <div>
    <q-list separator>
    <div v-if="sortedWinCounts.length > 0">
      <q-item v-for="winCount in sortedWinCounts" :key="winCount.user_id" class="q-my-sm">
      <q-item-section side>
        <div>
        <q-item-label style="font-weight: bold">{{ winCount.position }}</q-item-label>
        </div>
      </q-item-section>
      <q-item-section avatar>
        <q-btn :to="getUserLink(winCount.user_id)" round>
          <q-avatar color="primary" text-color="white">
            {{ usersMap.get(winCount.user_id)?.name.charAt(0).toUpperCase()}}
          </q-avatar>
        </q-btn>
      </q-item-section>
      <q-item-section>
        <q-item-label><p font=text-weight-bold class="q-pt-xs" style="text-transform: capitalize; text-align: left;">{{ usersMap.get(winCount.user_id)?.name }}</p></q-item-label>
      </q-item-section>
      <q-item-section side>
        <q-item-label style="color: black; font-size: 15px;">{{ winCount.wins}}</q-item-label>
      </q-item-section>
      </q-item>
      </div>
    </q-list>
  </div>
</template>

<script lang="ts">
import { LeaderboardWinCount } from 'src/models/Wins/wins';
import { defineComponent, PropType } from 'vue';
import { User } from 'src/models/Users/users';

export default defineComponent({
  name: 'WinsList',
  props: {
    winCounts: 
    {
      type: Array as PropType<LeaderboardWinCount[]>,
      default: () => []
    },
    users:
    {
      type: Array as PropType<User[]>,
      default: () => []
    },
  },
  methods: {
    getUserLink(userId: string): string
    {
      return `/user/${userId}`
    }
  },
  computed: {
    sortedWinCounts: function() : LeaderboardWinCount[] {
        return [...this.winCounts].sort((a, b) => a.position - b.position);
    },
    usersMap: function(): Map<string, User> {
      return this.users.reduce( function(map, user){
        map.set(user.id, user);
        return map;
      }, new Map<string, User>());
    }
  }
});
</script>
