package main;

import main.logic.*;
import main.model.automata.*;
import main.model.regex.ParsedRegex;

import java.util.SortedSet;
import java.util.Stack;

public class Debug {

    public static void main(String[] args) {
        // Validate input
//        ParsedRegex parsedRegex = new RegExParser().parse("(ab)*(c|de)+");
        ParsedRegex parsedRegex = new RegExParser().parse("ab");
        if (parsedRegex == null) return;
        System.out.println(parsedRegex.getSequence());

        // Parse into postfix notation to remove parenthesis
        PostfixNotationParser postfixParser = new PostfixNotationParser();
        Stack<Character> postfixResult = postfixParser.parse(parsedRegex);
        System.out.println(postfixResult);

        // Generate (in)valid words with postfix
        WordGenerator wordGenerator = new WordGenerator();
        SortedSet<String> validWords = wordGenerator.generateValidWords(postfixResult, 5);
        SortedSet<String> invalidWords = wordGenerator.generateFaultyWords(validWords, 5);
        System.out.println(validWords);

        // Build Automata
        AutomataBuilder automataBuilder = new AutomataBuilder();
        FA fa = automataBuilder.build(AutomataType.NDFA, postfixResult);
        fa.printTransitions();

        // Check if valid
        if (fa instanceof DFA) {
            DFA dfa = (DFA) fa;
            System.out.println(dfa.isAccepted("aab"));
        }
    }
}
