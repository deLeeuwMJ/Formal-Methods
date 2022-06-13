package main.logic;

import main.model.NFA;
import main.model.Transition;

import java.util.Stack;

public class ThompsonHandler {

    public NFA process(String regex) {
        if (!validRegEx(regex)) {
            System.out.println("Invalid Regular Expression Input.");
            return new NFA();
        }

        Stack<Character> operators = new Stack<Character>();
        Stack<NFA> operands = new Stack<NFA>();
        Stack<NFA> concat_stack = new Stack<NFA>();

        boolean ccflag = false; // concat flag
        char op, c; // current character of string
        int para_count = 0;
        NFA nfa1, nfa2;

        for (int i = 0; i < regex.length(); i++) {
            c = regex.charAt(i);
            if (alphabet(c)) {
                operands.push(new NFA(c));
                if (ccflag) { // concat this w/ previous
                    operators.push('.'); // '.' used to represent concat.
                } else
                    ccflag = true;
            } else {
                if (c == ')') {
                    ccflag = false;
                    if (para_count == 0) {
                        System.out.println("Error: More end parenthesis " +
                                "than beginning parenthesis");
                        System.exit(1);
                    } else {
                        para_count--;
                    }
                    // process stuff on stack till '('
                    while (!operators.empty() && operators.peek() != '(') {
                        op = operators.pop();
                        if (op == '.') {
                            nfa2 = operands.pop();
                            nfa1 = operands.pop();
                            operands.push(concat(nfa1, nfa2));
                        } else if (op == '|') {
                            nfa2 = operands.pop();

                            if (!operators.empty() &&
                                    operators.peek() == '.') {

                                concat_stack.push(operands.pop());
                                while (!operators.empty() &&
                                        operators.peek() == '.') {

                                    concat_stack.push(operands.pop());
                                    operators.pop();
                                }
                                nfa1 = concat(concat_stack.pop(),
                                        concat_stack.pop());
                                while (concat_stack.size() > 0) {
                                    nfa1 = concat(nfa1, concat_stack.pop());
                                }
                            } else {
                                nfa1 = operands.pop();
                            }
                            operands.push(union(nfa1, nfa2));
                        }
                    }
                } else if (c == '*') {
                    operands.push(kleene(operands.pop()));
                    ccflag = true;
                } else if (c == '(') { // if any other operator: push
                    operators.push(c);
                    para_count++;
                } else if (c == '|') {
                    operators.push(c);
                    ccflag = false;
                }
            }
        }
        while (operators.size() > 0) {
            if (operands.empty()) {
                System.out.println("Error: imbalance in operands and "
                        + "operators");
                System.exit(1);
            }
            op = operators.pop();
            if (op == '.') {
                nfa2 = operands.pop();
                nfa1 = operands.pop();
                operands.push(concat(nfa1, nfa2));
            } else if (op == '|') {
                nfa2 = operands.pop();
                if (!operators.empty() && operators.peek() == '.') {
                    concat_stack.push(operands.pop());
                    while (!operators.empty() && operators.peek() == '.') {
                        concat_stack.push(operands.pop());
                        operators.pop();
                    }
                    nfa1 = concat(concat_stack.pop(),
                            concat_stack.pop());
                    while (concat_stack.size() > 0) {
                        nfa1 = concat(nfa1, concat_stack.pop());
                    }
                } else {
                    nfa1 = operands.pop();
                }
                operands.push(union(nfa1, nfa2));
            }
        }
        return operands.pop();
    }

    public boolean validRegEx(String regex) {
        if (regex.isEmpty())
            return false;
        for (char c : regex.toCharArray())
            if (!validRegExChar(c))
                return false;
        return true;
    }

    private boolean validRegExChar(char c) {
        return alphabet(c) || regexOperator(c);
    }

    // Check if character is inside alphabet with ASCII table
    private boolean alphabet(char c) {
        return c >= 97 && c <= 122 || c == Transition.EPSILON;
    }

    private boolean regexOperator(char c) {
        return c == '(' || c == ')' || c == '*' || c == '|' || c == '+';
    }

    /*
    kleene() - Highest Precedence regular expression operator. Thompson
        algoritm for kleene star.
*/
    public static NFA kleene(NFA n) {
        NFA result = new NFA(n.states.size() + 2);
        result.transitions.add(new Transition<>(0, 1)); // new trans for q0

        // copy existing transitions
        result.transitions.addAll(n.transitions);

        for (Transition t : n.transitions) {
            int from = Integer.parseInt(t.getFromState().toString());
            int to = Integer.parseInt(t.getToState().toString());
            result.transitions.add(new Transition<>(from + 1, to + 1, t.getSymbol()));
        }

        // add empty transition from final n state to new final state.
        result.transitions.add(new Transition<>(n.states.size(), n.states.size() + 1));

        // Loop back from last state of n to initial state of n.
        result.transitions.add(new Transition<>(n.states.size(), 1));

        // Add empty transition from new initial state to new final state.
        result.transitions.add(new Transition<>(0, n.states.size() + 1));

        result.final_state = n.states.size() + 1;
        return result;
    }

    /*
        concat() - Thompson algorithm for concatenation. Middle Precedence.
    */
    private NFA concat(NFA n, NFA m) {
        ///*
        m.states.remove(0); // delete m's initial state

        // copy NFA m's transitions to n, and handles connecting n & m
        for (Transition t : m.transitions) {
            int from = Integer.parseInt(t.getFromState().toString());
            int to = Integer.parseInt(t.getToState().toString());
            n.transitions.add(new Transition<>(from + n.states.size() - 1, to + n.states.size() - 1, t.getSymbol()));
        }

        // take m and combine to n after erasing initial m state
        for (Integer s : m.states) {
            n.states.add(s + n.states.size() + 1);
        }

        n.final_state = n.states.size() + m.states.size() - 2;
        return n;
    }

    /*
        union() - Lowest Precedence regular expression operator. Thompson
            algorithm for union (or).
    */
    private NFA union(NFA n, NFA m) {
        NFA result = new NFA(n.states.size() + m.states.size() + 2);

        // the branching of q0 to beginning of n
        result.transitions.add(new Transition<>(0, 1));

        // copy existing transitions of n
        for (Transition t : n.transitions) {
            int from = Integer.parseInt(t.getFromState().toString());
            int to = Integer.parseInt(t.getToState().toString());
            result.transitions.add(new Transition<>(from + 1, to + 1, t.getSymbol()));
        }

        // transition from last n to final state
        result.transitions.add(new Transition<>(n.states.size(), n.states.size() + m.states.size() + 1));

        // the branching of q0 to beginning of m
        result.transitions.add(new Transition<>(0, n.states.size() + 1));

        // copy existing transitions of m
        for (Transition t : m.transitions) {
            int from = Integer.parseInt(t.getFromState().toString());
            int to = Integer.parseInt(t.getToState().toString());
            result.transitions.add(new Transition<>(from + n.states.size() + 1, to + n.states.size() + 1, t.getSymbol()));
        }

        // transition from last m to final state
        result.transitions.add(new Transition<>(m.states.size() + n.states.size(), n.states.size() + m.states.size() + 1));

        // 2 new states and shifted m to avoid repetition of last n & 1st m
        result.final_state = n.states.size() + m.states.size() + 1;

        return result;
    }
}
