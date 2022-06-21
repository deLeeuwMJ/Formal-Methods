package main.logic;

import main.model.Operator;
import main.model.RegexOperationSequence;

import java.util.List;
import java.util.Stack;

import static main.logic.InputValidator.getOperator;
import static main.logic.InputValidator.isOperator;

public class PostfixNotationParser {

    private int getPrecedence(String c) {
        if (isOperator(c)) {
            switch (getOperator(c)) {
                case OR:
                    return 1;
                case DOT:
                    return 2;
                case STAR:
                case PLUS:
                    return 3;
            }
        }
        return -1;
    }

    public Stack<String> parse(RegexOperationSequence operations) {
        List<String> sequence = operations.getSequence();

        Stack<String> outputQueue = new Stack<>();
        Stack<String> operatorQueue = new Stack<>();

        /*
        Using Shunt-Yard Algorithm to get rid of the parenthesis and convert into postfix
        Postfix explanation: https://runestone.academy/ns/books/published/pythonds/BasicDS/InfixPrefixandPostfixExpressions.html
        */
        for (int i = 0; i < sequence.size(); i++) {
            String val = operations.getSequence().get(i);

            // Check if its an operator;
            if (getPrecedence(val) > 0) {
                while (!operatorQueue.isEmpty() && getPrecedence(operatorQueue.peek()) >= getPrecedence(val)) {
                    outputQueue.push(operatorQueue.pop());
                }
                operatorQueue.push(val);
            } else if (val.equals(")")) {
                String x = operatorQueue.pop();
                while (!x.equals("(")) {
                    outputQueue.push(x);
                    x = operatorQueue.pop();
                }
            } else if (val.equals("(")) {
                operatorQueue.push(val);
            } else {
                //character is neither operator nor (
                outputQueue.push(val);
            }
        }

        if (!operatorQueue.isEmpty()) {
            for (int i = 0; i <= operatorQueue.size(); i++) {
                outputQueue.push(operatorQueue.pop());
            }
        }

        return outputQueue;
    }
}