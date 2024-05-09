package org.finder.Tree;

public class Normalizer {
    /**
     * Normaliza una palabra eliminando toda puntuación y convirtiéndola a minúsculas.
     * Esta función es esencial para asegurar que las comparaciones de palabras en el árbol AVL se hagan
     * de manera uniforme, independientemente de las diferencias de formato o puntuación en el texto original.
     *
     * @param word La palabra que se va a normalizar.
     * @return La palabra normalizada: sin puntuación y en minúsculas.
     */
    public static String normalizeWord(String word) {
        return word.toLowerCase().replaceAll("\\p{Punct}", "");
    }
}
