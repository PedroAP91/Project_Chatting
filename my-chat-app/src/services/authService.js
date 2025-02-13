// src/services/authService.js
import axios from 'axios';

const API_URL = 'http://localhost:8080/auth';

export default {
  register(user) {
    return axios.post(`${API_URL}/register`, user);
  },
  login(credentials) {
    return axios.post(`${API_URL}/login`, credentials);
  },
  refreshToken(refreshToken) {
    return axios.post(`${API_URL}/refresh-token`, null, {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${refreshToken}`,
      },
    });
  },
};
