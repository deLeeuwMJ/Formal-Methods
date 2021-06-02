package main.regex;

public class RegExpStartsWith extends RegExpOperator {

    @Override
    RegExpOperatorType getType() {
        return RegExpOperatorType.STARTS;
    }
}
