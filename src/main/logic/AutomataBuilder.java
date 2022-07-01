package main.logic;

import main.model.automata.*;
import main.model.regex.ParsedRegex;

import java.util.Arrays;

public class AutomataBuilder {

    public FA build(AutomataType type, ParsedRegex parsedRegex) {
        ThompsonConstructor thompson = new ThompsonConstructor();
        Ndfa2DfaConverter ndfa2dfa = new Ndfa2DfaConverter();
        DfaMinimizer dfa = new DfaMinimizer();

        FA automata = thompson.construct(parsedRegex);

        switch (type) {
            case DFA: // NFA > DFA
                automata = ndfa2dfa.convert((NDFA) automata);
                break;
            case MDFA: // NFA > DFA > MFA
//                automata = dfa.minimize(ndfa2dfa.convert((NDFA) automata));
                automata = dfa.minimize((DFA) minimizableDfaExample());
                break;
        }

        return automata;
    }

    /* https://www.javatpoint.com/automata-conversion-from-nfa-to-dfa Example 1*/
    private FA ndfaExample() {
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

    /* https://www.javatpoint.com/minimization-of-dfa */
    private FA minimizableDfaExample() {
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

        return dfa;
    }
}
