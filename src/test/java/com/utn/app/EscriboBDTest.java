package com.utn.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.utn.app.modelos.Alumno;

public class EscriboBDTest {

    //Clase falsa para reemplazar a AlumnoDAO
    private static class FalsoAlumnoDAO extends AlumnoDAO{
        private  List<Alumno> lote = null;

        public FalsoAlumnoDAO(){
            super(null); //no usaremos DataSource
        }

        
        @Override
        public void insertarLote(List<Alumno> lote) {
            this.lote = lote;
        }

        public List<Alumno> loteRecibido(){
            return this.lote;
        }
        
    }

    @Test
    @DisplayName("Debería tomar un lote y pasarlo a AlumnoDAO")
    void deberiaTomarUnLoteYPasarloAlDAO() throws InterruptedException{

        FalsoAlumnoDAO falsoDAO = new FalsoAlumnoDAO();

        BlockingQueue<List<Alumno>> cola = new ArrayBlockingQueue<>(5);

        List<Alumno> lote = new ArrayList<>();

        Alumno alumno = new Alumno();
        alumno.setNro_legajo(1);
        alumno.setApellido("Garmendia");
        alumno.setNombre("Jorge");
        lote.add(alumno);

        cola.put(lote);

        cola.put(Collections.emptyList());

        EscritorBD escritorBD = new EscritorBD(cola, falsoDAO);

        Thread hiloEscriboBD = new Thread(escritorBD);

        hiloEscriboBD.start();

        hiloEscriboBD.join();

        List<Alumno> lotePrueba = falsoDAO.loteRecibido();

        assertNotNull(lotePrueba, "El DAO debería haber recibido un lote");

        assertEquals(1, lotePrueba.size());

        Alumno alumnoRedibido = lotePrueba.get(0);
        assertEquals(1, alumnoRedibido.getNro_legajo());
        assertEquals("Garmendia", alumnoRedibido.getApellido());
        
    }

}
