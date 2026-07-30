# Viajar con Encanto – Backend
 
API REST desarrollada en **Spring Boot** que da soporte al blog de viajes **Viajar con Encanto**, una plataforma donde los usuarios pueden registrarse, iniciar sesión y compartir sus experiencias de viaje mediante publicaciones.
 
Este repositorio contiene la parte de backend del proyecto. El frontend (React + Vite + Tailwind) se encuentra en un repositorio aparte.

## ✨ Funcionalidades
 
- Registro e inicio de sesión de usuarios con autenticación **JWT**
- CRUD de publicaciones (crear, editar, listar y obtener por id)
- Subida de imagen asociada a cada publicación (`multipart/form-data`)
- Filtrado de publicaciones por destino/país
- Gestión de roles de usuario (solo USER)


## 🛠️ Tecnologías
 
- Java 25
- Spring Boot 3.5.16
- Spring Web
- Spring Data JPA
- Spring Security + JWT (`com.auth0:java-jwt`)
- Bean Validation (`spring-boot-starter-validation`)
- Lombok
- PostgreSQL (base de datos de producción/desarrollo)
- H2 (base de datos en memoria para tests)
- Maven


## 📁 Estructura del proyecto
 
El código sigue una arquitectura por capas dentro del paquete base `com.viajar.blog`:
 
```
com.viajar.blog
├── controller   # Controladores REST (endpoints de la API)
├── service      # Lógica de negocio
├── repository   # Acceso a datos (Spring Data JPA)
├── entity       # Entidades JPA (Publicacion, User, Role...)
└── security     # Configuración de seguridad y JWT
```

```
src/main/java/com/viajar/blog/
├── BlogApplication.java
├── config/
│   └── StaticResourceConfig.java
├── controller/
│   ├── PublicacionController.java
│   └── UserController.java
├── dto/
│   ├── PublicacionRequest.java
│   ├── RegisterRequest.java
│   ├── RegisterResponse.java
│   └── UserResponse.java
├── entity/
│   ├── PaisFilter.java
│   ├── Publicacion.java
│   ├── Role.java
│   └── User.java
├── exception/
│   └── GlobalExceptionHandler.java
├── repository/
│   ├── PublicacionRepository.java
│   ├── RoleRepository.java
│   └── UserRepository.java
├── security/
│   ├── CustomAuthenticationManager.java
│   ├── SpringConfig.java
│   ├── UserDetail.java
│   └── filter/
│       ├── JWTAuthentication.java
│       └── JWTAuthorization.java
├── seeder/
│   └── DataSeeder.java
└── service/
    ├── PublicacionService.java
    ├── PublicacionServiceImpl.java
    ├── RoleService.java
    ├── RoleServiceImpl.java
    ├── UserService.java
    └── UserServiceImpl.java
```
### Entidades principales
 
**Publicacion**
- `title`, `content`, `publishDate`, `image`
- `status`: enum `PaisFilter` (`ESPAÑA`, `PARIS`, `LONDRES`)
- Relación `ManyToOne` con `User` (autor de la publicación)
**User**
- `id`, `name`, `email` (único), `password`
- Relación `OneToMany` con `Publicacion`
- Relación `ManyToMany` con `Role` (tabla intermedia `user_role`)


## 🚀 Puesta en marcha
 
### Requisitos previos
 
- JDK 25
- Maven (o usar el wrapper incluido `mvnw` / `mvnw.cmd`)
- Una base de datos PostgreSQL en ejecución

## Configuración

### Base de datos

Crea una base de datos en PostgreSQL:

```sql
CREATE DATABASE viajar_encanto;
```

### Variables de entorno o archivo application-local.properties

Configura la conexión a la base de datos y demás propiedades en `src/main/resources/application.properties` (o `application.yml`):
 
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/viajar_encanto
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
 
spring.jpa.hibernate.ddl-auto=update
 
# Configuración JWT
jwt.secret=tu_clave_secreta
```
 
> Se recomienda no subir credenciales reales al repositorio; usa variables de entorno o un archivo `application-local.properties` ignorado por Git.

Copia el archivo `.env` en la raíz del proyecto (opcional):

```
JWT_SECRET=tu_clave_secreta
```

Por defecto usa `secreto`.

### Ejecutar el proyecto
 
```bash
# Clonar el repositorio
git clone https://github.com/rosana50factoria/proyecto-viajar-encanto-backend.git
cd proyecto-viajar-encanto-backend
 
# Ejecutar con el wrapper de Maven
./mvnw spring-boot:run
```
 
La API quedará disponible por defecto en `http://localhost:8080`.

## 🔐 Autenticación
 
La API utiliza **JWT** para proteger los endpoints:
 
1. El usuario se registra y luego inicia sesión (`/login`) con sus credenciales.
2. El servidor responde con un token JWT en la cabecera `Authorization`.
3. El cliente debe incluir ese token en la cabecera `Authorization` en las siguientes peticiones a endpoints protegidos.

## Endpoints

### Públicos

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/api/v1/users/register` | Registrar nuevo usuario |
| `GET` | `/api/v1/publicacion` | Listar todas las publicaciones |
| `GET` | `/uploads/**` | Servir imágenes subidas |

### Autenticación

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/login` | Iniciar sesión (devuelve token JWT en header `Authorization`) |

**Login** — enviar JSON con `email` y `password`:

```json
{
  "email": "rosa@gmail.com",
  "password": "admin"
}
```

El token JWT se devuelve en el header `Authorization: Bearer <token>`.

### Protegidos (requieren JWT)

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/v1/users` | Listar todos los usuarios |
| `GET` | `/api/v1/users/{id}` | Obtener usuario por ID |
| `GET` | `/api/v1/users/user/{name}` | Obtener usuario por nombre |
| `GET` | `/api/v1/publicacion/{id}` | Obtener publicación por ID |
| `POST` | `/api/v1/publicacion` | Crear publicación (multipart/form-data) |
| `PUT` | `/api/v1/publicacion/{id}` | Actualizar publicación (multipart/form-data) |
| `DELETE` | `/api/v1/publicacion/{id}` | Eliminar publicación |

### Publicación (multipart/form-data)

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `title` | text | Título |
| `content` | text | Contenido |
| `status` | text | País: `ESPAÑA`, `PARIS` o `LONDRES` |
| `image` | file | Imagen (opcional) |

## Datos de prueba

Cuando aun no tenemos datos en Base de datos al arrancar, el `DataSeeder` crea automáticamente:

| Email | Contraseña | Nombre |
|-------|-----------|--------|
| rosa@gmail.com | admin | admin |
| lola@gmail.com | 123456 | user1 |
| ana@gmail.com | 123456 | user2 |

También se crean dos publicaciones de ejemplo.

## 🗺️ Frontend
 
Este backend está pensado para consumirse desde el frontend en React del mismo proyecto (**Viajar con Encanto**), que gestiona el registro, login, listado, filtrado por destino y creación/edición de publicaciones con imagen.
 
## 👤 Autora
 
Proyecto individual desarrollado por **rosana50factoria** como parte de un bootcamp de desarrollo full stack.