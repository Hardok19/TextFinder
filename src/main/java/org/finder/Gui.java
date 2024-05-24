package org.finder;

// Importaciones necesarias para manejar archivos PDF y DOCX, componentes de GUI, y otras utilidades.
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.finder.FileReaders.PDFFileReader;
import org.finder.FileReaders.TextFileReader;
import org.finder.FileReaders.DocxFileReader;
import org.finder.Sorting.Sorting;
import org.finder.Tree.AVLTree;
import org.finder.biblioteca.*;
import org.finder.Results.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import java.awt.*;
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
    // Componentes de la GUI para mostrar archivos, ingresar texto de búsqueda, y mostrar resultados.
    private final JTextArea fileDisplayArea; // Área de texto para mostrar los archivos en la biblioteca.
    private final JTextField searchTextField; // Campo de texto para ingresar la palabra o frase a buscar.
    private final JPanel resultsPanel; // Panel para mostrar los resultados de la búsqueda.
    private final JComboBox<String> sortComboBox; // ComboBox para seleccionar el criterio de ordenamiento.

    // Árbol AVL para almacenar y buscar palabras en los archivos.
    private final AVLTree tree;

    // Variable para almacenar el criterio de ordenamiento seleccionado.
    private String orden = "";

    // Instancia de la clase biblioteca para manejar los resultados.
    private biblioteca biblioteca = new biblioteca();

    /**
     * Constructor para inicializar la interfaz gráfica.
     */
    public Gui() {
        setTitle("Library Application"); // Título de la ventana.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Operación de cierre por defecto.
        setSize(800, 600); // Tamaño de la ventana.
        setLayout(new BorderLayout()); // Layout de la ventana.

        // Inicializar el árbol AVL y los lectores de archivos.
        tree = new AVLTree();
        initializeFileReaders();

        // Columna derecha para mostrar los archivos.
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        fileDisplayArea = new JTextArea();
        fileDisplayArea.setEditable(false); // Área de texto no editable.
        JScrollPane scrollPane = new JScrollPane(fileDisplayArea); // Scroll para el área de texto.
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.setPreferredSize(new Dimension(200, getHeight())); // Tamaño preferido para el panel derecho.

        // Botones para agregar y eliminar archivos.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1)); // Layout en rejilla para los botones.

        JButton addButton = new JButton("Agregar Archivos");
        addButton.addActionListener(e -> addFiles()); // Acción al presionar el botón de agregar archivos.
        buttonPanel.add(addButton);

        JButton deleteButton = new JButton("Eliminar Archivos");
        deleteButton.addActionListener(e -> deleteFiles()); // Acción al presionar el botón de eliminar archivos.
        buttonPanel.add(deleteButton);

        rightPanel.add(buttonPanel, BorderLayout.SOUTH); // Añadir el panel de botones al sur del panel derecho.
        add(rightPanel, BorderLayout.EAST); // Añadir el panel derecho a la ventana principal.

        // Sección superior para la entrada de búsqueda y el botón de búsqueda.
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        searchTextField = new JTextField(); // Campo de texto para búsqueda.
        JButton searchButton = new JButton("Buscar");
        searchButton.addActionListener(e -> searchFiles()); // Acción al presionar el botón de búsqueda.
        topPanel.add(searchTextField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        // Botón de ordenamiento con desplegable.
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
        topPanel.setPreferredSize(new Dimension(getWidth(), 50)); // Tamaño preferido para el panel superior.
        add(topPanel, BorderLayout.NORTH); // Añadir el panel superior a la ventana principal.

        // Panel de resultados para mostrar los resultados de búsqueda.
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        JScrollPane resultsScrollPane = new JScrollPane(resultsPanel);
        add(resultsScrollPane, BorderLayout.CENTER); // Añadir el panel de resultados al centro de la ventana principal.

        setVisible(true); // Hacer visible la ventana principal.

        // Cargar archivos inicialmente.
        loadFiles();
    }

    /**
     * Inicializa los lectores de archivos y lee los archivos de la carpeta biblioteca.
     */
    private void initializeFileReaders() {
        // Crear instancias de los lectores de archivos y leer los archivos de la carpeta biblioteca.
        TextFileReader textFileReader = new TextFileReader(tree);
        PDFFileReader pdfFileReader = new PDFFileReader(tree);
        DocxFileReader docxFileReader = new DocxFileReader(tree);

        File libraryDir = new File("src/main/biblioteca");
        List<File> files = getAllFiles(libraryDir); // Obtener todos los archivos de la carpeta biblioteca.

        for (File file : files) {
            if (file.getName().endsWith(".txt")) {
                textFileReader.readFileAndInsertWords(file.getAbsolutePath()); // Leer y procesar archivos de texto.
            } else if (file.getName().endsWith(".pdf")) {
                pdfFileReader.readFileAndInsertWords(file.getAbsolutePath()); // Leer y procesar archivos PDF.
            } else if (file.getName().endsWith(".docx")) {
                docxFileReader.readFileAndInsertWords(file.getAbsolutePath()); // Leer y procesar archivos DOCX.
            }
        }
    }

    /**
     * Carga y muestra los archivos de la carpeta biblioteca en el área de texto.
     */
    private void loadFiles() {
        fileDisplayArea.setText(""); // Limpiar el área de texto.
        File libraryDir = new File("src/main/biblioteca");
        if (libraryDir.exists() && libraryDir.isDirectory()) {
            List<File> files = getAllFiles(libraryDir); // Obtener todos los archivos de la carpeta biblioteca.
            for (File file : files) {
                fileDisplayArea.append(file.getName() + " (" + getFileExtension(file) + ")\n"); // Mostrar los archivos en el área de texto.
            }
        } else {
            fileDisplayArea.setText("La carpeta 'biblioteca' no existe."); // Mensaje de error si la carpeta no existe.
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
                    fileList.add(file); // Añadir archivos a la lista.
                } else if (file.isDirectory()) {
                    fileList.addAll(getAllFiles(file)); // Recursivamente añadir archivos de subdirectorios.
                }
            }
        }
        return fileList; // Devolver la lista de archivos.
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
            return ""; // Extensión vacía.
        }
        return name.substring(lastIndexOf + 1); // Devolver la extensión del archivo.
    }

    /**
     * Busca archivos que contienen el texto ingresado y muestra los resultados en el panel de resultados.
     */
    private void searchFiles() {
        String searchText = UnicodeHelper.removeAccents(searchTextField.getText().toLowerCase()); // Eliminar acentos y convertir a minúsculas el texto de búsqueda.
        resultsPanel.removeAll(); // Limpiar el panel de resultados.
        this.biblioteca = new biblioteca(); // Crear nueva instancia de biblioteca para los resultados.
        List<String> results = tree.searchString(searchText); // Buscar la cadena en el árbol AVL.
        for (String result : results) {
            String[] parts = result.split(": ");
            String filePath = parts[0];
            String textSnippet = parts[4]; // Usar el fragmento de texto correspondiente.
            File file = new File(filePath);
            long creationDate = getFileCreationDate(file); // Obtener la fecha de creación del archivo.

            this.biblioteca.add(file, textSnippet, creationDate, parts[3]); // Añadir el resultado a la biblioteca.
        }
        mostrarpantalla(biblioteca); // Mostrar los resultados en la pantalla.
    }

    /**
     * Muestra los resultados en el panel de resultados y los ordena según el criterio seleccionado.
     *
     * @param biblioteca La biblioteca que contiene los resultados de la búsqueda.
     */
    public void mostrarpantalla(biblioteca biblioteca) {
        // Ordenar los resultados según el criterio seleccionado.
        if (orden.equals("Name")) {
            Sorting.quickSortByFileNameDescending(this.biblioteca.resultado());
        } else if (orden.equals("date")) {
            Sorting.bubbleSortDescending(biblioteca.resultado());
        } else if (orden.equals("size")) {
            Sorting.radixSortByFileSizeDescending(this.biblioteca.resultado());
        }

        // Añadir los resultados al panel de resultados.
        for (Result result : biblioteca.resultado()) {
            resultsPanel.add(createResultPanel(new File(result.getFilePath()), result.textSnippetm, result.creationDate, result.linePosition));
        }
        resultsPanel.revalidate(); // Refrescar el panel de resultados.
        resultsPanel.repaint(); // Repintar el panel de resultados.
    }

    /**
     * Obtiene la fecha de creación de un archivo.
     *
     * @param file El archivo del cual se desea obtener la fecha de creación.
     * @return La fecha de creación en milisegundos.
     */
    private long getFileCreationDate(File file) {
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class); // Leer los atributos del archivo.
            return attr.creationTime().toMillis(); // Devolver la fecha de creación en milisegundos.
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // En caso de error, devolver 0.
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
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Borde negro para el panel.

        String fileInfo = "Archivo: " + file.getName() +
                " | Fecha de creación: " + new SimpleDateFormat("dd/MM/yyyy").format(creationDate) +
                " | Tamaño: " + file.length() + " bytes";

        JLabel fileInfoLabel = new JLabel(fileInfo); // Etiqueta con la información del archivo.
        panel.add(fileInfoLabel, BorderLayout.NORTH); // Añadir la etiqueta al norte del panel.

        JTextArea occurrenceTextArea = new JTextArea(textSnippet); // Área de texto con el fragmento de texto encontrado.
        occurrenceTextArea.setEditable(false); // Hacer no editable el área de texto.
        panel.add(new JScrollPane(occurrenceTextArea), BorderLayout.CENTER); // Añadir el área de texto con scroll al centro del panel.

        JPanel buttonPanel = new JPanel();
        JButton openButton = new JButton("Abrir");

        openButton.addActionListener(e -> openFileAtOccurrence(file, searchTextField.getText(), linePosition)); // Acción al presionar el botón de abrir.

        buttonPanel.add(openButton); // Añadir el botón al panel de botones.

        panel.add(buttonPanel, BorderLayout.SOUTH); // Añadir el panel de botones al sur del panel principal.

        return panel; // Devolver el panel creado.
    }

    /**
     * Abre un archivo en una nueva ventana y resalta la palabra buscada.
     *
     * @param file         El archivo a abrir.
     * @param searchText   El texto buscado.
     * @param linePosition La posición de la línea en el archivo.
     */
    private void openFileAtOccurrence(File file, String searchText, String linePosition) {
        JFrame fileFrame = new JFrame("Archivo: " + file.getName());
        fileFrame.setSize(800, 600); // Tamaño de la ventana.
        fileFrame.setLayout(new BorderLayout());

        JTextArea fileContentArea = new JTextArea();
        fileContentArea.setEditable(false); // Área de texto no editable.
        JScrollPane fileScrollPane = new JScrollPane(fileContentArea); // Scroll para el área de texto.
        fileFrame.add(fileScrollPane, BorderLayout.CENTER);

        try {
            // Abrir y leer el archivo según su tipo.
            if (file.getName().endsWith(".txt")) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                fileContentArea.read(reader, null); // Leer el contenido del archivo.
                highlightText(fileContentArea, searchText); // Resaltar el texto buscado.
                fileContentArea.setCaretPosition(findPositionInText(fileContentArea.getText(), linePosition)); // Posicionar el cursor en la línea correcta.
            } else if (file.getName().endsWith(".pdf")) {
                PDDocument document = PDDocument.load(file);
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document); // Obtener el texto del archivo PDF.
                fileContentArea.setText(text); // Establecer el texto en el área de texto.
                highlightText(fileContentArea, searchText); // Resaltar el texto buscado.
                fileContentArea.setCaretPosition(findPositionInText(text, linePosition)); // Posicionar el cursor en la línea correcta.
                document.close(); // Cerrar el documento PDF.
            } else if (file.getName().endsWith(".docx")) {
                FileInputStream fis = new FileInputStream(file);
                XWPFDocument document = new XWPFDocument(fis);
                StringBuilder sb = new StringBuilder();
                for (XWPFParagraph para : document.getParagraphs()) {
                    sb.append(para.getText()).append("\n"); // Obtener el texto del archivo DOCX.
                }
                String text = sb.toString();
                fileContentArea.setText(text); // Establecer el texto en el área de texto.
                highlightText(fileContentArea, searchText); // Resaltar el texto buscado.
                fileContentArea.setCaretPosition(findPositionInText(text, linePosition)); // Posicionar el cursor en la línea correcta.
                document.close(); // Cerrar el documento DOCX.
            }
        } catch (IOException ex) {
            ex.printStackTrace(); // Manejar la excepción en caso de error.
        }

        fileFrame.setVisible(true); // Hacer visible la ventana del archivo.
    }

    /**
     * Encuentra la posición de la línea en el texto.
     *
     * @param text El texto completo.
     * @param linePosition La posición de la línea.
     * @return La posición del cursor en el texto.
     */
    private int findPositionInText(String text, String linePosition) {
        String[] lines = text.split("\n"); // Dividir el texto en líneas.
        int lineNum = Integer.parseInt(linePosition.split(":")[1].trim()); // Obtener el número de línea.
        int pos = 0;
        for (int i = 0; i < lineNum - 1 && i < lines.length; i++) {
            pos += lines[i].length() + 1; // +1 por el carácter de nueva línea.
        }
        return pos; // Devolver la posición del cursor.
    }

    /**
     * Resalta el texto buscado en el componente de texto.
     *
     * @param textComp El componente de texto.
     * @param pattern  El texto a resaltar.
     */
    private void highlightText(JTextComponent textComp, String pattern) {
        try {
            Highlighter hilite = textComp.getHighlighter(); // Obtener el resaltador del componente de texto.
            Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW); // Establecer el color de resaltado.
            String text = textComp.getText();
            int pos = 0;

            while ((pos = text.indexOf(pattern, pos)) >= 0) {
                hilite.addHighlight(pos, pos + pattern.length(), painter); // Resaltar el texto.
                pos += pattern.length();
            }
        } catch (BadLocationException e) {
            e.printStackTrace(); // Manejar la excepción en caso de error.
        }
    }

    /**
     * Permite al usuario agregar archivos o carpetas a la biblioteca.
     */
    private void addFiles() {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setMultiSelectionEnabled(true); // Permitir selección múltiple de archivos.
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // Permitir selección de archivos y carpetas.
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text, PDF, and Docx files", "txt", "pdf", "docx")); // Filtrar los tipos de archivos permitidos.
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles(); // Obtener los archivos seleccionados.
            File libraryDir = new File("src/main/biblioteca");
            for (File file : selectedFiles) {
                if (file.isDirectory()) {
                    copyDirectory(file, libraryDir); // Copiar el contenido de la carpeta.
                } else {
                    copyFile(file, libraryDir); // Copiar el archivo.
                }
            }
            loadFiles(); // Recargar la lista de archivos.
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
            Files.copy(sourceFile.toPath(), new File(destDir, sourceFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING); // Copiar el archivo.
        } catch (IOException e) {
            e.printStackTrace(); // Manejar la excepción en caso de error.
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
                    copyDirectory(file, new File(destDir, file.getName())); // Recursivamente copiar el contenido de la carpeta.
                } else {
                    copyFile(file, destDir); // Copiar el archivo.
                }
            }
        }
    }

    /**
     * Permite al usuario eliminar archivos o carpetas de la biblioteca.
     */
    private void deleteFiles() {
        JFileChooser fileChooser = new JFileChooser(new File("src/main/biblioteca"));
        fileChooser.setMultiSelectionEnabled(true); // Permitir selección múltiple de archivos.
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // Permitir selección de archivos y carpetas.
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles(); // Obtener los archivos seleccionados.
            for (File file : selectedFiles) {
                deleteFileOrDirectory(file); // Eliminar el archivo o carpeta.
            }
            loadFiles(); // Recargar la lista de archivos.
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
                    deleteFileOrDirectory(childFile); // Recursivamente eliminar el contenido de la carpeta.
                }
            }
        }
        this.biblioteca.delete(file); // Eliminar el archivo o carpeta de la biblioteca.
        file.delete(); // Eliminar el archivo o carpeta del sistema de archivos.
    }

    /**
     * Método principal para ejecutar la aplicación.
     *
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Gui::new); // Ejecutar la aplicación.
    }
}
