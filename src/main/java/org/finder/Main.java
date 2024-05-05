package org.finder;

import org.finder.FileReaders.DocxFileReader;
import org.finder.FileReaders.PDFFileReader;
import org.finder.FileReaders.TextFileReader;
import org.finder.Tree.AVLTree;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        AVLTree tree = new AVLTree(); // Crear la instancia del árbol AVL
        TextFileReader reader = new TextFileReader(tree); // Crear la instancia del lector de archivos de texto

        String filePath = "src/main/resources/example2.txt";

        // Leer los archivos y cargar las palabras en el árbol AVL
        reader.readFileAndInsertWords(filePath);
        // Frases y palabras para buscar
        String[] searches = {"example", "quick brown fox  "};

        // Bucle para realizar búsquedas y mostrar resultados
        for (String search : searches) {
            System.out.println("Buscando: '" + search + "'");
            List<String> results = tree.searchString(search);
            if (results.isEmpty()) {
                System.out.println("No se encontraron resultados para: " + search);
            } else {
                System.out.println("Resultados encontrados:");
                for (String result : results) {
                    System.out.println(result);
                }
            }
            System.out.println(); // Espacio entre resultados para claridad
        }
    }
}
