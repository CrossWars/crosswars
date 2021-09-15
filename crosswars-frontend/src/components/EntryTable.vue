<template>
  <div class="q-pa-md">
    <q-table
      title="Leaderboard"
      :rows="entries"
      :columns="columns"
      row-key="name"
      :pagination="pagination"
    />
  </div>
</template>

<script>
import { defineComponent } from "vue";
//check out https://codepen.io/smolinari/pen/WNQjVBN?editors=1010 for adding group chips
const columns = [
  {
    name: "position",
    align: "left",
    label: "#",
    field: "position",
    sortable: true,
  },
  {
    name: "user_name",
    label: "Name",
    field: (row) => row.user.name,
    align: "left",
  },
  {
    name: "time",
    label: "Time",
    field: "time",
    align: "left",
    sortable: true,
    format: (timeInSeconds, row) => {
      const mins = Math.floor(timeInSeconds / 60);
      const secs = timeInSeconds % 60;
      return mins + ":" + (secs < 10 ? "0" : "") + secs;
    },
  },
  {
    name: "groups",
    label: "Groups",
    align: "left",
    field: (row) => row.groups,
    //return list of each group name
    format: (groups, row) => groups.map((group) => group.name).join(", "),
  },
];
export default defineComponent({
  name: "EntryTable",
  props: {
    entries: Array,
  },
  data() {
    return {
      pagination: {
        sortBy: "position",
      },
      columns,
      leaderboard_entries: [],
    };
  },
  methods: {
    formatTime(timeInSeconds) {
      const mins = Math.floor(timeInSeconds / 60);
      const secs = timeInSeconds % 60;
      return mins + ":" + (secs < 10 ? "0" : "") + secs;
    },
  }
});
</script>
