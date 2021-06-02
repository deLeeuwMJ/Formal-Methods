package main.regex;

public class RegExpContains extends RegExpOperator {

    @Override
    RegExpOperatorType getType() {
        return RegExpOperatorType.CONTAINS;
    }
}