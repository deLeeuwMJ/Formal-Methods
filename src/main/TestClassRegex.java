package main;

import main.logic.PostfixNotationParser;
import main.logic.RegExParser;
import main.logic.WordGenerator;
import main.model.RegExp;
import main.model.RegexOperationSequence;

import java.util.List;
import java.util.SortedSet;
import java.util.Stack;

public class TestClassRegex {

    public static void main(String[] args){
        postFixParser();
    }

    private static void postFixParser() {
        RegexOperationSequence operationSequence = new RegExParser().parse("(aa|b)+");
        if (operationSequence.failed()) {
            return;
        } else System.out.println(operationSequence.getSequence());

        // Parse into postfix notation to remove parenthesis
        PostfixNotationParser postfixParser = new PostfixNotationParser();
        Stack<String> postfixResult = postfixParser.parse(operationSequence);
        System.out.println(postfixResult);

        // Generate words with postfix
        WordGenerator wordGenerator = new WordGenerator();
        SortedSet<String> words = wordGenerator.generate(postfixResult, 5);
        System.out.println(words);
    }
}
