<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated>
      <q-toolbar>
        <q-btn flat no-caps size="lg" label="CrossWars" to="/home"/>
        <q-space/>
        <q-btn-dropdown stretch flat icon="groups" v-if="groups.length > 0">
          <q-list>
            <q-item v-for="group in groups" :key="group.id" clickable :to="`/group/${group.id}`">
              <q-item-section>
                <q-item-label>{{group.name}}</q-item-label>
              </q-item-section>
            </q-item>
            <q-item clickable :to="`/create_group`">
              <q-item-section icon>
                <q-icon name="add_circle" color="secondary" text-color="white"/>
              </q-item-section>
              <q-item-section label>
                <q-item-label>Create a Group</q-item-label>
              </q-item-section>
            </q-item>
          </q-list>
        </q-btn-dropdown>
        <q-btn stretch flat icon="person" v-if="showUser" :to="`/user/${user.id}`"/>
      </q-toolbar>
    </q-header>
    <q-page-container>
      <router-view/>
    </q-page-container>
  </q-layout>
</template>

<script lang='ts'>
import { Group } from 'src/models/Groups/groups';
import { getGroupsByUserId } from 'src/models/Groups/groups.api';
import { User } from 'src/models/Users/users';
import { getUserByJWT } from 'src/models/Users/users.api';


import { defineComponent } from 'vue'

export default defineComponent({
  name: 'MainLayout',

  data() {
    return {
      user: {} as unknown as User,
      showUser: false,
      groups: [] as Group[],
    };
  },
  mounted() {
    this.getUserAndGroups();
  },
  methods: {
    async getUserAndGroups() {
      this.user = await getUserByJWT()
      this.showUser = true
      this.groups = await getGroupsByUserId(this.user.id)
    }
  }
})
</script>
