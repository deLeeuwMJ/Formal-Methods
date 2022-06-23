package main.model;

public class Transition {
    public int from, to;
    public String symbol;

    public Transition(int v1, int v2, String sym) {
        this.from = v1;
        this.to = v2;
        this.symbol = sym;
    }
}
