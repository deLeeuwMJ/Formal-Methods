package main.logic;

import main.model.automata.DFA;
import main.model.automata.NDFA;
import main.model.automata.Transition;
import main.ui.ConsoleTable;

import java.util.*;

public class Ndfa2DfaConverter {

    private Map<String, Integer> map;
    private int state = 'A';

    public DFA convert(NDFA ndfa) {
        map = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();

        // Printing
        ConsoleTable table = new ConsoleTable(ndfa.getLetters().size(), true);
        table.appendRow();
        table.appendColum("");
        for (int i = 0; i < ndfa.getLetters().size() - 1; i++) {
            table.appendColum(ndfa.getLetters().get(i));
        }

        // Logic
        String startState = ndfa.getStartStates().get(0);
        map.put(startState, state);
        queue.add(state++);
        while (!queue.isEmpty()) {
            Character[] dfaLine = new Character[ndfa.getLetters().size()];
            int character = queue.poll();
            table.appendRow();
            table.appendColum((char) character);

            // Check path for each letter
            for (int i = 0; i < ndfa.getLetters().size() - 1; i++) {
                char currLetter = ndfa.getLetters().get(i);
                int currStateIndex = 0;
                boolean validFlag = false;

                System.out.print("Reachable with " + currLetter + ": ");
                System.out.println(ndfa.getNextStates("q0", String.valueOf(currLetter), false));

                // Loop through all the transitions
                for (Transition t : ndfa.getTransitions()) {

                    // Check per state if there is transition from currentStateIndex with currLetter
                    String formatStateLabel = "q" + currStateIndex;
                    if (t.getOrigin().equals(formatStateLabel) && t.getSymbol().equals(String.valueOf(currLetter))) {
                        table.appendColum((char)(state));
                        validFlag = true;
                        break;
                    }
                }

                if (!validFlag){
                    table.appendColum(null);
                }
            }
        }

        // Result
        System.out.println("--------DFA--------");
        System.out.print(table);



        return new DFA();
    }
}
