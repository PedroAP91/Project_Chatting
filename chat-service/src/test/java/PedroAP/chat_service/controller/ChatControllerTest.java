package PedroAP.chat_service.controller;

import PedroAP.chat_service.controller.ChatController;
import PedroAP.chat_service.model.ChatMessage;
import com.proyect.chatting.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private ChatController chatController;


    @Test
    public void testSendMessage() {
        String token = "mock-token";

        // Usa lenient() para evitar el error
        lenient().when(jwtUtils.validateToken(anyString(), eq(false))).thenReturn(true);
        lenient().when(jwtUtils.getSubjectFromToken(anyString(), eq(false))).thenReturn("testUser");

        ChatMessage message = new ChatMessage("testUser", "Hola mundo");

        ChatMessage response = chatController.sendMessage("Bearer " + token, message);

        assertNotNull(response);
        assertEquals("Hola mundo", response.getText());
    }

}
