package PedroAP.chat_service.controller;

import PedroAP.chat_service.model.ChatMessage;
import com.proyect.chatting.security.JwtUtils;

import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final JwtUtils jwtUtils;

    public ChatController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public ChatMessage sendMessage(ChatMessage message) {
        // Aquí se invoca el método validateToken de JwtUtils
        String token = "someToken"; // En un escenario real, extraerías el token de los headers de la solicitud
        if (jwtUtils.validateToken(token)) {
            // Si el token es válido, procesamos el mensaje
            return message;
        } else {
            throw new IllegalArgumentException("Token inválido");
        }
    }
}
