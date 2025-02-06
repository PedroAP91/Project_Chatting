package PedroAP.auth_service.service;

import PedroAP.auth_service.dto.UserDTO;
import PedroAP.auth_service.model.User;
import PedroAP.auth_service.repository.UserRepository;
import com.proyect.chatting.security.JwtUtils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public UserDTO registerUser(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("El email ya está registrado");
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        User savedUser = userRepository.save(newUser);

        return new UserDTO(savedUser.getId(), savedUser.getEmail());
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        // Comparar contraseña usando BCrypt
        if (passwordEncoder.matches(password, user.getPassword())) {
            // Generar el token con jwtUtils
            String token = jwtUtils.generateToken(email);
            // Puedes agregar un log para confirmar
            System.out.println("Token generado: " + token);
            return token;
        } else {
            // Si la contraseña no coincide, lanza una excepción o maneja el error
            throw new RuntimeException("Credenciales inválidas");
        }
    }
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return new UserDTO(user.getId(), user.getEmail());
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return new UserDTO(user.getId(), user.getEmail());
    }
}
