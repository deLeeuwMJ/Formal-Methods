package main;

import main.logic.*;
import main.model.*;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.Stack;

public class DebugClass {

    public static void main(String[] args){
        RegexOperationSequence operationSequence = new RegExParser().parse("(a|b)*");
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

        // Build expression tree based on postfix
        ExpressionTreeConstructor treeBuilder = new ExpressionTreeConstructor();
        Node root = treeBuilder.construct(postfixResult);
        String result = treeBuilder.constructString(ExpressionTreeConstructor.PrintOrder.INORDER,root);
        System.out.println(result);

        // Build automata
        AutomataBuilder automataBuilder = new AutomataBuilder();
        Automata resultFA = automataBuilder.build(AutomataType.NFA, postfixResult, wordGenerator.getSymbols());

        // Convert NFA to DFA
        NFAtoDFAConverter nfaConverter = new NFAtoDFAConverter();
        nfaConverter.convert(resultFA);

        // Simulate automata
        AutomataSimulator automataSimulator = new AutomataSimulator();
        boolean matches = automataSimulator.simulate(LanguageMode.START, words, "aabc");
        System.out.println("Is valid: " + matches);
    }
}
