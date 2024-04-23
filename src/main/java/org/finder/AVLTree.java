package org.finder;
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
        return node.height;
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
        return height(node.left) - height(node.right);
    }
    /**
     * Normaliza una palabra eliminando toda puntuación y convirtiéndola a minúsculas.
     * Esta función es esencial para asegurar que las comparaciones de palabras en el árbol AVL se hagan
     * de manera uniforme, independientemente de las diferencias de formato o puntuación en el texto original.
     *
     * @param word La palabra que se va a normalizar.
     * @return La palabra normalizada: sin puntuación y en minúsculas.
     */
    public static String normalizeWord(String word) { // Eliminar puntuación y Convertir a minúsculas
        return word.toLowerCase().replaceAll("\\p{Punct}", "");
    }
    /**
     * Inserta una palabra en el árbol. Si la palabra ya existe, añade una nueva ocurrencia.
     *
     * @param word La palabra a insertar en el árbol.
     * @param doc El nombre del documento donde se encontró la palabra.
     * @param position La posición de la palabra en el documento.
     */
    public void insert(String word, String doc, int position) {
        String originalWord = word;
        word = normalizeWord(word);
        Occurrence occurrence = new Occurrence(doc, originalWord, position);
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
        TreeNode x = y.left;
        TreeNode T2 = x.right;
        // Realizar rotación
        x.right = y;
        y.left = T2;
        // Actualizar alturas
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
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
        TreeNode y = x.right;
        TreeNode T2 = y.left;
        // Realizar rotación
        y.left = x;
        x.right = T2;
        // Actualizar alturas
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
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
            newNode.occurrences.add(occurrence); // Añadir la ocurrencia al crear el nodo nuevo
            return newNode;
        }
        int result = root.word.compareTo(word);
        if (result > 0) {
            root.left = insertRecursive(root.left, word, occurrence);
        } else if (result < 0) {
            root.right = insertRecursive(root.right, word, occurrence);
        } else {
            // No se permiten claves duplicadas
            root.occurrences.add(occurrence); // Añadir la ocurrencia si la palabra ya existe
            return root;
        }
        // Actualizar altura de este nodo ancestro
        root.height = 1 + Math.max(height(root.left), height(root.right));
        // Obtener el factor de balance de este nodo ancestro para verificar si se desbalanceó
        int balance = getBalance(root);
        // Caso Izquierda-Izquierda
        if (balance > 1 && word.compareTo(root.left.word) < 0) {
            return rightRotate(root);
        }
        // Caso Derecha-Derecha
        if (balance < -1 && word.compareTo(root.right.word) > 0) {
            return leftRotate(root);
        }
        // Caso Izquierda-Derecha
        if (balance > 1 && word.compareTo(root.left.word) > 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        // Caso Derecha-Izquierda
        if (balance < -1 && word.compareTo(root.right.word) < 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }
        return root;
    }
    /**
     * Método recursivo para buscar un nodo por su palabra normalizada en el árbol AVL.
     * Navega por el árbol de forma recursiva hasta encontrar el nodo que contiene la palabra especificada o hasta que se alcanza una hoja del árbol.
     * Este método es crucial para operaciones de búsqueda y es utilizado internamente por el método público {@link #search}.
     *
     * @param root El nodo actual en el árbol que se está inspeccionando. Este parámetro cambia a medida que el método se llama recursivamente.
     * @param word La palabra normalizada que se busca en el árbol.
     * @return El nodo que contiene la palabra si se encuentra, o null si no se encuentra la palabra en el árbol.
     */
    private TreeNode searchRecursive(TreeNode root, String word) {
        if (root == null) {
            return null;  // El árbol está vacío o hemos llegado a una hoja sin encontrar el nodo.
        }
        if (root.word.compareTo(word) == 0) {
            return root;  // La palabra buscada coincide con el nodo actual.
        } else if (root.word.compareTo(word) > 0) {
            return searchRecursive(root.left, word);  // Buscar en el subárbol izquierdo.
        } else {
            return searchRecursive(root.right, word);  // Buscar en el subárbol derecho.
        }
    }
    /**
     * Método público para iniciar la búsqueda de una palabra en el árbol AVL.
     * Normaliza la palabra proporcionada antes de buscarla en el árbol para asegurar la consistencia en la comparación de palabras,
     * incluyendo la conversión a minúsculas, eliminación de puntuación y eliminación de espacios en blanco alrededor de la palabra.
     * Esto permite la búsqueda de una palabra en el árbol sin exponer la estructura interna del mismo y garantiza que las comparaciones
     * se hagan de manera uniforme.
     * Utiliza el método {@link #searchRecursive} para realizar la búsqueda de forma eficiente.
     *
     * @param word La palabra que se desea buscar en el árbol. La palabra será normalizada antes de la búsqueda, incluyendo el recorte de espacios.
     * @return El nodo que contiene la palabra normalizada, si se encuentra; de lo contrario, retorna null.
     */
    public TreeNode search(String word) {
        String normalizedWord = normalizeWord(word.trim());  // Normalizar la palabra recortando espacios y normalizando antes de la búsqueda
        return searchRecursive(root, normalizedWord);
    }
    /**
     * Busca una palabra en el árbol AVL y imprime detalles sobre el nodo encontrado.
     * Este método es una interfaz de usuario para mostrar la información asociada con una palabra específica,
     * incluyendo su forma normalizada y todas las ocurrencias de la palabra en los documentos.
     * Si la palabra no se encuentra en el árbol, se imprime un mensaje indicándolo.
     *
     * @param word La palabra que se busca en el árbol. La palabra debe estar normalizada antes de la búsqueda.
     */
    public void searchAndPrintDetails(String word) {
        TreeNode node = search(word);
        if (node != null) {
            // Imprimir detalles del nodo encontrado
            System.out.println("1. Palabra normalizada: " + node.word);
            System.out.println("2. Lista de ocurrencias:");
            for (Occurrence occurrence : node.occurrences) {
                System.out.println(occurrence.documentName + " / " + occurrence.originalWord + " / " + occurrence.position);
            }
        } else {
            System.out.println("Palabra no encontrada en el árbol.");
        }
    }
}