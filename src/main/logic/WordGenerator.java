package main.logic;

import main.model.RegExp;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.Stack;

import static main.logic.InputValidator.*;

public class WordGenerator {

    private List<String> symbols;

    public WordGenerator() {
        this.symbols = new ArrayList<>();
    }

    public SortedSet<String> generate(Stack<String> postFixStack, int length) {
        RegExp finalExpression = new RegExp();
        Stack<RegExp> regexQueue = new Stack<>();
        symbols.clear();

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
            } else { // its a symbol
                addSymbol(val);
                regexQueue.push(new RegExp(val));
            }
        }

        return finalExpression.getLanguage(length);
    }

    private void addSymbol(String val) {
        if (!symbols.contains(val)){
            symbols.add(val);
        }
    }

    public List<String> getSymbols() {
        return symbols;
    }
}
