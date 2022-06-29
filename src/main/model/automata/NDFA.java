package main.model.automata;

import java.util.*;

public class NDFA extends FA {

    public NDFA(){
        super();
    }

    public NDFA(List<Transition> t) {
        super(t);
    }

    public List<String> getNextStates(String state, String symbol, boolean isUsed) {
        HashSet<String> nextStates = new LinkedHashSet<>();

        // Loop through all the transitions
        for (Transition t : transitions) {
            // Check whether the current transition's origin is the given state
            if (t.getOrigin().equals(state)) {
                // Check whether the current transition's symbol is an epsilon
                if (t.getSymbol().equals("ε")) {
                    if (symbol.equals("ε")) {
                        // If the originally given symbol was an epsilon, add the transition to the list, and call the method again
                        nextStates.add(t.getDestination());
                        nextStates.addAll(getNextStates(t.getDestination(), "ε", isUsed));
                    } else {
                        // Call the method again
                        nextStates.addAll(getNextStates(t.getDestination(), symbol, isUsed));
                    }
                } else if (symbol.equals(t.getSymbol())) {
                    // If the given symbol matches the current transition's symbol, add the transitions destination node to the list
                    nextStates.add(t.getDestination());

                    // Update the boolean to ensure that only epsilon values will be allowed after this point
                    isUsed = true;

                    // Call the method again
                    nextStates.addAll(getNextStates(t.getDestination(), "ε", isUsed));
                }
            }

        }

        return new ArrayList<>(nextStates);
    }
}
