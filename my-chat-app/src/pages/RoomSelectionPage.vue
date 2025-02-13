<template>
    <v-container>
      <v-card class="pa-6" max-width="400" elevation="3">
        <v-card-title class="justify-center">
          <h1>Selecciona tu Sala</h1>
        </v-card-title>
        <v-card-text>
          <v-row>
            <v-col cols="12">
              <v-btn color="primary" block @click="createRoom">
                Crear Sala de Chat
              </v-btn>
            </v-col>
            <v-col cols="12">
              <v-text-field
                v-model="roomCodeInput"
                label="Código de sala"
                outlined
                dense
              ></v-text-field>
            </v-col>
            <v-col cols="12">
              <v-btn color="secondary" block @click="joinRoom">
                Unirse a Sala
              </v-btn>
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>
    </v-container>
  </template>
  
  <script lang="ts">
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';
  
  export default {
    name: 'RoomSelectionPage',
    setup() {
      const router = useRouter();
      const roomCodeInput = ref('');
  
      const generateRoomCode = (): string => {
        // Genera un número aleatorio de 5 dígitos (entre 10000 y 99999)
        return Math.floor(Math.random() * 90000 + 10000).toString();
      };
  
      const createRoom = () => {
        const newRoomCode = generateRoomCode();
        // Redirige a la sala de chat pasando el código como parámetro
        router.push({ name: 'ChatRoom', params: { roomCode: newRoomCode } });
      };
  
      const joinRoom = () => {
        if (roomCodeInput.value.trim() === '') {
          alert('Por favor, ingresa un código de sala.');
          return;
        }
        router.push({ name: 'ChatRoom', params: { roomCode: roomCodeInput.value } });
      };
  
      return { roomCodeInput, createRoom, joinRoom };
    },
  };
  </script>
  