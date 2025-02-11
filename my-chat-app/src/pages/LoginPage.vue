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

            <v-btn color="primary" block type="submit" class="mb-4">Entrar</v-btn>

            <v-alert v-if="error" type="error" dense class="mt-2">
              {{ error }}
            </v-alert>
          </v-form>
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
        const { token } = response.data;
        localStorage.setItem('token', token);
        router.push('/chat');
      } catch (err) {
        error.value = 'Credenciales inválidas';
      }
    };

    return { email, password, error, login, loginForm };
  },
};
</script>

<style scoped>
h1 {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 16px;
}

.v-main {
  background-color: #f5f5f5;
}
</style>
