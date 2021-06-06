package main.operator.regex;

import javafx.util.Pair;

import java.util.SortedSet;

public class RegExpEquals extends RegExpOperator {

    @Override
    public RegExpOperatorType getType() {
        return RegExpOperatorType.EQUALS;
    }

    @Override
    public Pair<Boolean, SortedSet<String>> execute(String input, String match) {
        return getNoResultFound();
    }
}