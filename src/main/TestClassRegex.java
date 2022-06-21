package main;

import main.logic.PostfixNotationParser;
import main.logic.RegExParser;
import main.model.RegExp;
import main.model.RegexOperationSequence;
import main.ui.LoggerBox;

import java.util.Stack;

public class TestClassRegex {

    public static void main(String[] args){
        postFixParser();
    }

    private static void postFixParser() {
        RegexOperationSequence operationSequence = new RegExParser().parse("a+b*c");
        if (operationSequence.failed()) {
            return;
        } else System.out.println(operationSequence.getSequence());

        // Parse into postfix notation to remove parenthesis
        PostfixNotationParser postfixParser = new PostfixNotationParser();
        Stack<String> postfixResult = postfixParser.parse(operationSequence);
        System.out.println(postfixResult);
    }

    public static void orTest(){
        // a | e
        RegExp a = new RegExp("a");
        RegExp e = new RegExp("e");
        RegExp exp = a.or(e);

        System.out.println(exp.getLanguage(3));

        RegExp plusOperation = exp.plus();

        System.out.println(plusOperation.getLanguage(3));
    }

    public static void dotTest(){
        // ae
        RegExp a = new RegExp("a");
        RegExp e = new RegExp("e");
        RegExp exp = a.dot(e);

        System.out.println(exp.getLanguage(3));

        RegExp plusOperation = exp.plus();

        System.out.println(plusOperation.getLanguage(3));
    }
}
