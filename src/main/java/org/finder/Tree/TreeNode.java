package org.finder.Tree;

import java.util.ArrayList;
import java.util.List;
/**
 * Representa un nodo dentro de un árbol AVL que almacena palabras normalizadas y sus ocurrencias en documentos.
 * Cada nodo mantiene una lista de ocurrencias de la palabra, junto con información sobre su posición y el documento original.
 */
public class TreeNode {
    private String word; // Palabra normalizada(toda minusculas y sin ningun signo). Cuando se quiera reconstruir el texto se debe usar el originalWord en ocurrences
    private List<Occurrence> occurrences;// Lista de ocurrencias de la palabra en diversos documentos.
    private int height;// Altura del nodo dentro del árbol AVL, usado para mantener el árbol balanceado.
    private TreeNode left;
    private TreeNode right;

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
            if (occurrence.getOriginalWord().equals(originalWord)) {
                return occurrence;  // Devuelve la ocurrencia que coincide exactamente con la palabra original ingresada.
            }
        }
        return null;  // No se encontró una ocurrencia exacta.
    }
    /**
     * Devuelve la palabra asociada a este objeto.
     * Este método permite acceder al valor del atributo 'word',
     * que representa una palabra específica manejada por la instancia de la clase.
     *
     * @return la palabra como una cadena de texto.
     */
    public String getWord() {
        return word;
    }

    /**
     * Añade una nueva ocurrencia a la lista de ocurrencias del nodo.
     * @param occurrence la ocurrencia a añadir.
     */
    public void addOccurrence(Occurrence occurrence) {
        this.occurrences.add(occurrence);
    }

    /**
     * Devuelve una copia de la lista de ocurrencias del nodo.
     * Esto evita que la lista original sea modificada externamente.
     * @return una copia de la lista de ocurrencias.
     */
    public List<Occurrence> getOccurrences() {
        return new ArrayList<>(this.occurrences);
    }
    /**
     * Establece la altura de este nodo.
     * Este método permite actualizar la altura del nodo, usualmente después de inserciones o eliminaciones.
     * @param height la nueva altura del nodo.
     */
    public void setHeight(int height) {
        this.height = height;
    }
    /**
     * Retorna la altura de este nodo.
     * La altura es utilizada comúnmente en estructuras de árboles balanceados como AVL o árboles rojo-negros.
     * @return la altura del nodo.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Retorna el nodo hijo izquierdo de este nodo.
     * @return el nodo izquierdo.
     */
    public TreeNode getLeft() {
        return left;
    }

    /**
     * Establece el nodo hijo izquierdo de este nodo.
     * @param left el nodo izquierdo a ser establecido.
     */
    public void setLeft(TreeNode left) {
        this.left = left;
    }

    /**
     * Retorna el nodo hijo derecho de este nodo.
     * @return el nodo derecho.
     */
    public TreeNode getRight() {
        return right;
    }

    /**
     * Establece el nodo hijo derecho de este nodo.
     * @param right el nodo derecho a ser establecido.
     */
    public void setRight(TreeNode right) {
        this.right = right;
    }
}
