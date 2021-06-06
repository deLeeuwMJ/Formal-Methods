package main.operator.automaton.dfa;

import main.operator.automaton.AutomatonOperator;
import main.operator.automaton.AutomatonOperatorType;

public class DfaAccept extends AutomatonOperator {

    @Override
    public AutomatonOperatorType getType(){
        return AutomatonOperatorType.ACCEPT;
    }
}