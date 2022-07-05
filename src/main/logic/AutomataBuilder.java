package main.logic;

import main.model.automata.AutomataType;
import main.model.automata.DFA;
import main.model.automata.FA;
import main.model.automata.NDFA;
import main.model.example.DfaExampleId;
import main.model.example.ExampleId;
import main.model.regex.ParsedRegex;

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
                break;
        }

        return automata;
    }

    public FA build(AutomataType type, ExampleId id) {
        Ndfa2DfaConverter ndfa2dfa = new Ndfa2DfaConverter();
        ExampleLoader exampleLoader = new ExampleLoader();
        DfaMinimizer dfa = new DfaMinimizer();
        FA automata = null;

        switch (type) {
            case NDFA: // REGEX > NDFA
                automata = exampleLoader.load(type, id);
                break;
            case DFA: // NDFA > DFA
                DfaExampleId dfaId = (DfaExampleId) id;
                if (dfaId.name().contains("PRE")) {
                    automata = exampleLoader.load(type, dfaId);
                } else if (!dfaId.name().contains("MDFA")){
                    automata = ndfa2dfa.convert((NDFA) exampleLoader.load(type, id));
                }
                break;
            case MDFA: // DFA > MDFA
                DfaExampleId mdfaId = (DfaExampleId) id;
                if (!mdfaId.name().contains("PRE") && mdfaId.name().contains("MDFA")) {
                    automata = dfa.minimize((DFA) exampleLoader.load(type, id));
                }
                break;
        }

        return automata;
    }

    private FA dfaExample(){
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

    private void testDfaStartsEndsContains()
    {
        DFA testDfaStartsWith = new DFA();
        testDfaStartsWith.startsWith("aab");

        DFA testDfaEndsWith = new DFA();
        testDfaEndsWith.endsWith("aab");

        DFA testDfaShouldContain = new DFA();
        testDfaShouldContain.Contains("aab");
    }
}
