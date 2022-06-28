package main.model;

import java.util.List;

public class ParsedRegex {

    private String regex;
    private List<Character> sequence;

    public ParsedRegex(List<Character> sequence, String regex) {
        this.sequence = sequence;
        this.regex = regex;
    }

    public String getRegexString() {
        return regex;
    }

    public List<Character> getSequence() {
        return sequence;
    }
}
