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
        registry.addEndpoint("/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String authHeader = accessor.getFirstNativeHeader("Authorization");
                    System.out.println("Token recibido en WebSocket: " + authHeader);

                    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                        System.err.println("Error: Header de autorización faltante o incorrecto.");
                        return null;
                    }

                    String token = authHeader.substring(7);
                    System.out.println("Token extraído: " + token);

                    if (!jwtUtils.validateToken(token)) {
                        System.err.println("Error: Token inválido.");
                        return null;
                    }

                    String usuario = jwtUtils.getSubjectFromToken(token);
                    System.out.println("Usuario autenticado: " + usuario);
                    accessor.setUser(new StompPrincipal(usuario));
                }

                return message;
            }

        });
    }


}


