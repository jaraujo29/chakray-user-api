# Chakray User Management API

API REST para la gestión de usuarios con seguridad AES-256.

## Características
- Registro y Login de usuarios.
- Cifrado de credenciales mediante AES-256.
- Pruebas automatizadas con JUnit y MockMvc.

## Documentación y Pruebas

### 1. Swagger UI
Disponible en: http://localhost:8080/swagger-ui/index.html

### 2. Postman
Se incluye colección JSON en la raíz para pruebas manuales.

### 3. Ejecución de Tests
Para validar el proyecto utilizando el Maven Wrapper, ejecute:

En Windows:
.\mvnw.cmd clean test

En Linux/macOS:
./mvnw clean test