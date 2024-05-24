package org.finder.Sorting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.finder.Results.*;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;


public class Sorting {
    private static final Logger logger = LogManager.getLogger(Sorting.class);

    /**
     * Ordena una lista de objetos Result en orden ascendente usando el algoritmo de Bubble Sort.
     * Este método compara objetos adyacentes en la lista y los intercambia si están en el orden incorrecto
     * (en este caso, si el objeto en la posición actual tiene un tiempo de archivo mayor que el siguiente).
     * El proceso se repite hasta que no se necesiten más intercambios, indicando que la lista está ordenada.
     *
     * @param results La lista de objetos Result que se va a ordenar.
     * El método utiliza una variable booleana 'swapped' para monitorear si se realizaron intercambios
     * en una pasada particular. Si no se realizan intercambios en una iteración del bucle externo,
     * el método termina prematuramente, ya que esto indica que la lista ya está ordenada.
     */
    public static void bubbleSortAscending(List<Result> results) {
        boolean swapped;
        for (int i = 0; i < results.size() - 1; i++) {
            swapped = false;
            for (int j = 0; j < results.size() - 1 - i; j++) {
                if (results.get(j).getFileTime().compareTo(results.get(j + 1).getFileTime()) > 0) {
                    // Intercambiar elementos
                    Result temp = results.get(j);
                    results.set(j, results.get(j + 1));
                    results.set(j + 1, temp);
                    swapped = true;
                }
            }
            // Si no se intercambiaron elementos, entonces la lista ya está ordenada
            if (!swapped) {
                break;
            }
        }
    }

    /**
     * Ordena una lista de objetos Result en orden descendente usando el algoritmo de Bubble Sort.
     * Este método compara objetos adyacentes en la lista y los intercambia si están en el orden incorrecto
     * (en este caso, si el objeto en la posición actual tiene un tiempo de archivo menor que el siguiente).
     * El proceso se repite hasta que no se necesiten más intercambios, indicando que la lista está ordenada.
     *
     * @param results La lista de objetos Result que se va a ordenar.
     * El método utiliza una variable booleana 'swapped' para monitorear si se realizaron intercambios
     * en una pasada particular. Si no se realizan intercambios en una iteración del bucle externo,
     * el método termina prematuramente, ya que esto indica que la lista ya está ordenada.
     */
    public static void bubbleSortDescending(List<Result> results) {
        boolean swapped;
        for (int i = 0; i < results.size() - 1; i++) {
            swapped = false;
            for (int j = 0; j < results.size() - 1 - i; j++) {
                if (results.get(j).getFileTime().compareTo(results.get(j + 1).getFileTime()) < 0) {
                    // Intercambiar elementos
                    Result temp = results.get(j);
                    results.set(j, results.get(j + 1));
                    results.set(j + 1, temp);
                    swapped = true;
                }
            }
            // Si no se intercambiaron elementos, entonces la lista ya está ordenada
            if (!swapped) {
                break;
            }
        }
    }

    /**
     * Ordena una lista de objetos Result por fileName en orden ascendente utilizando QuickSort.
     * @param results Lista de objetos Result a ordenar.
     */
    public static void quickSortByFileNameAscending(List<Result> results) {
        quickSortFileName(results, 0, results.size() - 1, true);
    }

    /**
     * Ordena una lista de objetos Result por fileName en orden descendente utilizando QuickSort.
     * @param results Lista de objetos Result a ordenar.
     */
    public static void quickSortByFileNameDescending(List<Result> results) {
        quickSortFileName(results, 0, results.size() - 1, false);
    }

    /**
     * Método recursivo QuickSort para ordenar una lista de Result por fileName.
     * @param results Lista de objetos Result a ordenar.
     * @param low Índice inferior del segmento de lista a ordenar.
     * @param high Índice superior del segmento de lista a ordenar.
     * @param ascending Booleano que indica si el ordenamiento debe ser ascendente (true) o descendente (false).
     */
    private static void quickSortFileName(List<Result> results, int low, int high, boolean ascending) {
        if (low < high) {
            int pi = partition(results, low, high, ascending);
            quickSortFileName(results, low, pi - 1, ascending);
            quickSortFileName(results, pi + 1, high, ascending);
        }
    }

    /**
     * Particiona la lista de Result para el algoritmo QuickSort, colocando los elementos menores (o mayores) que el pivote
     * a un lado según se especifique para ordenamiento ascendente o descendente.
     * @param results Lista de objetos Result a particionar.
     * @param low Índice inferior del segmento de lista a particionar.
     * @param high Índice superior del segmento de lista a particionar.
     * @param ascending Booleano que indica si el ordenamiento debe ser ascendente (true) o descendente (false).
     * @return Índice del pivote colocado en su posición correcta.
     */
    private static int partition(List<Result> results, int low, int high, boolean ascending) {
        Result pivot = results.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (ascending ? results.get(j).getFileName().compareTo(pivot.getFileName()) <= 0
                    : results.get(j).getFileName().compareTo(pivot.getFileName()) >= 0) {
                i++;
                // Intercambiar results[i] y results[j]
                Result temp = results.get(i);
                results.set(i, results.get(j));
                results.set(j, temp);
            }
        }

        // Intercambiar results[i+1] y results[high] (o el pivote)
        Result temp = results.get(i + 1);
        results.set(i + 1, results.get(high));
        results.set(high, temp);

        return i + 1;
    }

    /**
     * Ordena una lista de objetos Result por fileSize en orden ascendente utilizando Radix Sort.
     * @param results Lista de objetos Result a ordenar.
     */
    public static void radixSortByFileSizeAscending(List<Result> results) {
        // Encontrar el máximo para saber el número de dígitos
        int max = getMax(results);
        // Aplicar counting sort para cada dígito. La exp es 10^i
        // donde i es el número actual de dígitos
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSort(results, exp, true);
        }
    }

    /**
     * Ordena una lista de objetos Result por fileSize en orden descendente utilizando Radix Sort.
     * @param results Lista de objetos Result a ordenar.
     */
    public static void radixSortByFileSizeDescending(List<Result> results) {
        // Encontrar el máximo para saber el número de dígitos
        int max = getMax(results);
        // Aplicar counting sort para cada dígito. La exp es 10^i
        // donde i es el número actual de dígitos
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSort(results, exp, false);
        }
    }

    /**
     * Calcula el tamaño de archivo máximo en una lista de objetos Result.
     * Este método recorre todos los elementos de la lista y determina el máximo valor
     * de la propiedad fileSize de los objetos Result.
     *
     * @param results La lista de objetos Result de la cual obtener el tamaño de archivo máximo.
     * @return El valor máximo de fileSize encontrado en la lista de resultados.
     */
    private static int getMax(List<Result> results) {
        int max = results.get(0).getFileSize();
        for (Result result : results) {
            if (result.getFileSize() > max) {
                max = result.getFileSize();
            }
        }
        return max;
    }

    /**
     * Realiza un Counting Sort en una lista de objetos Result basado en un dígito específico determinado por 'exp'.
     * Este método ordena los objetos Result en función del dígito específico de su propiedad fileSize,
     * el cual se calcula usando la expresión (fileSize / exp) % 10.
     *
     * @param results La lista de objetos Result a ordenar.
     * @param exp La potencia de 10 que determina el dígito a considerar para el ordenamiento.
     * @param ascending Un booleano que determina si el ordenamiento debe ser ascendente (true) o descendente (false).
     *                  Si es ascendente, los elementos se acumulan en el array 'count' de manera incremental,
     *                  si es descendente, de manera decremental.
     * Este método ajusta primero el array 'count' para que cada índice contenga la posición acumulativa del dígito.
     * Luego reordena los objetos en un array 'output' basado en estas posiciones, y finalmente copia 'output' de vuelta
     * en 'results', asegurando que los objetos estén ordenados según el dígito en cuestión.
     */
    private static void countingSort(List<Result> results, int exp, boolean ascending) {
        List<Result> output = new ArrayList<>(results.size());
        int[] count = new int[10];
        for (int i = 0; i < results.size(); i++) {
            int index = (results.get(i).getFileSize() / exp) % 10;
            count[index]++;
        }

        // Cambia count[i] para que contenga la posición actual de este dígito en output[]
        if (ascending) {
            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }
        } else {
            for (int i = 8; i >= 0; i--) {
                count[i] += count[i + 1];
            }
        }

        // Construir el array de salida
        output = new ArrayList<>(results);
        for (int i = results.size() - 1; i >= 0; i--) {
            int index = (results.get(i).getFileSize() / exp) % 10;
            output.set(count[index] - 1, results.get(i));
            count[index]--;
        }

        // Copiar el output a results, para que results contenga los números ordenados
        for (int i = 0; i < results.size(); i++) {
            results.set(i, output.get(i));
        }
    }


}