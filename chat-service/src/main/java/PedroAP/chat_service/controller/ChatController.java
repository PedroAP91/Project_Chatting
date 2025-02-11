package PedroAP.chat_service.controller;

import PedroAP.chat_service.model.ChatMessage;
import com.proyect.chatting.security.JwtUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final JwtUtils jwtUtils;

    public ChatController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        // Agrega un log para confirmar que se recibe el mensaje
        System.out.println("Mensaje recibido en STOMP: " + message.getText());
        // Puedes agregar lógica adicional (por ejemplo, persistencia o timestamp) aquí
        return message;
    }
}
