package Bookstore;

public class AVLTree {
    class Node {
        int orderID;
        String bookName;
        Node left, right;
        int nodeheight;

        Node(int orderID, String bookName) {
            this.orderID = orderID;
            this.bookName = bookName;
            nodeheight = 1;
        }
    }

    private Node root;

    // Method to return the height of the tree
    public int getHeight() {
        return height(root);
    }

    // Helper function to get the height of a node
    private int height(Node node) {
        return (node == null) ? 0 : node.nodeheight;
    }
   
    // Helper function to update the height of a node
    public void updateHeight(Node node) {
    	/*
    	 * parameter node - node from which all descendent nodes are counted
         * return value - number of nodes under that node (including the node itself)
    	 */
    	node.nodeheight = max(height(node.left), height(node.right)) + 1;
    }

    // Count the total number of nodes in the tree
    public int getNodeCount() {
        return countNodes(root);
    }

    private int countNodes(Node node) {
    	/*
    	 * parameter node - node from which all descendent nodes are counted
    	 * return value - number of nodes under that node (including the node itself)
    	 */
        if (node == null) return 0;
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    // Helper function to get maximum of two integers
    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

    // Right rotation (single rotation)
    private Node rightRotate(Node x) {
		/*
		 * parameter x - root node of subtree being rotated
		 * return value - newly rotated node
		 */
        Node y = x.left;
        Node temp = y.right;
       
        // Perform rotation
        y.right = x;
        x.left = temp;
       
        // Update heights
        updateHeight(x);
        updateHeight(y);
       
        // Return new root
        return y;
    }

    // Left rotation (single rotation)
    private Node leftRotate(Node x) {
		/*
		 * parameter x - root node of subtree being rotated
		 * return value - newly rotated node
		 */
        Node y = x.right;
        Node temp = y.left;

        // Perform rotation
        y.left = x;
        x.right = temp;

        // Update heights
        updateHeight(x);
        updateHeight(y);

        // Return new root
        return y;
    }

    // Get balance factor of a node
    private int getBalance(Node node) {
		/*
		 * parameter node - node balance factor of which is being calculated
		 * return value - balance factor (difference in height between left and right daughter nodes)
		 */
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    // Insert method to add a new order
    public void insert(int orderID, String bookName) {
        root = insert(root, orderID, bookName);
    }

    private Node insert(Node node, int orderID, String bookName) {
		/* Normal BST insert
		 * parameter node - node in which the book is being inserted
		 * parameter orderID - number that determines the book's place in the tree
		 * parameter bookName  name of book
		 * return value - updated node object with new book information
		 */
        if (node == null)
            return new Node(orderID, bookName);

        if (orderID < node.orderID)
            node.left = insert(node.left, orderID, bookName);
        else if (orderID > node.orderID)
            node.right = insert(node.right, orderID, bookName);
        else
            return node; // Duplicate orderID is not allowed

        // Update height of the current node
        updateHeight(node);

        // Balance the tree
        return balanceTree(node);
    }

    // Search method to find a node by Order ID and return the book name
    public String search(int orderID) {
        return search(root, orderID);
    }

    private String search(Node node, int orderID) {
		/* parameter node - node being searched
		 * parameter orderID - ID of book being searched for
		 * return value - name of book being searched for
		 */
        if (node == null) {
            return null; // Order ID not found
        }

        if (orderID < node.orderID) {
            return search(node.left, orderID); // Search left subtree
        } else if (orderID > node.orderID) {
            return search(node.right, orderID); // Search right subtree
        } else {
            return node.bookName; // Order ID found, return book name
        }
    }

    // Delete a node
    public void delete(int orderID) {
        root = delete(root, orderID);
    }

    private Node delete(Node root, int orderID) {
		/* Perform standard BST delete
		 * parameter root - root node of the current subtree in which to search for and delete the node
		 * parameter orderID - unique identifier of the order to be deleted from the tree
		 * return value - root node of the modified subtree after deletion and rebalancing (if applicable)
		 */
        if (root == null)
            return root;

        if (orderID < root.orderID)
            root.left = delete(root.left, orderID);
        else if (orderID > root.orderID)
            root.right = delete(root.right, orderID);
        else {
            // Node with one child or no child
            if ((root.left == null) || (root.right == null)) {
                Node temp = (root.left != null) ? root.left : root.right;
                if (temp == null) {
                    temp = root;
                    root = null;
                } else
                    root = temp;
            } else {
                // Node with two children: Get the inorder successor
                Node temp = findMinOrder(root.right);

                root.orderID = temp.orderID;
                root.bookName = temp.bookName;

                root.right = delete(root.right, temp.orderID);
            }
        }

        if (root == null)
            return root;
       
        updateHeight(root);

        return balanceTree(root);
    }

    // Helper to balance the tree after insert or delete
    private Node balanceTree(Node node) {
    	/*
    	 * parameter node - root node of the subtree that may need balancing
    	 * return value - new root of the balanced subtree
    	 */
        int balance = getBalance(node);

        if (balance > 1) {
            if (getBalance(node.left) < 0)
                node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1) {
            if (getBalance(node.right) > 0)
                node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    // Find node with minimum order ID
    public Node findMinOrder() {
        return findMinOrder(root);
    }

    private Node findMinOrder(Node node) {
		/*
		 * parameter node - node being tested
		 * return value - node object with lowest order number
		 */
        while (node.left != null)
            node = node.left;
        return node;
    }

    // Find node with maximum order ID
    public Node findMaxOrder() {
        return findMaxOrder(root);
    }

    private Node findMaxOrder(Node node) {
		/*
		 * parameter node - node being tested
		 * return value - node object with highest order number
		 */
        while (node.right != null)
            node = node.right;
        return node;
    }

    // In-order traversal to print all books in ascending order
    public void inOrderTraversal() {
        inOrderTraversal(root);
    }

    private void inOrderTraversal(Node node) {
    	/*
    	 * parameter node - node object name and ID of which is being printed
    	 */
        if (node != null) {
            inOrderTraversal(node.left);
            System.out.println("Order ID: " + node.orderID + ", Book: " + node.bookName);
            inOrderTraversal(node.right);
        }
    }
}
