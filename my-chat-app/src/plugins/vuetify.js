import 'vuetify/styles';
import { createVuetify } from 'vuetify';
import { aliases, mdi } from 'vuetify/iconsets/mdi';
import { md3 } from 'vuetify/blueprints';  // Opcional: Usa el diseño de Material Design 3

const vuetify = createVuetify({
  blueprint: md3,  // Opcional: Material Design 3
  icons: {
    defaultSet: 'mdi',
    aliases,
    sets: {
      mdi,
    },
  },
  theme: {
    defaultTheme: 'light',
    themes: {
      light: {
        colors: {
          primary: '#6200EE',  // Cambia el color principal
          secondary: '#03DAC6',
          background: '#F5F5F5',
          surface: '#FFFFFF',
          error: '#B00020',
        },
      },
      dark: {
        colors: {
          primary: '#BB86FC',
          secondary: '#03DAC6',
          background: '#121212',
          surface: '#1E1E1E',
          error: '#CF6679',
        },
      },
    },
  },
});

export default vuetify;
