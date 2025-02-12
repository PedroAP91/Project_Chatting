<template>
  <v-container>
    <v-card>
      <v-card-title>Chat en tiempo real (STOMP)</v-card-title>
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
import { useRouter } from 'vue-router';
import { refreshToken } from '../utils/auth'; // Asegúrate de que esta función esté bien implementada

// Definición de la interfaz para los mensajes del chat
interface ChatMessage {
  from: string;
  text: string;
}

export default {
  name: 'ChatPage',
  setup() {
    const router = useRouter();
    // Se carga el historial de mensajes del localStorage para persistencia
    const messages = ref<ChatMessage[]>(JSON.parse(localStorage.getItem('messages') || '[]'));
    const message = ref('');
    const isConnected = ref(false);
    const client = ref<Client | null>(null);

    const connectToWebSocket = () => {
      // Obtenemos el token usando la clave "accessToken"
      const token = localStorage.getItem("accessToken");
      console.log("Token en ChatPage:", token);
      if (!token) {
        console.warn("⚠ No se encontró accessToken en localStorage. Redirigiendo a login...");
        router.push('/login');
        return;
      }

      // Configuración del cliente STOMP usando SockJS
      client.value = new Client({
        webSocketFactory: () => new SockJS("http://localhost:8081/chat"),
        reconnectDelay: 5000,
        debug: (msg) => console.log("STOMP debug:", msg),
        connectHeaders: {
          Authorization: `Bearer ${token}`,
        },
        onConnect: (frame) => {
          console.log("✅ Conectado con STOMP:", frame);
          isConnected.value = true;

          // Suscripción para recibir el historial de mensajes
          client.value?.subscribe("/topic/history", (messageFrame: IMessage) => {
            try {
              console.log("Historial recibido:", messageFrame.body);
              messages.value = JSON.parse(messageFrame.body);
              localStorage.setItem("messages", JSON.stringify(messages.value));
            } catch (error) {
              console.error("Error al parsear el historial:", error);
            }
          });

          // Suscripción para recibir nuevos mensajes en tiempo real
          client.value?.subscribe("/topic/messages", (messageFrame: IMessage) => {
            try {
              const newMessage = JSON.parse(messageFrame.body) as ChatMessage;
              messages.value.push(newMessage);
            } catch (error) {
              console.error("Error al parsear mensaje:", error);
            }
          });
        },
        onStompError: (frame) => {
          console.error("❌ Error STOMP:", frame);
        },
      });

      client.value.activate();
    };

    const sendMessage = async () => {
      if (message.value.trim() === '') return;
      if (!client.value || !isConnected.value) {
        console.error("⚠ No hay conexión WebSocket.");
        return;
      }

      // Se verifica (y refresca) el token antes de enviar el mensaje
      const isTokenValid = await refreshToken();
      if (!isTokenValid) {
        console.warn("⚠ El token es inválido. Redirigiendo a login.");
        alert("Sesión expirada. Por favor, inicia sesión nuevamente.");
        router.push('/login');
        return;
      }

      const msg: ChatMessage = { from: 'UsuarioX', text: message.value };

      client.value.publish({
        destination: '/app/sendMessage',
        body: JSON.stringify(msg),
        headers: {
          Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
      });

      console.log("📤 Mensaje enviado:", msg);
      message.value = '';
    };

    // Sincroniza el historial de mensajes en el localStorage para persistencia
    watch(messages, (newMessages) => {
      localStorage.setItem('messages', JSON.stringify(newMessages));
    }, { deep: true });

    onMounted(() => {
      connectToWebSocket();
    });

    return { messages, message, sendMessage, isConnected };
  }
};
</script>

<style scoped>
/* Puedes agregar aquí tus estilos para personalizar el componente */
</style>
