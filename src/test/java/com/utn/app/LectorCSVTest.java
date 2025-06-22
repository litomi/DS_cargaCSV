package com.utn.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import com.utn.app.modelos.Alumno;

public class LectorCSVTest {
    @TempDir
    Path carpetaTemporal;

    private File archivoCSV;
    private BlockingQueue<List<Alumno>> cola;

    @BeforeEach
    void setUp() throws IOException {
        archivoCSV = carpetaTemporal.resolve("alumnos._prueba.csv").toFile();
        
        // Extraído del archivo alumnos.csv para hacer la prueba
        try (FileWriter escritor = new FileWriter(archivoCSV)) {
            escritor.write("apellido,nombre,nro_documento,tipo_documento,fecha_nacimiento,sexo,nro_legajo,fecha_ingreso\n");
            escritor.write("VonRueden,Emely Maya Dare,35839804,LibretaEnrolamiento,1943-11-04,M,1,2014-04-16\n");
            escritor.write("Kub,Logan Lina Heaney,27605712,LibretaCivica,1982-11-13,F,2,2022-02-26\n");
            escritor.write("King,Abagail Pinkie Collins,4985084,Pasaporte,1952-10-15,M,3,2006-06-18\n");
            escritor.write("Lynch,Delta Kathryne Satterfield,9448776,LibretaCivica,1977-02-11,M,4,1975-07-05\n");
            escritor.write(
                    "Dickinson,Claud Kitty Christiansen,32000222,LibretaEnrolamiento,1984-11-18,M,5,1969-10-29\n");
            escritor.write("Dickinson,Deontae Alexanne Murray,35906383,Pasaporte,1941-09-10,M,6,2018-11-02\n");
            escritor.write("Schaefer,Delfina Sonny Williamson,35709955,LibretaCivica,1965-06-28,M,7,1996-01-19\n");
        }

        cola = new ArrayBlockingQueue<>(7);
    }

    @Test
    @DisplayName("Debería poder leer el archio CSV y encolar los lotes.")
    void deberiaPoderLeerCSVyEncolar() throws InterruptedException{
        int cantidadPorLote = 3;
        LectorCSV lectorCSV = new LectorCSV(cola, archivoCSV.getAbsolutePath(), cantidadPorLote);

        Thread hiloLector = new Thread(lectorCSV);

        hiloLector.start();

        hiloLector.join();  

        //Verificaciones
        assertEquals(3, cola.size(), "Debería haber 3 lotes en la cola");

        //Volcamos la cola en una arreglo
        List<List<Alumno>> lotesEncolados = new ArrayList<>();
        cola.drainTo(lotesEncolados);

        //Revisamos los lotes
        assertEquals(3, lotesEncolados.get(0).size(), "El primero lote debería tener 3 alumnos");
        assertEquals(3, lotesEncolados.get(1).size(), "El segundo lote debería tener 3 alumnos");
        assertEquals(1, lotesEncolados.get(2).size(), "El tercer lote debería tener 1 alumnos");

        //Ahora revisamos si los alumnos se cargan correctamente.
        //Tomamos el primero
        Alumno unAlumno = lotesEncolados.get(0).get(0);
        assertEquals(1, unAlumno.getNro_legajo());
        assertEquals("VonRueden", unAlumno.getApellido());
        assertEquals("Emely Maya Dare", unAlumno.getNombre());
    }
}
