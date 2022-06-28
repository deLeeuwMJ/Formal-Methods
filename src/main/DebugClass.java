package main;

import main.logic.*;
import main.model.*;

import java.util.SortedSet;
import java.util.Stack;

public class DebugClass {

    public static void main(String[] args) {
        // Validate input
        ParsedRegex parsedRegex = new RegExParser().parse("(ae)*(bc|d|fg)+");

        if (parsedRegex == null) return;
        System.out.println(parsedRegex.getSequence());

        // Parse into postfix notation to remove parenthesis
        PostfixNotationParser postfixParser = new PostfixNotationParser();
        Stack<Character> postfixResult = postfixParser.parse(parsedRegex);
        System.out.println(postfixResult);

        // Generate (in)valid words with postfix
        WordGenerator wordGenerator = new WordGenerator();
        SortedSet<String> validWords = wordGenerator.generateValidWords(postfixResult, 5);
        System.out.println(validWords);

        SortedSet<String> invalidWords = wordGenerator.generateFaultyWords(validWords, 5);
        System.out.println(invalidWords);

        // Build automata
//        AutomataBuilder automataBuilder = new AutomataBuilder();
//        Automata resultFA = automataBuilder.build(AutomataType.NFA, postfixResult, wordGenerator.getSymbols());

//        // Convert NFA to DFA
//        NFAtoDFAConverter nfaConverter = new NFAtoDFAConverter();
//        nfaConverter.convert(resultFA);
//
//        // Simulate automata
//        AutomataSimulator automataSimulator = new AutomataSimulator();
//        boolean matches = automataSimulator.simulate(LanguageMode.START, validWords, "aabc");
//        System.out.println("Is valid: " + matches);
    }
}
