package main.automaton.ndfa;

import main.automaton.AutomatonOperator;
import main.automaton.AutomatonOperatorType;

public class NdfaEndsWith extends AutomatonOperator {

    @Override
    public AutomatonOperatorType getType(){
        return AutomatonOperatorType.ENDS;
    }
}