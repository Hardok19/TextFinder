package org.finder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.finder.Sorting.Sorting;
import org.finder.biblioteca.*;

import org.finder.FileReaders.DocxFileReader;
import org.finder.FileReaders.PDFFileReader;
import org.finder.FileReaders.TextFileReader;
import org.finder.Tree.AVLTree;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    static AVLTree tree = new AVLTree(); // Crear la instancia del árbol AVL

    static biblioteca biblioteca = new biblioteca(); // biblioteca
    static TextFileReader readertxt = new TextFileReader(tree);
    static PDFFileReader readerpdf = new PDFFileReader(tree);
    static DocxFileReader readerdocx = new DocxFileReader(tree);

    public static void main(String[] args) {
        // Inicia App aquí
        JFrame frame = new JFrame("My App");
        App app = new App();
        frame.setContentPane(app.panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    //añade un archivo a la biblioteca y arbol
    public static void addFile(String path){
        File fileOrFolder = new File(path);
            if (fileOrFolder.exists()) {
                if (fileOrFolder.isFile()) {
                    biblioteca.add(new File(path));
                    add2tree(path);
                } else if (fileOrFolder.isDirectory()) {
                    addfolder(fileOrFolder);
                } else {
                    logger.error("No es ni un archivo ni una carpeta.");
                }
            } else {
                logger.error("La ruta especificada no existe o tiene un problema.");
            }

    }

    //Bucle para añadir cada archivo(y carpeta) al arbol y biblioteca al seleccionar una carpeta
    private static void addfolder(File folder){
        // Obtener la lista de archivos y subdirectorios en la carpeta
        File[] files = folder.listFiles();
        // Iterar sobre cada archivo y subdirectorio
        for (File file : files) {
            addFile(file.getAbsolutePath());
        }
    }


    //añade a los readers archivos en la biblioteca
    private static void add2tree(String path){
        if((path.endsWith(".txt"))){
            readertxt.readFileAndInsertWords(path);
        } else if((path.endsWith(".docx"))) {
            readerdocx.readFileAndInsertWords(path);
        } else if (path.endsWith(".pdf")) {
            readerpdf.readFileAndInsertWords(path);
        }
        else
            logger.error("Formato de archivo inválido");
    }


    //Elimina un archivo y refresca el arbol
    public static void deleteFile(String path){
        biblioteca.delete(new File(path));
        refresh();
    }

    //refresca toda la lista
    public static void refresh() {
        tree.clear();
        int i = 0;
        while (biblioteca.size > i) {
            add2tree(biblioteca.resultado().get(i).getFilePath());
            i += 1;
        }
    }


    //Refresca un solo archivo
    public static void refreshfile(String path) {
        deleteFile(path);
        biblioteca.add(new File(path));
        add2tree(path);

    }

}
