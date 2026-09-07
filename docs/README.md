# Documentación de ARKA-Lite

Toda la documentación mantenida por el equipo vive en esta carpeta. Cuando un cambio modifique el comportamiento, la arquitectura, la forma de desplegar o el proceso de trabajo, la documentación relacionada debe actualizarse en el mismo Pull Request.

## Índice

- [Descripción del proyecto](proyecto.md): propósito, tecnología, arquitectura, módulos, API y ejecución local.
- [Instalación y depuración](instalacion-y-debug.md): JDK 26, Maven Wrapper, dependencias y configuración del depurador.
- [Flujo de trabajo del equipo](flujo-de-trabajo.md): ramas, Pull Requests, revisión obligatoria y definición de terminado.
- [Guía de Git](git.md): configuración y comandos de uso diario, colaboración, recuperación y versiones.
- [Guía de Docker](docker.md): construcción, ejecución, puertos, diagnóstico y publicación de imágenes.
- [Estándares de desarrollo](estandares-de-desarrollo.md): reglas para Java, Spring, arquitectura, pruebas, API y seguridad.

## Regla de mantenimiento

No se deben crear archivos de documentación fuera de `docs/`. Las excepciones son archivos técnicos exigidos por herramientas en la raíz, como `pom.xml`, `Dockerfile`, `.gitignore` y `.dockerignore`.

Los ejemplos de esta documentación asumen PowerShell en Windows. En Linux o macOS se usa `./mvnw` donde se indique `.\mvnw.cmd`.
