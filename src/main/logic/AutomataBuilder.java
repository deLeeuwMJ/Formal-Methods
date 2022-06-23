package main.logic;

import main.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static main.logic.InputValidator.getOperator;
import static main.logic.InputValidator.isOperator;

public class AutomataBuilder {

    public class State {
        String label;
        State edge1;
        State edge2;

        public State() {
            this.label = "E";
            this.edge1 = null;
            this.edge2 = null;
        }

        public State(String label) {
            this.label = label;
            this.edge1 = null;
            this.edge2 = null;
        }

        public State(String label, State edge1, State edge2) {
            this.label = label;
            this.edge1 = edge1;
            this.edge2 = edge2;
        }
    }

    public class NFA {
        State initial;
        State accept;

        public NFA() {
            this.initial = null;
            this.accept = null;
        }

        public NFA(State initial, State accept) {
            this.initial = initial;
            this.accept = accept;
        }
    }

    public Automata build(AutomataType type, Stack<String> postfixStack, List<String> terminals) {
        Character[] alphabet = {'a', 'b'};
        Automata<String> m = new Automata<String>(alphabet);

        if (type == AutomataType.DFA) {
            m.addTransition(new Transition<String>("q0", "q1", 'a'));
            m.addTransition(new Transition<String>("q0", "q4", 'b'));

            m.addTransition(new Transition<String>("q1", "q4", 'a'));
            m.addTransition(new Transition<String>("q1", "q2", 'b'));

            m.addTransition(new Transition<String>("q2", "q3", 'a'));
            m.addTransition(new Transition<String>("q2", "q4", 'b'));

            m.addTransition(new Transition<String>("q3", "q1", 'a'));
            m.addTransition(new Transition<String>("q3", "q2", 'b'));

            // the error state, loops for a and b:
            m.addTransition(new Transition<String>("q4", 'a'));
            m.addTransition(new Transition<String>("q4", 'b'));

            // only on start state in a dfa:
            m.defineAsStartState("q0");

            // two final states:
            m.defineAsFinalState("q2");
            m.defineAsFinalState("q3");
        } else if (type == AutomataType.NFA) {
            Stack<NFA> nfaStack = new Stack<>();

            for (int i = 0; i < postfixStack.size(); i++) {
                String val = postfixStack.get(i);

                if (isOperator(val)) { // Is it an operator
                    NFA nfa1, nfa2, newNFA;
                    State accept = new State();
                    State initial = new State();

                    switch (getOperator(val)) {
                        case DOT:
                            nfa2 = nfaStack.pop();
                            nfa1 = nfaStack.pop();

                            nfa1.accept.edge1 = nfa2.initial;

                            newNFA = new NFA(initial, accept);
                            nfaStack.push(newNFA);
                            break;
                        case OR:
                            nfa2 = nfaStack.pop();
                            nfa1 = nfaStack.pop();

                            initial.edge1 = nfa1.initial;
                            initial.edge2 = nfa2.initial;

                            nfa1.accept.edge1 = accept;
                            nfa2.accept.edge1 = accept;

                            newNFA = new NFA(initial, accept);
                            nfaStack.push(newNFA);
                            break;
                        case PLUS:
                            nfa1 = nfaStack.pop();

                            initial.edge1 = nfa1.initial;

                            nfa1.accept.edge1 = nfa1.initial;
                            nfa1.accept.edge2 = accept;

                            newNFA = new NFA(initial, accept);
                            nfaStack.push(newNFA);
                            break;
                        case STAR:
                            nfa1 = nfaStack.pop();

                            initial.edge1 = nfa1.initial;
                            initial.edge2 = accept;

                            nfa1.accept.edge1 = nfa1.initial;
                            nfa1.accept.edge2 = accept;

                            newNFA = new NFA(initial, accept);
                            nfaStack.push(newNFA);
                            break;
                    }
                } else { // its a terminal
                    State accept = new State();
                    State initial = new State();

                    initial.label = val;
                    initial.edge1 = accept;

                    nfaStack.push(new NFA(initial, accept));
                }
            }
            NFA root = nfaStack.pop();

            List<State> current = new ArrayList<>();
            List<State> next = new ArrayList<>();
            
            current.addAll(follows(root.initial));
            String input = "ab";

            System.out.println(current);

            for (char s : input.toCharArray()){
                for (State c : current){
                    if (c.label.equals(String.valueOf(s))){
                        next.addAll(follows(c.edge1));
                    }
                }
                current = next;
                next = new ArrayList<>();
            }

            System.out.println("Match: " + current.contains(root.accept));
        }
        return m;
    }

    private List<State> follows(State state) {
        List<State> states = new ArrayList<>();
        states.add(state);

        if (state.label == null){
            if (state.edge1 != null){
                states.addAll(follows(state.edge1));
            }

            if (state.edge2 != null){
                states.addAll(follows(state.edge2));
            }
        }

        return states;
    }
}
