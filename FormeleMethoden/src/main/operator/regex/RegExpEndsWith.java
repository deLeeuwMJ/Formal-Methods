package main.operator.regex;

import javafx.util.Pair;
import main.common.RegExp;

import java.util.SortedSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpEndsWith extends RegExpOperator {

    @Override
    public RegExpOperatorType getType() {
        return RegExpOperatorType.ENDS;
    }

    @Override
    public Pair<Boolean, SortedSet<String>> execute(String input, String match) {
        RegExp regMatch = new RegExp(match);
        SortedSet<String> availableMatches = regMatch.dollar().getLanguage(MAX_STEPS);

        System.out.println("User input: " + input);
        System.out.println("Searching for: " + match);
        System.out.println("Available matches: " + availableMatches);
        System.out.println("--------------------");

        String[] matchArr = availableMatches.toArray(new String[availableMatches.size()]);
        Pattern pattern = Pattern.compile(matchArr[1], Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        boolean matchFound = matcher.find();

        if (matchFound) return getPair(matchFound, availableMatches);

        return getNoResultFound();
    }
}