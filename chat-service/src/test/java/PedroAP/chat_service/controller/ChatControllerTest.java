package PedroAP.chat_service.controller;

import PedroAP.chat_service.model.ChatMessage;
import com.proyect.chatting.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ChatControllerTest {

    private ChatController chatController;
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = mock(JwtUtils.class);
        chatController = new ChatController(jwtUtils);
    }

    @Test
    void testSendMessage() {
        // Configurar el mock para que devuelva resultados específicos
        Mockito.when(jwtUtils.validateToken(Mockito.anyString())).thenReturn(true);

        // Preparar el mensaje de prueba
        ChatMessage inputMessage = new ChatMessage("user", "Hello, world!");

        // Ejecutar el método
        ChatMessage result = chatController.sendMessage(inputMessage);

        // Verificar el resultado
        assertNotNull(result);
        assertEquals("user", result.getFrom());
        assertEquals("Hello, world!", result.getText());

        // Verificar que se llamó a validateToken exactamente una vez
        Mockito.verify(jwtUtils, times(1)).validateToken(Mockito.anyString());
    }
}
