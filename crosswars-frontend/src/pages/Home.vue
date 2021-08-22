<template lang="">
<div id="entry-form">
    <form @submit.prevent="handleSubmit">
      <label>Todays Crossword Time</label>
      <q-input
        type="text"
        :class="{ 'has-error': submitting && invalidTime }"
        v-model="entry.time"
        @focus="clearStatus"
        @keypress="clearStatus"
      />
      <p v-if="error && submitting" class="error-message">
        ❗Please fill out time field
      </p>
      <p v-if="success" class="success-message">✅ Time successfully added</p>
      <button>Add Time</button>
    </form>
  </div>
  <EntryTable/>
</template>
<script>
import { Cookies } from 'quasar'
import EntryTable  from 'components/EntryTable.vue';
import {EssentialLink} from "../components/EssentialLink.vue";
export default {
    name: "Home",
    components: {
      EntryTable
    },
    methods: {
      enterTime() {
        console.log(this.entry.time)
      }
    },
    data() {
      return {
        submitting: false,
        error: false,
        success: false,
        entry: {
          time: "",
          user_id: "123456789"
        }
      }
    },
}
</script>
<style scoped>
form {
  margin-bottom: 2rem;
}
[class*="-message"] {
  font-weight: 500;
}

.error-message {
  color: #d33c40;
}

.success-message {
  color: #32a95d;
}
</style>