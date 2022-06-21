package main.logic;

import main.model.Operator;
import main.model.RegexOperationSequence;

import java.util.List;
import java.util.Stack;

import static main.logic.InputValidator.getOperator;
import static main.logic.InputValidator.isOperator;

public class PostfixNotationParser {

    public Stack<String> parse (RegexOperationSequence operations) {
        List<String> sequence = operations.getSequence();

        Stack<String> outputQueue = new Stack<>();
        Stack<Operator> operatorQueue = new Stack<>();

        /*
        Using Shunt-Yard Algorithm to get rid of the parenthesis and convert into postfix
        Algorithm created based on visual example found at
        https://blog.cernera.me/converting-regular-expressions-to-postfix-notation-with-the-shunting-yard-algorithm/
        Postfix explanation: https://runestone.academy/ns/books/published/pythonds/BasicDS/InfixPrefixandPostfixExpressions.html
        */
        for (int i = 0; i < sequence.size(); i++) {
            String val = operations.getSequence().get(i);

            if (isOperator(val)) {
                Operator currentOperator = getOperator(val);

                if (currentOperator == Operator.RIGHT_PARENT) {
                    // pop everything in operator queue till left parenthesis
                    boolean leftParentFound = false;

                    do {
                        Operator currOperatorInStack = operatorQueue.pop();

                        if (currOperatorInStack == Operator.LEFT_PARENT) {
                            leftParentFound = true;
                        } else {
                            outputQueue.push(currOperatorInStack.name());
                        }

                    } while (!leftParentFound);
                } else if (currentOperator == Operator.SEPARATOR && !operatorQueue.isEmpty()) {
                    boolean prevSeparatorFound = false;

                    do {
                        Operator currOperatorInStack = operatorQueue.pop();

                        outputQueue.push(currOperatorInStack.name());
                        if (currOperatorInStack == Operator.SEPARATOR) {
                            prevSeparatorFound = true;
                            operatorQueue.push(currentOperator);
                        }

                    } while (!prevSeparatorFound);
                } else {
                    operatorQueue.push(currentOperator);
                }
            } else {
                outputQueue.push(val);
            }
        }

        // Remove last operator from operator stack which should be separator
        outputQueue.push(operatorQueue.pop().name());

        return outputQueue;
    }
}
