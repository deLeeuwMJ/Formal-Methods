package main.common;

import javafx.util.Pair;
import main.operator.regex.*;
import java.util.SortedSet;

public class RegExpHandler {

    public Pair<Boolean, SortedSet<String>> handle(RegExpOperatorType operatorType, String input, String match) {
        RegExpOperator operator = null;

        switch (operatorType) {
            case STARTS:
                operator = new RegExpStartsWith();
                break;
            case ENDS:
                operator = new RegExpEndsWith();
                break;
            case CONTAINS:
                operator = new RegExpContains();
                break;
            case EQUALS:
                operator = new RegExpEquals();
                break;
            case TOTAL_LENGTH:
                operator = new RegExpTotalLength();
                break;
            default:
                System.out.println("Not a valid operator");
        }

        if (operator == null) return null;
        return operator.execute(input, match);
    }
}
