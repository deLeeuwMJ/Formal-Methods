package main.automaton.dfa;

import main.automaton.AutomatonOperator;
import main.automaton.AutomatonOperatorType;

public class DfaAnd extends AutomatonOperator {

    @Override
    public AutomatonOperatorType getType(){
        return AutomatonOperatorType.AND;
    }
}