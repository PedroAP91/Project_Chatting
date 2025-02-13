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
        public String nick; // Nuevo campo para el nickname
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

    // Endpoint para registro: POST /auth/register
    @PostMapping("/register")
    public UserDTO register(@RequestBody RegisterRequest request) {
        // Se asume que userService.registerUser ahora acepta tres parámetros: email, password y nick.
        return userService.registerUser(request.email, request.password, request.nick);
    }

    // Endpoint para login: POST /auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String accessToken = userService.loginUser(request.email, request.password);
        String refreshToken = jwtUtils.generateRefreshToken(request.email);

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        ));
    }

    // Otros endpoints se mantienen igual...
}
