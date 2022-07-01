package main.logic;

import main.model.automata.*;
import main.model.regex.ParsedRegex;

import java.util.Arrays;

public class AutomataBuilder {

    public FA build(AutomataType type, ParsedRegex parsedRegex) {
        ThompsonConstructor thompson = new ThompsonConstructor();
        FA automata = thompson.construct(parsedRegex);

        // if DFA is selected
        if (type == AutomataType.DFA) {
            Ndfa2DfaConverter ndfa2dfa = new Ndfa2DfaConverter();
            automata = ndfa2dfa.convert((NDFA) automata);
//            automata = ndfa2dfa.convert((NDFA) ndfaExample2());
        }

        return automata;
    }

    private FA dfaExample() {
        DFA dfa = new DFA();

        dfa.addTransition(new Transition("q1", "q2", "a"));
        dfa.addTransition(new Transition("q1", "q1", "b"));
        dfa.addTransition(new Transition("q2", "q3", "a"));
        dfa.addTransition(new Transition("q2", "q1", "b"));
        dfa.addTransition(new Transition("q3", "q2", "a"));
        dfa.addTransition(new Transition("q3", "q4", "b"));
        dfa.addTransition(new Transition("q4", "q4", "a"));
        dfa.addTransition(new Transition("q4", "q4", "b"));
        dfa.addStartState("q1");
        dfa.addEndState("q4");

        return dfa;
    }

    private FA ndfaExample() {
        NDFA ndfa = new NDFA();

        ndfa.addTransition(new Transition("q1", "q4", "a"));
        ndfa.addTransition(new Transition("q1", "q2", "b"));

        ndfa.addTransition(new Transition("q2", "q4", "a"));
        ndfa.addTransition(new Transition("q2", "q1", "b"));
        ndfa.addTransition(new Transition("q2", "q3", "b"));
        ndfa.addTransition(new Transition("q2", "q3", "ε"));

        ndfa.addTransition(new Transition("q3", "q5", "a"));
        ndfa.addTransition(new Transition("q3", "q5", "b"));

        ndfa.addTransition(new Transition("q4", "q2", "ε"));
        ndfa.addTransition(new Transition("q4", "q3", "a"));

        ndfa.addTransition(new Transition("q5", "q4", "a"));
        ndfa.addTransition(new Transition("q5", "q1", "b"));

        ndfa.addAllLetters(Arrays.asList('a', 'b'));
        ndfa.addStartState("q1");
        ndfa.addEndState("q4");
        ndfa.addAllStates(Arrays.asList("q1", "q2", "q3", "q4", "q5"));


        return ndfa;
    }

    private FA ndfaExample2() {
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

        return ndfa;
    }
}
