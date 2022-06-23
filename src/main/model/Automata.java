package main.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Automata {

    private List<String> symbols;
    ArrayList<Integer> states;
    ArrayList<Transition> transitions;
    AutomataType type;

    void setStateSize(int size) {
        for (int i = 0; i < size; i++) this.states.add(i);
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    public AutomataType getType() {
        return type;
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public ArrayList<Integer> getStates() {
        return states;
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    public int getFinalState() {
        return states.size() - 1;
    }

    public int getStartState() { // Always start at 0
        return 0;
    }

    public String getMachine() {
        return "(" + formatList(getStates()) + ", " + formatList(getSymbols()) + ", Trans, " + getStartState() + ", {" + getFinalState() + "})";
    }

    private <T> String formatList(List<T> items) {
        StringBuilder result = new StringBuilder("{");

        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                if (i != 0) result.append(", ");
                result.append(items.get(i));
            }
        }
        
        result.append("}");

        return result.toString();
    }
}
