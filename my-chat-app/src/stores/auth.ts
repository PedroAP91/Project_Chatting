// src/stores/auth.ts
import { defineStore } from 'pinia';
import authService from '@/services/authService';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    accessToken: localStorage.getItem('accessToken') || '',
    refreshToken: localStorage.getItem('refreshToken') || '',
    user: null as null | { email: string; /* otros campos */ },
  }),
  getters: {
    isAuthenticated: (state) => !!state.accessToken,
  },
  actions: {
    async login(credentials: { email: string; password: string }) {
      try {
        const response = await authService.login(credentials);
        const { accessToken, refreshToken } = response.data;
        this.setTokens(accessToken, refreshToken);
        // Opcional: obtener datos adicionales del usuario
      } catch (error) {
        console.error('Error en login:', error);
        throw error;
      }
    },
    async register(user: { email: string; password: string }) {
      try {
        await authService.register(user);
        // Opcional: redirigir o iniciar sesión automáticamente tras el registro
      } catch (error) {
        console.error('Error en registro:', error);
        throw error;
      }
    },
    async refreshTokenAction() {
      if (!this.refreshToken) {
        console.error("No hay refreshToken almacenado");
        return false;
      }
      try {
        const response = await authService.refreshToken(this.refreshToken);
        const { accessToken } = response.data;
        this.setTokens(accessToken, this.refreshToken); // Asumiendo que el refreshToken no cambia
        return true;
      } catch (error) {
        console.error("Error al refrescar token:", error);
        this.clearAuth();
        return false;
      }
    },
    setTokens(accessToken: string, refreshToken: string) {
      this.accessToken = accessToken;
      this.refreshToken = refreshToken;
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
    },
    clearAuth() {
      this.accessToken = '';
      this.refreshToken = '';
      this.user = null;
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
    },
  },
});
