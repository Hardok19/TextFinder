package org.finder;

import java.util.ArrayList;
import java.util.List;
/**
 * Representa un nodo dentro de un árbol AVL que almacena palabras normalizadas y sus ocurrencias en documentos.
 * Cada nodo mantiene una lista de ocurrencias de la palabra, junto con información sobre su posición y el documento original.
 */
public class TreeNode {
    String word; // Palabra normalizada(toda minusculas y sin ningun signo). Cuando se quiera reconstruir el texto se debe usar el originalWord en ocurrences
    List<Occurrence> occurrences;// Lista de ocurrencias de la palabra en diversos documentos.
    int height;// Altura del nodo dentro del árbol AVL, usado para mantener el árbol balanceado.
    TreeNode left;
    TreeNode right;

    /**
     * Constructor para crear un nuevo nodo con una palabra específica.
     * Inicializa la lista de ocurrencias y configura la altura inicial del nodo a 1.
     *
     * @param word La palabra normalizada que se almacenará en este nodo.
     */
    public TreeNode(String word) {
        this.word = word;
        this.occurrences = new ArrayList<>();
        this.height = 1;
        this.left = null;
        this.right = null;
    }
    /**
     * Añade una nueva ocurrencia de la palabra a este nodo.
     * La ocurrencia contiene información sobre el documento, la palabra original y su posición en el documento.
     *
     * @param documentName El nombre del documento donde se encontró la palabra.
     * @param originalWord La palabra original, como apareció en el documento.
     * @param position La posición de la palabra en el documento.
     */
    public void addOccurrence(String documentName, String originalWord, int position) {
        Occurrence newOccurrence = new Occurrence(documentName, originalWord, position);
        this.occurrences.add(newOccurrence);
    }
}
/**
 * Clase para almacenar detalles de una ocurrencia de una palabra en un documento.
 * Incluye el nombre del documento, la palabra original y la posición de la palabra en el documento.
 */
class Occurrence {
    String documentName;// Nombre del documento donde se encontró la palabra.
    String originalWord;// Forma original de la palabra, con su capitalización y puntuación originales.
    int position;// Posición de la palabra en el documento.
    /**
     * Constructor para crear una nueva ocurrencia.
     *
     * @param documentName El nombre del documento donde se encontró la palabra.
     * @param originalWord La palabra original como se encontró en el documento.
     * @param position La posición de la palabra dentro del documento.
     */
    Occurrence(String documentName, String originalWord, int position) {
        this.documentName = documentName;
        this.originalWord = originalWord;
        this.position = position;
    }
}