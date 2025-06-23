package com.utn.app;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.utn.app.modelos.Alumno;

public class EscritorBD implements Runnable{
    private BlockingQueue<List<Alumno>> cola;
    private AlumnoDAO alumnoDAO;

    public EscritorBD(BlockingQueue<List<Alumno>> cola, AlumnoDAO alumnoDAO) {
        this.cola = cola;
        this.alumnoDAO = alumnoDAO;
    }

    @Override
    public void run() {
        try{
            while (true) {
                List<Alumno> lote = cola.take();
                if(lote.isEmpty()) break;
                alumnoDAO.insertarLote(lote);
            }
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
            System.out.println("Hilo interrumpido mientras esperaba la cola.");
        }
        
    }
}
