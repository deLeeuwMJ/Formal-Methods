package main.logic;

import main.model.Automata;
import main.model.AutomataType;

public class NFAtoDFAConverter {

    public void convert(Automata automata){
        if (automata.getType() != AutomataType.NFA) return;
    }
}
