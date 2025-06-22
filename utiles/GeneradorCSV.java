import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

/**
 * Utilidad para generar un archivo de pruebas
 * javac GeneradorCSV.java
 * java GeneradorCSV NUMERO_DE_REGISTROS RUTA_ARCHIVO_SALIDA
 */

public class GeneradorCSV {

    private static int NUMERO_DE_REGISTROS = 2_500_000;
    private static String RUTA_ARCHIVO_SALIDA = "alumnos.csv";

    public static void main(String[] args) {

        if(args.length > 0){
            NUMERO_DE_REGISTROS = Integer.parseInt(args[0]);
            RUTA_ARCHIVO_SALIDA = args.length > 1? args[1]+".csv":RUTA_ARCHIVO_SALIDA;
        }
        
        System.out.println("Ejecutando generador de archivos.");
        System.out.println("Se generarán " + NUMERO_DE_REGISTROS + " de registros.");

        long tiempoInicio = System.currentTimeMillis();
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(RUTA_ARCHIVO_SALIDA))) {
            escritor.write(
                    "nro_legajo,"
                            + "nombre,"
                            + "apellido,"
                            + "nro_documento,"
                            + "tipo_documento,"
                            + "fecha_nacimiento,"
                            + "fecha_ingreso,"
                            + "sexo\n");

            String[] nombres = {
                    "Juan Carlos Ramirez", "Jose Luis Rodriguez", "Maria de los Angeles", "Ana Sofia del Valle",
                    "Luis Alberto Gomez", "Miguel Angel Fernandez", "Francisco Javier Diaz", "Carlos Alberto Perez",
                    "Juan Jose Martinez", "David Alejandro Sanchez", "Laura Victoria Cruz", "Sofia Valentina Reyes",
                    "Maria Fernanda Lopez", "Andrea Carolina Garcia", "Diego Armando Torres", "Ricardo Enrique Soto",
                    "Jorge Luis Borges", "Oscar Daniel Acosta", "Hector Anibal Pedernera", "Julio Cesar Chavez",
                    "Maria del Carmen Ruiz", "Jose de la Cruz Vega", "Juan de Dios Castro", "Luis Miguel del Pino",
                    "Carlos Andres del Rio", "Javier Ignacio Santos", "Fernando Luis de la Rua", "Ana Maria de Jesus",
                    "Rosa Maria de la Paz", "Victor Hugo Morales"
            };
            String[] apellidos = {
                    "Perez", "Gomez", "Rodriguez", "Lopez", "Martinez", "Garcia", "Sanchez", "Diaz",
                    "Alvarez", "Romero", "Sosa", "Torres", "Ruiz", "Ramirez", "Flores", "Benitez",
                    "Acosta", "Medina", "Herrera", "Suarez", "Gimenez", "Gutierrez", "Pereyra",
                    "Rojas", "Molina", "Castro", "Ortiz", "Silva", "Nuñez", "Rios", "Ferreyra",
                    "Ponce", "Morales", "Godoy", "Vega", "Vera", "Villalba", "Cardozo", "Ledesma",
                    "Quiroga", "Castillo", "Moreno", "Roldan", "del Campo", "de la Torre", "Sanz de la Rosa",
                    "Perez de Leon", "Garcia de la Cruz", "de la Vega", "del Rio" };

            String[] documentos = { "DNI", "LibretaEnrolamiento", "Pasaporte", "Cédula federal" };

            Random rand = new Random();

            for (int i = 1; i <= NUMERO_DE_REGISTROS; i++) {
                String nro_legajo = String.valueOf(100000 + i);
                String nombre = nombres[rand.nextInt(nombres.length)];
                String apellido = apellidos[rand.nextInt(apellidos.length)];
                String nro_documento = String.valueOf(20_000_000 + i);
                String tipo_documento = documentos[rand.nextInt(documentos.length)];
                String fecha_nacimiento = generadorFecha(1970, 2000);
                String fecha_ingreso = generadorFecha(2018, 2025);
                String sexo = rand.nextBoolean() ? "M" : "F";

                String fila = String.join(",",
                        nro_legajo,
                        "\"" + nombre + "\"",
                        "\"" + apellido + "\"",
                        nro_documento,
                        tipo_documento,
                        fecha_nacimiento,
                        fecha_ingreso,
                        sexo);
                escritor.write(fila);
                escritor.newLine();
            }
            long tiempoFin = System.currentTimeMillis();
            System.out.println("Archivo generado en " + RUTA_ARCHIVO_SALIDA);
            System.out.println("Tiempo utilizado: " + (tiempoFin - tiempoInicio) + "ms");

        } catch (Exception e) {
            System.err.println("Error al escribir el archivo.");
            e.printStackTrace();
        }
    }

    private static String generadorFecha(int inicio, int fin) {
        Random rand = new Random();
        String anio = String.valueOf(rand.nextInt(inicio, fin));
        String dia = String.valueOf(rand.nextInt(1, 28));
        String mes = String.valueOf(rand.nextInt(1, 12));

        return String.format("%2s-%2s-%4s", dia, mes, anio);
    }

    
}
