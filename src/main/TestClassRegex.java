package main;

import main.logic.*;
import main.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.Stack;

public class TestClassRegex {

    public static void main(String[] args){
        RegexOperationSequence operationSequence = new RegExParser().parse("(aa|b)*");
//        RegexOperationSequence operationSequence = new RegExParser().parse("(cb|a)+");
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
        System.out.println(wordGenerator.getTerminals());

        // Build expression tree based on postfix
        ExpressionTreeConstructor treeBuilder = new ExpressionTreeConstructor();
        Node root = treeBuilder.construct(postfixResult);
        treeBuilder.print(ExpressionTreeConstructor.PrintOrder.INORDER,root);

        // Build automata
        AutomataBuilder automataBuilder = new AutomataBuilder();
        Automata resultFA = automataBuilder.build(AutomataType.NFA, postfixResult, wordGenerator.getTerminals());
        System.out.println(resultFA.getTransitions());

        // Draw automata
        // Todo

        // Simulate automata
        AutomataSimulator automataSimulator = new AutomataSimulator();
        boolean matches = automataSimulator.simulate(AutomataType.NFA, LanguageMode.ENDS, new ArrayList<>(words), "ca");
        System.out.println("\r\nIs valid: " + matches);
    }
}
