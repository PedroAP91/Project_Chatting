<template>
  <v-container>
    <v-card>
      <v-card-title>Chat en tiempo real (STOMP)</v-card-title>
      <v-card-text>
        <div v-if="!isConnected" class="mb-2" style="color: gray;">Conectando al chat...</div>
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
import { ref, onMounted, watch } from 'vue'
import { Client, type Message } from '@stomp/stompjs'
import SockJS from 'sockjs-client'

interface ChatMessage {
  from: string
  text: string
}

export default {
  name: 'ChatPage',
  setup() {
    const messages = ref<ChatMessage[]>([])
    const message = ref('')
    const isConnected = ref(false)
    const client = ref<Client | null>(null)

    onMounted(() => {
      client.value = new Client({
        webSocketFactory: () => new SockJS('http://localhost:8081/chat'),
        reconnectDelay: 5000,
        debug: (msg) => console.log('STOMP debug:', msg),
        connectHeaders: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        },
        onConnect: (frame) => {
          console.log('Conectado con STOMP:', frame)
          isConnected.value = true
        },
        onStompError: (frame) => {
          console.error('Error STOMP:', frame)
        },
      })
      client.value.activate()
    })

    // Suscribirse al tópico cuando se establezca la conexión
    watch(isConnected, (newVal) => {
      if (newVal && client.value) {
        client.value.subscribe('/topic/messages', (messageFrame: Message) => {
          console.log('Mensaje recibido:', messageFrame.body)
          const body: ChatMessage = JSON.parse(messageFrame.body)
          messages.value.push(body)
        })
      }
    })

    const sendMessage = () => {
  if (message.value.trim() === '') return;
  if (!client.value || !isConnected.value) {
    console.error("No underlying STOMP connection, please wait.");
    return;
  }
  const msg: ChatMessage = { from: 'UsuarioX', text: message.value };
  client.value.publish({
    destination: '/app/sendMessage',
    body: JSON.stringify(msg)
  });
  console.log('Mensaje enviado:', msg);
  message.value = '';
}

    return { messages, message, sendMessage, isConnected }
  },
}
</script>

<style scoped>
/* Personaliza tus estilos si es necesario */
</style>
