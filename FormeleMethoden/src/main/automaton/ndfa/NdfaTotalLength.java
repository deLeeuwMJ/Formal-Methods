package main.automaton.ndfa;

import main.automaton.AutomatonOperator;
import main.automaton.AutomatonOperatorType;

public class NdfaTotalLength extends AutomatonOperator {

    @Override
    public AutomatonOperatorType getType(){
        return AutomatonOperatorType.TOTAL_LENGTH;
    }
}