<template lang="">
<div>
  <h4 class="q-px-lg">Create a New Group</h4>
  <div id="group-form" >
    <form @submit.prevent="handleSubmit" class="q-px-lg">
       <q-input 
       ref="inputRef"
       outlined 
       v-model="name" 
       label="Group Name" 
       class="inputField"/>
      <p v-if="error" class="error-message">
        {{errorMessage}}
      </p>
      <div class="q-py-lg">
      <q-btn
      color="primary" 
      type="submit"
      label="Submit" 
      class="submitButton"
      :loading="loading">
        <template v-slot:loading>
          <q-spinner-dots />
        </template>
    </q-btn>
    </div>
    </form>
  </div>
</div>
</template>
<script lang="ts">


import { Group } from 'src/models/Groups/groups';
import { postNewGroup, addUserToGroup } from 'src/models/Groups/groups.api';
import {defineComponent} from 'vue'


export default defineComponent({
  name: 'CreateGroupPage',
  components: {
  },
  data() {
    return {
      name: '',
      userId: '123456789',
      error: false,
      errorMessage: '',
      showGroupPage: false,
      loading: false
    };
  },
  computed: {
  },
  mounted() {
  },
  methods: {
    async handleSubmit() {
      this.loading = true;
      if (!this.isGroupNameValid(this.name)) {
        this.error = true
        this.errorMessage = `The name "${this.name}" is invalid.`
        this.loading =  false
        return
      }
      const newGroup: Group = {id: '', name: this.name}
      postNewGroup(newGroup)
          .then(group => {
            addUserToGroup(group.id, this.userId).then(() => this.groupRedirect((group as Group).id))
          })
          .catch((error) => {
          this.error=true
          if(error.response && error.response.data.response_code == 400){
            this.errorMessage=error.response.data.message
          }
          else {
            this.errorMessage = 'An error occured.'
          }
          this.loading=false
      })
    },
    isGroupNameValid(groupName: string): boolean {
      if (groupName.length < 3 || groupName.length > 25) return false
      //match with alphanumeric, separated by space between words
      const match = groupName.match(/^[a-zA-Z1-9]+( ?[a-zA-Z-1-9])*$/)
      console.log(match)
      return match !== null && match[0] == groupName
    },
    groupRedirect(groupId: string) {
      this.$router.push({path: `/group/${groupId}`})
    },
  },
});
</script>
<style scoped>
    .capitalize {
        text-transform: capitalize;
    }
    .error-message {
      color: #d33c40;
      margin-top: 1rem;
    }
</style>