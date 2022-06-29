package main.logic;

import main.model.automata.NDFA;
import main.model.automata.Transition;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static main.logic.InputValidator.*;

public class ThompsonConstructor {

    private List<Transition> tempList;
    private List<String> stateList;

    public ThompsonConstructor() {
        tempList = new ArrayList<>();
        stateList = new ArrayList<>();
    }

    public NDFA construct(Stack<Character> postfix){
        Stack<Transition> queue = new Stack<>();
        NDFA ndfa = new NDFA();
        stateList.clear();
        tempList.clear();

        for (char c : postfix) {
            if (isRegexOperator(c)) { // Is it an operator
                char left, right;

                switch (c) {
//                    case DOT_OPERATOR_SYMBOL:
//                        right = queue.pop();
//                        left = queue.pop();
//
//                        tempList.add(concat(left, right));
//                        break;
//                    case OR_OPERATOR_SYMBOL:
//                        right = nfaStack.pop();
//                        left = nfaStack.pop();
//
//                        nfaStack.push(union(left, right));
//                        break;
//                    case PLUS_OPERATOR_SYMBOL:
//                        left = nfaStack.pop();
//
//                        nfaStack.push(more(left));
//                        break;
//                    case STAR_OPERATOR_SYMBOL:
//                        left = nfaStack.pop();
//
//                        nfaStack.push(kleene(left));
//                        break;
                }
            } else { // its a symbol
                queue.push(one(c));
            }
        }

        while(!queue.isEmpty()){
            tempList.add(queue.pop());
        }

        ndfa = new NDFA(tempList);
        ndfa.addStartState(tempList.get(0).getOrigin());
        ndfa.addEndState(tempList.get(tempList.size()-1).getDestination());

        return ndfa;
    }

    private Transition link(String s, String e){
        return new Transition(s, e, String.valueOf(EPSILON_SYMBOL));
    }

    private Transition link(String s, String e, char label){
        return new Transition(s, e, String.valueOf(label));
    }

    private void addState(String state){
        if (!stateList.contains(state)) stateList.add(state);
    }

    private Transition one(char label){
        String startState = "q" + stateList.size();
        addState(startState);

        String endState = "q" + stateList.size();
        addState(startState);

        return link(startState, endState, label);
    }

    private Transition empty(){
        String startState = "q" + stateList.size();
        addState(startState);

        String endState = "q" + stateList.size();
        addState(startState);

        return link(startState, endState);
    }

//    private Transition concat(char left, char right) {
//
//        // copy NFA m's getTransitions()
//        // to n, and handles connecting n & m
//        for (Transition t : m.getTransitions()
//        ) {
//            n.getTransitions()
//                    .add(new Transition(t.from + n.getStates().size() - 1,
//                            t.to + n.getStates().size() - 1, t.symbol));
//        }
//
//        // take m and combine to n after erasing initial m state
//        for (Integer s : m.getStates()) {
//            n.getStates().add(s + n.getStates().size() + 1);
//        }
//
//        n.final_state = n.getStates().size() + m.getStates().size() - 2;
//        return n;
//
//
//        return new Transition()
//    }

//    private NFA union(NFA n, NFA m) {
//        NFA result = new NFA(n.getStates().size() + m.getStates().size() + 2);
//
//        // the branching of q0 to beginning of n
//        result.getTransitions()
//                .add(new Transition(0, 1, String.valueOf(EPSILON_SYMBOL)));
//
//        // copy existing getTransitions()
//        // of n
//        for (Transition t : n.getTransitions()
//        ) {
//            result.getTransitions()
//                    .add(new Transition(t.from + 1,
//                            t.to + 1, t.symbol));
//        }
//
//        // transition from last n to final state
//        result.getTransitions()
//                .add(new Transition(n.getStates().size(),
//                        n.getStates().size() + m.getStates().size() + 1, String.valueOf(EPSILON_SYMBOL)));
//
//        // the branching of q0 to beginning of m
//        result.getTransitions()
//                .add(new Transition(0, n.getStates().size() + 1, String.valueOf(EPSILON_SYMBOL)));
//
//        // copy existing getTransitions()
//        // of m
//        for (Transition t : m.getTransitions()
//        ) {
//            result.getTransitions()
//                    .add(new Transition(t.from + n.getStates().size()
//                            + 1, t.to + n.getStates().size() + 1, t.symbol));
//        }
//
//        // transition from last m to final state
//        result.getTransitions()
//                .add(new Transition(m.getStates().size() + n.getStates().size(),
//                        n.getStates().size() + m.getStates().size() + 1, String.valueOf(EPSILON_SYMBOL)));
//
//        // 2 new getStates() and shifted m to avoid repetition of last n & 1st m
//        result.final_state = n.getStates().size() + m.getStates().size() + 1;
//
//        return result;
//    }
//
//    private NFA kleene(NFA n) {
//        NFA result = new NFA(n.getStates().size() + 2);
//        result.getTransitions()
//                .add(new Transition(0, 1, String.valueOf(EPSILON_SYMBOL))); // new trans for q0
//
//        // copy existing getTransitions()
//
//        for (Transition t : n.getTransitions()
//        ) {
//            result.getTransitions()
//                    .add(new Transition(t.from + 1,
//                            t.to + 1, t.symbol));
//        }
//
//        // add empty transition from final n state to new final state.
//        result.getTransitions()
//                .add(new Transition(n.getStates().size(),
//                        n.getStates().size() + 1, String.valueOf(EPSILON_SYMBOL)));
//
//        // Loop back from last state of n to initial state of n.
//        result.getTransitions()
//                .add(new Transition(n.getStates().size(), 1, String.valueOf(EPSILON_SYMBOL)));
//
//        // Add empty transition from new initial state to new final state.
//        result.getTransitions()
//                .add(new Transition(0, n.getStates().size() + 1, String.valueOf(EPSILON_SYMBOL)));
//
//        result.final_state = n.getStates().size() + 1;
//        return result;
//    }
//
//    private NFA more(NFA n) {
//        NFA result = new NFA(n.getStates().size() + 2);
//        result.getTransitions()
//                .add(new Transition(0, 1, String.valueOf(EPSILON_SYMBOL))); // new trans for q0
//
//        // copy existing getTransitions()
//
//        for (Transition t : n.getTransitions()
//        ) {
//            result.getTransitions()
//                    .add(new Transition(t.from + 1,
//                            t.to + 1, t.symbol));
//        }
//
//        // add empty transition from final n state to new final state.
//        result.getTransitions()
//                .add(new Transition(n.getStates().size(),
//                        n.getStates().size() + 1, String.valueOf(EPSILON_SYMBOL)));
//
//        // Loop back from last state of n to initial state of n.
//        result.getTransitions()
//                .add(new Transition(n.getStates().size(), 1, String.valueOf(EPSILON_SYMBOL)));
//
//        result.final_state = n.getStates().size() + 1;
//        return result;
//    }

}
