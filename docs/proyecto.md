# ARKA-Lite

## Objetivo

ARKA-Lite es una aplicación de ejemplo para gestionar solicitudes y generar notificaciones cuando una solicitud es enviada. El proyecto muestra separación por dominios, puertos y adaptadores, y comunicación desacoplada mediante eventos en memoria.

## Tecnología actual

- Java 26.
- Spring Boot 4.1.1.
- Maven y Maven Wrapper.
- Spring Web MVC para la API HTTP.
- Springdoc OpenAPI para documentación interactiva.
- JUnit 5 para pruebas.
- Docker con una imagen base Eclipse Temurin 26 JDK.

Las versiones efectivas se declaran en `pom.xml`; este documento debe actualizarse si cambian.

## Estructura

```text
.
├── docs/                         Documentación del proyecto
├── src/main/java/pa/gob/dntic/arkalite/
│   ├── eventos/                  Contratos de eventos compartidos
│   ├── solicitudes/              Dominio y adaptadores de solicitudes
│   ├── notificaciones/           Dominio y adaptadores de notificaciones
│   ├── transporte/               Bus de eventos en memoria
│   └── ArkaliteApplication.java  Arranque y raíz de composición
├── src/main/resources/           Configuración de Spring
├── src/test/                     Pruebas automatizadas
├── Dockerfile                    Imagen de ejecución
├── pom.xml                       Dependencias y compilación
└── mvnw / mvnw.cmd               Maven Wrapper
```

## Arquitectura

El código sigue una orientación de arquitectura hexagonal:

- `dominio`: reglas de negocio, entidades, servicios y puertos. Debe permanecer independiente de HTTP, bases de datos y detalles de Spring siempre que sea posible.
- `adaptadores/entrada`: traduce solicitudes externas al lenguaje del dominio; actualmente contiene controladores HTTP y manejadores de eventos.
- `adaptadores/salida`: implementa persistencia u otros servicios requeridos por el dominio; actualmente usa repositorios en memoria.
- `config`: conecta interfaces del dominio con implementaciones concretas.
- `transporte`: entrega eventos entre módulos dentro del mismo proceso.

El módulo de solicitudes publica `SolicitudEnviada`. El módulo de notificaciones reacciona al evento sin que solicitudes dependa de él. El estado es volátil: al reiniciar la aplicación se pierden solicitudes y notificaciones.

## API actual

| Método | Ruta | Resultado |
| --- | --- | --- |
| `GET` | `/solicitudes` | Lista todas las solicitudes. |
| `GET` | `/solicitudes/{id}` | Busca una solicitud por identificador. |
| `POST` | `/solicitudes/crear` | Crea la solicitud fija `INC-002` en estado `BORRADOR`. |
| `POST` | `/solicitudes/{id}/enviar` | Cambia una solicitud de `BORRADOR` a `ENVIADA` y publica un evento. |
| `GET` | `/notificaciones` | Lista las notificaciones creadas por eventos. |

El endpoint de creación es demostrativo: todavía no recibe un cuerpo ni permite al cliente elegir el identificador o tipo. Esto debe tenerse en cuenta antes de considerarlo una API productiva.

Con la aplicación iniciada:

```powershell
Invoke-RestMethod http://localhost:8080/solicitudes
Invoke-RestMethod -Method Post http://localhost:8080/solicitudes/crear
Invoke-RestMethod -Method Post http://localhost:8080/solicitudes/INC-002/enviar
Invoke-RestMethod http://localhost:8080/notificaciones
```

La interfaz OpenAPI suele estar disponible en `http://localhost:8080/swagger-ui/index.html` y la especificación JSON en `http://localhost:8080/v3/api-docs`.

## Requisitos locales

- JDK 26 disponible en `JAVA_HOME`.
- Docker Desktop o un motor Docker compatible, solo para ejecución en contenedor.
- Git.

No es obligatorio instalar Maven porque el repositorio contiene Maven Wrapper.

Para preparar el equipo y configurar el depurador, seguir la guía de [instalación y depuración](instalacion-y-debug.md).

## Compilar, probar y ejecutar

```powershell
# Ejecutar las pruebas
.\mvnw.cmd test

# Crear target/arkalite.jar
.\mvnw.cmd package

# Iniciar con Maven
.\mvnw.cmd spring-boot:run

# Iniciar el JAR ya generado
java -jar target/arkalite.jar
```

Para usar otro puerto local:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=9090"
java -jar target/arkalite.jar --server.port=9090
$env:SERVER_PORT = "9090"
.\mvnw.cmd spring-boot:run
```

## Configuración

La configuración base está en `src/main/resources/application.properties`. Spring Boot permite sobrescribir propiedades mediante variables de entorno, argumentos de línea de comandos o perfiles. No se deben guardar secretos en el repositorio; se inyectan desde el entorno o desde el sistema de secretos del despliegue.

## Limitaciones conocidas

- Los repositorios y el bus de eventos viven en memoria.
- No hay autenticación ni autorización.
- El contrato de creación usa datos fijos.
- No hay una base de datos ni migraciones.
- El bus de eventos no ofrece persistencia, reintentos ni entrega entre procesos.

Estas limitaciones son aceptables para un laboratorio, pero deben resolverse o aceptarse explícitamente antes de una puesta en producción.
