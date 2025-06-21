package com.utn.app;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.utn.app.modelos.Alumno;

public class EscritorBD implements Runnable{
    private BlockingQueue<List<Alumno>> cole;
    private AlumnoDAO alumnoDAO;

    public EscritorBD(BlockingQueue<List<Alumno>> cole, AlumnoDAO alumnoDAO) {
        this.cole = cole;
        this.alumnoDAO = alumnoDAO;
    }

    @Override
    public void run() {
        try{
            while (true) {
                List<Alumno> lote = cole.take();
                if(lote.isEmpty()) break;
                alumnoDAO.insertarLote(lote);
            }
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
            System.out.println("Hilo interrumpido mientras esperaba la cola.");
        }
        
    }
}
