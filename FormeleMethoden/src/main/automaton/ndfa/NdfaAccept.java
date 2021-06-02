package main.automaton.ndfa;

import main.automaton.AutomatonOperator;
import main.automaton.AutomatonOperatorType;

public class NdfaAccept extends AutomatonOperator {

    @Override
    public AutomatonOperatorType getType(){
        return AutomatonOperatorType.ACCEPT;
    }
}