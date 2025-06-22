package com.utn.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.utn.app.modelos.Alumno;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class AlumnoDaoTest {
    private static HikariDataSource ds;
    private static AlumnoDAO alumnoDAO;

    @BeforeAll
    static void setup(){
        Properties props = new Properties();
        try (InputStream entrada = AlumnoDaoTest.class.getClassLoader().getResourceAsStream("db.test.properties")) {
            assertNotNull(entrada, "El archivo no se encontró en la ruta src/test/resources");

            props.load(entrada);
            
        } catch (Exception e) {
            System.err.println("Falló la carga del archivo db.test.properties para la prueba");
        }

        ds = new HikariDataSource(new HikariConfig(props));

        alumnoDAO = new AlumnoDAO(ds);
    }

    @BeforeAll
    static void crearTabla(){
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
        + "nro_legajo SERIAL PRIMARY KEY,"
        + "nombre VARCHAR(200),"
        + "apellido VARCHAR(200),"
        + "nro_documento VARCHAR(20),"
        + "tipo_documento VARCHAR(50),"
        + "fecha_nacimiento VARCHAR(200),"
        + "fecha_ingreso VARCHAR(200),"
        + "sexo VARCHAR(20)"
        + ");";

        try(Connection conex = ds.getConnection()){
            PreparedStatement stmt = conex.prepareStatement(sql);
            stmt.execute();
        }catch(SQLException e){
            System.err.println("ERROR: No se pudo crear tabla");
        }
    }

    @AfterAll
    static void tearDown(){
        if(ds != null) ds.close();
    }

    @BeforeEach
    void limpiarTabla(){
        try(Connection conex = ds.getConnection()){
            PreparedStatement pstmt = conex.prepareStatement("TRUNCATE TABLE alumnos RESTART IDENTITY");
            pstmt.execute();
            System.out.println("Tabla 'alumnos' reiniciada para la prueba.");           
        }catch(SQLException e){
            System.err.println("ERROR: no se puedo limpiar la tabla.");
        }
    }

    @Test
    @DisplayName("Debería insertar un lote de alumnos correctamente en la base de datos")
    void deberiaInsertarElLoteCorrectamente() throws SQLException{
        List<Alumno> lotePrueba = new ArrayList<>();
        
        lotePrueba.add(
            new Alumno(
                1,
                "Emely Maya Dare",
                "VonRueden",
                "35839804",
                "LibretaEnrolamiento",
                "1982-11-13",
                "2014-04-16",
                "M"
            )
        );

        lotePrueba.add(
            new Alumno(
                2,
                "Abagail Pinkie Collins",
                "King",
                "4985084",
                "Pasaporte",
                "1952-10-15",
                "2006-06-18",
                "M"
            )
        );

        alumnoDAO.insertarLote(lotePrueba);

        try(Connection conex = ds.getConnection()){
            try(PreparedStatement pstmt = conex.prepareStatement("SELECT COUNT(*) FROM alumnos")){
                ResultSet rs = pstmt.executeQuery();
                assertTrue(rs.next(), "La consulta debería dar una fila");
                int cantidadFilas = rs.getInt(1);
                assertEquals(2, cantidadFilas, "La tabla debería tener 2 filas, después de inserción");
            }

            try(PreparedStatement pstmt = conex.prepareStatement("SELECT nombre, apellido FROM alumnos WHERE nro_legajo = ?")){
                pstmt.setInt(1, 2);
                ResultSet rs = pstmt.executeQuery();
                assertTrue(rs.next(), "Debería encontrarse un alumno con número de legajo: 2");
                assertEquals("Abagail Pinkie Collins", rs.getString("nombre"));
                assertEquals("King", rs.getString("apellido"));

            }
        }
    }
}
