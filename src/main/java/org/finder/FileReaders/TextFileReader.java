package org.finder.FileReaders;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.finder.Tree.AVLTree;

/**
 * Clase encargada de leer archivos de texto y extraer palabras para insertarlas en un árbol AVL.
 * Esta clase ofrece la funcionalidad de procesar archivos de texto, extrayendo cada palabra y
 * gestionando su inserción en el árbol AVL según su aparición en el texto.
 * Utiliza un enfoque secuencial y acumulativo para mantener un conteo correcto de las posiciones de las palabras.
 */
public class TextFileReader {
    // Crear una instancia del logger para la clase
    private static final Logger logger = LogManager.getLogger(TextFileReader.class);
    private AVLTree avlTree;// Referencia al árbol AVL donde se insertarán las palabras.
    /**
     * Construye un nuevo lector de archivos de texto asociado a un árbol AVL específico.
     * Este constructor permite la inyección de una instancia de AVLTree, facilitando la gestión de las palabras extraídas.
     *
     * @param avlTree La instancia del árbol AVL que se utilizará para insertar las palabras.
     */
    public TextFileReader(AVLTree avlTree) {
        this.avlTree = avlTree;
    }

    /**
     * Lee un archivo de texto línea por línea, procesa cada línea para extraer palabras,
     * y las inserta en el árbol AVL con sus ocurrencias.
     *
     * @param filePath La ruta al archivo de texto que será procesado.
     */
    public void readFileAndInsertWords(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int wordCount = 0;  // Contador acumulativo de palabras para mantener la posición secuencial.

            // Procesar el archivo línea por línea
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\\s+"); // Divide cada línea en palabras usando espacios.

                // Procesar cada palabra en la línea
                for (String word : words) {
                    if (!word.isEmpty()) {  // Evitar procesar cadenas vacías.
                        avlTree.insert(word, filePath, wordCount + 1); // Incrementar la posición de la palabra por 1
                        wordCount++; // Aumentar el contador acumulativo de palabras
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error al leer el archivo: " + e.getMessage(), e);
        }
    }
}
