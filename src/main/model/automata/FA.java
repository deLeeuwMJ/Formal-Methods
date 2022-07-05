package main.model.automata;

import main.logic.Ndfa2DfaConverter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

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

    //todo reset seenletters for every endstate
    //todo check transitions from endstate for current letter
    public void modifyTransitions(ModifyTransitions m) {
        LinkedHashSet<Character> seenLetters = new LinkedHashSet<>();
        List<Transition> copyList = new ArrayList<>(transitions);

        for (Transition t : copyList) {
            if (getEndStates().contains(t.getDestination())) {
                for (int i = 0; i < letters.size() - 1; i++) { // -1 to prevent use of epsilon
                    Character c = letters.get(i);

                    if (!seenLetters.contains(c)) {
                        if (seenLetters.add(c)) seenLetters.add(c);
                        if (m == ModifyTransitions.FINAL_ITSELF) {
                            transitions.add(new Transition(t.getDestination(), t.getDestination(), String.valueOf(c)));
                        } else if (m == ModifyTransitions.FINAL_TO_START) {
                            transitions.add(new Transition(t.getDestination(), getStartStates().get(0), String.valueOf(c)));
                        }
                    }
                }
            }
        }
    }

}
