Project_Chatting
Proyecto basado en microservicios para autenticación y chat en tiempo real, desarrollado con Spring Boot, WebSockets y JWT.

Tabla de Contenidos
Descripción General
Arquitectura
Tecnologías Utilizadas
Instalación y Configuración
Servicios
Auth Service
Chat Service
Uso
Endpoints
Cliente HTML
Pruebas
Mejoras Futuras
Descripción General
Este proyecto consiste en una arquitectura de microservicios que incluye:

Auth Service: Servicio para la autenticación de usuarios y generación de tokens JWT.
Chat Service: Servicio que gestiona la conexión WebSocket para el chat en tiempo real.
El objetivo es proporcionar una solución escalable, segura y fácil de desplegar en la nube (AWS).

Arquitectura
(Opcional)

Descripción general de los componentes:

Auth Service: Realiza operaciones de registro, autenticación y validación de usuarios.
Chat Service: Maneja conexiones WebSocket, validación JWT y retransmisión de mensajes.
Decisiones clave:

Bases de datos: (RDS, DynamoDB, H2 en desarrollo)
Despliegue en AWS: ECS o Elastic Beanstalk (Pendiente)
Comunicación segura mediante JWT.
Tecnologías Utilizadas
Backend: Spring Boot, Spring Security, Spring Data JPA, WebSockets
Frontend: SockJS, STOMP, HTML/CSS/JavaScript
Base de datos: H2 en desarrollo, opción para RDS/DynamoDB en producción
Autenticación: JSON Web Tokens (JWT)
Contenerización: Docker
Instalación y Configuración
Requisitos
Java 17+
Maven 3+
Docker (opcional, para despliegue local con Docker Compose)
Clonar el repositorio
bash
Copiar
Editar
git clone https://github.com/PedroAP91/Proyect_Chatting.git
cd Proyect_Chatting
Construir los servicios
bash
Copiar
Editar
mvn clean install
Ejecutar localmente
Auth Service:
bash
Copiar
Editar
mvn -pl auth-service spring-boot:run
Chat Service:
bash
Copiar
Editar
mvn -pl chat-service spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
Configuración
Archivos application.yml/properties: Configura tus variables de entorno como base de datos, JWT, etc.
Servicios
Auth Service
Registro: POST /auth/register
Recibe email y contraseña.
Login: POST /auth/login
Devuelve un token JWT al validar las credenciales.
Chat Service
WebSocket Endpoint: /chat
Prefijo de mensajes del cliente: /app/message
Prefijo de mensajes del servidor: /topic/messages
Uso
Endpoints
Ejemplo de petición POST para registro:

http
Copiar
Editar
POST /auth/register
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "password123"
}
Cliente HTML
Puedes abrir el archivo chat.html en un navegador para probar la conexión WebSocket. Este cliente básico permite enviar y recibir mensajes.

Pruebas
Pruebas Unitarias
Para ejecutar las pruebas unitarias, utiliza:

bash
Copiar
Editar
mvn test
Pruebas de Integración
Pendiente de implementación.

Mejoras Futuras
Dockerizar los servicios y configurar Docker Compose.
Despliegue en AWS (ECS o Elastic Beanstalk).
Implementar cliente frontend en React/Angular/Vue.
Añadir pruebas de integración y E2E.
Configurar monitoreo con AWS CloudWatch y X-Ray.
Implementar HTTPS y gestión centralizada de secretos.
Contribuciones
Las contribuciones son bienvenidas. Si deseas colaborar, abre un issue o envía un pull request.