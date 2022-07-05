package main.logic;

import main.model.LanguageMode;
import main.model.automata.AutomataType;
import main.model.automata.DFA;
import main.model.automata.FA;
import main.model.automata.NDFA;
import main.model.example.DfaExampleId;
import main.model.example.ExampleId;
import main.model.regex.ParsedRegex;

public class AutomataBuilder {

    public FA build(AutomataType type, LanguageMode language, ParsedRegex parsedRegex) {
        ThompsonConstructor thompson = new ThompsonConstructor();
        Ndfa2DfaConverter ndfa2dfa = new Ndfa2DfaConverter();
        DfaMinimizer dfa = new DfaMinimizer();

        FA automata = thompson.construct(parsedRegex);

        switch (type) {
            case DFA: // NFA > DFA
                automata = ndfa2dfa.convert((NDFA) automata);

                if (language == LanguageMode.START) {
                    automata.modifyTransitions(FA.ModifyTransitions.FINAL_ITSELF);
                } else if (language == LanguageMode.ENDS) {
                    automata.modifyTransitions(FA.ModifyTransitions.FINAL_TO_START);
                }

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
                } else if (!dfaId.name().contains("MDFA")) {
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
}
