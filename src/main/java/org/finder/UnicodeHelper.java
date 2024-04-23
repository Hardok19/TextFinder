package org.finder;
/**
 * Proporciona métodos utilitarios para convertir entre cadenas de texto y sus correspondientes arreglos de puntos de código Unicode.
 * Esta clase puede ser utilizada para manipular y analizar datos de texto a nivel de punto de código, lo cual es útil
 * en contextos donde se requiere el procesamiento de Unicode.
 */
public class UnicodeHelper {
    /**
     * Convierte una cadena dada en un arreglo de puntos de código Unicode.
     * Este método mapea cada carácter de la cadena de entrada a su correspondiente punto de código Unicode.
     *
     * @param input La cadena que será convertida en puntos de código Unicode.
     * @return Un arreglo de enteros donde cada entero representa un punto de código Unicode de la cadena de entrada.
     */
    public static int[] stringToUnicodeArray(String input) {
        int[] unicodeArray = new int[input.length()];
        for (int i = 0; i < input.length(); i++) {
            unicodeArray[i] = input.charAt(i);
        }
        return unicodeArray;
    }
    /**
     * Convierte un arreglo de puntos de código Unicode en una cadena de texto.
     * Este método construye una cadena concatenando los caracteres correspondientes a cada punto de código en el arreglo.
     *
     * @param unicodeArray El arreglo de puntos de código Unicode que será convertido en una cadena de texto.
     * @return Una cadena de texto construida a partir de los puntos de código Unicode proporcionados.
     */
    public static String unicodeArrayToString(int[] unicodeArray) {
        StringBuilder result = new StringBuilder();
        for (int code : unicodeArray) {
            result.append((char) code);
        }
        return result.toString();
    }
}
