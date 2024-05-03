package org.finder;
public class Main {
    public static void main(String[] args) {
        AVLTree tree = new AVLTree(); // Crear la instancia del árbol AVL
        TextFileReader reader = new TextFileReader(tree); // Crear la instancia del lector de archivos de texto
        PDFFileReader pdfReader = new PDFFileReader(tree); // Crear la instancia del lector de archivos PDF
        DocxFileReader docxReader = new DocxFileReader(tree);


        String filePath = "src/main/resources/example.txt"; // Asegúrate de que la ruta del archivo sea correcta
        String pdfFilePath = "src/main/resources/example.pdf"; // Asegúrate de que la ruta del archivo PDF sea correcta
        String docxFilePath = "src/main/resources/example.docx"; // Asegúrate de que la ruta del archivo sea correcta


        // Leer el archivo y cargar las palabras en el árbol AVL
        reader.readFileAndInsertWords(filePath);
        // Leer el archivo PDF y cargar las palabras en el árbol AVL
        pdfReader.readFileAndInsertWords(pdfFilePath);
        // Leer el archivo DOCX y cargar las palabras en el árbol AVL
        docxReader.readFileAndInsertWords(docxFilePath);

        // Realizar búsquedas de ejemplo y mostrar resultados
        String[] wordsToSearch = {"example", "hello", "quick", "nonexistent", "#", "12345", "word123", "123word", "quantum", "zephyr"};

        for (String word : wordsToSearch) {
            System.out.println("Buscando la palabra: " + word);
            tree.searchAndPrintDetails(word);
            System.out.println(); // Espacio entre resultados
        }
    }
}