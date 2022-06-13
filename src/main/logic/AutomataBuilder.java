package main.logic;

import main.model.Automata;
import main.model.ExecutionResult;
import main.model.NFA;
import main.model.Transition;

import java.util.*;

public class AutomataBuilder {

    private final SortedSet<Character> terminalList;
    private Automata<String> automata;
    private final List<String> nodeFinals;
    private String nodeStart;

    public AutomataBuilder() {
        terminalList = new TreeSet<>();
        nodeFinals = new ArrayList<>();
    }

    public ExecutionResult init() {
        if (getAlphabet().length != 0) {
            automata = new Automata<String>(getAlphabet());
            return ExecutionResult.PASSED;
        } else return ExecutionResult.FAILED;
    }

    public void addTerminals(String characters) {
        char[] splitString = characters.toCharArray();

        for (char c : splitString) {
            if (!terminalList.contains(c)) {
                terminalList.add(c);
            }
        }
    }

    private Character[] getAlphabet() {
        return terminalList.toArray(new Character[0]);
    }

    public String getMachine() {
        String alphabet = Arrays.toString(getAlphabet());

        return "M = ({" + formatArrayString(getNodes().toString()) + "}, {" + formatArrayString(alphabet) + "}, S, " + nodeStart + ", {" + formatArrayString(nodeFinals.toString()) + "})";
    }

    private List<String> getNodes() {
        List<Transition> transitionsResult = automata.getTransitions();
        List<String> nodes = new ArrayList<>();

        for (Transition t : transitionsResult) {
            String node = t.getFromState().toString();
            if (!nodes.contains(node)) {
                nodes.add(node);
            }
        }

        return nodes;
    }

    private String formatArrayString(String s) {
        return s.substring(1, s.length() - 1);
    }

    public void reset() {
        terminalList.clear();
        nodeFinals.clear();
        nodeStart = "";
        automata = new Automata<>();
    }

    public void addTransition(String from, String to, char label) {
        automata.addTransition(new Transition<String>(from, to, label));
    }

    public void addSelfTransition(String node, char label) {
        automata.addTransition(new Transition<String>(node, label));
    }

    public void defineStart(String node) {
        automata.defineAsStartState(node);
        nodeStart = node;
    }

    public void defineFinal(String node) {
        automata.defineAsFinalState(node);
        nodeFinals.add(node);
    }

    public Automata<String> get() {
        return automata;
    }

    public void convert(NFA result) {
        StringBuilder terminalBuilder = new StringBuilder();

        // Create fitting string for function
        for (Transition t : result.transitions){
            terminalBuilder.append(t.getSymbol());
        }
        addTerminals(terminalBuilder.toString());

        init();

        // Create fitting string for function
        for (Transition t : result.transitions){
            addTransition(t.getFromState().toString(), t.getToState().toString(), t.getSymbol());
        }

        defineStart("0");
        defineFinal(String.valueOf(result.final_state));

    }
}
