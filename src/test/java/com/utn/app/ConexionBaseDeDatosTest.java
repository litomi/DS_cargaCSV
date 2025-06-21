package com.utn.app;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ConexionBaseDeDatosTest {
    private static HikariDataSource ds;

    @BeforeAll
    static void configurarDatosPrueba() {
        System.out.println("Configurando los datos para las pruebas. 'DataSource'");
        Properties props = new Properties();

        try {
            InputStream entrada = ConexionBaseDeDatosTest.class.getClassLoader()
                    .getResourceAsStream("db.test.properties");
            assertNotNull(entrada, "El archivo de configuraciones no puedo ser cargado.");
            props.load(entrada);

        } catch (Exception e) {
            System.out.println("Falló la carga del archivo de configuraciones.");
            e.printStackTrace();
        }

        HikariConfig config = new HikariConfig(props);
        ds = new HikariDataSource(config);

    }

    @AfterAll
    static void cerrarDatosPrueba() {
        if (ds != null) {
            System.out.println("Cerrando 'Datasource' de pruebas");
            ds.close();
        }
    }

    @Test
    @DisplayName("Debería obtener una conexión válidad a la base de datos.")
    void deberiaObtenerConexcionValida(){
        assertDoesNotThrow(() ->{
            try(Connection conex = ds.getConnection()){
                assertNotNull(conex, "La conexión no debería ser nula");

                assertTrue(conex.isValid(1), "La conexión debería ser válida");
                
                System.out.println("Prueba de conexión exitosa.");

            }
        });
    }
}
