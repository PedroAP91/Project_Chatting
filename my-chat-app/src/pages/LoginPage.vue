<template>
  <v-app>
    <v-main>
      <v-container class="d-flex justify-center align-center" style="height: 100vh;">
        <v-card class="pa-6" max-width="400" elevation="3">
          <v-card-title class="justify-center">
            <h1>Iniciar sesión</h1>
          </v-card-title>

          <v-form @submit.prevent="login" ref="loginForm">
            <v-text-field
              v-model="email"
              label="Correo electrónico"
              type="email"
              required
              outlined
              class="mb-4"
            ></v-text-field>

            <v-text-field
              v-model="password"
              label="Contraseña"
              type="password"
              required
              outlined
              class="mb-4"
            ></v-text-field>

            <v-btn color="primary" block type="submit" class="mb-4">
              Entrar
            </v-btn>

            <v-alert v-if="error" type="error" dense class="mt-2">
              {{ error }}
            </v-alert>
          </v-form>

          <!-- Enlace para ir a la página de registro -->
          <div class="text-center mt-4">
            <span>¿No tienes cuenta?</span>
            <v-btn variant="text" color="secondary" @click="goToRegister">
              Regístrate aquí
            </v-btn>
          </div>
        </v-card>
      </v-container>
    </v-main>
  </v-app>
</template>

<script lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  name: 'LoginPage',
  setup() {
    const router = useRouter();
    const email = ref('');
    const password = ref('');
    const error = ref('');
    const loginForm = ref(null);

    const login = async () => {
      try {
        const response = await axios.post('http://localhost:8080/auth/login', {
          email: email.value,
          password: password.value,
        });
        // Extraer y almacenar los tokens correctamente
        const { accessToken, refreshToken } = response.data;
        console.log("Token recibido:", accessToken);
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
        localStorage.removeItem('messages');
        console.log("LocalStorage después del login:", {
          accessToken: localStorage.getItem('accessToken'),
          refreshToken: localStorage.getItem('refreshToken')
        });
        router.push({ name: 'RoomSelection' });
      } catch (err) {
        error.value = 'Credenciales inválidas';
        console.error("Error en login:", err);
      }
    };

    const goToRegister = () => {
      router.push('/register');
    };

    return { email, password, error, login, loginForm, goToRegister };
  },
};
</script>
