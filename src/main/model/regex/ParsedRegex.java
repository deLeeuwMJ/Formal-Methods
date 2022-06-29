package main.model.regex;

import java.util.List;

public class ParsedRegex {

    private final String regex;
    private final List<Character> sequence;

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
