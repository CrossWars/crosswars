<template lang="">
    <div>
        
    </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
    mounted() {
        const telegramScript = document.createElement('script')
        telegramScript.setAttribute('src', 'https://www.google.com/recaptcha/api.js')
        telegramScript.setAttribute('data-telegram-login', 'samplebot')
        telegramScript.setAttribute('data-size', 'large')
        telegramScript.setAttribute('data-onauth', 'onTelegramAuth(user)')
    },
    methods: {
        onTelegramAuth(user: any) {
            console.log(user);
            alert('Logged in as ' + user.first_name + ' ' + user.last_name + ' (' + user.id + (user.username ? ', @' + user.username : '') + ')');
        }
    }
});
</script>