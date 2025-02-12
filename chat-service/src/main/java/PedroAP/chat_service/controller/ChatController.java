package PedroAP.chat_service.controller;

import PedroAP.chat_service.model.ChatMessage;
import com.proyect.chatting.security.JwtUtils;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    private final JwtUtils jwtUtils;
    private final List<ChatMessage> messageHistory = new ArrayList<>();

    public ChatController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(@Header("Authorization") String token, ChatMessage message) {
        // Extraer el token sin "Bearer "
        String extractedToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        // Validar el token antes de permitir enviar el mensaje
        if (!jwtUtils.validateToken(extractedToken, false)) {
            throw new SecurityException("Token inválido");
        }

        System.out.println("Mensaje recibido en STOMP: " + message.getText());

        // Guardar mensaje en historial (máximo 50 mensajes)
        if (messageHistory.size() >= 50) {
            messageHistory.remove(0); // Eliminar el más antiguo
        }
        messageHistory.add(message);

        return message;
    }

    @MessageMapping("/history")
    @SendTo("/topic/history")
    public List<ChatMessage> getHistory() {
        return messageHistory;
    }
}
