package main.model;

public class Transition {

    private String origin;
    private String destination;
    private String symbol;

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
        return "{" + origin + "} -(" + symbol + ")-> {" + destination + "}";
    }
}
