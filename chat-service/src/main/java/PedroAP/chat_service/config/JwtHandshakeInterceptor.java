package PedroAP.chat_service.config;

import com.proyect.chatting.security.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtils jwtUtils;

    public JwtHandshakeInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        List<String> authHeaders = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        String token = null;

        if (authHeaders != null && !authHeaders.isEmpty()) {
            token = authHeaders.get(0);
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
        }

        if (token != null) {
            System.out.println("🔹 Token recibido: " + token);

            if (jwtUtils.validateToken(token, false)) {
                String email = jwtUtils.getSubjectFromToken(token, false);
                System.out.println("✅ Usuario autenticado: " + email);
                attributes.put("jwt", token);
                attributes.put("email", email);
                return true;
            } else {
                System.err.println("⛔ Error: Token inválido en WebSocket handshake.");
            }
        } else {
            System.err.println("⛔ Error: No se encontró token en WebSocket handshake.");
        }

        return false;
    }


    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // No se requiere acción después del handshake
    }
}
