package main;

import main.logic.*;
import main.model.*;

import java.util.SortedSet;
import java.util.Stack;

public class Debug {

    public static void main(String[] args) {
        // Validate input
                ParsedRegex parsedRegex = new RegExParser().parse("(ab)*(c|de)+");
//        ParsedRegex parsedRegex = new RegExParser().parse("(ab)*(c|de)+");
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

        // Generate NDFA
        NDFA tempNDFA = new NDFA();
        tempNDFA.addTransition(new Transition("q1", "q4", "a"));
        tempNDFA.addTransition(new Transition("q1", "q2", "b"));

        tempNDFA.addTransition(new Transition("q2", "q4", "a"));
        tempNDFA.addTransition(new Transition("q2", "q1", "b"));
        tempNDFA.addTransition(new Transition("q2", "q3", "b"));
        tempNDFA.addTransition(new Transition("q2", "q3", "ε"));

        tempNDFA.addTransition(new Transition("q3", "q5", "a"));
        tempNDFA.addTransition(new Transition("q3", "q5", "b"));

        tempNDFA.addTransition(new Transition("q4", "q2", "ε"));
        tempNDFA.addTransition(new Transition("q4", "q3", "a"));

        tempNDFA.addTransition(new Transition("q5", "q4", "a"));
        tempNDFA.addTransition(new Transition("q5", "q1", "b"));

        tempNDFA.addStartState("q1");
        tempNDFA.addEndState("q4");

        // Convert NDFA to DFA



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
