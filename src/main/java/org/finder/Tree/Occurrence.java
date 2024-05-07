package org.finder.Tree;

import java.util.ArrayList;

/**
 * Clase para almacenar detalles de una ocurrencia de una palabra en un documento.
 * Incluye el nombre del documento, la palabra original y la posición de la palabra en el documento.
 */
public class Occurrence {
    String documentName; // Nombre del documento donde se encontró la palabra.
    String originalWord; // Forma original de la palabra, con su capitalización y puntuación originales.
    int position; // Posición de la palabra en el documento.
    ArrayList<Integer> lineposition; // (linea, posición de la palabra en la línea)
    public Occurrence previous;
    public Occurrence next;

    /**
     * Constructor para crear una nueva ocurrencia.
     *
     * @param documentName El nombre del documento donde se encontró la palabra.
     * @param originalWord La palabra original como se encontró en el documento.
     * @param position     La posición de la palabra dentro del documento.
     * @param line         Número de línea en el documento.
     * @param linePosition Posición de la palabra dentro de la línea.
     */
    public Occurrence(String documentName, String originalWord, int position, int line, int linePosition) {
        this.documentName = documentName;
        this.originalWord = originalWord;
        this.position = position;
        this.lineposition = new ArrayList<>();
        this.lineposition.add(line);
        this.lineposition.add(linePosition);
    }
}