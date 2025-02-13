<template>
  <v-app>
    <v-main>
      <v-container class="d-flex justify-center align-center" style="height: 100vh;">
        <v-card class="pa-6" max-width="400" elevation="3">
          <v-card-title class="justify-center">
            <h1>Registro</h1>
          </v-card-title>

          <v-form @submit.prevent="register" ref="registerForm">
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

            <!-- Nuevo campo para el nickname -->
            <v-text-field
              v-model="nick"
              label="Nickname"
              type="text"
              required
              outlined
              class="mb-4"
            ></v-text-field>

            <v-btn color="primary" block type="submit" class="mb-4">
              Registrarse
            </v-btn>

            <v-alert v-if="error" type="error" dense class="mt-2">
              {{ error }}
            </v-alert>
          </v-form>

          <!-- Enlace para volver al login -->
          <div class="text-center mt-4">
            <span>¿Ya tienes cuenta?</span>
            <v-btn variant="text" color="secondary" @click="goToLogin">
              Inicia sesión aquí
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
import authService from '@/services/authService';

export default {
  name: 'RegisterPage',
  setup() {
    const router = useRouter();
    const email = ref('');
    const password = ref('');
    const nick = ref(''); // Nuevo campo para el nickname
    const error = ref('');
    const registerForm = ref(null);

    const register = async () => {
      try {
        // Envia el nickname junto con email y password
        await authService.register({ email: email.value, password: password.value, nick: nick.value });
        // Una vez registrado, redirige al login
        router.push('/login');
      } catch (err) {
        error.value = 'Error en el registro';
        console.error("Error en registro:", err);
      }
    };

    const goToLogin = () => {
      router.push('/login');
    };

    return { email, password, nick, error, register, registerForm, goToLogin };
  },
};
</script>
