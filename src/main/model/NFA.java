package main.model;

import java.util.ArrayList;
import java.util.List;

public class NFA extends Automata {

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
}

