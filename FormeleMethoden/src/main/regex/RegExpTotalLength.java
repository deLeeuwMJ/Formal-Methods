package main.regex;

public class RegExpTotalLength extends RegExpOperator {

    @Override
    RegExpOperatorType getType() {
        return RegExpOperatorType.TOTAL_LENGTH;
    }
}