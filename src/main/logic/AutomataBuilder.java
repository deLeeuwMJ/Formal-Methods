package main.logic;

import main.model.Automata;
import main.model.AutomataType;
import main.model.Node;
import main.model.Transition;

import java.util.List;
import java.util.Stack;

public class AutomataBuilder {

    public Automata build(AutomataType type, Stack<String> root, List<String> terminals) {
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
            m.addTransition( new Transition<String> ("A", "C", 'a') );
            m.addTransition( new Transition<String> ("A", "B", 'b') );
            m.addTransition( new Transition<String> ("A", "C", 'b') );

            m.addTransition( new Transition<String> ("B", "C", 'b') );
            m.addTransition( new Transition<String> ("B", "C"));

            m.addTransition( new Transition<String> ("C", "D", 'a') );
            m.addTransition( new Transition<String> ("C", "E", 'a') );
            m.addTransition( new Transition<String> ("C", "D", 'b') );

            m.addTransition( new Transition<String> ("D", "B", 'a') );
            m.addTransition( new Transition<String> ("D", "C", 'a') );

            m.addTransition( new Transition<String> ("E", "a") );
            m.addTransition( new Transition<String> ("E", "D") );

            // only on start state in a dfa:
            m.defineAsStartState("A");

            // two final states:
            m.defineAsFinalState("C");
            m.defineAsFinalState("E");
        }
        return m;
    }
}
