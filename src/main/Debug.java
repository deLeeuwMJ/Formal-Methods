package main;

import main.logic.*;
import main.model.*;

import java.util.SortedSet;
import java.util.Stack;

public class Debug {

    public static void main(String[] args) {
        // Validate input
        ParsedRegex parsedRegex = new RegExParser().parse("(ab)*(c|de)+");
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



        // Build automata based on postfix expression
//        AutomataBuilder automataBuilder = new AutomataBuilder();
//        Automata resultNFA = automataBuilder.build(AutomataType.NFA, postfixResult, wordGenerator.getSymbols());
//        System.out.println(resultNFA.getTransitions());

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
