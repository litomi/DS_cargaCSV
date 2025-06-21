package com.utn.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.utn.app.modelos.Alumno;
import com.zaxxer.hikari.HikariDataSource;

public class AlumnoDAO {
    private final HikariDataSource ds;

    public AlumnoDAO(HikariDataSource dataSource) {
        this.ds = dataSource;
    }
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

    public void insertarLote(List<Alumno> lote){
        String sql = String.format("INSERT INTO alumnos("
        + "nro_lejago," 
        + "nombre," 
        + "apellido," 
        + "nro_documento," 
        + "tipo_documento," 
        + "fecha_nacimiento," 
        + "fecha_ingreso," 
        + "sexo) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");

        try (Connection conex = ds.getConnection()){
            PreparedStatement pstmt = conex.prepareStatement(sql);

            for(Alumno alumno : lote){
                pstmt.setInt(1, alumno.getNro_legajo());
                pstmt.setString(2, alumno.getNombre());
                pstmt.setString(3, alumno.getApellido());
                pstmt.setString(4, alumno.getNro_documento());
                pstmt.setString(5, alumno.getTipo_documento());
                pstmt.setString(6, alumno.getFecha_nacimiento());
                pstmt.setString(7, alumno.getFecha_ingreso());
                pstmt.setString(8, alumno.getSexo());
                pstmt.addBatch();
            }
            pstmt.executeBatch();

        } catch (SQLException e) {
            System.out.println("ERROR: no se pudo insertar el lote en la base de datos.");
            e.printStackTrace();
        }
    }
}
