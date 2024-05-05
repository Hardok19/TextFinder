package org.finder.Tree;

/**
 * Clase para almacenar detalles de una ocurrencia de una palabra en un documento.
 * Incluye el nombre del documento, la palabra original y la posición de la palabra en el documento.
 */
public class Occurrence {
    String documentName;// Nombre del documento donde se encontró la palabra.
    String originalWord;// Forma original de la palabra, con su capitalización y puntuación originales.
    int position;// Posición de la palabra en el documento.
    public Occurrence previous;
    public Occurrence next;

    /**
     * Constructor para crear una nueva ocurrencia.
     *
     * @param documentName El nombre del documento donde se encontró la palabra.
     * @param originalWord La palabra original como se encontró en el documento.
     * @param position     La posición de la palabra dentro del documento.
     */
    public Occurrence(String documentName, String originalWord, int position) {
        this.documentName = documentName;
        this.originalWord = originalWord;
        this.position = position;
    }
}
