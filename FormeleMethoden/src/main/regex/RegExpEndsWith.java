package main.regex;

public class RegExpEndsWith extends RegExpOperator {

    @Override
    RegExpOperatorType getType() {
        return RegExpOperatorType.ENDS;
    }
}