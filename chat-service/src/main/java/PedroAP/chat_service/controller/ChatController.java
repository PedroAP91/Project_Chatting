package PedroAP.chat_service.controller;

import PedroAP.chat_service.model.ChatMessage;
import com.proyect.chatting.security.JwtUtils;
import org.springframework.messaging.handler.annotation.Header;
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
    public ChatMessage sendMessage(@Header("Authorization") String token, ChatMessage message) {
        // Extraer el token si tiene el prefijo "Bearer "
        String extractedToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        // Validar el token antes de permitir enviar el mensaje
        if (!jwtUtils.validateToken(extractedToken)) {
            throw new SecurityException("Token inválido");
        }

        System.out.println("Mensaje recibido en STOMP: " + message.getText());
        return message;
    }
}
