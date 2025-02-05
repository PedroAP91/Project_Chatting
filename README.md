
<<<<<<< HEAD
# Proyecto Chatting

Este proyecto es una aplicación de microservicios desarrollada en Java utilizando Spring Boot. Los microservicios incluyen un servicio de autenticación (Auth Service) y un servicio de chat en tiempo real (Chat Service) basado en WebSockets.

## Hoja de ruta del proyecto

### 1. Arquitectura general en AWS (Pendiente)
- Definir arquitectura general en AWS:
  - Servicios Auth y Chat.
  - Decidir entre RDS o DynamoDB para almacenamiento de datos.
  - Decidir entorno de despliegue (ECS, Elastic Beanstalk, etc.).

---

### 2. Microservicio de Autenticación (Auth Service)
- Crear un proyecto Spring Boot con Spring Security, Spring Data JPA.
- Estructura del proyecto:
  - Paquetes: `model`, `controller`, `service`, `security`, `config`.

**Endpoints implementados:**
- `POST /auth/register`: Registro de usuario ✅.
- `POST /auth/login`: Validar credenciales y devolver token JWT ✅.

**Implementaciones adicionales:**
- DTOs: `UserDTO`, `LoginRequest`, `LoginResponse`, `RegisterRequest`.
- Validación JWT:
  - Generación/validación del token (`JwtUtils`) ✅.
  - Seguridad configurada en `SecurityConfig` ✅.
  - Filtro de autenticación JWT (`JwtAuthenticationFilter`) ✅.
  - Protección de endpoints para usuarios autenticados ✅.

---

### 3. Microservicio de Chat (Pendiente)
- Configuración inicial:
  - Endpoint WebSocket `/chat`.
  - Conexión WebSocket configurada con SockJS y STOMP.
  - Recepción y retransmisión de mensajes a través del tópico `/topic/messages`.

- Validación del token JWT durante la conexión:
  - Implementar `JwtHandshakeInterceptor`.
  - Resolver problemas de CORS y validación.

---

### 4. Comunicación entre microservicios (Pendiente)
- Configurar comunicación segura entre Auth Service y Chat Service.
- Clave secreta compartida para validación de JWT.

---

### 5. Despliegue en AWS (Pendiente)
- Configurar contenedores Docker para Auth y Chat.
- Subir imágenes a AWS Elastic Container Registry (ECR).
- Configurar un entorno de despliegue (ECS, Elastic Beanstalk).
- Configurar balanceador de carga (ALB).

---

### 6. Frontend (Pendiente)
- Implementar cliente en React/Angular/Vue:
  - Consumir endpoints de Auth (login/registro).
  - Abrir conexión WebSocket para el chat.

**Progreso:**
- Cliente HTML básico con conexión WebSocket configurado ✅.

---

### 7. Seguridad y buenas prácticas
- Contraseñas almacenadas con BCrypt ✅.
- Validación JWT implementada ✅.

**Pendientes:**
- Configurar tráfico seguro (HTTPS).
- Gestionar secretos con AWS Secrets Manager.
- Configurar monitoreo con AWS CloudWatch/X-Ray.

---

### 8. Testing (Pendiente)
- Pruebas unitarias (JUnit) para los endpoints.
- Pruebas de integración para comunicación entre microservicios.

---

## Resumen del progreso actual
- Microservicio Auth funcional (registro, login, validación JWT).
- Seguridad básica implementada.
- Pendientes:
  - Validación JWT en WebSockets.
  - Despliegue en AWS.
  - Cliente frontend completo.
=======
>>>>>>> 4de8cce7e4b5dd001da5e18c1950a0565e58fa25
