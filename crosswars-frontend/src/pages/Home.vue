<template lang="">
  <EntryForm @add:entry="addEntry" :loading="submitLoading"/>
  <EntryTable :entries="leaderboardEntries"/>

</template>
<script>
import EntryTable from "components/EntryTable.vue";
import EntryForm from "components/EntryForm.vue";

export default {
  name: "Home2",
  components: {
    EntryTable,
    EntryForm,
  },
  data() {
    return {
      user_id: "123456789",
      user_name: "testdude",
      groupIdToGroup: new Map(),
      userIdToUser: new Map(),
      groupToEntries: new Map(),
      userIdToLeaderboardEntry: new Map(),
      submitLoading: false,
    };
  },
  computed: {
    leaderboardEntries: function() {
      return Array.from(this.userIdToLeaderboardEntry.values())
    }
  },
  mounted() {
    this.getDataForLeaderboardEntries();
  },
  methods: {
    async addEntry(entry) {
      this.submitLoading = true;
      entry.user_id = this.user_id;
      this.$api.post(`/entries`, entry).then(() => {
        this.submitLoading = false;
        var leaderboardEntry = {
          user: { user_id: this.user_id, name: this.user_name },
          groups: Array.from(this.groupIdToGroup.values()),
          time: entry.time,
        };
        this.userIdToLeaderboardEntry.set(this.user_id, leaderboardEntry)
        this.setPositions();
      });
    },
    async getDataForLeaderboardEntries() {
      this.$api.get(`/groups?user_id=${this.user_id}`).then((result) => {
        this.groupIdToGroup = this.arrayToMap(result.data, "id");
        let promises = [];
        for (let group of result.data) {
          //get uses of group, put in groupToUsers map
          promises.push(
            this.$api
              .get(`/groups/users?group_id=${group.id}`)
              .then((usersResponse) => {
                for (let user of usersResponse.data) {
                  this.userIdToUser.set(user.user_id, user);
                }
              })
          );
          //get entries of group, put in groupToEntries map
          promises.push(
            this.$api
              .get(
                `/entries/groups?group_id=${group.id}`
              )
              .then((entryResponse) => {
                this.groupToEntries.set(group.id, entryResponse.data);
              })
          );
        }
        Promise.all(promises).then(() => {
          this.createLeaderboardEntries();
        });
      });
    },
    createLeaderboardEntries() {
      //add User names and groups to each entry
      for (let [group_id, group] of this.groupIdToGroup) {
        for (let entry of this.groupToEntries.get(group_id)) {
          //userID not in leaderboardEntry dict, so add it
          if (!this.userIdToLeaderboardEntry.has(entry.user_id)) {
            var leaderboardEntry = {
              user: this.userIdToUser.get(entry.user_id),
              groups: [group],
              date: entry.date,
              time: entry.time,
            };
            this.userIdToLeaderboardEntry.set(entry.user_id, leaderboardEntry);
          } else {
            //entry already exists for this user, just add group to entry
            this.userIdToLeaderboardEntry.get(entry.user_id).groups.push(group);
          }
        }
      }
      this.setPositions();
    },
    setPositions() {
      //Positions looks like [[entry1, entry2], [entry3], [entry4]], index+1 is place in leaderboard
      let positions = [];

      //compute place in positions array
      for (let entry of this.userIdToLeaderboardEntry.values()) {
        // add first entry
        if (positions.length === 0) {
          positions.push([entry]);
          continue;
        }
        var i = 0;
        var inserted = false;
        while (i < positions.length && !inserted) {
          if (entry.time < positions[i][0].time) {
            //insert at i
            positions.splice(i, 0, [entry]);
            inserted = true;
          } else if (entry.time === positions[i][0].time) {
            positions[i].push(entry);
            inserted = true;
          } else {
            i++;
          }
        }
        if (!inserted) {
          positions.push([entry]);
        }
      }
      //set position of each entry object
      for (var i = 0; i < positions.length; i++) {
        for (let entry of positions[i]) {
          entry.position = i + 1;
        }
      }
    },
    arrayToMap(arr, keyField) {
      /* takes an array of objects and makes it into a map based on a key
      [{id: 1, name: Rob}] -> {1: {id: 1, name: rob}}
      */
      var map = new Map();
      for (let obj of arr) {
        map.set(obj[keyField], obj);
      }
      return map;
    },
  },
};
</script>
<style scoped>
</style>