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

    // Método actualizado para aceptar email, password y nick
    public UserDTO registerUser(String email, String password, String nick) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("El email ya está registrado");
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setNick(nick);  // Asigna el nickname

        User savedUser = userRepository.save(newUser);

        // Construir el UserDTO con el nick
        return new UserDTO(savedUser.getId(), savedUser.getEmail(), savedUser.getNick());
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            // Genera el token incluyendo el nick
            String token = jwtUtils.generateAccessToken(email, user.getNick());
            System.out.println("Token generado: " + token);
            return token;
        } else {
            throw new RuntimeException("Credenciales inválidas");
        }
    }



    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return new UserDTO(user.getId(), user.getEmail(), user.getNick());
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return new UserDTO(user.getId(), user.getEmail(), user.getNick());
    }
}
