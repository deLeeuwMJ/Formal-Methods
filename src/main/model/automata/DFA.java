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

    public DFA startsWith(String input) {
        NDFA ndfa = new NDFA();

        for (int i = 0; i < input.length(); i++) {

            String currentState = "q" + (i + 1);
            String nextState = "q" + (i + 2);
            String symbol = input;

            ndfa.addTransition(new Transition(currentState, nextState, symbol));
            ndfa.addState(currentState);
        }

        String endState = "q" + (input.length());
        ndfa.addTransition(new Transition(endState, endState, "a"));
        ndfa.addTransition(new Transition(endState, endState, "b"));

        ndfa.addStartState("q1");
        ndfa.addEndState(endState);
        ndfa.getStartStates().add("q1");
        ndfa.getEndStates().add(endState);

        ndfa.addAllLetters(Arrays.asList('a', 'b'));

        Ndfa2DfaConverter ndfa2DfaConverter = new Ndfa2DfaConverter();
        return ndfa2DfaConverter.convert(ndfa);
    }

    public DFA endsWith(String input) {
        NDFA ndfa = new NDFA();

        // (a|b)*aab

        for (int i = 0; i < input.length(); i++) {

            String currentState = "q" + (i);
            String nextState = "q" + (i + 1);
            String symbol = String.valueOf(input.charAt(i));

            ndfa.addTransition(new Transition(currentState, nextState, symbol));

//            String returnSymbol = symbol.equals("a") ? "b" : "a";
//            ndfa.addTransition(new Transition(currentState, "q1", returnSymbol));
            ndfa.addState(currentState);
            ndfa.addState(nextState);
        }

        String startState = ndfa.getStates().get(0);
        String endState = "q" + (ndfa.getStates().size() - 1);
        ndfa.addTransition(new Transition(endState, startState, "a"));
        ndfa.addTransition(new Transition(endState, startState, "b"));

        ndfa.addStartState(startState);
        ndfa.addEndState(endState);

        ndfa.addAllLetters(Arrays.asList('a', 'b'));

        Ndfa2DfaConverter ndfa2DfaConverter = new Ndfa2DfaConverter();
        return ndfa2DfaConverter.convert(ndfa);
    }

    public DFA contains(String input) {
        NDFA ndfa = new NDFA();

        for (int i = 0; i < input.length(); i++) {

            String currentState = "q" + (i + 1);
            String nextState = "q" + (i + 2);
            String symbol = input;

            ndfa.addTransition(new Transition(currentState, nextState, symbol));

            String returnSymbol = symbol == "a" ? "b" : "a";
            ndfa.addTransition(new Transition(currentState, "q1", returnSymbol));
            ndfa.addState(currentState);
        }

        String endState = "q" + (input.length() + 1);
        ndfa.addTransition(new Transition(endState, endState, "a"));
        ndfa.addTransition(new Transition(endState, endState, "b"));

        ndfa.addStartState("q1");
        ndfa.addEndState(endState);
        ndfa.getStartStates().add("q1");
        ndfa.getEndStates().add(endState);

        ndfa.addAllLetters(Arrays.asList('a', 'b'));

        Ndfa2DfaConverter ndfa2DfaConverter = new Ndfa2DfaConverter();
        return ndfa2DfaConverter.convert(ndfa);
    }
}
