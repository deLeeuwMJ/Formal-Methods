package main.model.automata;

import static main.logic.InputValidator.EPSILON_SYMBOL;

public class Transition {

    private String origin;
    private String destination;
    private String symbol;

    public Transition() {
    }

    public Transition(String origin, String destination, String symbol) {
        this.origin = origin;
        this.destination = destination;
        this.symbol = symbol;
    }

    public Transition(String origin, String destination) {
        this.origin = origin;
        this.destination = destination;
        this.symbol = String.valueOf(EPSILON_SYMBOL);
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "(" + origin + ") --" + symbol + "--> (" + destination + ")";
    }
}
