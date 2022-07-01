package main.model.automata;

import java.util.List;

public class MDFA extends FA {

    public MDFA(){
        super();
    }

    public MDFA(List<Transition> t) {
        super(t);
    }

    @Override
    public void addAllLetters(List<Character> list) {
        letters.addAll(list);
    }
}
