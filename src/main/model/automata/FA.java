package main.model.automata;

import main.logic.Ndfa2DfaConverter;

import java.util.ArrayList;
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

    public void startsWith(String input){
        NDFA ndfa = new NDFA();

        for(int i = 0; i < input.length(); i++){

            String currentState = "q" + (i+1);
            String nextState = "q" + (i+2);
            String symbol = input ;

            ndfa.addTransition(new Transition(currentState,nextState,symbol));
        }

        String endState = "q" + (input.length());
        ndfa.addTransition(new Transition(endState, endState, "a"));
        ndfa.addTransition(new Transition(endState, endState, "b"));

        ndfa.addStartState("q1");
        ndfa.addEndState(endState);
        ndfa.getStartStates().add("q1");
        ndfa.getEndStates().add(endState);

        Ndfa2DfaConverter ndfa2DfaConverter = new Ndfa2DfaConverter();
        ndfa2DfaConverter.convert(ndfa);
    }

    public void endsWith(String input){
        NDFA ndfa = new NDFA();

        for(int i = 0; i < input.length(); i++){

            String currentState = "q" + (i+1);
            String nextState = "q" + (i+2);
            String symbol = input ;

            ndfa.addTransition(new Transition(currentState,nextState,symbol));

            String returnSymbol = symbol == "a" ? "b" : "a";
            ndfa.addTransition(new Transition(currentState, "q1", returnSymbol));
        }

        String endState = "q" + (input.length()+ 1);
        ndfa.addTransition(new Transition(endState, "q1", "a"));
        ndfa.addTransition(new Transition(endState, "q1", "b"));

        ndfa.addStartState("q1");
        ndfa.addEndState(endState);
        ndfa.getStartStates().add("q1");
        ndfa.getEndStates().add(endState);

        Ndfa2DfaConverter ndfa2DfaConverter = new Ndfa2DfaConverter();
        ndfa2DfaConverter.convert(ndfa);
    }

    public void Contains(String input){
        NDFA ndfa = new NDFA();

        for(int i = 0; i < input.length(); i++){

            String currentState = "q" + (i+1);
            String nextState = "q" + (i+2);
            String symbol = input ;

            ndfa.addTransition(new Transition(currentState,nextState,symbol));

            String returnSymbol = symbol == "a" ? "b" : "a";
            ndfa.addTransition(new Transition(currentState, "q1", returnSymbol));
        }

        String endState = "q" + (input.length() + 1);
        ndfa.addTransition(new Transition(endState, endState, "a"));
        ndfa.addTransition(new Transition(endState, endState, "b"));

        ndfa.addStartState("q1");
        ndfa.addEndState(endState);
        ndfa.getStartStates().add("q1");
        ndfa.getEndStates().add(endState);

        Ndfa2DfaConverter ndfa2DfaConverter = new Ndfa2DfaConverter();
        ndfa2DfaConverter.convert(ndfa);
    }
}
