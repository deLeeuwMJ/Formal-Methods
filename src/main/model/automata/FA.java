package main.model.automata;

import java.util.*;

import static main.logic.InputValidator.EPSILON_SYMBOL;

public class FA {

    final List<Character> letters;
    final List<Transition> transitions;
    final List<String> states;
    final List<String> startStates;
    final List<String> endStates;

    public FA() {
        states = new ArrayList<>();
        transitions = new ArrayList<>();
        startStates = new ArrayList<>();
        endStates = new ArrayList<>();
        letters = new ArrayList<>();
    }

    public FA(List<Transition> t) {
        transitions = t;
        states = new ArrayList<>();
        startStates = new ArrayList<>();
        endStates = new ArrayList<>();
        letters = new ArrayList<>();
    }

    public void addState(String s) {
        if (!states.contains(s)) states.add(s);
    }

    public void addAllStates(List<String> list) {
        states.addAll(list);
    }

    public void addAllLetters(List<Character> list) {
        letters.addAll(list);
        letters.add(EPSILON_SYMBOL);
    }

    public void addTransition(Transition t) {
        transitions.add(t);
    }

    public void addStartState(String s) {
        startStates.add(s);
    }

    public void addEndState(String s) {
        endStates.add(s);
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public List<String> getStartStates() {
        return startStates;
    }

    public List<String> getEndStates() {
        return endStates;
    }

    public List<Character> getLetters() {
        return letters;
    }

    public List<String> getStates() {
        return states;
    }

    public void printTransitions() {
        for (Transition t : transitions) System.out.println(t.toString());
    }

    public enum ModifyTransitions {
        FINAL_TO_START, FINAL_ITSELF
    }

    public void modifyTransitions(ModifyTransitions m) {
        List<Transition> copyList = new ArrayList<>(transitions);
        HashMap<String, List<Transition>> lookupTransitionTable = new HashMap<>();

        // Get all possible Transitions from end state;
        for (String endState : getEndStates()) {
            List<Transition> allTransitionsContainsEndState = new ArrayList<>();
            for (Transition t : copyList) {
                if (endState.equals(t.getOrigin()) || endState.equals(t.getDestination())) {
                    allTransitionsContainsEndState.add(t);
                }
            }
            lookupTransitionTable.put(endState, allTransitionsContainsEndState);
        }

        HashMap<String, LinkedHashSet<String>> lookupInOutTable = new HashMap<>();


        // retrieve all symbols going in and out
        for (String endState : getEndStates()) {
            List<Transition> dependableTransitions = lookupTransitionTable.get(endState);
            LinkedHashSet<String> goingOut = new LinkedHashSet<>();

            // Get all characters out
            for (Transition t : dependableTransitions) {
                if (t.getOrigin().equals(endState)) {
                    if (goingOut.add(t.getSymbol())) goingOut.add(t.getSymbol());
                }
            }

            lookupInOutTable.put(endState, goingOut);
        }

        // Create new states
        for (String endState : getEndStates()) {
            LinkedHashSet<String> goingInOut = lookupInOutTable.get(endState);
            for (int i = 0; i < letters.size() - 1; i++) {
                String letter = String.valueOf(letters.get(i));

                if (!goingInOut.contains(letter)) {
                    if (m == ModifyTransitions.FINAL_ITSELF) {
                        transitions.add(new Transition(endState, endState, letter));
                    } else if (m == ModifyTransitions.FINAL_TO_START) {
                        transitions.add(new Transition(endState, getStartStates().get(0), letter));
                    }
                }
            }
        }
    }
}
