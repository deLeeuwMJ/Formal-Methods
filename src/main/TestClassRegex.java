package main;

import main.logic.*;
import main.model.*;

import java.util.SortedSet;
import java.util.Stack;

public class TestClassRegex {

    public static void main(String[] args){
        RegexOperationSequence operationSequence = new RegExParser().parse("(a|bc)*");
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
//        AutomataSimulator automataSimulator = new AutomataSimulator();
//        boolean matches = automataSimulator.simulate(AutomataType.NFA, LanguageMode.ENDS, new ArrayList<>(words), "ca");
//        System.out.println("\r\nIs valid: " + matches);
    }
}
