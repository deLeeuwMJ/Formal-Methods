package main.logic;

import main.model.automata.DFA;
import main.model.automata.FA;
import main.model.automata.MDFA;
import main.model.automata.Transition;
import main.ui.ConsoleTable;

import java.util.*;

public class DfaMinimizer {

    public FA minimize(DFA dfa) {
        // Separate non finals and finals
        // Uses linkedHashSet to easily identify duplicates
        Set<Transition> nonFinals = new LinkedHashSet<>();
        Set<Transition> finals = new LinkedHashSet<>();

        // Add non finals based on finals
        for (String s : dfa.getStates()) {
            for (Transition t : dfa.getTransitions()) {
                if (s.equals(t.getOrigin())) {

                    // If startState is final state add to finals queue otherwise add to nonFinals
                    if (dfa.getEndStates().contains(s)) {
                        if (finals.add(t)) finals.add(t);
                    } else if (nonFinals.add(t)) nonFinals.add(t);
                }
            }
        }

        // Keep iterating until there are no more changes possible
        int prevTotalSize = 0;
        int currNonFinalsSize = nonFinals.size();
        int currFinalsSize = finals.size();
        Set<String> knownStates = new LinkedHashSet<>();

        while (prevTotalSize < (currFinalsSize + currNonFinalsSize)) {
            // Remove duplicates
            nonFinals = mergeSimilarEndStates(dfa, nonFinals);
            finals = mergeSimilarEndStates(dfa, finals);

            // Save all starting states
            knownStates.addAll(getStates(nonFinals));
            knownStates.addAll(getStates(finals));

            // Replace non reachable next states
            nonFinals = replaceNonReachable(knownStates, nonFinals);
            finals = replaceNonReachable(knownStates, finals);

            // Print progress
            printSet(dfa, nonFinals);
            printSet(dfa, finals);

            // Update values (needs to be in this order)
            currNonFinalsSize = nonFinals.size();
            currFinalsSize = finals.size();
            prevTotalSize = currNonFinalsSize + currFinalsSize;

            knownStates.addAll(getStates(nonFinals));
            knownStates.addAll(getStates(finals));
        }

        // Combine transitions
        Set<Transition> allTransitions = new LinkedHashSet<>();
        allTransitions.addAll(nonFinals);
        allTransitions.addAll(finals);

        // Print final table
        printSet(dfa, allTransitions);

        // Create MDFA
        MDFA mdfa = new MDFA(new ArrayList<>(allTransitions));
        mdfa.addAllStates(getStates(allTransitions));

        // Mark states
        for (String state : mdfa.getStates()) {
            // Add Start states
            for (String startState : dfa.getStartStates())
                if (state.contains(startState)) mdfa.addStartState(state);

            // Add Final states
            for (String endState : dfa.getEndStates()) if (state.contains(endState)) mdfa.addEndState(state);
        }

        return mdfa;
    }

    private Set<Transition> replaceNonReachable(Set<String> knownStates, Set<Transition> transitions) {
        Set<Transition> newSet = new LinkedHashSet<>();

        for (Transition t : transitions){
            for (String start : getStates(transitions)) {
                if (start.equals(t.getOrigin())) {
                    String next = t.getDestination();
                    if (!knownStates.contains(t.getDestination())){
                        next = start;
                    }

                    newSet.add(new Transition(start, next, t.getSymbol()));
                }
            }
        }

        return newSet;
    }

    private LinkedHashSet<Transition> mergeSimilarEndStates(DFA dfa, Set<Transition> transitions) {
        TreeMap<String, String> combinedStateDictionary = new TreeMap<>();
        LinkedHashSet<Transition> newTransitionList = new LinkedHashSet<>();

        // Combine next states and put them in a dictionary
        List<String> states = getStates(transitions);

        for (String state : states){
            StringBuilder combinedEnds = new StringBuilder();
            int letterIndex = 0;

            for (Transition t : transitions) {
                if (state.equals(t.getOrigin())) {
                    combinedEnds.append(t.getDestination());
                    if (letterIndex++ < dfa.getLetters().size() - 1) combinedEnds.append('.'); // '.' to separate them later
                }
            }
            combinedStateDictionary.put(state, combinedEnds.toString());
        }

        // By switches keys and values it will remove any duplicates, because Hashmap can't contain duplicate keys
        // Makes use of TreeMap to make sure the first indexed state gets used instead of last
        combinedStateDictionary = reverseMap(reverseMap(combinedStateDictionary.descendingMap()));

        // Create new transitions based on removed duplicates;
        for (String start : combinedStateDictionary.keySet()) {
            String[] splitStates = splitState(combinedStateDictionary.get(start));

            for (int i = 0; i < dfa.getLetters().size(); i++) {
                String end = splitStates[i];
                String symbol = String.valueOf(dfa.getLetters().get(i));

                Transition transition = new Transition(start, end, symbol);
                newTransitionList.add(transition);
            }
        }

        return newTransitionList;
    }

    private TreeMap<String, String> reverseMap(Map<String, String> m) {
        TreeMap<String, String> newMap = new TreeMap<>();

        for (Map.Entry<String, String> entry : m.entrySet()) {
            if (!newMap.containsKey(entry.getKey())) newMap.put(entry.getValue(), entry.getKey());
        }

        return newMap;
    }

    private List<String> getStates(Set<Transition> allTransitions) {
        List<String> states = new ArrayList<>();
        allTransitions.stream().filter(t -> !states.contains(t.getOrigin())).forEach(t -> states.add(t.getOrigin()));
        return states;
    }

    private String[] splitState(String s) {
        return s.split("\\.");
    }

    private void printSet(DFA dfa, Set<Transition> transitions) {
        List<String> states = getStates(transitions);
        ConsoleTable table = newTable(dfa);

        for (String start : states){
            table.appendRow();
            table.appendColum(start);

            for (Transition t : transitions){
                if (t.getOrigin().equals(start)){
                    table.appendColum(t.getDestination());
                }
            }
        }

        System.out.println(table);
    }

    private ConsoleTable newTable(DFA dfa) {
        ConsoleTable table = new ConsoleTable(dfa.getLetters().size() + 1, true);
        table.appendRow();
        table.appendColum("");
        for (int i = 0; i < dfa.getLetters().size(); i++) {
            table.appendColum(dfa.getLetters().get(i));
        }
        return table;
    }
}
