package main.model;

import java.util.ArrayList;

public class NFA {
    public ArrayList<Integer> states;
    public ArrayList <Transition> transitions;
    public int final_state;

    public NFA(){
        this.states = new ArrayList <Integer> ();
        this.transitions = new ArrayList <Transition> ();
        this.final_state = 0;
    }

    public NFA(int size){
        this.states = new ArrayList <Integer> ();
        this.transitions = new ArrayList <Transition> ();
        this.final_state = 0;
        this.setStateSize(size);
    }

    public NFA(char c){
        this.states = new ArrayList<Integer> ();
        this.transitions = new ArrayList <Transition> ();
        this.setStateSize(2);
        this.final_state = 1;
        this.transitions.add(new Transition("0", "1", c));
    }

    public void setStateSize(int size){
        for (int i = 0; i < size; i++)
            this.states.add(i);
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    public void print(){
        for (Transition t: transitions){
            System.out.println(t.toString());
        }
    }
}
