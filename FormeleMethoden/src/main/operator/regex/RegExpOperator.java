package main.operator.regex;

import javafx.util.Pair;
import main.operator.DefaultOperator;

import java.util.SortedSet;
import java.util.TreeSet;

public abstract class RegExpOperator extends DefaultOperator {

    private final SortedSet<String> set = new TreeSet<>();
    final Integer MAX_STEPS = 5;

    RegExpOperatorType getType() {
        return RegExpOperatorType.STARTS;
    }

    public abstract Pair<Boolean, SortedSet<String>> execute(String input, String match);

    final Pair<Boolean, SortedSet<String>> getPair(Boolean success, String string){
        addToSet(string);
        return new Pair<>(success, set);
    }

    final Pair<Boolean, SortedSet<String>> getPair(Boolean success, SortedSet<String> set){
        return new Pair<>(success, set);
    }

    final Pair<Boolean, SortedSet<String>> getNoResultFound(){
        addToSet("no matches");
        return new Pair<>(false, set);
    }

    private void addToSet(String string){
        set.clear();
        set.add(string);
    }
}
