package main.logic;

import main.model.Operator;
import main.model.RegexOperationSequence;

import java.util.Stack;

import static main.logic.InputValidator.isAlphabet;
import static main.logic.InputValidator.validRegEx;

public class RegExParser {

    // Empty list means not valid regex
    public RegexOperationSequence parse(String regex) {
        if (!validRegEx(regex)) {
            System.out.println("Invalid Regular Expression Input.");
            return new RegexOperationSequence();
        }

        char c;
        boolean isAbleToConcat = false;
        int parenthesisCounter = 0;

        Stack<String> concatStack = new Stack<String>();
        for (int i = 0; i < regex.length(); i++) {
            c = regex.charAt(i);

            if (isAlphabet(c)) {
                if (isAbleToConcat) { // concat this w/ previous
                    concatStack.push(Operator.DOT.name());
                } else isAbleToConcat = true;

                concatStack.push(String.valueOf(c));
            } else {
                if (c == ')') {
                    isAbleToConcat = false;
                    if (parenthesisCounter % 2 == 0){ // always need to be even parenthesis count
                        return new RegexOperationSequence();
                    } else parenthesisCounter--;
                    concatStack.push(Operator.RIGHT_PARENT.name());
                } else if (c == '*') {
                    concatStack.push(Operator.STAR.name());
                    isAbleToConcat = false;
                } else if (c == '+') {
                    concatStack.push(Operator.PLUS.name());
                    isAbleToConcat = false;
                } else if (c == '#'){
                    concatStack.push(Operator.SEPARATOR.name());
                    isAbleToConcat = false;
                } else if (c == '(') {
                    concatStack.push(Operator.LEFT_PARENT.name());
                    parenthesisCounter++;
                } else if (c == '|') {
                    concatStack.push(Operator.OR.name());
                    isAbleToConcat = false;
                }
            }
        }

        return new RegexOperationSequence(concatStack, regex);
    }
}
