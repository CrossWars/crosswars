<template lang="">
  <div v-if="showPage" class="center">
    <div v-if="!isMember">
      <h4 class="q-px-lg">You have been invited to join <br><strong>{{group.name}}</strong></h4>
      <div class="q-pb-md">
        <q-btn
          color="primary"
          label="Join"
          @click="joinGroup"
          :loading="loading">
            <template v-slot:loading>
              <q-spinner-dots />
            </template>
        </q-btn>
      </div>
      <div>
        <q-btn
        outline
        color="primary"
        label="Ignore"
        @click="goHome"
        :loading="loading">
          <template v-slot:loading>
            <q-spinner-dots />
          </template>
        </q-btn>
      </div>
    </div>
    <div v-else>
      <h4 class="q-px-lg">You are already a member of <br><strong>{{group.name}}</strong></h4>
      <div class="q-pb-md">
        <q-btn
          color="primary"
          label="Ignore"
          @click="goHome">
        </q-btn>
      </div>
    </div>
  </div>
</template>
<script lang="ts">


import { Group } from 'src/models/Groups/groups';
import { getGroupByGroupId, addUserToGroup, getIsMember } from 'src/models/Groups/groups.api';
import { User } from 'src/models/Users/users';
import { getUserByJWT } from 'src/models/Users/users.api';
import {defineComponent} from 'vue'


export default defineComponent({
  name: 'CreateGroupPage',
  components: {
  },
  data() {
    return {
      isMember: true,
      group: {} as Group,
      user: {} as User,
      error: false,
      errorMessage: '',
      showPage: false,
      loading: false
    };
  },
  computed: {
  },
  mounted() {
    this.getGroupInfo();
  },
  methods: {
    async getGroupInfo()
    {
        this.user = await getUserByJWT();
        this.isMember = await getIsMember(this.$route.params.groupID as string, this.user.id)
        this.group = await getGroupByGroupId(this.$route.params.groupID as string)
        this.showPage = true;
    },
    async joinGroup()
    {
      addUserToGroup(this.group.id, this.user.id).then(
        () => this.groupRedirect(this.group.id))
    },
    goHome() {
      this.$router.push({path: '/home'})
    },
    groupRedirect(groupId: string) {
      this.$router.push({path: `/group/${groupId}`})
    },
  },
});
</script>
<style scoped>
  .center {
    margin: 0 auto;
    max-width: 500px;
    height: 200px;
    text-align: center
  }
</style>