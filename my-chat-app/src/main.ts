// src/main.ts
import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import vuetify from './plugins/vuetify';
import { createPinia } from 'pinia';

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);    // Aquí se integra Pinia
app.use(router);
app.use(vuetify);
app.mount('#app');
