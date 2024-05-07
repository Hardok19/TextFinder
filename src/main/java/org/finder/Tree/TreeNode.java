package org.finder.Tree;

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
     * Busca una ocurrencia específica dentro de las ocurrencias del nodo donde la palabra original es igual a la palabra ingresada.
     * Esta función es útil cuando se necesita encontrar una ocurrencia exacta de una palabra en un texto, incluyendo diferencias de mayúsculas y minúsculas o puntuación.
     *
     * @param originalWord La palabra original que se busca dentro de las ocurrencias del nodo.
     * @return La ocurrencia que coincide exactamente con la palabra original, si se encuentra; de lo contrario, retorna null.
     */
    public Occurrence findExactOccurrence(String originalWord) {
        for (Occurrence occurrence : this.occurrences) {
            if (occurrence.originalWord.equals(originalWord)) {
                return occurrence;  // Devuelve la ocurrencia que coincide exactamente con la palabra original ingresada.
            }
        }
        return null;  // No se encontró una ocurrencia exacta.
    }
}
