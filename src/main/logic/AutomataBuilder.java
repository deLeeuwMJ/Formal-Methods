package main.logic;

import main.model.automata.*;

import java.util.Stack;

public class AutomataBuilder {

    public FA build(AutomataType type, Stack<Character> postfix){
        FA automata = null;

        if (type == AutomataType.DFA){
            automata = new DFA();

            automata.addTransition(new Transition("q1", "q2", "a"));
            automata.addTransition(new Transition("q1", "q1", "b"));
            automata.addTransition(new Transition("q2", "q3", "a"));
            automata.addTransition(new Transition("q2", "q1", "b"));
            automata.addTransition(new Transition("q3", "q2", "a"));
            automata.addTransition(new Transition("q3", "q4", "b"));
            automata.addTransition(new Transition("q4", "q4", "a"));
            automata.addTransition(new Transition("q4", "q4", "b"));
            automata.addStartState("q1");
            automata.addEndState("q4");

        } else if (type == AutomataType.NDFA){
            automata = new NDFA();

            automata.addTransition(new Transition("q1", "q4", "a"));
            automata.addTransition(new Transition("q1", "q2", "b"));

            automata.addTransition(new Transition("q2", "q4", "a"));
            automata.addTransition(new Transition("q2", "q1", "b"));
            automata.addTransition(new Transition("q2", "q3", "b"));
            automata.addTransition(new Transition("q2", "q3", "ε"));

            automata.addTransition(new Transition("q3", "q5", "a"));
            automata.addTransition(new Transition("q3", "q5", "b"));

            automata.addTransition(new Transition("q4", "q2", "ε"));
            automata.addTransition(new Transition("q4", "q3", "a"));

            automata.addTransition(new Transition("q5", "q4", "a"));
            automata.addTransition(new Transition("q5", "q1", "b"));

            automata.addStartState("q1");
            automata.addEndState("q4");
        }

        return automata;
    }
}
