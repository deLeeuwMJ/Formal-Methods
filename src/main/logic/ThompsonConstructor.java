package main.logic;

import main.model.automata.NDFA;
import main.model.automata.Transition;
import main.model.regex.ParsedRegex;

import java.util.*;

import static main.logic.InputValidator.*;

public class ThompsonConstructor {

    private String regex;
    public String startNode = null;
    public String endNode = null;
    public String selectedNode = null;
    public List<String> connect2end = new ArrayList<>();
    public HashMap<Integer, String> endNodes = new LinkedHashMap<>();
    public List<Bridge> bridges = new ArrayList<>();
    public String firstnode = null;
    public String finalNode = null;

    public class Bridge {
        public String startnode;
        public String endnode;
        public String key;

        public Bridge(String startnode, String endnode, String key) {
            this.startnode = startnode;
            this.endnode = endnode;
            this.key = key;
        }
    }

    public NDFA construct(ParsedRegex parsedRegex) {
        // Adds a 1 as end flag for the regex
        regex = parsedRegex.getRegexString() + "1";
        NDFA ndfa = new NDFA();

        // Main loop for converting regex to NDFA
        for (int i = 0; i < regex.length(); i++) {
            char c = regex.charAt(i);

            // Checks if a new alphabet has started
            if (c == '(') {
                startNode = "q" + i;
                selectedNode = startNode;
                if (endNode != null) {
                    link(endNode, startNode, "");
                } else {
                    firstnode = startNode;
                }
                continue;
            }

            // checks if alphabet has ended
            if (c == ')') {
                endNode = "q" + i;
                link(selectedNode, endNode, "");
                connectEnds(connect2end, endNode);
                connect2end = new ArrayList<>();
                selectedNode = endNode;
                endNodes.put(i, endNode);
                continue;
            }

            // Makes 2 links from the start of the alphabet to the end
            if (c == '*') {
                if (startNode != null && endNode != null) {
                    link(startNode, endNode, "");
                    link(endNode, startNode, "");
                }
            }

            // Makes a link from the back to the start of the alphabet
            if (c == '+') {
                if (startNode != null && endNode != null) {
                    link(endNode, startNode, "");
                }
            }

            // Creates the alphabet nodes and links staring node to it
            if (isAlphabet(c)) {
                List<Character> chars = new ArrayList<>();

                chars.add(c);

                while (true) {
                    char t = regex.charAt(i + 1);
                    if (isAlphabet(t)) {
                        chars.add(t);
                        i++;
                    } else {
                        String n = "q" + i;
                        link(selectedNode, n, String.valueOf(chars));

                        if (t != '|')
                            selectedNode = n;
                        else
                            connect2end.add(n);

                        break;
                    }
                }
            }

            // Marks the end of the regex sting
            if (c == '1') {
                String n = "q" + i;
                link(selectedNode, n, "");
                this.finalNode = n;
            }
        }

        // Converts the bridge object to Customtransition
        for (Bridge b : bridges) {
            ndfa.addTransition(new Transition(b.startnode, b.endnode, b.key));
        }
        ndfa.addStartState(this.firstnode);
        ndfa.addEndState(this.finalNode);

        return ndfa;
    }


    // Links 2 nodes with a lable for graphviz
    public void link(String s, String e, String text) {
        if (text.equals("")) {
            text = "ε";
        }
        this.bridges.add(new Bridge(s, e, text));
    }

    // Links all nodes with alphabet to the ending node
    public void connectEnds(List<String> nodes, String end) {
        if (nodes.isEmpty()) {
            return;
        }

        for (String n : nodes) {
            link(n, end, "");
        }
    }
}
