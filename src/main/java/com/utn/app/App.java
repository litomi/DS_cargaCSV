package com.utn.app;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.utn.app.modelos.Alumno;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class App {
    private static final int NRO_HILOS = Runtime.getRuntime().availableProcessors() * 2;
    private static final int CAPACIDAD_COLA = 15;
    private static final int CAPACIDAD_LOTE = 50_000;
    // private static final String RUTA_ARCHIVO = "src/main/resources/alumnos.csv";
    private static final String RUTA_ARCHIVO = "alumnos.csv";


    public static void main(String[] args) {
        long tiempoInicio = System.currentTimeMillis();
        Properties props = cargaPropiedades();

        if(props == null){
            System.err.println("ERROR: No se pudo cargar la configuración de la base de datos");
        }

        try (HikariDataSource ds = new HikariDataSource(new HikariConfig(props))) {

            AlumnoDAO alumnoDAO = new AlumnoDAO(ds);
            BlockingQueue<List<Alumno>> cola = new ArrayBlockingQueue<>(CAPACIDAD_COLA);
            ExecutorService ejecutorEscritores = Executors.newFixedThreadPool(NRO_HILOS);

            for(int i = 0; i < NRO_HILOS; i++){ 
                ejecutorEscritores.submit(new EscritorBD(cola, alumnoDAO));
            }

            Thread hiloLectorCSV = new Thread(new LectorCSV(cola, RUTA_ARCHIVO, CAPACIDAD_LOTE));
            hiloLectorCSV.start();
            hiloLectorCSV.join();

            System.out.println("El lector del archivo ha finalizado.");

            for(int i = 0; i < NRO_HILOS; i++){
                cola.put(Collections.emptyList());
            }

            ejecutorEscritores.shutdown();

            if(!ejecutorEscritores.awaitTermination(CAPACIDAD_COLA, TimeUnit.HOURS)){
                System.err.println("Los escritores no terminaron en el tiempo esperado. Apagando.");
                ejecutorEscritores.shutdown();
            }

            
        } catch (Exception e) {
            e.printStackTrace();
        }

        long tiempoFin = System.currentTimeMillis();

        System.out.println("Tiempo: " + (tiempoFin - tiempoInicio) + "ms");

    }

    public static Properties cargaPropiedades(){
        Properties props = new Properties();

        try(InputStream entrada = App.class.getClassLoader().getResourceAsStream("db.properties")){
            if(entrada == null){
                System.err.println("No se pudo leer el archivo de propiedades en 'resources'");
                return null;
            }
            props.load(entrada);

            return props;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static void crearTabla(){
          /*
     *  nro_lejago,
        nombre,
        apellido,
        nro_documento,
        tipo_documento,
        fecha_nacimiento,
        fecha_ingreso,
        sexo
     */
        String sql = "CREATE TABLE IF NOT EXISTS alumnos("
        + "nro_lejago int, " 
        + "nombre VARCHAR(100), " 
        + "apellido VARCHAR(100), " 
        + "nro_documento VARCHAR(100), " 
        + "tipo_documento VARCHAR(100), " 
        + "fecha_nacimiento VARCHAR(100), " 
        + "fecha_ingreso VARCHAR(100), " 
        + "sexo VARCHAR(100));";

    }
}
