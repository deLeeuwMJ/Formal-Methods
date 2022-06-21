package main.logic;

import main.model.AutomataType;
import main.model.LanguageMode;

import java.util.List;

public class AutomataSimulator {

    public boolean simulate(AutomataType type, LanguageMode mode, List<String> validWords, String input) {
        if (type == AutomataType.DFA) {
            for (String word : validWords) {
                switch (mode){
                    case START:
                        if (input.startsWith(word, 0)) return true;
                        break;
                    case CONTAINS:
                        if (input.contains(word)) return true;
                        break;
                    case ENDS:
                        if (input.endsWith(word)) return true;
                        break;
                }
            }
        }
        return false;
    }
}
