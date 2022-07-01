package main.logic;

import main.model.regex.ParsedRegex;

import java.util.Stack;

import static main.logic.InputValidator.*;

public class PostfixNotationParser {

    private int getPrecedence(char c) {
        switch (c) {
            case OR_OPERATOR_SYMBOL:
                return 1;
            case DOT_OPERATOR_SYMBOL:
                return 2;
            case STAR_OPERATOR_SYMBOL:
            case PLUS_OPERATOR_SYMBOL:
                return 3;
        }
        return -1;
    }

    public Stack<Character> parse(ParsedRegex parsedRegex) {
        Stack<Character> outputQueue = new Stack<>();
        Stack<Character> operatorQueue = new Stack<>();

        /*
        Using Shunt-Yard Algorithm to get rid of the parenthesis and convert into postfix
        Postfix explanation: https://runestone.academy/ns/books/published/pythonds/BasicDS/InfixPrefixandPostfixExpressions.html
        */
        for (int i = 0; i < parsedRegex.getInfixSequence().size(); i++) {
            char val = parsedRegex.getInfixSequence().get(i);

            // Check if its an operator;
            if (getPrecedence(val) > 0) {
                while (!operatorQueue.isEmpty() && getPrecedence(operatorQueue.peek()) >= getPrecedence(val)) {
                    outputQueue.push(operatorQueue.pop());
                }
                operatorQueue.push(val);
            } else if (val == ')') {
                char x = operatorQueue.pop();
                while (x != '(') {
                    outputQueue.push(x);
                    x = operatorQueue.pop();
                }
            } else if (val == '(') {
                operatorQueue.push(val);
            } else {
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