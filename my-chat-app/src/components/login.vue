<template>
    <div class="login">
      <h2>Iniciar Sesión</h2>
      <form @submit.prevent="handleLogin">
        <input v-model="email" type="email" placeholder="Email" required />
        <input v-model="password" type="password" placeholder="Contraseña" required />
        <button type="submit">Entrar</button>
      </form>
      <p v-if="error">{{ error }}</p>
    </div>
  </template>
  
  <script>
  import authService from '../services/authService';
  
  export default {
    data() {
      return {
        email: '',
        password: '',
        error: ''
      };
    },
    methods: {
      async handleLogin() {
        try {
          const response = await authService.login({ email: this.email, password: this.password });
          // Guarda el token en el almacenamiento local o en un gestor de estado
          localStorage.setItem('token', response.data.token);
          // Redirige a la vista del chat
          this.$router.push('/chat');
        } catch (err) {
          this.error = 'Error al iniciar sesión. Verifica tus credenciales.';
        }
      }
    }
  };
  </script>
  
  <style scoped>
  /* Agrega estilos según necesites */
  </style>
  