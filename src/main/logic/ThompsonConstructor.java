package main.logic;

import main.model.automata.NDFA;
import main.model.automata.Transition;
import main.model.regex.ParsedRegex;

import java.util.*;

import static main.logic.InputValidator.*;

public class ThompsonConstructor {

    public class NFA {

        public ArrayList<String> states;
        public ArrayList<Transition> transitions;

        public String startState = "q0";
        public String finalState;

        public NFA(int size) {
            this.states = new ArrayList<>();
            this.transitions = new ArrayList<>();
            this.finalState = "q0";
            this.setStateSize(size);
        }

        public NFA(char c) {
            this.states = new ArrayList<>();
            this.transitions = new ArrayList<>();
            this.setStateSize(2);
            this.finalState = "q1";
            this.transitions.add(new Transition("q0", "q1", String.valueOf(c)));
        }

        public void setStateSize(int size) {
            for (int i = 0; i < size; i++)
                this.states.add("q" + i);
        }
    }

    public NDFA construct(ParsedRegex parsedRegex) {
        Stack<NFA> nfaStack = new Stack<>();

        for (char c : parsedRegex.getPostfixSequence()) {
            if (isRegexOperator(c)) {
                NFA nfa1, nfa2;

                switch (c) {
                    case DOT_OPERATOR_SYMBOL:
                        nfa2 = nfaStack.pop();
                        nfa1 = nfaStack.pop();

                        nfaStack.push(concat(nfa1, nfa2));
                        break;
                    case OR_OPERATOR_SYMBOL:
                        nfa2 = nfaStack.pop();
                        nfa1 = nfaStack.pop();

                        nfaStack.push(union(nfa1, nfa2));
                        break;
                    case PLUS_OPERATOR_SYMBOL:
                        nfa1 = nfaStack.pop();

                        nfaStack.push(plus(nfa1));
                        break;
                    case STAR_OPERATOR_SYMBOL:
                        nfa1 = nfaStack.pop();

                        nfaStack.push(kleene(nfa1));
                        break;
                }
            } else {
                nfaStack.push(new NFA(c));
            }
        }

        NFA root = nfaStack.pop();
        root.finalState = newStateLabel(root.states.size() - 1);

        NDFA ndfa = new NDFA(root.transitions);
        ndfa.addStartState(root.startState);
        ndfa.addEndState(root.finalState);

        return ndfa;
    }

    private String newStateLabel(int add) {
        return "q" + add;
    }

    private String newStateLabel(String stateLabel, int add) {
        String splitNumber = stateLabel.substring(1);
        int newStateNumber = Integer.parseInt(splitNumber) + add;

        return "q" + newStateNumber;
    }

    private NFA concat(NFA n, NFA m) {
        m.states.remove(0); // delete m's initial state

        // copy NFA m's transitions to n, and handles connecting n & m
        for (Transition t : m.transitions) {
            n.transitions.add(new Transition(newStateLabel(t.getOrigin(), n.states.size() - 1), newStateLabel(t.getDestination(), n.states.size() - 1), t.getSymbol()));
        }

        // take m and combine to n after erasing initial m state
        for (String s : m.states) {
            n.states.add(newStateLabel(s, n.states.size() + 1));
        }

        return n;
    }

    private NFA union(NFA n, NFA m) {
        NFA result = new NFA(n.states.size() + m.states.size() + 2);

        // the branching of q0 to beginning of n
        result.transitions.add(new Transition(newStateLabel(0), newStateLabel(1)));

        // copy existing transitions of n
        for (Transition t : n.transitions) {
            result.transitions.add(new Transition(newStateLabel(t.getOrigin(), 1), newStateLabel(t.getDestination(), 1), t.getSymbol()));
        }

        // transition from last n to final state
        result.transitions.add(new Transition(newStateLabel(n.states.size()), newStateLabel(n.states.size() + m.states.size() + 1)));

        // the branching of q0 to beginning of m
        result.transitions.add(new Transition(newStateLabel(0), newStateLabel(n.states.size() + 1)));

        // copy existing transitions of m
        for (Transition t : m.transitions) {
            result.transitions.add(new Transition(newStateLabel(t.getOrigin(), n.states.size() + 1), newStateLabel(t.getDestination(), n.states.size() + 1), t.getSymbol()));
        }

        // transition from last m to final state
        result.transitions.add(new Transition(newStateLabel(m.states.size() + n.states.size()), newStateLabel(n.states.size() + m.states.size() + 1)));

        return result;
    }

    private NFA kleene(NFA n) {
        NFA result = new NFA(n.states.size() + 2);

        // the branching of q0 to beginning of n
        result.transitions.add(new Transition(newStateLabel(0), newStateLabel(1)));

        // copy existing transitions of n
        for (Transition t : n.transitions) {
            result.transitions.add(new Transition(newStateLabel(t.getOrigin(), 1), newStateLabel(t.getDestination(), 1), t.getSymbol()));
        }

        // add empty transition from final n state to new final state.
        result.transitions.add(new Transition(newStateLabel(n.states.size()), newStateLabel(n.states.size() + 1)));

        // Loop back from last state of n to initial state of n.
        result.transitions.add(new Transition(newStateLabel(n.states.size()), newStateLabel(1)));

        // Add empty transition from new initial state to new final state.
        result.transitions.add(new Transition(newStateLabel(0), newStateLabel(n.states.size() + 1)));
        return result;
    }

    private NFA plus(NFA n) {
        NFA result = new NFA(n.states.size() + 2);
        result.transitions.add(new Transition(newStateLabel(0), newStateLabel(1)));

        // copy existing transitions
        for (Transition t : n.transitions) {
            result.transitions.add(new Transition(newStateLabel(t.getOrigin(), 1), newStateLabel(t.getDestination(), 1), t.getSymbol()));
        }

        // add empty transition from final n state to new final state.
        result.transitions.add(new Transition(newStateLabel(n.states.size()), newStateLabel(n.states.size() + 1)));

        // Loop back from last state of n to initial state of n.
        result.transitions.add(new Transition(newStateLabel(n.states.size()), newStateLabel(1)));

        return result;
    }
}
