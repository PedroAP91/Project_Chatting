package PedroAP.chat_service.controller;

import PedroAP.chat_service.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    // Cuando el cliente envíe un mensaje a "/app/message", este método lo procesará
    @MessageMapping("/message")
    // Y retransmitirá el mensaje a todos los suscritos al destino "/topic/messages"
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) throws Exception {
        // Aquí puedes agregar lógica de procesamiento (por ejemplo, guardar en base de datos o filtrar contenido)
        return message;
    }
}