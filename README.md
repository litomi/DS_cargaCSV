# Cargador Masivo de CSV a PostgreSQL

##Materia: Desarrollo de Software.

### Descripción

Aplicación de consola hecha en Java para leer un archivo CSV de gran volumen (diseñada para 2.5 millones de registros) e insertarlos en una base de datos PostgreSQL de la manera más rápida y eficiente posible.

La arquitectura se basa en un patrón de concurrencia **Lector-Escritor** para procesar la lectura del archivo y la escritura en la base de datos de forma paralela.

### Características Principales

* **Procesamiento Concurrente:** Un único hilo (`LectorCSV`) se dedica a leer el archivo mientras un pool de hilos (`EscritorBD`) se dedica a escribir en la base de datos.
* **Alto Rendimiento:** Utiliza un pool de conexiones **HikariCP** y la técnica de **JDBC Batching** para optimizar la inserción de datos.
* **Eficiencia de Memoria:** Lee el archivo CSV en modo "streaming", sin cargarlo completamente en memoria, lo que permite procesar archivos de cualquier tamaño con un bajo consumo de RAM.
* **Configuración Externalizada:** Separa la lógica de la aplicación de los datos de configuración, los cuales se gestionan en un archivo `db.properties`.

---

## Guía de Ejecución

### Opción 1: Ejecución Rápida (Recomendado para probar la aplicación)

Esta opción es la más simple. Existe una carpeta `dist/` en el repositorio con la aplicación ya compilada.

1.  **En la carpeta `dist/`**. Encontrará:
    * `CargadorCSV-1.0-SNAPSHOT.jar`: La aplicación lista para ejecutar.
    * `db.ejemplo.properties`: El archivo de configuración que se debe editar.

2.  **Prepare los Archivos:**
    * **Configuración:** Abra `db.ejemplo.properties` con un editor de texto y rellene los datos de conexión a su base de datos. Renombre el archivo a `db.properties`
    * **Datos:** Asegúrese de tener su archivo `alumnos.csv` en el mismo directorio.

3.  **Prepare la base de datos:**
    Antes de ejecutar la aplicación, necesita crear la tabla de destino. Conéctese a su base de datos y ejecute el siguiente script SQL:

    ```sql
    CREATE TABLE alumnos (
    nro_legajo INT NOT NULL UNIQUE,
    nombre VARCHAR(255),
    apellido VARCHAR(255),
    nro_documento VARCHAR(50),
    tipo_documento VARCHAR(100),
    fecha_nacimiento VARCHAR(20),
    fecha_ingreso VARCHAR(20),
    sexo CHAR(1)
    );
    ```

3.  **Ejecute la Aplicación:**
    Abra una terminal en la carpeta `dist/` y ejecute el siguiente comando:

    ```bash
    java -jar CargadorCSV-1.0-SNAPSHOT.jar
    ```
    ¡Y listo! La aplicación comenzará el proceso de carga.


---

### Opción 2: Compilación desde Cero

Pasos para compilar el proyecto.

#### 1. Requisitos Previos

* **Java Development Kit (JDK)**: Versión 11 o superior.
* **Apache Maven**: Versión 3.6 o superior.
* **PostgreSQL**: Un servidor de base de datos activo.

#### 2. Clonar el Repositorio

```bash
git clone [https://github.com/litomi/DS_cargaCSV.git](https://github.com/litomi/DS_cargaCSV.git)
cd DS_cargaCSV
```

#### 3. Preparar la Base de Datos

Aplicar el procedimiento del paso 3 de la opción 1.


#### 4. Configurar el Entorno de Ejecución

La aplicación busca un archivo `db.properties` en el mismo directorio desde donde se ejecuta.

1.  **Cree su archivo de configuración:** Copie o renombre la plantilla `db.ejemplo.properties`  a `db.properties` en la raíz del proyecto.

    ```bash
    cp db.ejemplo.properties db.properties
    ```
2.  **Editar `db.properties`:** Abrir el archivo `db.properties` y rellenar con los valores de su configuración local:
    * `jdbcUrl`: La URL a su base de datos.
    * `username`: Su usuario de PostgreSQL.
    * `password`: La contraseña del usuario.

#### 5. Compilar y Empaquetar

Usar Maven para compilar el proyecto y crear el `.jar` ejecutable.

```bash
mvn clean package
```
Esto generará el archivo `CargadorCSV-1.0-SNAPSHOT.jar` en la carpeta `target/`.

#### 6. Ejecutar la Aplicación

Para ejecutar, asegúrese de que el `.jar`, `db.properties` y `alumnos.csv` estén en el mismo directorio.

```bash
# Copie el jar a la raíz del proyecto para ejecutarlo junto a db.properties
cp target/CargadorCSV-1.0-SNAPSHOT.jar .

# Ejecute la aplicación
java -jar CargadorCSV-1.0-SNAPSHOT.jar
```

---

## Ejecución de Pruebas
Copie o renombre el archivo `db.test ejemplo.properties`  que se encuentra en `src/test/resources/` a `db.test.properties`.

Rellene con los valores de su configuración local:
    * `jdbcUrl`: La URL a su base de datos.
    * `username`: Su usuario de PostgreSQL.
    * `password`: La contraseña del usuario.

Recomendado: utilice una base de datos creada para pruebas, por ejemplo `sysacad_pruebas`, cree la tabla correspondiente -remitase al paso 3 de la opción 1 (***Ejécución rápida***).

Para ejecutar los tests unitarios y de integración del proyecto, use el siguiente comando de Maven.

```bash
mvn test
```
*(Asegúrese de tener un archivo `db.test.properties` en `src/test/resources` para que los tests de integración funcionen).*
