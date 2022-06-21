package main.logic;

import main.model.*;

import java.util.*;

public class AutomataBuilder {

    private Automata<String> automata;
    private final List<String> nodeFinals;
    private String nodeStart;

    public AutomataBuilder() {
        nodeFinals = new ArrayList<>();
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

    public Automata<String> build(List<String> regexOperations, List<String> terminals) {
        automata = new Automata<String>();
        nodeFinals.clear();
        nodeStart = "";

        List<String> sigmaList = Arrays.asList("a","b");
        List<String> states = Arrays.asList("A", "B");

        // Q * SIGMA
        addTransition("A", "B", 'a');
        addSelfTransition("A", 'b');
        addSelfTransition("B", 'a');
        addSelfTransition("B", 'b');

        defineStart("A");
        defineFinal("B");

        return automata;
    }

    public String getMachine() {
        return "M = ({" + formatArrayString(getNodes().toString()) + "}, {" + formatArrayString("ab") + "}, " + nodeStart + ", {" + formatArrayString(nodeFinals.toString()) + "}, P)";
    }
}
