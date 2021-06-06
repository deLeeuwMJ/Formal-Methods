package main.operator.regex;

import javafx.util.Pair;

import java.util.SortedSet;

public class RegExpContains extends RegExpOperator {

    @Override
    public RegExpOperatorType getType() {
        return RegExpOperatorType.CONTAINS;
    }

    @Override
    public Pair<Boolean, SortedSet<String>> execute(String input, String match) {
        if (input.contains(match)) {
            return getPair(true, match);
        }
        return getNoResultFound();
    }
}