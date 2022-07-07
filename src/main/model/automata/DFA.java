package main.model.automata;

import main.logic.Ndfa2DfaConverter;

import java.util.Arrays;
import java.util.List;

public class DFA extends FA {

    public DFA() {
        super();
    }

    public DFA(List<Transition> t) {
        super(t);
    }

    public boolean isAccepted(String word) {
        // Initialize the first state (should always be q0)
        String currentState = "q0";

        // Loop through the characters of the give word
        for (char currentChar : word.toCharArray()) {
            // Loop through all the transitions within the dfa
            for (Transition t : transitions) {
                if (t.getOrigin().equals(currentState) && t.getSymbol().equals(String.valueOf(currentChar))) {
                    // Look up what the new state would be, based on the current state and the current symbol
                    currentState = t.getDestination();
                    break;
                }
            }
        }

        // Check whether the state where the word ends up in is an end state
        return endStates.contains(currentState);
    }

    @Override
    public void addAllLetters(List<Character> list) {
        letters.addAll(list);
    }
}
