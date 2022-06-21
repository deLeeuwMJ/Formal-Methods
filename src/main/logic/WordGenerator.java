package main.logic;

import main.model.RegExp;

import java.util.SortedSet;
import java.util.Stack;

import static main.logic.InputValidator.*;

public class WordGenerator {

    public SortedSet<String> generate(Stack<String> postFixStack, int length) {
        RegExp finalExpression = new RegExp();

        Stack<RegExp> regexQueue = new Stack<>();

        for (int i = 0; i < postFixStack.size(); i++) {
            String val = postFixStack.get(i);

            if (isOperator(val)) { // Is it an operator
                switch (getOperator(val)){
                    case DOT:
                        RegExp tempDotRegex = regexQueue.pop();

                        while (!regexQueue.isEmpty()){
                            tempDotRegex = tempDotRegex.dot(regexQueue.pop());
                        }
                        finalExpression = finalExpression.or(tempDotRegex);
                        break;
                    case OR:
                        RegExp tempOrRegex = regexQueue.pop();

                        while (!regexQueue.isEmpty()){
                            tempOrRegex = tempOrRegex.or(regexQueue.pop());
                        }
                        finalExpression = finalExpression.or(tempOrRegex);
                        break;
                    case STAR:
                        finalExpression = finalExpression.star();
                        break;
                    case PLUS:
                        finalExpression = finalExpression.plus();
                        break;
                }
            } else { // its a terminal
                regexQueue.push(new RegExp(val));
            }
        }

        return finalExpression.getLanguage(length);
    }
}
