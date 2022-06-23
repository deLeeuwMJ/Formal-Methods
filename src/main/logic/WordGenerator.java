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

    /*
        POSTFIX [a, b, c, DOT, OR, STAR]
        STACK a > a,b > a,b,c
        DOT
        Reg1 = c (pop)
        Reg2 = b (pop)
        RegDot = reg2.dot(reg1);
        push STACK bc > a, bc
        reg1 = bc (pop)
        reg2 = a (pop)
        RegOr = reg2.or(reg1)
        push stack regOr
        reg1 = a|bc (pop)
        regStar = reg1.star
    */

    public SortedSet<String> generate(Stack<String> postFixStack, int length) {
        Stack<RegExp> regexQueue = new Stack<>();
        symbols.clear();

        for (String val : postFixStack) {
            if (isOperator(val)) { // Is it an operator
                switch (getOperator(val)) {
                    case DOT:
                        RegExp tempDotRegex1 = regexQueue.pop();
                        RegExp tempDotRegex2 = regexQueue.pop();

                        regexQueue.push(tempDotRegex2.dot(tempDotRegex1));
                        break;
                    case OR:
                        RegExp tempOrRegex1 = regexQueue.pop();
                        RegExp tempOrRegex2 = regexQueue.pop();

                        regexQueue.push(tempOrRegex2.or(tempOrRegex1));
                        break;
                    case PLUS:
                        RegExp tempPlusRegex = regexQueue.pop();
                        regexQueue.push(tempPlusRegex.plus());
                        break;
                    case STAR:
                        RegExp tempStarRegex = regexQueue.pop();
                        regexQueue.push(tempStarRegex.star());
                        break;
                    default:
                        // Do nothing
                }
            } else { // its a symbol
                addSymbol(val);
                regexQueue.push(new RegExp(val));
            }
        }

        // There should be one item left in queue
        RegExp finalExpression = regexQueue.pop();

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
