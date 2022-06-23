package main.logic;

import main.model.Node;

import java.util.EmptyStackException;
import java.util.Stack;

import static main.logic.InputValidator.isOperator;

public class ExpressionTreeConstructor {

    private StringBuilder expressionBuilder;

    public Node construct(Stack<String> postFixStack) {
        // Empty stack to store tree pointers
        Stack<Node> nodeStack = new Stack<>();

        for (String val : postFixStack) {
            if (isOperator(val)) { // Is it an operator
                Node x = null;
                Node y = null;

                try {
                    x = nodeStack.pop();;
                    y = nodeStack.pop();
                } catch (EmptyStackException e){
                    // ignore
                }

                Node node = new Node(val, y, x);

                nodeStack.push(node);
            } else nodeStack.push(new Node(val));
        }

        // a pointer to the root of the expression tree remains on the stack
        return nodeStack.peek();
    }

    public enum PrintOrder {
        INORDER, POSTORDER
    }

    public String constructString(PrintOrder order, Node root){
        expressionBuilder = new StringBuilder(order.name() + " > ");

        switch (order){
            case INORDER:
                inOrder(root);
                break;
            case POSTORDER:
                postOrder(root);
                break;
        }

        return expressionBuilder.toString();
    }

    // Print the postfix expression for an expression tree
    private void postOrder(Node root) {
        if (root == null) return;

        postOrder(root.left);
        postOrder(root.right);
        expressionBuilder.append(root.value);
    }

    // Print the infix expression for an expression tree
    private void inOrder(Node root) {
        if (root == null) return;

        if (isOperator(root.value)) {
            expressionBuilder.append("(");
        }

        inOrder(root.left);
        expressionBuilder.append(root.value);
        inOrder(root.right);

        if (isOperator(root.value)) {
            expressionBuilder.append(")");
        }
    }
}
