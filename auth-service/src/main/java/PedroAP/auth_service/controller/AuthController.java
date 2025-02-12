package PedroAP.auth_service.controller;

import PedroAP.auth_service.dto.UserDTO;
import PedroAP.auth_service.service.UserService;
import com.proyect.chatting.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Autowired
    public AuthController(JwtUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    // DTO para la petición de registro
    static class RegisterRequest {
        public String email;
        public String password;
    }

    // DTO para la petición de login
    static class LoginRequest {
        public String email;
        public String password;
    }

    // DTO para la respuesta de login
    static class LoginResponse {
        public String accessToken;
        public String refreshToken;

        public LoginResponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    // ✅ Endpoint para registro: POST /auth/register
    @PostMapping("/register")
    public UserDTO register(@RequestBody RegisterRequest request) {
        return userService.registerUser(request.email, request.password);
    }

    // ✅ Endpoint para login: POST /auth/login
    // ✅ Método ÚNICO para login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String accessToken = userService.loginUser(request.email, request.password);
        String refreshToken = jwtUtils.generateRefreshToken(request.email); // ✅ Generar refreshToken

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken // ✅ Incluir refreshToken en la respuesta
        ));
    }


    // ✅ Endpoint para obtener usuario por ID: GET /auth/user/{id}
    @GetMapping("/user/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // ✅ Endpoint para obtener usuario por email: GET /auth/user?email={email}
    @GetMapping("/user")
    public UserDTO getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }

    // ✅ Endpoint para refrescar el token: POST /auth/refresh-token
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("🚨 Error: No se proporcionó un refresh token válido.");
        }

        String extractedToken = refreshToken.substring(7); // Eliminar "Bearer "

        if (!jwtUtils.validateToken(extractedToken, true)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("🚨 Refresh Token inválido o expirado.");
        }

        String email = jwtUtils.getSubjectFromToken(extractedToken, true);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("🚨 No se pudo extraer el usuario del token.");
        }

        String newAccessToken = jwtUtils.generateAccessToken(email);
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}
