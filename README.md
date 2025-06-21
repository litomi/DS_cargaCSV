#CargadorMasivoCSV


Se trabajará con hilos.
Un hilo para el lector del archivo.
Los demás para los grabadores en base de datos.
Flujo:
El lector de archivo, en un hilo. leerá del archivo, creará un lote de filas y lo insertará en la cola. Inmediatamente comenzará a crear el siguiente lote.
El escritor de la base, tomará un lote de la cola, se lo pasará al DAO y este lo insertará en la base de datos en la base de datos.
