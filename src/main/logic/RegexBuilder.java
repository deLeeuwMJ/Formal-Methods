package main.logic;

import main.model.ExecutionResult;
import main.model.Operator;
import main.model.RegExp;

public class RegexBuilder {

    private String regexString;
    private RegExp lastRegex;
    private Operator currOperator;

    public RegexBuilder() {
        lastRegex = new RegExp();
        regexString = "";
    }

    public ExecutionResult setOperator(String val) {
        Operator operator;

        try {
            operator = Operator.valueOf(val);
        } catch (NullPointerException e) {
            return ExecutionResult.FAILED;
        }

        currOperator = operator;
        return ExecutionResult.PASSED;
    }

    private boolean isInvalidPrevRegex() {
        return lastRegex.getLanguage(3).isEmpty();
    }

    public void reset(){
        regexString = "";
        currOperator = null;
        lastRegex = new RegExp();
    }

    public void init(String terminals) {
        RegExp newRegex = new RegExp(terminals);

        switch (currOperator) {
            case PLUS:  // expr: baa+"
                if (isInvalidPrevRegex()) break;
                lastRegex = lastRegex.plus();
                regexString = "(" + regexString + ")+";
                break;
            case STAR:  // expr: "(a|b)*"
                if (isInvalidPrevRegex()) break;
                lastRegex = lastRegex.star();
                regexString = "(" + regexString + ")*";
                break;
            case OR:    // expr: "baa | bb"
                if (isInvalidPrevRegex()) break;
                lastRegex = lastRegex.or(newRegex);
                regexString = regexString + "|" + terminals;
                break;
            case DOT:   // expr: "b.a"
                if (isInvalidPrevRegex()) break;
                lastRegex = lastRegex.dot(newRegex);
                regexString = regexString + terminals;
                break;
            case ONE:   // expr: "baa"
                lastRegex = newRegex;
                regexString = terminals;
                break;
        }
    }

    public RegExp get() {
        return lastRegex;
    }

    public String getString() {
        return regexString;
    }

    public Operator getOperator() {
        return currOperator;
    }
}
