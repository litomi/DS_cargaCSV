# Proyecto: Cargador Masivo de Alumnos a PostgreSQL
Aplicación hecha en Java para consola. Lee un archivo CSV y los inserta en una base de datos PostgreSQL, en forma rápida y eficiente. Utiliza un patrón de trabajo por hilos.

## Requisitos Previos

* **Java Development Kit (JDK)**: Versión 11 o superior.
* **Apache Maven**: Para compilar y empaquetar el proyecto.
* **PostgreSQL**: Un servidor de base de datos corriendo localmente o accesible por red.
* **Git**: Para clonar el repositorio.

## Pasos para la Configuración y Ejecución

Seguir los siguientes pasos para la compilación y ejecución.

### 1. Clonar el Repositorio

Abra una terminal y clone el proyecto desde GitHub:

Desde una terminal clonar el proyecto desde GitHUb.

```bash
git clone [https://github.com/litomi/DS_cargaCSV.git](https://github.com/litomi/DS_cargaCSV.git)
cd DS_cargaCSV
```

### 2. Configuración del entorno

La aplicación necesita conectarse a su base de datos y además necesita encontrar el archivo CSV.

1.  **situese en la carpeta de recursos:**
    ```bash
    cd src/main/resources
    ```

2.  **Cree su archivo de configuración:**
    Debe hacer una copia de `db.properties.example` y llámela `db.properties`.
    ```bash
    cp db.properties.example db.properties
    ```

3.  **Edite `db.properties`:**
    Abra el archivo `db.properties` con su editor de texto y rellene los valores con su configuración local:
    * `jdbcUrl`: La URL a su base de datos. Verifique que sea correcto.
    * `username`: Su usuario de PostgreSQL.
    * `password`: La contraseña de tal usuario.
    * `csv.filepath`: La **ruta absoluta** al archivo `alumnos.csv` en su equipo.

### 3. Crear la Tabla en la Base de Datos

Antes de ejecutar la aplicación se debe crear la tabla donde se insertará los datos. Conecte a la base de datos y ejecute
el siguiente código SQL:

```sql
-- Asegúrese de que la estructura de la tabla y los tipos de dato
-- coincidan con su modelo 'Alumno.java'.

CREATE TABLE mi_tabla_masiva (
    nro_legajo SERIAL PRIMARY KEY,
    nombre VARCHAR(255),
    apellido VARCHAR(255),
    nro_documento VARCHAR(50),
    tipo_documento VARCHAR(10),
    fecha_nacimiento VARCHAR(10),
    fecha_ingreso VARCHAR(10),
    sexo CHAR(1)
);
```

### 4. Compilar y Empaquetar la Aplicación

Desde la carpeta raíz del proyecto y use Maven para compilar el código y crear un archivo `.jar` ejecutable.

```bash
mvn clean package
```

Este comando creará un archivo en la carpeta `target/`, por ejemplo: `cargadorMasivoCSV-1.0-SNAPSHOT.jar`.

### 5. Ejecutar la Aplicación

¡Listo! Ahora se puede ejecutar el comando:

```bash
java -jar target/cargadorMasivoCSV-1.0-SNAPSHOT.jar
```
La consola mostrará el progreso de la ejecución.
