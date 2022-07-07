package main.logic;

public class InputValidator {

    public static final char STAR_OPERATOR_SYMBOL = '*';
    public static final char PLUS_OPERATOR_SYMBOL = '+';
    public static final char DOT_OPERATOR_SYMBOL = '.';
    public static final char OR_OPERATOR_SYMBOL = '|';
    public static final char EPSILON_SYMBOL = 'ε';

    public static boolean validRegEx(String regex) {
        if (regex.isEmpty()) return false;
        for (char c : regex.toCharArray()) {
            if (!validRegExChar(c)) return false;
        }

        return true;
    }

    public static boolean validRegExChar(char c) {
        return isAlphabet(c) || isRegexOperator(c);
    }

    // Check if character is inside alphabet with ASCII table
    public static boolean isAlphabet(char c) {
        return c >= 97 && c <= 122 || c == EPSILON_SYMBOL;
    }

    public static boolean isRegexOperator(char c) {
        return c == '(' || c == ')' || c == STAR_OPERATOR_SYMBOL || c == OR_OPERATOR_SYMBOL || c == PLUS_OPERATOR_SYMBOL || c == DOT_OPERATOR_SYMBOL;
    }

    public static boolean evenAmountOfParenthesis(String regex) {
        if (regex.isEmpty()) return false;

        int parenthesisCounter = 0;
        for (char c : regex.toCharArray()) {
            if (c == ')' || c == '(') parenthesisCounter++;
        }

        return parenthesisCounter % 2 == 0;
    }
}
