package main.operator.regex;

import javafx.util.Pair;

import java.util.SortedSet;

public class RegExpTotalLength extends RegExpOperator {

    @Override
    public RegExpOperatorType getType() {
        return RegExpOperatorType.TOTAL_LENGTH;
    }

    @Override
    public Pair<Boolean, SortedSet<String>> execute(String input, String match) {
        return getNoResultFound();
    }
}