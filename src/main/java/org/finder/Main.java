package org.finder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.finder.FileReaders.DocxFileReader;
import org.finder.FileReaders.PDFFileReader;
import org.finder.FileReaders.TextFileReader;
import org.finder.Tree.AVLTree;
import org.finder.biblioteca.biblioteca;

import java.io.File;
import java.util.List;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    static AVLTree tree = new AVLTree(); // Crear la instancia del árbol AVL

    static org.finder.biblioteca.biblioteca biblioteca = new biblioteca(); // biblioteca
    static TextFileReader readertxt = new TextFileReader(tree);
    static PDFFileReader readerpdf = new PDFFileReader(tree);
    static DocxFileReader readerdocx = new DocxFileReader(tree);


    public static void main(String[] args) {
        Gui.main(args);

    }


}