<template>
  <v-container>
    <v-card>
      <v-card-title>Chat en tiempo real (STOMP)</v-card-title>
      <v-card-text>
        <div style="max-height: 300px; overflow-y: auto;">
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
        <v-btn color="primary" @click="sendMessage">Enviar</v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script>
import { ref, onMounted } from 'vue'
import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'

export default {
  name: 'ChatRealTime',
  setup() {
    const messages = ref([])
    const message = ref('')
    let client = null

    onMounted(() => {
      // Conecta al servidor WebSocket (ajusta la URL real)
      client = new Client({
        webSocketFactory: () => new SockJS('http://localhost:8081/chat'),
        reconnectDelay: 5000,
        connectHeaders: {
          // Si necesitas enviar token, por ejemplo:
          // Authorization: `Bearer ${localStorage.getItem('token')}`
        },
        onConnect: (frame) => {
          console.log('Conectado con STOMP:', frame)
          // Suscribirse al topic
          client.subscribe('/topic/messages', (messageFrame) => {
            const body = JSON.parse(messageFrame.body)
            messages.value.push(body)
          })
        },
        onStompError: (frame) => {
          console.error('Error STOMP:', frame)
        },
      })

      client.activate()
    })

    const sendMessage = () => {
      if (message.value.trim() !== '') {
        const msg = { from: 'UsuarioX', text: message.value }  // Aquí puedes personalizar el "from"
        // Publicar el mensaje en el destino configurado en el servidor (por ejemplo, /app/sendMessage)
        client.publish({
          destination: '/app/sendMessage',
          body: JSON.stringify(msg)
        })
        message.value = ''
      }
    }

    return { messages, message, sendMessage }
  },
}
</script>
