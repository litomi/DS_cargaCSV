package com.utn.app;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.utn.app.modelos.Alumno;
import com.univocity.parsers.common.record.Record;

public class LectorCSV implements Runnable {
    private BlockingQueue<List<Alumno>> cola;
    private String rutaArchivo;
    private int cantidadRegistros;

    @Override
    public void run() {
        List<Alumno> lote = new ArrayList<>(cantidadRegistros);

        try {
            // Configuraciones para Univocity
            CsvParserSettings config = new CsvParserSettings();
            config.setHeaderExtractionEnabled(true);
            config.setLineSeparatorDetectionEnabled(true);
            config.setDelimiterDetectionEnabled(true);

            CsvParser parser = new CsvParser(config);

            for (Record registro : parser.iterateRecords(lectorArchivo(rutaArchivo))) {
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
                lote.add(
                        new Alumno(
                                registro.getInt("nro_lejago"),
                                registro.getString("nombre"),
                                registro.getString("apellido"),
                                registro.getString("nro_documento"),
                                registro.getString("tipo_documento"),
                                registro.getString("fecha_nacimiento"),
                                registro.getString("fecha_ingreso"),
                                registro.getString("sexo")));

                if (lote.size() == cantidadRegistros) {
                    cola.put(lote);
                    lote = new ArrayList<>(cantidadRegistros);
                }

            }

            // Si el último lote tiene registros pero no está lleno, lo agregamos.
            if (!lote.isEmpty()) {
                cola.put(lote);
            }
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: archivo no encontrado.");
            e.printStackTrace();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            System.err.println("ERROR: El hilo fue interrumpido mienstras esperaba.");
        }
    }

    private Reader lectorArchivo(String ruta) throws FileNotFoundException {
        return new FileReader(ruta);
    }

}
