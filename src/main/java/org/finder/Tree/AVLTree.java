package org.finder.Tree;
import java.util.ArrayList;
import java.util.List;
/**
 * Clase para representar un árbol AVL que gestiona palabras y sus ocurrencias en documentos.
 * Proporciona métodos para insertar palabras y buscar en el árbol, manteniendo el equilibrio del árbol.
 */
public class AVLTree {
    private TreeNode root;
    /**
     * Calcula y devuelve la altura de un nodo en el árbol AVL.
     * La altura es usada para ayudar a mantener el árbol balanceado y es crucial para las operaciones de rotación.
     * Si el nodo es null, retorna 0, indicando que la altura de un nodo inexistente es cero.
     *
     * @param node El nodo del cual se desea obtener la altura.
     * @return La altura del nodo especificado, o 0 si el nodo es null.
     */
    private int height(TreeNode node) {
        if (node == null)
            return 0;
        return node.getHeight();
    }
    /**
     * Calcula y devuelve el factor de balance de un nodo específico en el árbol AVL.
     * El factor de balance es la diferencia entre la altura del subárbol izquierdo y la altura del subárbol derecho.
     * Este factor es utilizado para determinar si el árbol está desbalanceado y si son necesarias rotaciones para balancearlo.
     * Un factor de balance de 0 indica que el árbol está perfectamente balanceado en ese nodo,
     * mientras que un valor distinto de 0 indica que los subárboles no están balanceados.
     *
     * @param node El nodo para el cual calcular el factor de balance.
     * @return El factor de balance del nodo especificado. Si el nodo es null, el factor de balance es 0.
     */
    private int getBalance(TreeNode node) {
        if (node == null)
            return 0;
        return height(node.getLeft()) - height(node.getRight());
    }
    /**
     * Inserta una palabra en el árbol. Si la palabra ya existe, añade una nueva ocurrencia.
     *
     * @param word La palabra a insertar en el árbol.
     */
    public void insert(String word, Occurrence occurrence) {
        root = insertRecursive(root, word, occurrence);
    }
    /**
     * Realiza una rotación hacia la derecha en el subárbol cuya raíz es el nodo proporcionado.
     * Esta operación es necesaria para mantener el árbol AVL balanceado cuando se insertan o eliminan nodos.
     *
     * @param y El nodo que actuará como raíz del subárbol que será rotado hacia la derecha.
     * @return El nuevo nodo raíz después de la rotación.
     */
    private TreeNode rightRotate(TreeNode y) {
        TreeNode x = y.getLeft();
        TreeNode T2 = x.getRight();
        // Realizar rotación
        x.setRight(y);
        y.setLeft(T2);
        // Actualizar alturas
        y.setHeight(Math.max(height(y.getLeft()), height(y.getRight())) + 1);
        x.setHeight(Math.max(height(x.getLeft()), height(x.getRight())) + 1);
        return x;
    }
    /**
     * Realiza una rotación hacia la izquierda en el subárbol cuya raíz es el nodo proporcionado.
     * Al igual que la rotación hacia la derecha, esta operación ayuda a mantener el árbol balanceado
     * tras inserciones o eliminaciones que pueden haberlo desbalanceado.
     *
     * @param x El nodo que actuará como raíz del subárbol que será rotado hacia la izquierda.
     * @return El nuevo nodo raíz después de la rotación.
     */
    private TreeNode leftRotate(TreeNode x) {
        TreeNode y = x.getRight();
        TreeNode T2 = y.getLeft();
        // Realizar rotación
        y.setLeft(x);
        x.setRight(T2);
        // Actualizar alturas
        x.setHeight(Math.max(height(x.getLeft()), height(x.getRight())) + 1);
        y.setHeight(Math.max(height(y.getLeft()), height(y.getRight())) + 1);
        return y;
    }
    /**
     * Método recursivo para insertar un nodo en el árbol AVL. Asume que la palabra recibida ya está normalizada.
     * Este método inserta una nueva palabra o añade una ocurrencia a una palabra existente,
     * manteniendo el árbol balanceado mediante rotaciones necesarias según el factor de balance.
     *
     * @param root El nodo que actualmente se está evaluando; null si el subárbol está vacío.
     * @param word La palabra normalizada que se va a insertar en el árbol.
     * @param occurrence La ocurrencia de la palabra que incluye detalles como el documento y la posición.
     * @return Retorna el nuevo nodo raíz después de posibles rotaciones para mantener el árbol balanceado.
     */
    private TreeNode insertRecursive(TreeNode root, String word, Occurrence occurrence) {
        if (root == null) {
            TreeNode newNode = new TreeNode(word);
            newNode.addOccurrence(occurrence); // Añadir la ocurrencia al crear el nodo nuevo
            return newNode;
        }
        int result = root.getWord().compareTo(word);
        if (result > 0) {
            root.setLeft(insertRecursive(root.getLeft(), word, occurrence));
        } else if (result < 0) {
            root.setRight(insertRecursive(root.getRight(), word, occurrence));
        } else {
            // No se permiten claves duplicadas
            root.addOccurrence(occurrence); // Añadir la ocurrencia si la palabra ya existe
            return root;
        }
        // Actualizar altura de este nodo ancestro
        root.setHeight(1 + Math.max(height(root.getLeft()), height(root.getRight())));
        // Obtener el factor de balance de este nodo ancestro para verificar si se desbalanceó
        int balance = getBalance(root);
        // Caso Izquierda-Izquierda
        if (balance > 1 && word.compareTo(root.getLeft().getWord()) < 0) {
            return rightRotate(root);
        }
        // Caso Derecha-Derecha
        if (balance < -1 && word.compareTo(root.getRight().getWord()) > 0) {
            return leftRotate(root);
        }
        // Caso Izquierda-Derecha
        if (balance > 1 && word.compareTo(root.getLeft().getWord()) > 0) {
            root.setLeft(leftRotate(root.getLeft()));
            return rightRotate(root);
        }
        // Caso Derecha-Izquierda
        if (balance < -1 && word.compareTo(root.getRight().getWord()) < 0) {
            root.setRight(rightRotate(root.getRight()));
            return leftRotate(root);
        }
        return root;
    }
    /**
     * Método recursivo para buscar un nodo por su palabra normalizada en el árbol AVL.
     * Navega por el árbol de forma recursiva hasta encontrar el nodo que contiene la palabra especificada o hasta que se alcanza una hoja del árbol.
     * Este método es crucial para operaciones de búsqueda y es utilizado internamente por el método público {@link #searchTreeNode}.
     *
     * @param root El nodo actual en el árbol que se está inspeccionando. Este parámetro cambia a medida que el método se llama recursivamente.
     * @param word La palabra normalizada que se busca en el árbol.
     * @return El nodo que contiene la palabra si se encuentra, o null si no se encuentra la palabra en el árbol.
     */
    private TreeNode searchTreeNodeRecursive(TreeNode root, String word) {
        if (root == null) {
            return null;  // El árbol está vacío o hemos llegado a una hoja sin encontrar el nodo.
        }
        if (root.getWord().compareTo(word) == 0) {
            return root;  // La palabra buscada coincide con el nodo actual.
        } else if (root.getWord().compareTo(word) > 0) {
            return searchTreeNodeRecursive(root.getLeft(), word);  // Buscar en el subárbol izquierdo.
        } else {
            return searchTreeNodeRecursive(root.getRight(), word);  // Buscar en el subárbol derecho.
        }
    }
    /**
     * Método público para iniciar la búsqueda de una palabra en el árbol AVL.
     * Normaliza la palabra proporcionada antes de buscarla en el árbol para asegurar la consistencia en la comparación de palabras,
     * incluyendo la conversión a minúsculas, eliminación de puntuación y eliminación de espacios en blanco alrededor de la palabra.
     * Esto permite la búsqueda de una palabra en el árbol sin exponer la estructura interna del mismo y garantiza que las comparaciones
     * se hagan de manera uniforme.
     * Utiliza el método {@link #searchTreeNodeRecursive} para realizar la búsqueda de forma eficiente.
     *
     * @param word La palabra que se desea buscar en el árbol. La palabra será normalizada antes de la búsqueda, incluyendo el recorte de espacios.
     * @return El nodo que contiene la palabra normalizada, si se encuentra; de lo contrario, retorna null.
     */
    public TreeNode searchTreeNode(String word) {
        if (root == null) {
            return null;
        }
        word = word.trim();// Normalizar la palabra recortando espacios
        String normalizedWord = Normalizer.normalizeWord(word);
        TreeNode node = searchTreeNodeRecursive(root, normalizedWord);
        if (node == null){
            return null;
        } else {
            return node;
        }
    }
    /**
     * Busca todas las ocurrencias de una palabra o frase en el árbol AVL, utilizando una búsqueda normalizada.
     * Devuelve una lista de todas las ocurrencias que coinciden con la palabra o frase normalizada proporcionada.
     *
     * @param input La palabra o frase que se desea buscar en el árbol.
     * @return Una lista de todas las ocurrencias que coinciden con la entrada normalizada, o una lista vacía si no se encuentra ninguna.
     */
    public List<Occurrence> searchAllOccurrences(String input) {
        String normalizedInput = Normalizer.normalizeWord(input);  // Normaliza la entrada
        List<Occurrence> occurrences = new ArrayList<>();

        String[] words = normalizedInput.trim().split("\\s+");
        if (words.length == 1) {
            TreeNode node = searchTreeNode(words[0]);  // Busca el nodo usando la palabra normalizada
            if (node != null) {
                return new ArrayList<>(node.getOccurrences());  // Devuelve todas las ocurrencias de la palabra normalizada
            }
        } else {
            TreeNode node = searchTreeNode(words[0]);
            if (node != null) {
                for (Occurrence occurrence : node.getOccurrences()) {
                    boolean matches = true;
                    Occurrence current = occurrence;
                    for (int i = 1; i < words.length; i++) {
                        current = current.getNext();
                        if (current == null || !Normalizer.normalizeWord(current.getOriginalWord()).equals(words[i])) {
                            matches = false;
                            break;
                        }
                    }
                    if (matches) {
                        occurrences.add(occurrence);  // Añade la ocurrencia de la primera palabra de la frase si toda la frase coincide
                    }
                }
            }
        }
        return occurrences;  // Devuelve la lista de ocurrencias encontradas o vacía si no se encuentra ninguna
    }
    /**
     * Busca una palabra o frase en el árbol AVL y devuelve todas las coincidencias encontradas.
     * Devuelve el nombre del documento y la oración contextualizada con la palabra o frase rodeada por tres numerales (###).
     *
     * @param input La palabra o frase que se desea buscar.
     * @return Una lista de cadenas con el nombre del documento y la oración contextualizada para cada coincidencia.
     */
    public List<String> searchString(String input) {
        List<Occurrence> occurrences = searchAllOccurrences(input);
        List<String> results = new ArrayList<>();
        String[] words = input.trim().split("\\s+");
        int wordsLength = words.length;
        for (Occurrence occurrence : occurrences) {
            String contextualSentence = SentenceAroundWord(occurrence, wordsLength);
            results.add(occurrence.getDocumentName() +
                    ": " + "Pocición general:" + occurrence.getPosition() +
                    ": " + "Linea:" + occurrence.getLineposition().get(0) +
                    ": " + "Pocición en linea:" + occurrence.getLineposition().get(1) +
                    ": " + contextualSentence);
        }
        return results;
    }
    /**
     * Construye y devuelve la oración completa alrededor de una ocurrencia de palabra o frase dada,
     * limitando la búsqueda a 20 palabras antes y 20 palabras después. La palabra o frase central
     * está destacada por tres numerales (###) a cada lado, lo que facilita su identificación en el texto.
     *
     * @param occurrence La ocurrencia inicial desde donde se debe comenzar a construir la oración.
     * @param length La longitud de la frase a destacar, empezando desde la ocurrencia dada.
     * @return La oración formateada con la palabra o frase destacada y limitada por palabras cercanas
     *         hasta encontrar un punto o alcanzar el límite de 20 palabras adicionales antes y después.
     */
    private String SentenceAroundWord(Occurrence occurrence, int length) {
        StringBuilder sentence = new StringBuilder();
        Occurrence current = occurrence;
        // Recolectar hasta 20 palabras antes o hasta encontrar un punto.
        int count = 0;
        boolean foundPoint = false;  // Bandera para detectar la presencia de un punto.
        while (current.getPrevious() != null && count < 20) {
            if (current.getPrevious().getOriginalWord().contains(".")) {
                foundPoint = true;  // Se detectó un punto, indicando un posible inicio de oración.
            }
            if (foundPoint) {
                break;  // Detener la recolección de palabras previas una vez que se encuentra un punto.
            }
            // Insertar la palabra anterior al principio del constructor de cadenas.
            sentence.insert(0, " " + current.getPrevious().getOriginalWord());
            current = current.getPrevious();
            count++;
        }
        // Agregar los numerales y la palabra o frase destacada.
        sentence.append("###");
        if (length == 1){
            sentence.append(" ").append(occurrence.getOriginalWord());
        }else {
            for (int i = 0; i < length && occurrence != null; i++) {
                sentence.append(" ").append(occurrence.getOriginalWord());
                occurrence = occurrence.getNext();
            }
        }
        sentence.append(" ###");
        // Recolectar hasta 20 palabras después o hasta encontrar un punto.
        current = occurrence;  // Continuar desde la última ocurrencia modificada.
        count = 0;
        while (current != null && current.getNext() != null && count < 20) {
            sentence.append(" ").append(current.getNext().getOriginalWord());
            if (current.getNext().getOriginalWord().contains(".")) {
                break;  // Detener la recolección si se encuentra un punto.
            }
            current = current.getNext();
            count++;
        }
        return sentence.toString().trim();  // Retornar la oración limpiando espacios adicionales.
    }
}
