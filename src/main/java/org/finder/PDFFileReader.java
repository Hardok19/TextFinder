package org.finder;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;

/**
 * Clase encargada de leer archivos PDF y extraer palabras para insertarlas en un árbol AVL.
 * Esta clase ofrece la funcionalidad de procesar archivos PDF, extrayendo cada palabra y
 * gestionando su inserción en el árbol AVL según su aparición en el texto.
 * Utiliza un enfoque secuencial y acumulativo para mantener un conteo correcto de las posiciones de las palabras.
 */
public class PDFFileReader {
    private static final Logger logger = LogManager.getLogger(PDFFileReader.class);
    private AVLTree avlTree; // Referencia al árbol AVL donde se insertarán las palabras.

    public PDFFileReader(AVLTree avlTree) {
        this.avlTree = avlTree;
    }

    /**
     * Lee un archivo PDF y procesa su contenido para extraer palabras,
     * y las inserta en el árbol AVL con sus ocurrencias.
     *
     * @param filePath La ruta al archivo PDF que será procesado.
     */
    public void readFileAndInsertWords(String filePath) {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            String[] words = text.split("\\s+");
            int wordCount = 0;  // Contador acumulativo de palabras para mantener la posición secuencial.

            for (String word : words) {
                if (!word.isEmpty()) {
                    avlTree.insert(word, filePath, wordCount + 1);
                    wordCount++;
                }
            }
        } catch (IOException e) {
            logger.error("Error al leer el archivo PDF: " + e.getMessage(), e);
        }
    }
}
