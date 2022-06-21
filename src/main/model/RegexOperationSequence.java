package main.model;

import java.util.ArrayList;
import java.util.List;

public class RegexOperationSequence {

    private String regex;
    private List<String> sequence;

    public RegexOperationSequence() {
        sequence = new ArrayList<>();
        regex = null;
    }

    public RegexOperationSequence(List<String> sequence, String regex) {
        this.sequence = sequence;
        this.regex = regex;
    }

    public void addOperation(String op){
        sequence.add(op);
    }

    public String getRegexString() {
        return regex;
    }

    public List<String> getSequence() {
        return sequence;
    }

    public boolean failed() {
        return sequence.isEmpty();
    }
}
