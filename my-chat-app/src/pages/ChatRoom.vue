<template>
    <v-container>
      <v-card>
        <v-card-title>
          <span>Sala de Chat: {{ roomCode }}</span>
          <v-spacer></v-spacer>
          <v-btn variant="text" color="secondary" @click="inviteRoom">
            Invitar
          </v-btn>
        </v-card-title>
        <v-card-text>
          <div v-if="!isConnected" class="mb-2" style="color: gray;">
            Conectando al chat...
          </div>
          <div v-else style="max-height: 300px; overflow-y: auto;">
            <div v-for="(msg, index) in messages" :key="index" class="mb-2">
              <strong>{{ msg.from }}:</strong> {{ msg.text }}
            </div>
          </div>
        </v-card-text>
        <v-card-actions>
          <v-text-field
            v-model="message"
            label="Escribe tu mensaje..."
            outlined
            dense
            hide-details
            @keyup.enter="sendMessage"
          ></v-text-field>
          <v-btn :disabled="!isConnected" color="primary" @click="sendMessage">
            Enviar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-container>
  </template>
  
  <script lang="ts">
  import { ref, onMounted, watch } from 'vue';
  import { Client, type IMessage } from '@stomp/stompjs';
  import SockJS from 'sockjs-client';
  import { useRouter, useRoute } from 'vue-router';
  
  interface ChatMessage {
    from: string;
    text: string;
  }
  
  export default {
    name: 'ChatRoom',
    setup() {
      const router = useRouter();
      const route = useRoute();
      const roomCode = ref(route.params.roomCode as string || '');
      const messages = ref<ChatMessage[]>(JSON.parse(localStorage.getItem('messages') || '[]'));
      const message = ref('');
      const isConnected = ref(false);
      const client = ref<Client | null>(null);
  
      // Limpia el historial al iniciar la sala
      localStorage.removeItem('messages');
  
      const connectToWebSocket = () => {
        const token = localStorage.getItem("accessToken");
        if (!token) {
          router.push('/login');
          return;
        }
        client.value = new Client({
          webSocketFactory: () => new SockJS("http://localhost:8081/chat"),
          reconnectDelay: 5000,
          debug: (msg) => console.log("STOMP debug:", msg),
          connectHeaders: {
            Authorization: `Bearer ${token}`,
          },
          onConnect: (frame) => {
            console.log("Conectado con STOMP:", frame);
            isConnected.value = true;
            // Suscripción para el historial de mensajes de la sala
            client.value?.subscribe("/topic/history/" + roomCode.value, (messageFrame: IMessage) => {
              console.log("Historial recibido (raw):", messageFrame.body);
              try {
                messages.value = JSON.parse(messageFrame.body);
                localStorage.setItem("messages", JSON.stringify(messages.value));
                console.log("Historial actualizado:", messages.value);
              } catch (error) {
                console.error("Error al parsear el historial:", error);
              }
            });
            // Suscripción para nuevos mensajes en tiempo real
            client.value?.subscribe("/topic/messages/" + roomCode.value, (messageFrame: IMessage) => {
              console.log("Mensaje recibido (raw):", messageFrame.body);
              try {
                const newMessage = JSON.parse(messageFrame.body) as ChatMessage;
                messages.value.push(newMessage);
                console.log("Array de mensajes actualizado:", messages.value);
              } catch (error) {
                console.error("Error al parsear mensaje:", error);
              }
            });
          },
          onStompError: (frame) => {
            console.error("Error STOMP:", frame);
          },
        });
        client.value.activate();
      };
  
      const sendMessage = async () => {
        if (message.value.trim() === '') return;
        if (!client.value || !isConnected.value) {
          console.error("No hay conexión WebSocket.");
          return;
        }
        const accessToken = localStorage.getItem('accessToken');
        if (!accessToken) {
          alert("No se encontró access token. Inicia sesión nuevamente.");
          router.push('/login');
          return;
        }
        const msg: ChatMessage = { from: 'Usuario', text: message.value }; // Reemplaza 'Usuario' por el nick real
        client.value.publish({
          destination: '/app/sendMessage/' + roomCode.value,
          body: JSON.stringify(msg),
          headers: {
            Authorization: `Bearer ${accessToken}`
          }
        });
        console.log("Mensaje enviado:", msg);
        message.value = '';
      };
  
      const inviteRoom = async () => {
        try {
          await navigator.clipboard.writeText(roomCode.value);
          alert(`Código copiado al portapapeles: ${roomCode.value}`);
        } catch (error) {
          console.error('Error copiando el código al portapapeles:', error);
        }
      };
  
      watch(messages, (newMessages) => {
        localStorage.setItem('messages', JSON.stringify(newMessages));
      }, { deep: true });
  
      onMounted(() => {
        connectToWebSocket();
      });
  
      return { roomCode, messages, message, sendMessage, isConnected, inviteRoom };
    }
  };
  </script>
  
  <style scoped>
  /* Estilos personalizados */
  </style>
  