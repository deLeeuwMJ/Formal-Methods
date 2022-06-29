package main.logic;

import main.model.automata.*;
import main.model.regex.ParsedRegex;

public class AutomataBuilder {

    public FA build(AutomataType type, ParsedRegex parsedRegex) {
        ThompsonConstructor thompson = new ThompsonConstructor();
        FA automata = thompson.construct(parsedRegex);

        // if DFA is selected
        if (type == AutomataType.DFA) {
            Ndfa2DfaConverter ndfa2dfa = new Ndfa2DfaConverter();
            automata.printTransitions();
            automata = ndfa2dfa.convert((NDFA) automata);
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

        ndfa.addStartState("q1");
        ndfa.addEndState("q4");

        return ndfa;
    }
}
