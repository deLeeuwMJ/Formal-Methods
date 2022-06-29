package main.model.automata;

import java.util.*;

public class DFA extends FA {

    private final List<String> startNdfaStates;
    private final List<String> endNdfaStates;

    public DFA(){
        startNdfaStates = new ArrayList<>();
        endNdfaStates = new ArrayList<>();
    }

    public DFA(NDFA ndfa) {
        startNdfaStates = ndfa.getStartStates();
        endNdfaStates = ndfa.getEndStates();
    }

    public boolean isAccepted(String word)
    {
        // Initialize the first state (should always be q1)
        String currentState = "q1";

        // Loop through the characters of the give word
        for(char currentChar : word.toCharArray())
        {
            // Loop through all the transitions within the dfa
            for(Transition t : transitions)
            {
                if(t.getOrigin().equals(currentState) && t.getSymbol().equals(String.valueOf(currentChar)))
                {
                    // Look up what the new state would be, based on the current state and the current symbol
                    currentState = t.getDestination();
                    break;
                }
            }
        }

        // Check whether the state where the word ends up in is an end state
        return endStates.contains(currentState);
    }
}
