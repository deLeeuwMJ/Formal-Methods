package main.automaton.ndfa;

import main.automaton.AutomatonOperator;
import main.automaton.AutomatonOperatorType;

public class NdfaStartsWith extends AutomatonOperator {

    @Override
    public AutomatonOperatorType getType(){
        return AutomatonOperatorType.STARTS;
    }
}