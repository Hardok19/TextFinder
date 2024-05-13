package org.finder;

import org.finder.FileReaders.DocxFileReader;
import org.finder.FileReaders.PDFFileReader;
import org.finder.FileReaders.TextFileReader;
import org.finder.Tree.AVLTree;
import java.util.List;
import org.finder.Sorting.FileInfo;

public class Main {
    public static void main(String[] args) {
        AVLTree tree = new AVLTree(); // Crear la instancia del árbol AVL

        TextFileReader readertxt = new TextFileReader(tree);
        PDFFileReader readerpdf = new PDFFileReader(tree);
        DocxFileReader readerdocx = new DocxFileReader(tree);

        String filePath1 = "src/main/resources/example.txt";
        String filePath2 = "src/main/resources/example.pdf";
        String filePath3 = "src/main/resources/example.docx";

        FileInfo fileInfo = new FileInfo();
        fileInfo.printFileInfo(filePath1);
        fileInfo.printFileInfo(filePath2);
        fileInfo.printFileInfo(filePath3);

        readertxt.readFileAndInsertWords(filePath1);

        // Frases y palabras para buscar
        String[] searches = {"EXAMPLE", "fox   ", "multiple "};

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
