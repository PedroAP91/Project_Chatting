package PedroAP.chat_service.controller;

import PedroAP.chat_service.model.ChatMessage;
import com.proyect.chatting.security.JwtUtils;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class ChatController {

    private final JwtUtils jwtUtils;
    // Mapa para almacenar el historial de mensajes por sala (roomCode)
    private final Map<String, List<ChatMessage>> roomHistories = new ConcurrentHashMap<>();

    public ChatController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @MessageMapping("/sendMessage/{roomCode}")
    @SendTo("/topic/messages/{roomCode}")
    public ChatMessage sendMessage(@DestinationVariable("roomCode") String roomCode,
                                   @Header("Authorization") String token,
                                   @Payload ChatMessage message) {
        String extractedToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        if (!jwtUtils.validateToken(extractedToken, false)) {
            throw new SecurityException("Token inválido");
        }
        // Extraer el nick del token
        String nick = (String) jwtUtils.getClaimFromToken(extractedToken, "nick", false);
        // Si no está, usa el subject como fallback
        if (nick == null) {
            nick = jwtUtils.getSubjectFromToken(extractedToken, false);
        }
        message.setFrom(nick);
        System.out.println("Mensaje recibido en sala " + roomCode + " de " + nick + ": " + message.getText());

        List<ChatMessage> history = roomHistories.computeIfAbsent(roomCode, k -> new ArrayList<>());
        if (history.size() >= 50) {
            history.remove(0);
        }
        history.add(message);
        return message;
    }



    @MessageMapping("/history/{roomCode}")
    @SendTo("/topic/history/{roomCode}")
    public List<ChatMessage> getHistory(@DestinationVariable("roomCode") String roomCode) {
        return roomHistories.getOrDefault(roomCode, Collections.emptyList());
    }
}
