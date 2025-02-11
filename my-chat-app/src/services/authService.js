// src/services/authService.js
import axios from 'axios';

const API_URL = 'http://tu-api.com/auth'; // Cambia esto por la URL real de tu servicio de autenticación

export default {
  register(user) {
    return axios.post(`${API_URL}/register`, user);
  },
  login(credentials) {
    return axios.post(`${API_URL}/login`, credentials);
  }
};