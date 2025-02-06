package PedroAP.auth_service.service;

import PedroAP.auth_service.dto.UserDTO;
import PedroAP.auth_service.model.User;
import PedroAP.auth_service.repository.UserRepository;
import com.proyect.chatting.security.JwtUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        String email = "test@example.com";
        String password = "password123";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(email);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTO result = userService.registerUser(email, password);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        String email = "test@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(email, "password123");
        });

        assertEquals("El email ya está registrado", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginUser_Success() {
        String email = "test@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword";

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtUtils.generateToken(email)).thenReturn("validToken");

        String token = userService.loginUser(email, password);

        assertNotNull(token);
        assertEquals("validToken", token);
    }

    @Test
    void testLoginUser_InvalidPassword() {
        String email = "test@example.com";
        String password = "password123";

        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.loginUser(email, password);
        });

        assertEquals("Credenciales inválidas", exception.getMessage());
    }

    @Test
    void testGetUserById_UserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(userId);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
    }
}
