package main.logic;

import main.model.Operator;
import main.model.RegExp;

import java.util.ArrayList;
import java.util.List;

public class RegexBuilder {

    private final List<String> terminalBuffer;
    private String regexString;
    private RegExp lastRegex;

    public RegexBuilder() {
        terminalBuffer = new ArrayList<>();
    }

    // todo fix concat issue
    public RegExp build(List<String> operations) {
        boolean parentFlag = false;
        Operator lastOperator = null;

        terminalBuffer.clear();
        String concatBuffer = "";
        regexString = "";

        lastRegex = new RegExp();

        for (int i = 0; i < operations.size(); i++) {
            String indexValue = operations.get(i);

            boolean isOperator = false;
            Operator tempOperator = null;

            try {
                tempOperator = Operator.valueOf(indexValue);
                isOperator = true;
            } catch (IllegalArgumentException e) {
                // Do nothing
            }

            if (isOperator) {
                switch (tempOperator) {  // Operators that don't require next value
                    case LEFT_PARENT:
                        parentFlag = true;
                        break;
                    case RIGHT_PARENT:
                        parentFlag = false;
                        break;
                    case PLUS:  // Eg baa+"
                        lastRegex = lastRegex.plus();
                        regexString = "(" + regexString + ")+";
                        break;
                    case STAR:  // Eg "(a|b)*"
                        lastRegex = lastRegex.star();
                        regexString = "(" + regexString + ")*";
                        break;
                }
                lastOperator = tempOperator;
            } else { // Is terminal
                if (lastOperator != null) {
                    switch (lastOperator) { // Operators that do require next value
                        case OR:    // Eg "baa | bb"
                            lastRegex = lastRegex.or(new RegExp(indexValue));
                            regexString = regexString + "|" + indexValue;
                            break;
                        case DOT:   // Eg "b.a"
                            lastRegex = lastRegex.dot(new RegExp(indexValue));
                            regexString = regexString + indexValue;
                            break;
                    }
                }

                if ((i + 1) < operations.size()) {
                    if (Operator.valueOf(operations.get(i + 1)) != Operator.DOT) { // proceed if it isn't a concat operation
                        String concatString = concatBuffer + indexValue;
                        terminalBuffer.add(concatString);
                        concatBuffer = "";
                    } else {
                        if (regexString.length() == 0) {
                            lastRegex = new RegExp(indexValue);
                            regexString += indexValue; // Needed to fix issue with first char on new Regex
                        }
                        concatBuffer += indexValue;
                    }
                }
            }
        }

        return lastRegex;
    }

    public String getString() {
        return regexString;
    }

    public List<String> getTerminals() {
        return terminalBuffer;
    }
}
