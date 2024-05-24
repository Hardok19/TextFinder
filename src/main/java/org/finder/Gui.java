package org.finder;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.finder.FileReaders.PDFFileReader;
import org.finder.FileReaders.TextFileReader;
import org.finder.FileReaders.DocxFileReader;
import org.finder.Sorting.Sorting;
import org.finder.Tree.AVLTree;
import org.finder.biblioteca.*;
import org.finder.Results.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Clase principal para la aplicación de biblioteca con interfaz gráfica.
 * Permite agregar, eliminar y buscar archivos en la biblioteca.
 */
public class Gui extends JFrame {
    private final JTextArea fileDisplayArea; // Área de texto para mostrar los archivos en la biblioteca
    private final JTextField searchTextField; // Campo de texto para ingresar la palabra o frase a buscar
    private JPanel resultsPanel; // Panel para mostrar los resultados de la búsqueda
    private final JComboBox<String> sortComboBox; // ComboBox para seleccionar el criterio de ordenamiento

    private final AVLTree tree; // Árbol AVL para almacenar y buscar palabras en los archivos

    private String orden = "";

    private biblioteca biblioteca = new biblioteca();

    /**
     * Constructor para inicializar la interfaz gráfica.
     */
    public Gui() {
        setTitle("Library Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Inicializar el árbol AVL y los lectores de archivos
        tree = new AVLTree();
        initializeFileReaders();

        // Columna derecha para mostrar los archivos
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        fileDisplayArea = new JTextArea();
        fileDisplayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(fileDisplayArea);
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.setPreferredSize(new Dimension(200, getHeight()));

        // Botones para agregar y eliminar archivos
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));

        JButton addButton = new JButton("Agregar Archivos");
        addButton.addActionListener(e -> addFiles());
        buttonPanel.add(addButton);

        JButton deleteButton = new JButton("Eliminar Archivos");
        deleteButton.addActionListener(e -> deleteFiles());
        buttonPanel.add(deleteButton);

        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(rightPanel, BorderLayout.EAST);

        // Sección superior para la entrada de búsqueda y el botón de búsqueda
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        searchTextField = new JTextField();
        JButton searchButton = new JButton("Buscar");
        searchButton.addActionListener(e -> searchFiles());
        topPanel.add(searchTextField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        // Botón de ordenamiento con desplegable
        String[] sortOptions = {"Nombre del archivo", "Fecha de creación", "Tamaño"};
        sortComboBox = new JComboBox<>(sortOptions);
        sortComboBox.addActionListener(e -> {
            String selectedOption = (String) sortComboBox.getSelectedItem();
            switch (selectedOption) {
                case "Nombre del archivo":
                    orden = "Name";
                    break;
                case "Fecha de creación":
                    Sorting.bubbleSortDescending(biblioteca.resultado());
                    orden = "date";
                    break;
                case "Tamaño":
                    Sorting.radixSortByFileSizeDescending(biblioteca.resultado());
                    orden = "size";
                    break;
            }
        });
        topPanel.add(sortComboBox, BorderLayout.WEST);
        topPanel.setPreferredSize(new Dimension(getWidth(), 50));
        add(topPanel, BorderLayout.NORTH);

        // Panel de resultados para mostrar los resultados de búsqueda
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        JScrollPane resultsScrollPane = new JScrollPane(resultsPanel);
        add(resultsScrollPane, BorderLayout.CENTER);

        setVisible(true);

        // Cargar archivos inicialmente
        loadFiles();
    }

    /**
     * Inicializa los lectores de archivos y lee los archivos de la carpeta biblioteca.
     */
    private void initializeFileReaders() {
        // Crear instancias de los lectores de archivos y leer los archivos de la carpeta biblioteca
        TextFileReader textFileReader = new TextFileReader(tree);
        PDFFileReader pdfFileReader = new PDFFileReader(tree);
        DocxFileReader docxFileReader = new DocxFileReader(tree);

        File libraryDir = new File("src/main/biblioteca");
        List<File> files = getAllFiles(libraryDir);

        for (File file : files) {
            if (file.getName().endsWith(".txt")) {
                textFileReader.readFileAndInsertWords(file.getAbsolutePath());
            } else if (file.getName().endsWith(".pdf")) {
                pdfFileReader.readFileAndInsertWords(file.getAbsolutePath());
            } else if (file.getName().endsWith(".docx")) {
                docxFileReader.readFileAndInsertWords(file.getAbsolutePath());
            }
        }
    }

    /**
     * Carga y muestra los archivos de la carpeta biblioteca en el área de texto.
     */
    private void loadFiles() {
        fileDisplayArea.setText("");
        File libraryDir = new File("src/main/biblioteca");
        if (libraryDir.exists() && libraryDir.isDirectory()) {
            List<File> files = getAllFiles(libraryDir);
            for (File file : files) {
                fileDisplayArea.append(file.getName() + " (" + getFileExtension(file) + ")\n");
            }
        } else {
            fileDisplayArea.setText("La carpeta 'biblioteca' no existe.");
        }
    }

    /**
     * Obtiene todos los archivos de un directorio, incluyendo los archivos en subdirectorios.
     *
     * @param dir El directorio raíz.
     * @return Lista de archivos encontrados.
     */
    private List<File> getAllFiles(File dir) {
        List<File> fileList = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && (file.getName().endsWith(".txt") || file.getName().endsWith(".pdf") || file.getName().endsWith(".docx"))) {
                    fileList.add(file);
                } else if (file.isDirectory()) {
                    fileList.addAll(getAllFiles(file));
                }
            }
        }
        return fileList;
    }

    /**
     * Obtiene la extensión de un archivo.
     *
     * @param file El archivo del cual se desea obtener la extensión.
     * @return La extensión del archivo.
     */
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // Extensión vacía
        }
        return name.substring(lastIndexOf + 1);
    }

    /**
     * Busca archivos que contienen el texto ingresado y muestra los resultados en el panel de resultados.
     */
    private void searchFiles() {
        String searchText = UnicodeHelper.removeAccents(searchTextField.getText().toLowerCase());
        resultsPanel.removeAll();
        this.biblioteca = new biblioteca();
        List<String> results = tree.searchString(searchText);
        for (String result : results) {
            String[] parts = result.split(": ");
            String filePath = parts[0];
            String textSnippet = parts[4]; // Usar el fragmento de texto correspondiente
            File file = new File(filePath);
            long creationDate = getFileCreationDate(file);


            this.biblioteca.add(file, textSnippet, creationDate, parts[3]);

        }
        mostrarpantalla(biblioteca);

    }




    public void mostrarpantalla(biblioteca biblioteca){
        if(orden.equals("Name")){
            Sorting.quickSortByFileNameDescending(this.biblioteca.resultado());
        } else if(orden.equals("date")) {
            Sorting.bubbleSortDescending(biblioteca.resultado());
        } else if(orden.equals("size")) {
            Sorting.radixSortByFileSizeDescending(this.biblioteca.resultado());
        }

        for(Result result : biblioteca.resultado()){
            resultsPanel.add(createResultPanel((new File(result.getFilePath())), result.textSnippetm, result.creationDate, result.linePosition));

        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }






    /**
     * Obtiene la fecha de creación de un archivo.
     *
     * @param file El archivo del cual se desea obtener la fecha de creación.
     * @return La fecha de creación en milisegundos.
     */
    private long getFileCreationDate(File file) {
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            return attr.creationTime().toMillis();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Crea un panel para mostrar la información de un archivo y un fragmento de texto encontrado.
     *
     * @param file         El archivo.
     * @param textSnippet  El fragmento de texto encontrado.
     * @param creationDate La fecha de creación del archivo.
     * @param linePosition La posición de la línea en el archivo.
     * @return El panel creado.
     */
    private JPanel createResultPanel(File file, String textSnippet, long creationDate, String linePosition) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        String fileInfo = "Archivo: " + file.getName() +
                " | Fecha de creación: " + new SimpleDateFormat("dd/MM/yyyy").format(creationDate) +
                " | Tamaño: " + file.length() + " bytes";

        JLabel fileInfoLabel = new JLabel(fileInfo);
        panel.add(fileInfoLabel, BorderLayout.NORTH);

        JTextArea occurrenceTextArea = new JTextArea(textSnippet);
        occurrenceTextArea.setEditable(false);
        panel.add(new JScrollPane(occurrenceTextArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton openButton = new JButton("Abrir");

        openButton.addActionListener(e -> openFileAtOccurrence(file, textSnippet, searchTextField.getText(), linePosition));

        buttonPanel.add(openButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Abre un archivo en una nueva ventana y resalta la palabra buscada.
     *
     * @param file         El archivo a abrir.
     * @param textSnippet  El fragmento de texto encontrado.
     * @param searchText   El texto buscado.
     * @param linePosition La posición de la línea en el archivo.
     */
    private void openFileAtOccurrence(File file, String textSnippet, String searchText, String linePosition) {
        JFrame fileFrame = new JFrame("Archivo: " + file.getName());
        fileFrame.setSize(800, 600);
        fileFrame.setLayout(new BorderLayout());

        JTextArea fileContentArea = new JTextArea();
        fileContentArea.setEditable(false);
        JScrollPane fileScrollPane = new JScrollPane(fileContentArea);
        fileFrame.add(fileScrollPane, BorderLayout.CENTER);

        try {
            if (file.getName().endsWith(".txt")) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                fileContentArea.read(reader, null);
                highlightText(fileContentArea, searchText);
                int linePos = extractLinePosition(linePosition);
                fileContentArea.setCaretPosition(linePos); // Posicionar el cursor
            } else if (file.getName().endsWith(".pdf")) {
                PDDocument document = PDDocument.load(file);
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                BufferedImage image = pdfRenderer.renderImage(extractLinePosition(linePosition));
                ImageIcon icon = new ImageIcon(image);
                JLabel label = new JLabel(icon);
                fileFrame.add(new JScrollPane(label), BorderLayout.CENTER);
                document.close();
            } else if (file.getName().endsWith(".docx")) {
                FileInputStream fis = new FileInputStream(file);
                XWPFDocument document = new XWPFDocument(fis);
                StringBuilder sb = new StringBuilder();
                for (XWPFParagraph para : document.getParagraphs()) {
                    sb.append(para.getText()).append("\n");
                }
                fileContentArea.setText(sb.toString());
                highlightText(fileContentArea, searchText);
                int linePos = extractLinePosition(linePosition);
                fileContentArea.setCaretPosition(linePos);
                document.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        fileFrame.setVisible(true);
    }

    /**
     * Extrae la posición de la línea a partir de la cadena de posición.
     *
     * @param linePosition La cadena de posición de la línea.
     * @return La posición de la línea como entero.
     */
    private int extractLinePosition(String linePosition) {
        // Extraer solo el número de la posición de línea
        String[] parts = linePosition.split(":");
        return Integer.parseInt(parts[1].trim());
    }

    /**
     * Resalta el texto buscado en el componente de texto.
     *
     * @param textComp El componente de texto.
     * @param pattern  El texto a resaltar.
     */
    private void highlightText(JTextComponent textComp, String pattern) {
        try {
            Highlighter hilite = textComp.getHighlighter();
            Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
            String text = textComp.getText();
            int pos = 0;

            while ((pos = text.indexOf(pattern, pos)) >= 0) {
                hilite.addHighlight(pos, pos + pattern.length(), painter);
                pos += pattern.length();
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permite al usuario agregar archivos o carpetas a la biblioteca.
     */
    private void addFiles() {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text, PDF, and Docx files", "txt", "pdf", "docx"));
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            File libraryDir = new File("src/main/biblioteca");
            for (File file : selectedFiles) {
                if (file.isDirectory()) {
                    copyDirectory(file, libraryDir);
                } else {
                    copyFile(file, libraryDir);
                }
            }
            loadFiles(); // Recargar la lista de archivos
        }
    }

    /**
     * Copia un archivo a la carpeta de la biblioteca.
     *
     * @param sourceFile El archivo de origen.
     * @param destDir    El directorio de destino.
     */
    private void copyFile(File sourceFile, File destDir) {
        try {
            Files.copy(sourceFile.toPath(), new File(destDir, sourceFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copia una carpeta y su contenido a la carpeta de la biblioteca.
     *
     * @param sourceDir La carpeta de origen.
     * @param destDir   El directorio de destino.
     */
    private void copyDirectory(File sourceDir, File destDir) {
        File[] files = sourceDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    copyDirectory(file, new File(destDir, file.getName()));
                } else {
                    copyFile(file, destDir);
                }
            }
        }
    }

    /**
     * Permite al usuario eliminar archivos o carpetas de la biblioteca.
     */
    private void deleteFiles() {
        JFileChooser fileChooser = new JFileChooser(new File("src/main/biblioteca"));
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            for (File file : selectedFiles) {
                deleteFileOrDirectory(file);
            }
            loadFiles(); // Recargar la lista de archivos
        }
    }

    /**
     * Elimina un archivo o carpeta de la biblioteca.
     *
     * @param file El archivo o carpeta a eliminar.
     */
    private void deleteFileOrDirectory(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File childFile : files) {
                    deleteFileOrDirectory(childFile);
                }
            }
        }
        this.biblioteca.delete(file);
        file.delete();
    }

    /**
     * Método principal para ejecutar la aplicación.
     *
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Gui::new);
    }
}
