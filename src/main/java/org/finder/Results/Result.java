package org.finder.Results;
import java.nio.file.attribute.*;
public class Result {
    private String fileName;
    private String filePath;
    private int fileSize;
    private FileTime fileTime;

    /**
     * Constructor para crear un nuevo objeto Result.
     * Inicializa un nuevo objeto con los datos proporcionados para representar un archivo,
     * incluyendo su nombre, ruta, tamaño y la hora de modificación.
     *
     * @param fileName El nombre del archivo.
     * @param filePath La ruta del archivo.
     * @param fileSize El tamaño del archivo en bytes.
     * @param fileTime La hora de modificación del archivo, utilizando la clase FileTime.
     */
    public Result(String fileName, String filePath, int fileSize, FileTime fileTime) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.fileTime = fileTime;
    }

    /**
     * Devuelve el nombre del archivo representado por este objeto.
     *
     * @return El nombre del archivo.
     */
    public String getFileName() {return fileName;}

    /**
     * Devuelve el tamaño del archivo representado por este objeto.
     *
     * @return El tamaño del archivo en bytes.
     */
    public int getFileSize() {return fileSize;}

    /**
     * Devuelve la hora de modificación del archivo representado por este objeto.
     *
     * @return La hora de modificación del archivo, utilizando la clase FileTime.
     */
    public FileTime getFileTime(){return fileTime;}





}
