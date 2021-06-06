package main.operator.automaton;

import main.operator.DefaultOperator;

public abstract class AutomatonOperator extends DefaultOperator {

    public AutomatonOperatorType getType() {
        return AutomatonOperatorType.STARTS;
    }
}
