package main.logic;

import main.model.*;

import java.util.Stack;

import static main.logic.InputValidator.isAlphabet;
import static main.logic.InputValidator.isRegexOperator;


public class RegexBuilder {

    public State build(Stack<String> postfixStack) {
        OwnStack<Fragment> fragmentStack = new OwnStack <Fragment> ();
        Fragment completeNfa = new Fragment();
        State matchstate = new State();

        for (int i = 0; i < postfixStack.size(); i++) {
            String val = postfixStack.get(i);

            if(isAlphabet(val.charAt(0))){
                State literalState = new State(val.charAt(0));
                fragmentStack.push(new Fragment(literalState, literalState));
            }
            else if (Operator.valueOf(val) == Operator.DOT){
                Fragment previous2 = fragmentStack.pop();
                Fragment previous1 = fragmentStack.pop();
                patchFragmentToAState(previous1, previous2.getStart());
                fragmentStack.push(new Fragment(previous1.getStart(), previous2.getOutArrows() ) );
            }
            else if (Operator.valueOf(val) == Operator.OR){
                Fragment previous2 = fragmentStack.pop();
                Fragment previous1 = fragmentStack.pop();
                State newState = new State(previous1.getStart(), previous2.getStart());
                fragmentStack.push(new Fragment(newState, appendOutArrows(previous1.getOutArrows(), previous2.getOutArrows())));
            }
            else if (Operator.valueOf(val) == Operator.STAR){
                Fragment previous = fragmentStack.pop();
                State newState = new State(previous.getStart(), null);
                patchFragmentToAState(previous, newState);
                fragmentStack.push(new Fragment(newState, newState));
            }
        }
        completeNfa = fragmentStack.pop();
        patchFragmentToAState(completeNfa, matchstate);
        return completeNfa.getStart();
    }

    private OwnArrayList<State> appendOutArrows(OwnArrayList<State> a, OwnArrayList<State> b){
        OwnArrayList <State> appended = new OwnArrayList<State>();
        for (int i = 0; i < a.size(); i++){
            appended.add(a.get(i));
        }
        for (int i = 0; i < b.size(); i++){
            appended.add(b.get(i));
        }
        return appended;
    }
    private void patchFragmentToAState(Fragment a, State s){
        OwnArrayList<State> toBePatched = a.getOutArrows();
        for (int i = 0; i < toBePatched.size(); i++){
            State openarrows = toBePatched.get(i);
            openarrows.patch(s);
        }
    }

}
