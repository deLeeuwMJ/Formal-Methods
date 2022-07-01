package main.model.automata;

public class DFA extends FA {

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
}
