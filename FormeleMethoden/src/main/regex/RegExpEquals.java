package main.regex;

public class RegExpEquals extends RegExpOperator {

    @Override
    RegExpOperatorType getType() {
        return RegExpOperatorType.EQUALS;
    }
}