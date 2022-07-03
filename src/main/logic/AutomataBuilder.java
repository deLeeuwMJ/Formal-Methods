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
                automata = dfa.minimize(ndfa2dfa.convert((NDFA) automata));
//                automata = dfa.minimize((DFA) minimizableDfaExample());
                break;
        }

        return automata;
    }


    private FA ndfaExample() {
        NDFA ndfa = new NDFA();



        return ndfa;
    }


    private FA minimizableDfaExample() {
        DFA dfa = new DFA();



        return dfa;
    }
}
