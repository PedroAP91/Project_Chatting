package PedroAP.chat_service.controller;

import PedroAP.chat_service.controller.ChatController;
import PedroAP.chat_service.model.ChatMessage;
import com.proyect.chatting.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private ChatController chatController;

    @Test
    void testSendMessage() {
        // Simula que el token es válido
        when(jwtUtils.validateToken("mock-token")).thenReturn(true);

        // Crea un mensaje simulado
        ChatMessage message = new ChatMessage();
        message.setText("Hola Mundo!");

        // Llama al método con el token de prueba
        chatController.sendMessage("mock-token", message);

        // Verifica que se llamó a la validación del token
        verify(jwtUtils).validateToken("mock-token");
    }
}
