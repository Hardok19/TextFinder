package org.finder.Tree;
import java.util.ArrayList;

/**
 * Clase para almacenar detalles de una ocurrencia de una palabra en un documento.
 * Incluye el nombre del documento, la palabra original y la posición de la palabra en el documento.
 */
public class Occurrence {
    private String documentName; // Nombre del documento donde se encontró la palabra.
    private String originalWord; // Forma original de la palabra, con su capitalización y puntuación originales.
    private int position; // Posición de la palabra en el documento.
    private ArrayList<Integer> lineposition; // (linea, posición de la palabra en la línea)
    private Occurrence previous;
    private Occurrence next;

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

    /**
     * Devuelve el nombre del documento donde ocurre la palabra.
     * @return el nombre del documento.
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     * Devuelve la palabra original encontrada en el documento.
     * @return la palabra original.
     */
    public String getOriginalWord() {
        return originalWord;
    }

    /**
     * Devuelve la posición de la palabra en el documento.
     * Esta posición podría referirse a un índice en el texto.
     * @return la posición de la palabra en el documento.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Devuelve una lista de las posiciones de línea donde se encuentra la palabra.
     * Cada entero en la lista representa una línea del documento donde aparece la palabra.
     * @return una lista de posiciones de línea.
     */
    public ArrayList<Integer> getLineposition() {
        return lineposition;
    }

    /**
     * Devuelve la ocurrencia previa de la palabra en el documento.
     * Esto puede ser útil para navegar hacia atrás a través de las ocurrencias de palabras.
     * @return la ocurrencia previa.
     */
    public Occurrence getPrevious() {
        return previous;
    }

    /**
     * Devuelve la próxima ocurrencia de la palabra en el documento.
     * Esto facilita la navegación hacia adelante a través de las ocurrencias de palabras.
     * @return la próxima ocurrencia.
     */
    public Occurrence getNext() {
        return next;
    }

    /**
     * Establece la ocurrencia previa de la palabra en el documento.
     * Este método se usa para vincular esta ocurrencia con la que la precede.
     * @param previous la ocurrencia previa a ser establecida.
     */
    public void setPrevious(Occurrence previous) {
        this.previous = previous;
    }

    /**
     * Establece la próxima ocurrencia de la palabra en el documento.
     * Este método se usa para vincular esta ocurrencia con la que la sigue.
     * @param next la próxima ocurrencia a ser establecida.
     */
    public void setNext(Occurrence next) {
        this.next = next;
    }
}