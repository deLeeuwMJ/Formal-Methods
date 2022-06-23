package main.logic;

import main.model.*;

import java.util.*;

import static main.logic.InputValidator.*;

public class AutomataBuilder {

    public Automata build(AutomataType type, Stack<String> postfixStack, List<String> symbols) {
        Automata resultFA = null;

        if (type == AutomataType.DFA) {
//            m.addTransition(new Transition<String>("q0", "q1", 'a'));
//            m.addTransition(new Transition<String>("q0", "q4", 'b'));
//
//            m.addTransition(new Transition<String>("q1", "q4", 'a'));
//            m.addTransition(new Transition<String>("q1", "q2", 'b'));
//
//            m.addTransition(new Transition<String>("q2", "q3", 'a'));
//            m.addTransition(new Transition<String>("q2", "q4", 'b'));
//
//            m.addTransition(new Transition<String>("q3", "q1", 'a'));
//            m.addTransition(new Transition<String>("q3", "q2", 'b'));
//
//            // the error state, loops for a and b:
//            m.addTransition(new Transition<String>("q4", 'a'));
//            m.addTransition(new Transition<String>("q4", 'b'));
//
//            // only on start state in a dfa:
//            m.defineAsStartState("q0");
//
//            // two final getStates():
//            m.defineAsFinalState("q2");
//            m.defineAsFinalState("q3");
        } else if (type == AutomataType.NFA) {
            Stack<NFA> nfaStack = new Stack<>();

            for (int i = 0; i < postfixStack.size(); i++) {
                String val = postfixStack.get(i);

                if (isOperator(val)) { // Is it an operator
                    NFA nfa1, nfa2;

                    switch (getOperator(val)) {
                        case DOT:
                            nfa2 = nfaStack.pop();
                            nfa1 = nfaStack.pop();

                            nfaStack.push(concat(nfa1, nfa2));
                            break;
                        case OR:
                            nfa2 = nfaStack.pop();
                            nfa1 = nfaStack.pop();

                            nfaStack.push(union(nfa1, nfa2));
                            break;
                        case PLUS:
                            nfa1 = nfaStack.pop();

                            nfaStack.push(more(nfa1));
                            break;
                        case STAR:
                            nfa1 = nfaStack.pop();

                            nfaStack.push(kleene(nfa1));
                            break;
                    }
                } else { // its a symbol
                    nfaStack.push(new NFA(val));
                }
            }

            // There should be one NFA left in stack
            NFA root = nfaStack.pop();
            root.setSymbols(symbols);

            resultFA = root;
        }
        return resultFA;
    }

    private NFA concat(NFA n, NFA m) {
        m.getStates().remove(0); // delete m's initial state

        // copy NFA m's getTransitions()
        // to n, and handles connecting n & m
        for (Transition t : m.getTransitions()
        ) {
            n.getTransitions()
                    .add(new Transition(t.from + n.getStates().size() - 1,
                    t.to + n.getStates().size() - 1, t.symbol));
        }

        // take m and combine to n after erasing initial m state
        for (Integer s : m.getStates()) {
            n.getStates().add(s + n.getStates().size() + 1);
        }

        n.final_state = n.getStates().size() + m.getStates().size() - 2;
        return n;
    }

    private NFA union(NFA n, NFA m) {
        NFA result = new NFA(n.getStates().size() + m.getStates().size() + 2);

        // the branching of q0 to beginning of n
        result.getTransitions()
                .add(new Transition(0, 1, EPSILON_SYMBOL));

        // copy existing getTransitions()
        // of n
        for (Transition t : n.getTransitions()
        ) {
            result.getTransitions()
                    .add(new Transition(t.from + 1,
                    t.to + 1, t.symbol));
        }

        // transition from last n to final state
        result.getTransitions()
                .add(new Transition(n.getStates().size(),
                n.getStates().size() + m.getStates().size() + 1, EPSILON_SYMBOL));

        // the branching of q0 to beginning of m
        result.getTransitions()
                .add(new Transition(0, n.getStates().size() + 1, EPSILON_SYMBOL));

        // copy existing getTransitions()
        // of m
        for (Transition t : m.getTransitions()
        ) {
            result.getTransitions()
                    .add(new Transition(t.from + n.getStates().size()
                    + 1, t.to + n.getStates().size() + 1, t.symbol));
        }

        // transition from last m to final state
        result.getTransitions()
                .add(new Transition(m.getStates().size() + n.getStates().size(),
                n.getStates().size() + m.getStates().size() + 1, EPSILON_SYMBOL));

        // 2 new getStates() and shifted m to avoid repetition of last n & 1st m
        result.final_state = n.getStates().size() + m.getStates().size() + 1;

        return result;
    }

    private NFA kleene(NFA n) {
        NFA result = new NFA(n.getStates().size() + 2);
        result.getTransitions()
                .add(new Transition(0, 1, EPSILON_SYMBOL)); // new trans for q0

        // copy existing getTransitions()

        for (Transition t : n.getTransitions()
        ) {
            result.getTransitions()
                    .add(new Transition(t.from + 1,
                    t.to + 1, t.symbol));
        }

        // add empty transition from final n state to new final state.
        result.getTransitions()
                .add(new Transition(n.getStates().size(),
                n.getStates().size() + 1, EPSILON_SYMBOL));

        // Loop back from last state of n to initial state of n.
        result.getTransitions()
                .add(new Transition(n.getStates().size(), 1, EPSILON_SYMBOL));

        // Add empty transition from new initial state to new final state.
        result.getTransitions()
                .add(new Transition(0, n.getStates().size() + 1, EPSILON_SYMBOL));

        result.final_state = n.getStates().size() + 1;
        return result;
    }

    private NFA more(NFA n) {
        NFA result = new NFA(n.getStates().size() + 2);
        result.getTransitions()
                .add(new Transition(0, 1, EPSILON_SYMBOL)); // new trans for q0

        // copy existing getTransitions()

        for (Transition t : n.getTransitions()
        ) {
            result.getTransitions()
                    .add(new Transition(t.from + 1,
                    t.to + 1, t.symbol));
        }

        // add empty transition from final n state to new final state.
        result.getTransitions()
                .add(new Transition(n.getStates().size(),
                n.getStates().size() + 1, EPSILON_SYMBOL));

        // Loop back from last state of n to initial state of n.
        result.getTransitions()
                .add(new Transition(n.getStates().size(), 1, EPSILON_SYMBOL));

        result.final_state = n.getStates().size() + 1;
        return result;
    }

}
