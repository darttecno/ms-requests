# ms-requests

Este microservicio se encarga de gestionar las solicitudes de medicamentos.

## Instalación y Ejecución

Sigue estos pasos para configurar y ejecutar el proyecto en tu entorno local.

### Prerrequisitos

Asegúrate de tener instalado lo siguiente:
- **Java Development Kit (JDK)**: Versión 17 o superior.
- **Apache Maven**: Para la gestión de dependencias y la compilación del proyecto.
- **PostgreSQL**: Como base de datos.

### Configuración

1.  **Clona el repositorio**:
    ```bash
    git clone <URL_DEL_REPOSITORIO>
    cd ms-requests
    ```

2.  **Configura la base de datos**:
    - Asegúrate de que tu instancia de PostgreSQL esté en ejecución.
    - Crea una base de datos para este microservicio.
    - Renombra o copia el archivo `src/main/resources/application.properties.example` a `src/main/resources/application.properties` (si existiera un archivo de ejemplo).
    - Modifica `src/main/resources/application.properties` con las credenciales de tu base de datos:
      ```properties
      spring.datasource.url=jdbc:postgresql://localhost:5432/nombre_de_tu_bd
      spring.datasource.username=tu_usuario
      spring.datasource.password=tu_contraseña
      spring.jpa.hibernate.ddl-auto=none
      spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.yaml
      ```

### Compilación

Usa Maven para compilar el proyecto. Esto descargará todas las dependencias necesarias y empaquetará la aplicación en un archivo JAR.

```bash
mvn clean install
```

### Ejecución de Pruebas

Para ejecutar las pruebas unitarias y de integración del proyecto, utiliza el siguiente comando de Maven:

```bash
mvn test
```

Este comando compilará el código de prueba y ejecutará todas las pruebas definidas en el proyecto.

### Ejecución

Una vez compilado, puedes ejecutar la aplicación de dos maneras:

1.  **Usando el plugin de Spring Boot (recomendado para desarrollo)**:
    Este comando iniciará la aplicación directamente con Maven.
    ```bash
    mvn spring-boot:run
    ```

2.  **Ejecutando el archivo JAR**:
    Este comando ejecuta el JAR generado en el paso de compilación.
    ```bash
    java -jar target/ms-requests-0.0.1-SNAPSHOT.jar
    ```

La aplicación debería iniciarse y conectarse a la base de datos. Las migraciones de Liquibase se aplicarán automáticamente al arrancar, creando las tablas necesarias.
