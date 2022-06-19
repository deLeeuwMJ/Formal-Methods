package main.logic;

import main.model.NFA;
import main.model.Transition;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class RegExParser {

    // Empty list means not valid regex
    public List<String> parse(String regex) {
        if (!validRegEx(regex)) {
            System.out.println("Invalid Regular Expression Input.");
            return Collections.emptyList();
        }

        char c;
        boolean isAbleToConcat = false;
        int parenthesisCounter = 0;

        Stack<String> concatStack = new Stack<String>();
        for (int i = 0; i < regex.length(); i++) {
            c = regex.charAt(i);

            if (alphabet(c)) {
                if (isAbleToConcat) { // concat this w/ previous
                    concatStack.push("DOT"); // 'DOT' represent concat
                } else isAbleToConcat = true;

                concatStack.push(String.valueOf(c));
            } else {
                if (c == ')') {
                    isAbleToConcat = false;
                    if (parenthesisCounter % 2 == 0){ // always need to be even parenthesis count
                        return Collections.emptyList();
                    } else parenthesisCounter--;
                    concatStack.push("RIGHT_PARENT");
                } else if (c == '*') {
                    concatStack.push("STAR");
                    isAbleToConcat = true;
                } else if (c == '+') {
                    concatStack.push("PLUS");
                    isAbleToConcat = true;
                } else if (c == '(') {
                    concatStack.push("LEFT_PARENT");
                    parenthesisCounter++;
                } else if (c == '|') {
                    concatStack.push("OR");
                    isAbleToConcat = false;
                }
            }
        }

        return concatStack;
    }

    private boolean validRegEx(String regex) {
        if (regex.isEmpty())
            return false;
        for (char c : regex.toCharArray())
            if (!validRegExChar(c))
                return false;
        return true;
    }

    private boolean validRegExChar(char c) {
        return alphabet(c) || regexOperator(c);
    }

    // Check if character is inside alphabet with ASCII table
    private boolean alphabet(char c) {
        return c >= 97 && c <= 122 || c == Transition.EPSILON;
    }

    private boolean regexOperator(char c) {
        return c == '(' || c == ')' || c == '*' || c == '|' || c == '+';
    }

}
