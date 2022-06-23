package main.logic;

import main.model.LanguageMode;

import java.util.SortedSet;

public class AutomataSimulator {

    public boolean simulate(LanguageMode mode, SortedSet<String> validWords, String input) {
        for (String word : validWords) {

            // To deal with regex that uses STAR
            if (word.isEmpty()) {
                if (input.isEmpty()){
                    return true;
                } else continue;
            }

            switch (mode) {
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
        return false;
    }
}
