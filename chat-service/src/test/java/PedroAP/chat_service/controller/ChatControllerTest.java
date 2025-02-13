package PedroAP.chat_service.controller;

import PedroAP.chat_service.model.ChatMessage;
import com.proyect.chatting.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private ChatController chatController;

    @Test
    public void testSendMessage() {
        String token = "mock-token";
        String roomCode = "12345";

        // Configuramos el mock para que el token sea válido
        when(jwtUtils.validateToken(anyString(), eq(false))).thenReturn(true);

        // Creamos un mensaje de prueba
        ChatMessage message = new ChatMessage("testUser", "Hola mundo");

        // Llamamos al método sendMessage con roomCode, token y el mensaje
        ChatMessage response = chatController.sendMessage(roomCode, "Bearer " + token, message);

        // Verificamos que la respuesta no sea nula y tenga el texto esperado
        assertNotNull(response);
        assertEquals("Hola mundo", response.getText());
    }
}
