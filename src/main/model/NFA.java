package main.model;

import java.util.ArrayList;
import java.util.List;

public class NFA extends Automata {

    public List<String> symbols;
    public ArrayList<Integer> states;
    public ArrayList<Transition> transitions;

    public int start_state = 0;
    public int final_state;

    public NFA() {
        super.type = AutomataType.NFA;
        this.states = new ArrayList<Integer>();
        this.transitions = new ArrayList<Transition>();
        this.final_state = 0;
    }

    public NFA(int size) {
        super.type = AutomataType.NFA;
        this.states = new ArrayList<Integer>();
        this.transitions = new ArrayList<Transition>();
        this.final_state = 0;
        this.setStateSize(size);
    }

    public NFA(String c) {
        super.type = AutomataType.NFA;
        this.states = new ArrayList<Integer>();
        this.transitions = new ArrayList<Transition>();
        this.setStateSize(2);
        this.final_state = 1;
        this.transitions.add(new Transition(
                0, 1, c));
    }

    public void setStateSize(int size) {
        for (int i = 0; i < size; i++)
            this.states.add(i);
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    public void printTransitions() {
        for (Transition t : transitions) {
            System.out.println("(" + t.from + ", " + t.symbol + ", " + t.to + ")");
        }
    }

    public void printSymbols() {
        System.out.println(symbols);
    }

    public int getFinalState() {
        return transitions.size();
    }
}

