package main.logic;

import main.model.regex.RegExp;

import java.util.*;

import static main.logic.InputValidator.*;

public class WordGenerator {

    private final List<Character> symbols;

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

    public SortedSet<String> generateValidWords(Stack<Character> postFixStack, int length) {
        Stack<RegExp> regexQueue = new Stack<>();
        symbols.clear();

        for (char c : postFixStack) {
            if (isRegexOperator(c)) { // Is it an operator
                switch (c) {
                    case DOT_OPERATOR_SYMBOL:
                        RegExp tempDotRegex1 = regexQueue.pop();
                        RegExp tempDotRegex2 = regexQueue.pop();

                        regexQueue.push(tempDotRegex2.dot(tempDotRegex1));
                        break;
                    case OR_OPERATOR_SYMBOL:
                        RegExp tempOrRegex1 = regexQueue.pop();
                        RegExp tempOrRegex2 = regexQueue.pop();

                        regexQueue.push(tempOrRegex2.or(tempOrRegex1));
                        break;
                    case PLUS_OPERATOR_SYMBOL:
                        RegExp tempPlusRegex = regexQueue.pop();
                        regexQueue.push(tempPlusRegex.plus());
                        break;
                    case STAR_OPERATOR_SYMBOL:
                        RegExp tempStarRegex = regexQueue.pop();
                        regexQueue.push(tempStarRegex.star());
                        break;
                    default:
                        // Do nothing
                }
            } else { // its a symbol
                addSymbol(c);
                regexQueue.push(new RegExp(String.valueOf(c)));
            }
        }

        // There should be one item left in queue if users is performing multiple regex
        RegExp root = regexQueue.pop();

        return root.getLanguage(length);
    }

    // Depends on generateValidWords(); creates expression (Symbol1|Symbol2|Symbol3)*; steps based on given if its lower than use symbols size;
    public SortedSet<String> generateFaultyWords(SortedSet<String> validSet, int length){
        Stack<RegExp> regexQueue = new Stack<>();

        // Convert into regex
        for (char c : symbols){
            regexQueue.push(new RegExp(String.valueOf(c)));
        }

        // Perform OR on every symbol and then Plus it
        do{
           RegExp exp1 = regexQueue.pop();
           RegExp exp2 = regexQueue.pop();

           regexQueue.push(exp2.or(exp1));
        } while (regexQueue.size() != 1);

        // One should be left this is the final expression
        RegExp finalExp = regexQueue.pop();
        RegExp plusExp = finalExp.star();

        // Generate new valid set
        SortedSet<String> generatedSymbolsSet = plusExp.getLanguage(Math.max(length, symbols.size()));

        return getDifferenceInSet(validSet, generatedSymbolsSet);
    }

    private SortedSet<String> getDifferenceInSet(SortedSet<String> validSet, SortedSet<String> generatedSymbolsSet) {
       // compare the once that are similar and remove them
        SortedSet<String> invalidSet = new TreeSet<>();

        for (String word : generatedSymbolsSet){
            if (!validSet.contains(word)) invalidSet.add(word);
        }

        return invalidSet;
    }

    private void addSymbol(char val) {
        if (!symbols.contains(val)){
            symbols.add(val);
        }
    }

    public List<Character> getSymbols() {
        return symbols;
    }
}
