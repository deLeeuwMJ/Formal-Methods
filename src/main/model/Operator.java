package main.model;

public enum Operator {
    PLUS('+'),
    STAR('*'),
    OR('|'),
    DOT('.'),
    ONE();

    private final char symbol;

    Operator() {
        this.symbol = '0';
    }

    Operator(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}
