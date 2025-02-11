// src/main.ts
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify';

import '@mdi/font/css/materialdesignicons.css';  // Importar iconos Material Design


createApp(App)
  .use(router)
  .use(vuetify)  // Aquí lo aplicamos
  .mount('#app')
