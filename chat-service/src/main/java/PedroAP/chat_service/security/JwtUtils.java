package PedroAP.chat_service.security;

public class JwtUtils {

    // Método de validación (impleméntalo según tus necesidades)
    public static boolean validateToken(String token) {
        // Ejemplo básico: verifica que el token no sea nulo ni vacío.
        return token != null && !token.trim().isEmpty();
    }
}
