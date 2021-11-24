<template lang="">
<div id="entry-form">
    <form @submit.prevent="handleSubmit">
       <q-input 
       ref="EntryInput"
       inputmode="numeric"
       outlined 
       v-model="timeString" 
       label="Enter Your Time" 
       class="inputField"
       mask="#:##"
       fill-mask="0"
       reverse-fill-mask
       @focus="onFocus" />

      <p v-if="error" class="error-message">
        ❗Please fill out time field
      </p>
      <p v-if="success" class="success-message">✅ Time successfully added</p>
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
    </form>
  </div>
</template>
<script>
import { ref } from "vue";

export default {
  name: "EntryForm",
  setup() {
    const submitting = ref(false);
  },
  props: {
    loading: Boolean,
  },
  methods: {
    handleSubmit() {
      this.submitting = true;
      this.success = false;
      let time = this.parseTime(this.timeString);
      if (time <= 0) {
        console.log(time);
        this.error = true;
        this.submitting = false;
        return;
      }
      this.$emit("add:entry", { time: time });
      this.timeString = "0:00";
      this.error = false;
      this.success = true;
      this.submitting = false;
    },
    onFocus() {
      let e = this.$refs.EntryInput.getNativeElement();
      setTimeout(()=> {
        e.setSelectionRange(e.value.length, e.value.length)
      }, 1)  
      console.log(e);
      //sets caret to end, but doesn't work in Safari :(
      /**var el = this.$refs.EntryInput.getNativeElement()
      if (typeof el.selectionStart == "number") {
          el.selectionStart = el.selectionEnd = el.value.length;
      } else if (typeof el.createTextRange != "undefined") {
          el.focus();
          var range = el.createTextRange();
          range.collapse(false);
          range.select();
      }*/
    },
    parseTime(time) {
      const mins_secs = time.split(":");
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
      timeString: "0:00",
    };
  },
  computed: {},
};
</script>
<style scoped>
.inputField {
  margin: 2rem;
}
.submitButton {
  margin-left: 2rem;
}
form {
  margin-bottom: 2rem;
  max-width: 300px;
}
[class*="-message"] {
  font-weight: 500;
}

.error-message {
  color: #d33c40;
  margin-left: 2rem;
}

.success-message {
  color: #32a95d;
  margin-left: 2rem;
}
</style>