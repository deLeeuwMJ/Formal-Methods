package main.operator.automaton.ndfa;

import main.operator.automaton.AutomatonOperator;
import main.operator.automaton.AutomatonOperatorType;

public class NdfaEndsWith extends AutomatonOperator {

    @Override
    public AutomatonOperatorType getType(){
        return AutomatonOperatorType.ENDS;
    }
}