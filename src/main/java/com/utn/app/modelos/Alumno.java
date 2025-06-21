package com.utn.app.modelos;

import java.time.LocalDate;

import com.univocity.parsers.annotations.Format;
import com.univocity.parsers.annotations.Parsed;

public class Alumno {

    /*
     * nro_lejago,
     * nombre,
     * apellido,
     * nro_documento,
     * tipo_documento,
     * fecha_nacimiento,
     * fecha_ingreso,
     * sexo
     */

    @Parsed(field = "nro_legajo")
    private int nro_legajo;

    @Parsed(field = "nombre")
    private String nombre;

    @Parsed(field = "apellido")
    private String apellido;

    @Parsed(field = "nro_documento")
    private String nro_documento;

    @Parsed(field = "tipo_documento")
    private String tipo_documento;

    @Parsed(field = "fecha_nacimiento")
    private String fecha_nacimiento;

    @Parsed(field = "fecha_ingreso")
    private String fecha_ingreso;

    @Parsed(field = "sexo")
    private String sexo;

    public Alumno() {
    }

    public int getNro_legajo() {
        return nro_legajo;
    }

    public void setNro_legajo(int nro_legajo) {
        this.nro_legajo = nro_legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNro_documento() {
        return nro_documento;
    }

    public void setNro_documento(String nro_documento) {
        this.nro_documento = nro_documento;
    }

    public String getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(String fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    @Override
    public String toString() {
        return "Alumno [nro_legajo=" + nro_legajo + ", nombre=" + nombre + ", apellido=" + apellido + ", nro_documento="
                + nro_documento + ", tipo_documento=" + tipo_documento + ", fecha_nacimiento=" + fecha_nacimiento
                + ", fecha_ingreso=" + fecha_ingreso + ", sexo=" + sexo + "]";
    }

    public Alumno(int nro_legajo, String nombre, String apellido, String nro_documento, String tipo_documento,
            String fecha_nacimiento, String fecha_ingreso, String sexo) {
        this.nro_legajo = nro_legajo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nro_documento = nro_documento;
        this.tipo_documento = tipo_documento;
        this.fecha_nacimiento = fecha_nacimiento;
        this.fecha_ingreso = fecha_ingreso;
        this.sexo = sexo;
    }

}
