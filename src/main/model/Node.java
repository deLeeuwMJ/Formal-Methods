package main.model;

public class Node {

    public String value;
    public Node left;
    public Node right;

    public Node(String value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public Node(String value, Node left, Node right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }
}
