<template>
  <div>
    <q-list separator>
      <q-item v-for="entry in sortedEntries" :key="entry.user_name" class="q-my-sm" clickable v-ripple>
      <q-item-section side>
        <div>
        <q-item-label style="font-weight: bold">{{ entry.position }}</q-item-label>
        </div>
      </q-item-section>

      <q-item-section avatar>
        <q-avatar color="primary" text-color="white">
          {{ entry.user.name.charAt(0)}}
        
        </q-avatar>
         <q-item-label font=text-weight-bold class="q-py-s">{{ entry.user.name }}</q-item-label>

      </q-item-section>
      
      <q-item-section name>
        <div>
          <q-chip clickable @click="groupRedirect(group.id)" size="s" v-for="group in entry.groups" :key="group.id">
            {{ group.name}}
          </q-chip>
        </div>
      </q-item-section>
      <q-item-section side>
        <q-item-label>{{ formatTime(entry.time) }}</q-item-label>
      </q-item-section>
      </q-item>
    </q-list>
  </div>
</template>

<script>
import { defineComponent } from "vue";

export default defineComponent({
  name: "EntryTable",
  props: {
    entries: Array,
    validator: (prop) => prop.every(e => typeof e === 'Object')
  },
  setup() {
    return {
      groupRedirect(groupId) {
        this.$router.push({path: `/group/${groupId}`})
      }
    }
  },
  data() {
    return {
    };
  },
  methods: {
    formatTime(timeInSeconds) {
      const mins = Math.floor(timeInSeconds / 60);
      const secs = timeInSeconds % 60;
      return mins + ":" + (secs < 10 ? "0" : "") + secs;
    },
  },
  computed: {
    sortedEntries: function() {
        return [...this.entries].sort((a, b) => a.position - b.position);
      }
    }
});
</script>
