package org.finder.FileReaders;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.finder.Tree.AVLTree;
import org.finder.Tree.Normalizer;
import org.finder.Tree.Occurrence;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Clase para leer archivos DOCX y extraer palabras para su inserción en un árbol AVL.
 * Esta clase utiliza la biblioteca Apache POI para procesar archivos DOCX.
 * Extrae todas las palabras de cada párrafo del documento y las inserta en el árbol AVL,
 * manteniendo un conteo de su posición para facilitar búsquedas y referencias futuras.
 */
public class DocxFileReader {
    private static final Logger logger = LogManager.getLogger(DocxFileReader.class);
    private AVLTree avlTree; // Árbol AVL donde se insertarán las palabras.

    /**
     * Constructor que inicializa el DocxFileReader con un árbol AVL específico.
     *
     * @param avlTree el árbol AVL en el que se insertarán las palabras.
     */
    public DocxFileReader(AVLTree avlTree) {
        this.avlTree = avlTree;
    }

    /**
     * Lee un archivo DOCX desde una ruta de archivo especificada y extrae palabras de cada párrafo.
     * Las palabras extraídas se insertan en el árbol AVL junto con su posición en el texto.
     *
     * @param filePath la ruta del sistema de archivos hacia el archivo DOCX que se va a procesar.
     */
    public void readFileAndInsertWords(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(fis)) {
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            int wordCount = 0; // Contador acumulativo de palabras para mantener la posición secuencial.
            Occurrence previous = null;

            for (XWPFParagraph paragraph : paragraphs) {
                String[] words = paragraph.getText().split("\\s+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        String originalWord = word;
                        word = Normalizer.normalizeWord(word);
                        Occurrence occurrence = new Occurrence(filePath, originalWord, wordCount + 1);
                        avlTree.insert(word, occurrence);
                        wordCount++;
                        if (previous != null) {
                            occurrence.previous = previous;
                            previous.next = occurrence;
                        }
                        previous = occurrence;
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error al leer el archivo: " + e.getMessage(), e);
        }
    }
}
