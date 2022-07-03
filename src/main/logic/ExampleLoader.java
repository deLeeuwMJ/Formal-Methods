package main.logic;

import main.model.automata.*;
import main.model.example.DfaExampleId;
import main.model.example.ExampleId;
import main.model.example.MdfaExampleId;
import main.model.example.NdfaExampleId;

import java.util.Arrays;

public class ExampleLoader {

    public FA load(AutomataType automataType, ExampleId id){
        FA automata = null;
        switch (automataType){
            case NDFA:
                automata = loadNdfa((NdfaExampleId) id);
                break;
            case DFA:
                automata = loadDfa((DfaExampleId) id);
                break;
            case MDFA:
                automata = loadMdfa((MdfaExampleId) id);
                break;
        }

        return automata;
    }

    private NDFA loadNdfa(NdfaExampleId id){
        NDFA ndfa = new NDFA();

        switch (id) {
            case NDFA_1: // https://www.javatpoint.com/automata-conversion-from-nfa-to-dfa Example 1
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
                break;
        }

        return ndfa;
    }

    private DFA loadDfa(DfaExampleId id){
        DFA dfa = new DFA();

        switch (id ) {
            case DFA_1:
                break;
        }

        return dfa;
    }

    private DFA loadMdfa(MdfaExampleId id){
        DFA dfa = new DFA();

        switch (id ) {
            case MDFA_1: // https://www.javatpoint.com/minimization-of-dfa
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
                break;
        }

        return dfa;
    }
}
