package PedroAP.chat_service.config;

import PedroAP.chat_service.security.JwtUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // Se asume que el token se envía como parámetro de la URL, por ejemplo: /chat?token=xxx
        List<String> tokenList = request.getHeaders().get("Sec-WebSocket-Protocol");
        String token = null;
        if (tokenList != null && !tokenList.isEmpty()) {
            // Aquí podrías obtener el token del encabezado o modificar la estrategia para extraerlo
            token = tokenList.get(0);
        }

        // O también puedes obtenerlo desde la URI, según tu diseño
        // String token = extractTokenFromUri(request.getURI().toString());

        // Valida el token usando una utilidad (debes implementarla o usar la ya existente en Auth Service)
        if (token != null && JwtUtils.validateToken(token)) {
            // Puedes almacenar información en 'attributes' para usarla en el contexto del WebSocket
            attributes.put("jwt", token);
            return true;
        }
        // Si el token es inválido, se rechaza el handshake
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