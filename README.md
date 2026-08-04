# Control de Activos

API REST para centralizar el inventario y la operación de infraestructura tecnológica distribuida por cliente y sucursal. El proyecto permite registrar cámaras y otros equipos, dar seguimiento a incidencias, adjuntar evidencia y producir documentación operativa.

> Proyecto de portafolio desarrollado con Java y Spring Boot. Está pensado para mostrar el diseño de una API empresarial, su capa de seguridad y la automatización de flujos administrativos.

## Problema que resuelve

Cuando el inventario de equipos y las incidencias se gestionan en archivos dispersos, conocer el estado de una sucursal, mantener la trazabilidad de un activo o generar evidencia para una visita técnica se vuelve lento y propenso a errores. **Control de Activos** organiza esa información en una API con acceso autenticado y recursos relacionados.

## Funcionalidades principales

- Gestión de clientes, sucursales, usuarios y categorías documentales.
- Inventario de hardware, con soporte especializado para cámaras: identificadores, modelo, serie, red, ubicación y evidencia fotográfica.
- Registro y seguimiento de reportes: comentarios, fotos, actualización y cierre.
- Panel de indicadores para consultar información consolidada.
- Importación y exportación de cámaras en archivos XLSX, incluida una plantilla de importación.
- Carga de archivos asociados a sucursales y evidencia asociada a reportes.
- Generación de reportes fotográficos y memorias técnicas en PDF.
- Autenticación stateless basada en JWT y autorización por roles.
- Respuestas de error centralizadas para recursos inexistentes, conflictos, validación y autenticación.

## Tecnologías

| Área | Tecnologías |
| --- | --- |
| Lenguaje y framework | Java 25, Spring Boot 4, Spring MVC |
| Persistencia | Spring Data JPA, MySQL 8 |
| Seguridad | Spring Security, JWT (JJWT), BCrypt |
| Documentos | Apache POI (XLSX), Thymeleaf y OpenHTMLtoPDF |
| Utilidades | Lombok, Maven Wrapper, Docker |

## Arquitectura

El código sigue una separación por responsabilidades para mantener la lógica de negocio independiente de la capa HTTP:

```text
Cliente HTTP
    │
    ├── Controllers  →  validan y exponen la API REST
    ├── Services     →  concentran reglas de negocio
    ├── Repositories →  acceso a datos con JPA
    └── MySQL        →  persistencia de entidades y relaciones

Módulos transversales: JWT · manejo global de errores · archivos · PDF/XLSX
```

## API disponible

Salvo `POST /api/v1/login`, todos los endpoints bajo `/api/v1/**` requieren el encabezado `Authorization: Bearer <token>`. `{id}` representa el identificador del recurso correspondiente.

### Acceso y administración

| Método y ruta | Qué hace |
| --- | --- |
| `POST /api/v1/login` | Autentica al usuario y devuelve un JWT. |
| `GET /api/v1/dashboard` | Devuelve datos consolidados para el panel. |
| `GET /api/v1/users` | Lista usuarios. |
| `GET /api/v1/users/roles` | Lista roles disponibles. |
| `POST /api/v1/users` | Crea un usuario. |
| `PUT /api/v1/users/{id}` | Edita un usuario. |
| `PUT /api/v1/users/me/password` | Cambia la contraseña del usuario autenticado. |
| `PUT /api/v1/users/{id}/reset-password` | Restablece la contraseña de un usuario. |
| `PUT /api/v1/users/{id}/disable-user` | Deshabilita un usuario. |
| `PUT /api/v1/users/{id}/enable-user` | Habilita un usuario. |

### Clientes, sucursales y archivos

| Método y ruta | Qué hace |
| --- | --- |
| `GET /api/v1/clients` | Lista clientes. |
| `POST /api/v1/clients` | Registra un cliente. |
| `PUT /api/v1/clients/{id}` | Actualiza un cliente. |
| `POST /api/v1/clients/{id}/photo` | Carga o reemplaza la imagen de un cliente. |
| `GET /api/v1/clients/{id}/branches` | Lista las sucursales de un cliente. |
| `POST /api/v1/clients/{id}/branches` | Registra una sucursal para un cliente. |
| `PUT /api/v1/branches/{id}` | Actualiza una sucursal. |
| `GET /api/v1/branches/{id}/files` | Lista archivos adjuntos de una sucursal. |
| `POST /api/v1/branches/{id}/files` | Adjunta un archivo y sus metadatos a una sucursal. |
| `GET /api/v1/file-categories` | Lista categorías de archivos. |
| `POST /api/v1/file-categories` | Crea una categoría de archivo. |
| `PUT /api/v1/file-categories/{id}` | Actualiza una categoría de archivo. |

### Inventario, cámaras y reportes

| Método y ruta | Qué hace |
| --- | --- |
| `GET /api/v1/hardware` | Lista el inventario de hardware. |
| `GET /api/v1/hardware/{id}` | Consulta el detalle de un equipo. |
| `GET /api/v1/hardware/{id}/reports` | Lista reportes asociados a un equipo. |
| `POST /api/v1/hardware/{id}/reports` | Crea un reporte para un equipo. |
| `GET /api/v1/hardware/{id}/camera` | Obtiene datos editables de una cámara. |
| `PUT /api/v1/hardware/{id}/camera` | Actualiza los datos de una cámara. |
| `POST /api/v1/hardware/{id}/camera/photos` | Carga evidencia fotográfica de una cámara. |
| `GET /api/v1/branches/{id}/hardware` | Lista el hardware de una sucursal. |
| `POST /api/v1/branches/{id}/hardware/camera` | Registra una cámara en una sucursal. |
| `GET /api/v1/reports` | Lista reportes. |
| `GET /api/v1/reports/{id}` | Consulta el detalle de un reporte. |
| `PUT /api/v1/reports/{id}` | Actualiza un reporte. |
| `PUT /api/v1/reports/{id}/close` | Cierra un reporte. |
| `POST /api/v1/reports/{id}/comments` | Agrega un comentario a un reporte. |
| `PUT /api/v1/comments/{id}` | Actualiza un comentario. |
| `POST /api/v1/reports/{id}/photos` | Adjunta una foto a un reporte. |
| `DELETE /api/v1/reports/{id}/photos/{photoId}` | Elimina una foto de un reporte. |

### Importación y documentos

| Método y ruta | Qué hace |
| --- | --- |
| `GET /api/v1/branches/import-template` | Descarga la plantilla XLSX para importar cámaras. |
| `POST /api/v1/branches/{id}/import-cameras` | Importa cámaras desde un archivo XLSX. |
| `GET /api/v1/branches/{id}/export-cameras` | Exporta las cámaras de una sucursal a XLSX. |
| `GET /api/v1/{cameraId}/photoReportByCameraID` | Genera un PDF fotográfico de una cámara. |
| `GET /api/v1/{branchId}/photoReport` | Genera un PDF fotográfico de una sucursal. |
| `GET /api/v1/{branchId}/technicalMemory` | Genera la memoria técnica PDF de una sucursal. |

Consulta ejemplos de peticiones y datos de prueba en [API_REQUESTS.md](API_REQUESTS.md) y [Testing dataset.md](Testing%20dataset.md).

## Requisitos

- JDK 25
- MySQL 8 o compatible
- Maven no es obligatorio: el proyecto incluye Maven Wrapper.
- Opcional: Docker para crear una imagen de la API.

## Ejecución local

1. Crea una base de datos vacía en MySQL, por ejemplo `control_activos`.
2. Crea el directorio que se usará para archivos adjuntos:

   ```powershell
   New-Item -ItemType Directory -Force .\uploads
   ```

3. Configura las variables de entorno. Sustituye los valores de ejemplo por tus propias credenciales; no las incluyas en el repositorio.

   ```powershell
   $env:DB_URL = "jdbc:mysql://localhost:3306/control_activos"
   $env:DB_USERNAME = "tu_usuario"
   $env:DB_PASSWORD = "tu_contrasena"
   $env:APP_STORAGE_BASE_PATH = (Resolve-Path .\uploads).Path
   ```

4. Inicia la API:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

La aplicación queda disponible en `http://localhost:3000` y Hibernate crea o actualiza el esquema según las entidades.

## Autenticación

### Importante: primer acceso en una instalación nueva

La API **no incluye datos semilla ni un endpoint público de registro**. Por diseño, `POST /api/v1/users` está protegido con JWT. Por ello, después de crear una base de datos vacía debes insertar manualmente un primer usuario activo en la tabla `user_entity` antes de poder iniciar sesión o usar el resto de la API.

La contraseña debe guardarse con un hash BCrypt válido, el rol debe ser `ADMIN` o `USER`, y `active` debe estar en `true`. Una vez creado ese usuario inicial, puedes autenticarte y administrar los usuarios siguientes desde la propia API.

Solicita un token con `POST /api/v1/login`:

```json
{
  "username": "usuario",
  "password": "contrasena"
}
```

La respuesta incluye el token JWT. En las siguientes solicitudes autenticadas envíalo así:

```http
Authorization: Bearer <token>
```

En otras palabras: con una base vacía el login no funcionará hasta que exista manualmente el primer usuario con una contraseña BCrypt.

## Pruebas y empaquetado

```powershell
# Ejecutar pruebas
.\mvnw.cmd test

# Generar el JAR ejecutable
.\mvnw.cmd clean package
```

## Docker

El repositorio incluye un `Dockerfile` multi-stage que compila y ejecuta la API. Antes de levantar el contenedor, proporciona las mismas variables de entorno de base de datos y almacenamiento mediante un mecanismo seguro para tu entorno.

```powershell
docker build -t control-activos-api .
docker run --rm -p 3000:3000 `
  -e DB_URL="jdbc:mysql://host.docker.internal:3306/control_activos" `
  -e DB_USERNAME="tu_usuario" `
  -e DB_PASSWORD="tu_contrasena" `
  -e APP_STORAGE_BASE_PATH="/app/uploads" `
  -v "${PWD}/uploads:/app/uploads" `
  control-activos-api
```

## Estructura del proyecto

```text
src/main/java/.../
├── controller/   # Endpoints HTTP
├── services/     # Reglas de negocio
├── repository/   # Persistencia JPA
├── models/       # Entidades y DTOs
├── config/       # Seguridad y configuración
├── Jwt/          # Utilidades de tokens
└── exception/    # Errores y manejador global

src/main/resources/
├── templates/    # Plantillas HTML para PDF
└── static/       # Estilos y recursos de los documentos
```

## Próximas mejoras

- Aumentar la cobertura de pruebas unitarias e integración.
- Documentar el contrato completo con OpenAPI/Swagger.
- Añadir migraciones versionadas de base de datos con Flyway o Liquibase.
- Incorporar un perfil de desarrollo con datos iniciales reproducibles.

## Autor

**Jaime** — Proyecto incluido en mi portafolio profesional.

Si deseas conocer decisiones de implementación o una demostración del proyecto, puedes contactarme a través de los enlaces de mi perfil profesional.
