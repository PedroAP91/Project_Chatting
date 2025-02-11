// src/composables/useStompClient.ts
import { ref } from 'vue'
import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'

export function useStompClient() {
  const client = ref<Client | null>(null)
  const isConnected = ref(false)

  const connect = () => {
    client.value = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8081/chat'),
      reconnectDelay: 5000,
      onConnect: (frame) => {
        console.log('Conectado con STOMP:', frame)
        isConnected.value = true
      },
      onStompError: (frame) => {
        console.error('Error STOMP:', frame)
      },
    })
    client.value.activate()
  }

  return { client, isConnected, connect }
}
