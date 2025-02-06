package PedroAP.chat_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import com.proyect.chatting.security.JwtUtils;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtUtils jwtUtils; // Asegúrate de que esta clase esté correctamente implementada

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Configura el broker simple para los tópicos
        registry.enableSimpleBroker("/topic");
        // Prefijo para mensajes dirigidos a métodos en los controladores
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registra el endpoint del WebSocket y usa allowedOriginPatterns para CORS
        registry.addEndpoint("/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                // Extraemos el header del mensaje
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                // Si es un mensaje de conexión, validamos el token
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String authHeader = accessor.getFirstNativeHeader("Authorization");
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.substring(7);
                        // Validamos el token (aquí puedes personalizar la validación)
                        if (jwtUtils.validateToken(token)){
                            // Si el token es válido, obtenemos el email u otra información del token
                            String email = jwtUtils.getSubjectFromToken(token);
                            // Puedes establecer un usuario autenticado en el contexto del WebSocket
                            accessor.setUser(new StompPrincipal(email));
                        } else {
                            // Si el token es inválido, rechaza el mensaje de conexión
                            return null;
                        }
                    } else {
                        // Si no se envía token, también puedes decidir rechazar la conexión o permitirla de forma anónima
                        return null;
                    }
                }
                return message;
            }
        });
    }
}
