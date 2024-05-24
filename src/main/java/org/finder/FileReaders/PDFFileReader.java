package org.finder.FileReaders;

import org.finder.Tree.AVLTree;
import org.finder.Tree.Normalizer;
import org.finder.Tree.Occurrence;
import java.io.IOException;
import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Clase encargada de leer archivos PDF y extraer palabras para insertarlas en un árbol AVL.
 * Esta clase ofrece la funcionalidad de procesar archivos PDF, extrayendo cada palabra y
 * gestionando su inserción en el árbol AVL según su aparición en el texto.
 * Utiliza un enfoque secuencial y acumulativo para mantener un conteo correcto de las posiciones de las palabras.
 * Además, esta clase es capaz de manejar textos distribuidos a lo largo de múltiples páginas,
 * conservando la precisión en la ubicación de las palabras tanto en el documento como en la estructura de líneas.
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
            int pageCounter = 0;
            while (pageCounter < document.getNumberOfPages()) {
                pdfStripper.setStartPage(pageCounter + 1);
                pdfStripper.setEndPage(pageCounter + 1);
                String pageText = pdfStripper.getText(document);
                String[] lines = pageText.split("\\r?\\n");
                int lineCount = 0;
                int wordCount = 0;  // Contador acumulativo de palabras para mantener la posición secuencial.
                Occurrence previous = null;

                for (String line : lines) {
                    lineCount++;
                    String[] words = line.split("\\s+");
                    int lineWordCount = 0;  // Contador de palabras en la línea actual.

                    for (String word : words) {
                        if (!word.isEmpty()) {
                            lineWordCount++;
                            String originalWord = word;
                            word = Normalizer.normalizeWord(word);
                            Occurrence occurrence = new Occurrence(filePath, originalWord, wordCount + 1, lineCount, lineWordCount);
                            avlTree.insert(word, occurrence);
                            wordCount++;
                            if (previous != null) {
                                occurrence.setPrevious(previous);
                                previous.setNext(occurrence);
                            }
                            previous = occurrence;
                        }
                    }
                }
                pageCounter++;
            }
        } catch (IOException e) {
            logger.error("Error al leer el archivo PDF: " + e.getMessage(), e);
        }
    }
}