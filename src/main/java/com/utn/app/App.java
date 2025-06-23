package com.utn.app;

import java.io.FileInputStream;
import java.io.IOException;
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

    public static void main(String[] args) {
        long tiempoInicio = System.currentTimeMillis();

        System.out.println("Iniciando la aplicación...");

        Properties props = cargaPropiedades();

        if (props == null) {
            System.err.println("ERROR: No se pudo cargar los datos de configuración.");
        } else {
            System.out.println("Datos de configuación cargados.");
        }

        //Valores tomados desde el archivo de propiedades
        final String RUTA_ARCHIVO = props.getProperty("csv.archivo", "alumnos.csv");
        final int CAPACIDAD_COLA = Integer.parseInt(props.getProperty("app.capacidadCola", "10"));
        final int CAPACIDAD_LOTE = Integer.parseInt(props.getProperty("app.capacidadLote", "15000"));

        //Filtrado de las variables de configuración para Hikari
        Properties propsHikari = filtrarPropiedadesHikari(props);

        try (HikariDataSource ds = new HikariDataSource(new HikariConfig(propsHikari))) {
            System.out.println("Conexion a la base de datos establecida.");

            AlumnoDAO alumnoDAO = new AlumnoDAO(ds);
            BlockingQueue<List<Alumno>> cola = new ArrayBlockingQueue<>(CAPACIDAD_COLA);
            ExecutorService ejecutorEscritores = Executors.newFixedThreadPool(NRO_HILOS);

            System.out.println("Iniciando los hilos escritores...");
            
            for (int i = 0; i < NRO_HILOS; i++) {
                ejecutorEscritores.submit(new EscritorBD(cola, alumnoDAO));
            }

            System.out.println("Iniciando el lector de CSV...");
            Thread hiloLectorCSV = new Thread(new LectorCSV(cola, RUTA_ARCHIVO, CAPACIDAD_LOTE));
            hiloLectorCSV.start();
            hiloLectorCSV.join();

            System.out.println("La lectura del archivo ha finalizado.");

            for (int i = 0; i < NRO_HILOS; i++) {
                cola.put(Collections.emptyList());
            }

            ejecutorEscritores.shutdown();
            System.out.println("Esperando que los escritores terminen...");

            if (!ejecutorEscritores.awaitTermination(CAPACIDAD_COLA, TimeUnit.HOURS)) {
                System.err.println("Los escritores no terminaron en el tiempo esperado. Apagando.");
                ejecutorEscritores.shutdown();
            } else {
                System.out.println("Los hilos escritores han terminado.");
            }

            long tiempoFin = System.currentTimeMillis();
            System.out.println("Tiempo: " + (tiempoFin - tiempoInicio) + "ms");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Properties cargaPropiedades() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("app.properties")) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println("No se pudo cargar app.properties: " + e.getMessage());
            return null;
        }
        return props;
    }

    public static Properties filtrarPropiedadesHikari(Properties props) {

        Properties propiedades = new Properties();
        for (String key : props.stringPropertyNames()) {
            // para HikariCP
            if (key.equals("jdbcUrl") ||
                    key.equals("username") ||
                    key.equals("password") ||
                    key.equals("maximumPoolSize")) {
                propiedades.setProperty(key, props.getProperty(key));
            }
        }

        return propiedades;
    }
}
