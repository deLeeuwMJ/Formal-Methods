package main.logic;

import main.model.automata.*;
import main.model.example.DfaExampleId;
import main.model.example.ExampleId;
import main.model.example.NdfaExampleId;

import java.util.Arrays;

public class ExampleLoader {

    public FA load(AutomataType automataType, ExampleId id) {
        FA automata = null;

        switch (automataType) {
            case NDFA:
                automata = loadNdfa((NdfaExampleId) id);
                break;
            case MDFA:
            case DFA:
                automata = loadDfa((DfaExampleId) id);
                break;
        }

        return automata;
    }

    private NDFA loadNdfa(NdfaExampleId id) {
        NDFA ndfa = new NDFA();

        switch (id) {
            case SIMPLE_NDFA:
                ndfa.addTransition(new Transition("q0", "q0", "a"));
                ndfa.addTransition(new Transition("q0", "q1", "b"));

                ndfa.addAllStates(Arrays.asList("q0", "q1"));
                ndfa.addAllLetters(Arrays.asList('a', 'b'));
                ndfa.addStartState("q0");
                ndfa.addEndState("q1");

                break;
        }

        return ndfa;
    }

    private FA loadDfa(DfaExampleId id) {
        FA automata = null;

        switch (id) {
            case PRE_NDFA_2_DFA: // https://www.javatpoint.com/automata-conversion-from-nfa-to-dfa Example 1
            case POST_NDFA_2_DFA:
                NDFA ndfa = new NDFA();

                ndfa.addTransition(new Transition("q0", "q0", "a"));
                ndfa.addTransition(new Transition("q0", "q1", "b"));

                ndfa.addTransition(new Transition("q1", "q1", "a"));
                ndfa.addTransition(new Transition("q1", "q1", "b"));

                ndfa.addTransition(new Transition("q1", "q2", "a"));
                ndfa.addTransition(new Transition("q2", "q1", "b"));

                ndfa.addTransition(new Transition("q2", "q2", "a"));
                ndfa.addTransition(new Transition("q2", "q2", "b"));

                ndfa.addAllStates(Arrays.asList("q0", "q1", "q2"));
                ndfa.addAllLetters(Arrays.asList('a', 'b'));
                ndfa.addStartState("q0");
                ndfa.addEndState("q2");

                automata = ndfa;
                break;
            case PRE_DFA_2_MDFA:
            case POST_DFA_2_MDFA:
                DFA dfa = new DFA();
                dfa.addTransition(new Transition("q0", "q1", "a"));
                dfa.addTransition(new Transition("q0", "q3", "b"));

                dfa.addTransition(new Transition("q1", "q0", "a"));
                dfa.addTransition(new Transition("q1", "q3", "b"));

                dfa.addTransition(new Transition("q3", "q5", "a"));
                dfa.addTransition(new Transition("q3", "q5", "b"));

                dfa.addTransition(new Transition("q5", "q5", "a"));
                dfa.addTransition(new Transition("q5", "q5", "b"));

                dfa.addAllStates(Arrays.asList("q0", "q1", "q3", "q5"));
                dfa.addAllLetters(Arrays.asList('a', 'b'));
                dfa.addStartState("q1");
                dfa.addEndState("q3");
                dfa.addEndState("q5");

                automata = dfa;
                break;
        }

        return automata;
    }
}
