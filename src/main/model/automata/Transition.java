package main.model.automata;

public class Transition {

    private final String origin;
    private final String destination;
    private final String symbol;

    public Transition(String origin, String destination, String symbol) {
        this.origin = origin;
        this.destination = destination;
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
