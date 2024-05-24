package org.finder.Sorting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.finder.FileReaders.PDFFileReader;

import java.nio.file.*;
import java.nio.file.attribute.*;
import java.io.IOException;

public class FileInfo {
    private static final Logger logger = LogManager.getLogger(FileInfo.class);

    /**
     * Imprime el tamaño y la fecha de creación del archivo especificado.
     *
     * @param filePath la ruta al archivo cuyos datos se van a obtener.
     */
    public void printFileInfo(String filePath) {
        Path path = Paths.get(filePath);
        try {
            // Obtener el tamaño del archivo
            long fileSize = Files.size(path);
            System.out.println("Tamaño del archivo: " + fileSize + " bytes");

            // Obtener la fecha de creación del archivo
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            FileTime fileTime = attrs.creationTime();
            logger.info("Fecha de creación del archivo: " + fileTime);

        } catch (IOException e) {
            logger.error("Error al acceder al archivo: " + e.getMessage());
        }
    }
}