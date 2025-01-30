package PedroAP.auth_service.controller;

import PedroAP.auth_service.dto.UserDTO;
import PedroAP.auth_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

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
        public String token;

        public LoginResponse(String token) {
            this.token = token;
        }
    }

    // Endpoint para registro: POST /auth/register
    @PostMapping("/register")
    public UserDTO register(@RequestBody RegisterRequest request) {
        return userService.registerUser(request.email, request.password);
    }

    // Endpoint para login: POST /auth/login
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        String token = userService.loginUser(request.email, request.password);
        return new LoginResponse(token);
    }

    // Endpoint para obtener usuario por ID: GET /auth/user/{id}
    @GetMapping("/user/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Endpoint para obtener usuario por email: GET /auth/user?email={email}
    @GetMapping("/user")
    public UserDTO getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }
}
