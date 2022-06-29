package main.model.regex;

import java.util.ArrayList;
import java.util.List;

public class ParsedRegex {

    private final String regex;
    private final List<Character> infixSequence;
    private List<Character> postfixSequence;

    public ParsedRegex(List<Character> sequence, String regex) {
        this.postfixSequence = new ArrayList<>();
        this.infixSequence = sequence;
        this.regex = regex;
    }

    public void setPostfixSequence(List<Character> postfixSequence) {
        this.postfixSequence = postfixSequence;
    }

    public List<Character> getPostfixSequence() {
        return postfixSequence;
    }

    public String getRegexString() {
        return regex;
    }

    public List<Character> getInfixSequence() {
        return infixSequence;
    }
}
