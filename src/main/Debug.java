package main;

import main.logic.*;
import main.model.automata.DFA;
import main.model.automata.FA;
import main.model.automata.NDFA;
import main.model.automata.Transition;
import main.model.regex.ParsedRegex;

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
//        FA tempNDFA = new NDFA();
//        tempNDFA.addTransition(new Transition("q1", "q4", "a"));
//        tempNDFA.addTransition(new Transition("q1", "q2", "b"));
//
//        tempNDFA.addTransition(new Transition("q2", "q4", "a"));
//        tempNDFA.addTransition(new Transition("q2", "q1", "b"));
//        tempNDFA.addTransition(new Transition("q2", "q3", "b"));
//        tempNDFA.addTransition(new Transition("q2", "q3", "ε"));
//
//        tempNDFA.addTransition(new Transition("q3", "q5", "a"));
//        tempNDFA.addTransition(new Transition("q3", "q5", "b"));
//
//        tempNDFA.addTransition(new Transition("q4", "q2", "ε"));
//        tempNDFA.addTransition(new Transition("q4", "q3", "a"));
//
//        tempNDFA.addTransition(new Transition("q5", "q4", "a"));
//        tempNDFA.addTransition(new Transition("q5", "q1", "b"));
//
//        tempNDFA.addStartState("q1");
//        tempNDFA.addEndState("q4");
//
//        tempNDFA.printTransitions();

        DFA dfa = new DFA();
        dfa.addTransition(new Transition("q1", "q2", "a"));
        dfa.addTransition(new Transition("q1", "q1", "b"));
        dfa.addTransition(new Transition("q2", "q3", "a"));
        dfa.addTransition(new Transition("q2", "q1", "b"));
        dfa.addTransition(new Transition("q3", "q2", "a"));
        dfa.addTransition(new Transition("q3", "q4", "b"));
        dfa.addTransition(new Transition("q4", "q4", "a"));
        dfa.addTransition(new Transition("q4", "q4", "b"));
        dfa.addStartState("q1");
        dfa.addEndState("q4");
        System.out.println(dfa.isAccepted("ccb"));

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
