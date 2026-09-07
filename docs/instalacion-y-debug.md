# Instalación del entorno y depuración

Esta guía explica cómo preparar una computadora para compilar, ejecutar, probar y depurar ARKA-Lite. Los pasos principales están escritos para Windows y PowerShell.

## Requisitos

| Herramienta | Obligatoria | Uso |
| --- | --- | --- |
| Git | Sí | Descargar el repositorio y trabajar con ramas. |
| JDK 26 | Sí | Compilar, ejecutar y depurar el código Java. |
| Maven | No | El proyecto incluye Maven Wrapper y descarga la versión necesaria. |
| IntelliJ IDEA o Visual Studio Code | Recomendado | Editar el código, ejecutar pruebas y usar breakpoints. |
| Docker Desktop | Opcional | Construir y ejecutar la aplicación como contenedor. |

El archivo `pom.xml` define Java 26 mediante `<java.version>26</java.version>`. Instalar únicamente un JRE no es suficiente: se necesita un JDK porque incluye el compilador `javac` y las herramientas de depuración.

## 1. Instalar Git

Descargar Git para Windows desde [git-scm.com/download/win](https://git-scm.com/download/win) y completar el instalador. Después, abrir una terminal nueva y comprobar:

```powershell
git --version
```

Configurar la identidad que aparecerá en los commits:

```powershell
git config --global user.name "Nombre Apellido"
git config --global user.email "correo@ejemplo.com"
```

## 2. Instalar el JDK 26

### Opción A: instalarlo desde IntelliJ IDEA

1. Abrir **File > Project Structure > Project**.
2. En **SDK**, seleccionar **Download JDK**.
3. Elegir la versión `26`.
4. Elegir un proveedor, por ejemplo Eclipse Temurin.
5. Completar la descarga y seleccionar ese JDK como SDK del proyecto.

IntelliJ permite descargar o seleccionar un JDK desde la configuración del proyecto, como explica su [documentación oficial de SDKs](https://www.jetbrains.com/help/idea/sdk.html).

### Opción B: instalar Eclipse Temurin manualmente

1. Abrir la página oficial de [Eclipse Temurin](https://adoptium.net/temurin/releases/?version=26).
2. Seleccionar Java `26`, Windows, la arquitectura de la computadora y paquete `JDK`.
3. Descargar el instalador `.msi`.
4. Durante la instalación, habilitar las opciones para agregar Java a `PATH` y configurar `JAVA_HOME`, si aparecen.
5. Cerrar y volver a abrir PowerShell para cargar las variables nuevas.

### Verificar la instalación

```powershell
java --version
javac --version
where.exe java
where.exe javac
$env:JAVA_HOME
```

Tanto `java` como `javac` deben mostrar la versión `26`. Si muestran `25`, `21`, `17` u otra versión, la terminal está utilizando un JDK distinto del requerido.

## 3. Configurar `JAVA_HOME` si apunta a otra versión

Buscar en Windows **Editar las variables de entorno para su cuenta** y realizar estos cambios:

1. Crear o editar `JAVA_HOME` para que apunte a la carpeta del JDK 26, no a su carpeta `bin`.
2. En `Path`, colocar `%JAVA_HOME%\bin` antes de rutas de otras instalaciones de Java.
3. Cerrar todas las terminales y abrir una nueva.
4. Ejecutar nuevamente `java --version` y `javac --version`.

Ejemplo de una ruta de instalación:

```text
C:\Program Files\Eclipse Adoptium\jdk-26...
```

El nombre exacto de la carpeta depende de la actualización instalada. No se debe copiar literalmente el ejemplo sin comprobar la ruta real.

Para probar temporalmente otra instalación sin modificar Windows de forma permanente:

```powershell
$env:JAVA_HOME = "C:\ruta\real\al\jdk-26"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
java --version
javac --version
```

Esta configuración temporal solo afecta a la ventana actual de PowerShell.

## 4. Descargar el proyecto

```powershell
git clone https://github.com/USUARIO/REPOSITORIO.git
cd REPOSITORIO
```

Si el repositorio ya está descargado:

```powershell
git switch main
git pull origin main
```

Reemplazar `USUARIO/REPOSITORIO` por la dirección correcta. Si se configuró un origen equivocado, consultar la sección [Corregir un `origin` equivocado](git.md#corregir-un-origin-equivocado).

## 5. Maven y dependencias del proyecto

No se debe instalar Maven manualmente. El repositorio contiene:

```text
mvnw
mvnw.cmd
.mvn/wrapper/maven-wrapper.properties
```

En Windows se usa `mvnw.cmd`; el Wrapper descarga y utiliza Maven 3.9.16 según la configuración del proyecto. La primera ejecución necesita acceso a internet para descargar Maven y las dependencias.

Comprobar Java y Maven:

```powershell
.\mvnw.cmd --version
```

La salida debe indicar Java 26. Después se descargan las dependencias, ejecutan las pruebas y construye el JAR con:

```powershell
.\mvnw.cmd package
```

Maven guarda las dependencias descargadas en la caché local del usuario. Las siguientes ejecuciones normalmente reutilizan esa caché.

### Dependencias declaradas actualmente

Las dependencias se administran en `pom.xml`; no se descargan manualmente una por una.

| Dependencia | Uso |
| --- | --- |
| `spring-boot-starter-webmvc` | Crear la aplicación web y los controladores HTTP. |
| `springdoc-openapi-starter-webmvc-ui` | Generar la especificación OpenAPI y Swagger UI. |
| `spring-boot-starter-webmvc-test` | Ejecutar pruebas de la aplicación con JUnit y herramientas de Spring. |

No hace falta agregar una dependencia al `pom.xml` para usar breakpoints. El protocolo de depuración y herramientas como `jdb` forman parte del JDK; Oracle describe estas herramientas en su [guía oficial de diagnóstico para JDK 26](https://docs.oracle.com/en/java/javase/26/troubleshoot/diagnostic-tools.html).

## 6. Abrir y ejecutar el proyecto

### IntelliJ IDEA

1. Seleccionar **File > Open** y abrir la carpeta del repositorio.
2. Esperar a que IntelliJ importe `pom.xml` y descargue las dependencias.
3. Abrir **File > Project Structure > Project** y confirmar que el SDK sea Java 26.
4. Abrir `src/main/java/pa/gob/dntic/arkalite/ArkaliteApplication.java`.
5. Usar el botón verde junto al método `main` y seleccionar **Run 'ArkaliteApplication'**.

Desde PowerShell también se puede iniciar con:

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicación utiliza por defecto `http://localhost:8080`.

## 7. Depurar con IntelliJ IDEA

1. Abrir una clase donde se quiera detener la ejecución, por ejemplo `SolicitudController` o `ServicioDeSolicitudes`.
2. Hacer clic en el margen izquierdo junto al número de línea para crear un breakpoint rojo.
3. Abrir `ArkaliteApplication.java`.
4. Usar el botón verde junto a `main` y seleccionar **Debug 'ArkaliteApplication'**, o elegir el icono del insecto en la barra superior.
5. Enviar una petición que ejecute esa línea.
6. Cuando el programa se detenga, revisar variables, pila de llamadas y expresiones en la ventana **Debug**.
7. Usar **Step Over** para avanzar una línea, **Step Into** para entrar en un método y **Resume Program** para continuar hasta el siguiente breakpoint.

Ejemplos para activar breakpoints de los controladores:

```powershell
Invoke-RestMethod http://localhost:8080/solicitudes
Invoke-RestMethod -Method Post http://localhost:8080/solicitudes/crear
Invoke-RestMethod -Method Post http://localhost:8080/solicitudes/INC-002/enviar
Invoke-RestMethod http://localhost:8080/notificaciones
```

La configuración debe usar:

```text
Main class: pa.gob.dntic.arkalite.ArkaliteApplication
JDK/JRE: 26
Working directory: raíz del repositorio
```

## 8. Depurar con Visual Studio Code

Si el equipo utiliza Visual Studio Code, instalar desde el panel de extensiones:

- **Extension Pack for Java**, publicado por Microsoft.

Este paquete incluye soporte del lenguaje, depurador, ejecución de pruebas y soporte para Maven. La [documentación oficial de VS Code](https://code.visualstudio.com/docs/java/java-debugging) explica que el depurador puede detectar la clase principal sin exigir inicialmente un archivo `launch.json`.

Después:

1. Abrir la carpeta completa del repositorio.
2. Esperar a que Java termine de importar el proyecto Maven.
3. Comprobar que VS Code utiliza JDK 26.
4. Abrir `ArkaliteApplication.java`.
5. Colocar un breakpoint.
6. Presionar `F5` o seleccionar **Run > Start Debugging**.
7. Elegir Java si VS Code solicita el tipo de depurador.

No se debe confirmar la carpeta `.vscode/` si contiene preferencias personales; actualmente está excluida por `.gitignore`.

## 9. Depuración por puerto

Esta opción sirve para conectar IntelliJ o VS Code a una JVM que fue iniciada por separado.

Primero generar el JAR:

```powershell
.\mvnw.cmd package
```

Iniciar Java esperando al depurador en el puerto 5005:

```powershell
java "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=localhost:5005" -jar target/arkalite.jar
```

Significado de las opciones:

- `transport=dt_socket`: conecta el depurador mediante un puerto TCP.
- `server=y`: la aplicación espera que el depurador se conecte.
- `suspend=y`: la aplicación no continúa hasta recibir la conexión.
- `address=localhost:5005`: acepta conexiones locales en el puerto 5005.

Después se crea en el IDE una configuración **Remote JVM Debug** con host `localhost` y puerto `5005`.

El puerto de depuración nunca debe exponerse públicamente ni habilitarse en producción. El protocolo JDWP permite controlar la ejecución de la JVM y examinar sus datos.

## 10. Depurar pruebas

En IntelliJ o VS Code se puede colocar un breakpoint en una prueba y seleccionar **Debug** junto a la clase o al método de prueba.

Para ejecutar todas las pruebas sin depurador:

```powershell
.\mvnw.cmd test
```

Para ejecutar una sola clase:

```powershell
.\mvnw.cmd test "-Dtest=ArkaliteApplicationTests"
```

## 11. Problemas frecuentes

### Maven indica que la versión de Java no es compatible

Ejecutar:

```powershell
java --version
javac --version
.\mvnw.cmd --version
```

Los tres deben utilizar Java 26. Si Maven muestra otra versión, corregir `JAVA_HOME` y reiniciar la terminal o el IDE.

### El IDE muestra imports de Spring en rojo

- Confirmar conexión a internet.
- Confirmar que `pom.xml` fue reconocido como proyecto Maven.
- Recargar el proyecto desde la ventana Maven del IDE.
- Ejecutar `.\mvnw.cmd package` para comprobar si el problema también ocurre fuera del IDE.

### El puerto 8080 está ocupado

Iniciar la aplicación en otro puerto:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=9090"
```

Las peticiones deberán usar `http://localhost:9090`.

### El breakpoint no se activa

- Confirmar que la aplicación se inició con **Debug**, no solamente con **Run**.
- Confirmar que la petición pasa por la clase y línea seleccionadas.
- Comprobar que el IDE utiliza el código y JDK del proyecto actual.
- Detener procesos antiguos que todavía estén usando el puerto 8080.
- Volver a iniciar la sesión de depuración después de cambios importantes.

## Lista de comprobación

Antes de comenzar a desarrollar:

- `git --version` responde correctamente.
- `java --version` y `javac --version` muestran Java 26.
- `JAVA_HOME` apunta al JDK 26.
- `.\mvnw.cmd --version` muestra Maven y Java 26.
- `.\mvnw.cmd package` descarga las dependencias y genera `target/arkalite.jar`.
- La clase `ArkaliteApplication` inicia en modo Run y Debug.
- Una petición a `http://localhost:8080/solicitudes` obtiene respuesta.
