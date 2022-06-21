package main.logic;

import main.model.Operator;
import main.model.Transition;

public class InputValidator {

    private static final char REGEX_SEPARATOR_SYMBOL = '#';

    public static boolean validRegEx(String regex) {
        if (regex.isEmpty())
            return false;
        for (char c : regex.toCharArray())
            if (!validRegExChar(c))
                return false;
        return true;
    }

    public static boolean validRegExChar(char c) {
        return isAlphabet(c) || isRegexOperator(c);
    }

    // Check if character is inside alphabet with ASCII table
    public static boolean isAlphabet(char c) {
        return c >= 97 && c <= 122 || c == Transition.EPSILON;
    }

    public static boolean isRegexOperator(char c) {
        return c == '(' || c == ')' || c == '*' || c == '|' || c == '+' || c == REGEX_SEPARATOR_SYMBOL;
    }

    public static boolean isOperator(String indexValue) {
        boolean isOperator = false;

        try {
            getOperator(indexValue);
            isOperator = true;
        } catch (IllegalArgumentException e) {
            // Do nothing
        }

        return isOperator;
    }

    public static Operator getOperator(String indexValue) throws IllegalArgumentException {
        return Operator.valueOf(indexValue);
    }
}
