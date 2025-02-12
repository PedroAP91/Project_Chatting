import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import vuetify from 'vite-plugin-vuetify';
import * as path from 'path';

export default defineConfig({
  plugins: [
    vue(),
    vuetify({ autoImport: true }),  // Agrega el plugin de Vuetify
  ],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src'),
      'vue': 'vue/dist/vue.esm-bundler.js',  // Mantenemos esta línea
    },
  },
  optimizeDeps: {
    // Excluimos Vuetify para que Vite no intente pre-optimizarlo
    exclude: ['vuetify']
  },
  define: {
    global: 'globalThis'
  },
  server: {
    port: 5176,  // Puerto configurado
  },
});