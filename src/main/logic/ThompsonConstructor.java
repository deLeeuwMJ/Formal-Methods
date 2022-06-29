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
        stateList.clear();
        tempList.clear();
        NDFA ndfa;

        for (char c : postfix) {
            if (isRegexOperator(c)) { // Is it an operator
                Transition left, right;
                stateList.clear();

                switch (c) {
                    case DOT_OPERATOR_SYMBOL:
                        right = queue.pop();
                        left = queue.pop();

                        concat(left, right);
                        break;
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

    private Transition link(String s, String e, String symbol){
        return new Transition(s, e, symbol);
    }

    private void addState(String state){
        if (!stateList.contains(state)) stateList.add(state);
    }

    private Transition one(char label){
        String startState = getNewState();
        addState(startState);

        String endState = getNewState();
        addState(startState);

        return link(startState, endState, String.valueOf(label));
    }

    private Transition empty(){
        String startState = getNewState();
        addState(startState);

        String endState = getNewState();
        addState(startState);

        return link(startState, endState);
    }

    private void concat(Transition left, Transition right) {
        String lStartState = getNewState();
        String lNextState = getNewState();

        Transition leftLinked = link(lStartState, lNextState, left.getSymbol());
        tempList.add(leftLinked);

        String rStartState = getNewState();
        String rFinalState = getNewState();

        Transition rightLinked = link(rStartState, rFinalState, right.getSymbol());
        tempList.add(rightLinked);

        Transition lrLinked = link(leftLinked.getDestination(), rightLinked.getOrigin());
        tempList.add(lrLinked);
    }

    private String getNewState(){
        String state = "q" + stateList.size();
        addState(state);
        return state;
    }

}
