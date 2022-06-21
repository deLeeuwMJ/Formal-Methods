package main.logic;

import main.model.RegExp;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.Stack;

import static main.logic.InputValidator.*;

public class WordGenerator {

    private List<String> terminals;

    public WordGenerator() {
        this.terminals = new ArrayList<>();
    }

    public SortedSet<String> generate(Stack<String> postFixStack, int length) {
        RegExp finalExpression = new RegExp();
        Stack<RegExp> regexQueue = new Stack<>();
        terminals.clear();

        for (int i = 0; i < postFixStack.size(); i++) {
            String val = postFixStack.get(i);

            if (isOperator(val)) { // Is it an operator
                switch (getOperator(val)){
                    case DOT:
                        RegExp tempDotRegex = regexQueue.pop();

                        while (!regexQueue.isEmpty()){
                            tempDotRegex = regexQueue.pop().dot(tempDotRegex);
                        }
                        finalExpression = tempDotRegex.or(finalExpression);
                        break;
                    case OR:
                        RegExp tempOrRegex = regexQueue.pop();

                        while (!regexQueue.isEmpty()){
                            tempOrRegex = tempOrRegex.or(regexQueue.pop());
                        }
                        finalExpression = tempOrRegex.or(finalExpression);
                        break;
                    case PLUS:
                        finalExpression = finalExpression.plus();
                        break;
                    case STAR:
                        finalExpression = finalExpression.star();
                        break;

                }
            } else { // its a terminal
                addTerminal(val);
                regexQueue.push(new RegExp(val));
            }
        }

        return finalExpression.getLanguage(length);
    }

    private void addTerminal(String val) {
        if (!terminals.contains(val)){
            terminals.add(val);
        }
    }

    public List<String> getTerminals() {
        return terminals;
    }
}
