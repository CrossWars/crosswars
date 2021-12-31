<template lang="">
<div class="q-pt-md q-px-md" id="entry-form">
    <form @submit.prevent="handleSubmit">
       <q-input 
       ref="inputRef"
       inputmode="numeric"
       outlined 
       v-model="timeString" 
       label="Enter Your Time" 
       class="q-py-lg"
       mask="#:##"
       fill-mask="0"
       reverse-fill-mask
       @focus="onFocus" />
      <div class="q-pt-sm">
      <q-btn 
      color="primary" 
      type="submit"
      label="Submit"
      :loading="loading">
        <template v-slot:loading>
          <q-spinner-dots />
        </template>
    </q-btn>
    </div>
    <p v-if="error" class="error-message">
        Please fill out time field
      </p>
    </form>
  </div>
</template>
<script lang='ts'>
import { defineComponent } from 'vue';
export default defineComponent({
  name: 'EntryForm',
  props: {
    loading: Boolean,
  },
  methods: {
    handleSubmit() {
      this.submitting = true;
      this.success = false;
      let time = this.parseTime(this.timeString);
      if (time <= 0 || time >= 24*60*60) {
        this.error = true;
        this.submitting = false;
        return;
      }
      this.$emit('add:entry', time);
      this.timeString = '0:00'
      this.error = false;
      this.success = true;
      this.submitting = false;
    },
    onFocus() {
      //sets caret to end of input field. I can't get ESLint to like this code, so for now the errors are just disabled
      // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment, @typescript-eslint/no-unsafe-member-access, @typescript-eslint/no-unsafe-call, @typescript-eslint/no-explicit-any
      let e = (this.$refs['inputRef'] as any).getNativeElement();
      setTimeout(()=> {
        // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access, @typescript-eslint/no-unsafe-call
        e.setSelectionRange(e.value.length, e.value.length)
      }, 1)  
      console.log(e);
    },
    parseTime(time: string): number {
      const mins_secs = time.split(':');
      console.log(mins_secs);
      const mins = parseInt(mins_secs[0]);
      const secs = parseInt(mins_secs[1]);
      return mins * 60 + secs;
    },
  },
  data() {
    return {
      error: false,
      success: false,
      submitting: false,
      timeString: '0:00',
    };
  },
});
</script>
<style scoped>
form {
  margin-bottom: 2rem;
  max-width: 300px;
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