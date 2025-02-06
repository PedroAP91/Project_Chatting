package PedroAP.chat_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import com.proyect.chatting.security.JwtUtils;


import java.util.List;
import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        List<String> tokenList = request.getHeaders().get("Sec-WebSocket-Protocol");

        if (tokenList != null && !tokenList.isEmpty()) {
            String token = tokenList.get(0);

            if (jwtUtils.validateToken(token)) {
                String email = jwtUtils.getSubjectFromToken(token);
                if (email != null) {
                    attributes.put("jwt", token);
                    attributes.put("email", email);  // Añade el email a los atributos si es necesario
                    return true;
                }
            }
        }

        return false;
    }




    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // No se requiere acción después del handshake
    }

    // Método opcional para extraer token desde la URI
    /*private String extractTokenFromUri(String uri) {
        // Implementa la lógica de extracción, por ejemplo, usando URIBuilder o String manipulation
    }*/
}