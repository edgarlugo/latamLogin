# latamLogin

Aplicación web Spring Boot con un formulario de login que valida las credenciales contra una API REST externa y muestra el código y mensaje que esta devuelve.

## Stack

- Java 25
- Spring Boot 4.1 (Web MVC, Thymeleaf, Validation, Security, RestClient)
- Bootstrap 5 (webjar)
- Maven Wrapper (Maven 3.9)

## Requisitos

- JDK 25

## Ejecutar

```bash
./mvnw spring-boot:run
```

En Windows usa `mvnw.cmd`. La aplicación queda en http://localhost:8080.

## Configuración

La URL de la API de login se configura con la variable de entorno `LOGIN_API_URL` (o la propiedad `latam.login-api.url`):

```bash
LOGIN_API_URL=https://mi-servidor/api/login ./mvnw spring-boot:run
```

| Propiedad | Valor por defecto | Descripción |
|---|---|---|
| `latam.login-api.url` | `https://www.ficcion.cl/ws/api/login` | Endpoint `POST` de login |
| `latam.login-api.locale` | `ES-es` | Valor de la cabecera `Locale` |
| `latam.login-api.connect-timeout` | `5s` | Timeout de conexión |
| `latam.login-api.read-timeout` | `10s` | Timeout de lectura |

### Contrato esperado de la API

Petición (`POST`, `application/json`):

```json
{ "login": "usuario", "password": "secreta" }
```

Respuesta (`2xx`, o `4xx` cuando rechaza las credenciales):

```json
{ "codigo": "0", "mensaje": "Bienvenido" }
```

## Tests

```bash
./mvnw verify
```
