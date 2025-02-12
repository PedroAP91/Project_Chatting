package PedroAP.auth_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class ConfigChecker {

    @Value("${jwt.secret:NOT_FOUND}")
    private String jwtSecret;

    @PostConstruct
    public void printConfig() {
        System.out.println("JWT Secret: " + jwtSecret);

    }

}
