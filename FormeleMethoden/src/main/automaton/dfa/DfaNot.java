package main.automaton.dfa;

import main.automaton.AutomatonOperator;
import main.automaton.AutomatonOperatorType;

public class DfaNot extends AutomatonOperator {

    @Override
    public AutomatonOperatorType getType(){
        return AutomatonOperatorType.NOT;
    }
}