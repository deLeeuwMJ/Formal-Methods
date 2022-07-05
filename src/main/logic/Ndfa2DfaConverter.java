package main.logic;

import main.model.automata.DFA;
import main.model.automata.NDFA;
import main.model.automata.Transition;
import main.ui.ConsoleTable;

import java.util.*;

public class Ndfa2DfaConverter {

    public static class TableTransition {
        public String start;
        public String letter;
        public String end;

        @Override
        public String toString() {
            return "(" + start + ") --" + letter + "--> (" + end + ")";
        }
    }

    public DFA convert(NDFA ndfa) {
        List<TableTransition> transitionList = iterate(ndfa, ndfa.getStates());
        List<String> newStatesList = combineStates(transitionList, ndfa.getStates());

        int prevSize = ndfa.getStates().size();
        int currSize = newStatesList.size();

        // Keep iterating until there are no more changes possible
        while (prevSize < currSize) {
//            prevSize = newStatesList.size(); //temp

            transitionList = iterate(ndfa, newStatesList);
            newStatesList = combineStates(transitionList, newStatesList);

            // Eliminate non reachable
            List<String> reachableOnly = eliminateNonReachable(transitionList, newStatesList);

            prevSize = reachableOnly.size();
            currSize = newStatesList.size();

            // Must be last in order
            newStatesList = reachableOnly;

//            currSize = newStatesList.size(); //temp
        }

        // Create DFA()
        DFA dfa = new DFA();
        dfa.addAllStates(newStatesList);

        // Mark states
        for (String state : newStatesList) {
            // Add Start states
            for (String startState : ndfa.getStartStates())
                if (state.contains(startState)) dfa.addStartState(state);

            // Add Final states
            for (String endState : ndfa.getEndStates()) if (state.contains(endState)) dfa.addEndState(state);
        }

        // Add transitions
        for (TableTransition t : transitionList) {
            dfa.addTransition(new Transition(t.start, t.end, t.letter));
        }

        // Add letters
        dfa.addAllLetters(ndfa.getLetters());

        return dfa;
    }

    // Todo debug why elminates non reachable after size >= 5
    private List<String> eliminateNonReachable(List<TableTransition> transitions, List<String> combinedList) {
        // Uses linkedHashSet to easily identify duplicates
        LinkedHashSet<String> seenStates = new LinkedHashSet<>();

        // Add start state as seen
        seenStates.add("q0");

        // Eliminate start states that aren't mentioned as end state
        for (String startState : combinedList) {
            for (TableTransition t : transitions) {

                // Only include transitions its currently iterating over
                if (startState.equals(t.start) && seenStates.contains(t.start)) {
                    // Add to list of seen states
                    if (seenStates.add(t.end)) seenStates.add(t.end);
                }
            }
        }

        return new ArrayList<>(seenStates);
    }

    private List<TableTransition> iterate(NDFA ndfa, List<String> states) {
        ConsoleTable table = newTable(ndfa);

        List<TableTransition> transitions = new ArrayList<>();
        for (String state : states) {

            // New row for each state
            table.appendRow();
            table.appendColum(state);

            // Fill in fields
            for (int i = 0; i < ndfa.getLetters().size() - 1; i++) {
                // Set start
                TableTransition transition = new TableTransition();
                transition.start = state;

                // Set letter
                String letter = String.valueOf(ndfa.getLetters().get(i));
                transition.letter = letter;

                // Set end
                String result = calculateEndState(ndfa, transition.start, transition.letter);
                transition.end = result;
                table.appendColum(result);

                // Add to list
                transitions.add(transition);
            }
        }
        System.out.println(table);

        return transitions;
    }

    private String calculateEndState(NDFA ndfa, String state, String letter) {
        String result = "TRAP";

        // If start state is multi-state
        if (containsBrackets(state)) {
            // Makes use of queue to retrieve first item which stack doesn't provide
            Queue<List<String>> nextStatesStack = new LinkedList<>();
            String[] singleStates = splitStates(state);

            for (String singleState : singleStates) {
                nextStatesStack.add(ndfa.getNextStates(singleState, letter, false));
            }

            // Uses linkedHashSet to easily identify duplicates
            LinkedHashSet<String> newEndStates = new LinkedHashSet<>();
            while (!nextStatesStack.isEmpty()) {
                for (String s : nextStatesStack.poll()) if (newEndStates.add(s)) newEndStates.add(s);
            }

            result = newEndStates.toString();

        } else { // Is single state

            // If there isn't anything reachable > NULL (=trap)
            List<String> reachableStates = ndfa.getNextStates(state, letter, false);

            if (!reachableStates.isEmpty()) {
                String newState = reachableStates.toString();
                if (reachableStates.size() > 1) {
                    result = newState;
                } else { // one state
                    result = newState.substring(1, newState.length() - 1);
                }
            }
        }

        // Uses containsBrackets here in combination with length to determine if its an empty result;
        return containsBrackets(result) && result.length() == 2 ? "TRAP" : result;
    }

    private List<String> combineStates(List<TableTransition> transitionList, List<String> oldList) {
        // Uses linkedHashSet to easily identify duplicates
        LinkedHashSet<String> edgeEndingStates = new LinkedHashSet<>(oldList);

        // Ending states
        for (TableTransition t : transitionList) {
            if (edgeEndingStates.add(t.end)) edgeEndingStates.add(t.end);
        }

        return new ArrayList<>(edgeEndingStates);
    }

    private String[] splitStates(String multiState) {
        String formatString = multiState.substring(1, multiState.length() - 1);
        return formatString.split(", ");
    }

    private boolean containsBrackets(String s) {
        return s.contains("[") && s.contains("]");
    }

    private ConsoleTable newTable(NDFA ndfa) {
        ConsoleTable table = new ConsoleTable(ndfa.getLetters().size(), true);
        table.appendRow();
        table.appendColum("");
        for (int i = 0; i < ndfa.getLetters().size() - 1; i++) {
            table.appendColum(ndfa.getLetters().get(i));
        }
        return table;
    }
}
