package main.automaton.dfa;

import main.automaton.AutomatonOperator;
import main.automaton.AutomatonOperatorType;

public class DfaEquals extends AutomatonOperator {

    @Override
    public AutomatonOperatorType getType(){
        return AutomatonOperatorType.EQUALS;
    }
}