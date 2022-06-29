package main.logic;

import main.model.regex.ParsedRegex;

import java.util.ArrayList;
import java.util.List;

import static main.logic.InputValidator.*;

public class RegExParser {

    public ParsedRegex parse(String regex) {

        // Check input symbols
        if (!validRegEx(regex)) {
            System.out.println("Invalid Regular Expression Symbol Input.");
            return null;
        }

        // Check parenthesis
        if (!evenAmountOfParenthesis(regex)){
            System.out.println("Invalid Regular Expression Parenthesis Input.");
            return null;
        }

        return new ParsedRegex(join(regex.toCharArray()), regex);
    }

    private List<Character> join(char[] regexArr){
        List<Character> result = new ArrayList<>();

        for (int i = 0; i < regexArr.length - 1; i++) {
            char first = regexArr[i];
            char second = regexArr[i + 1];

            result.add(first);
            if (first != '(' && first != OR_OPERATOR_SYMBOL && isAlphabet(second)) {
                result.add(DOT_OPERATOR_SYMBOL);
            } else if (second == '(' && first != OR_OPERATOR_SYMBOL && first != '(') {
                result.add(DOT_OPERATOR_SYMBOL);
            }
        }

        result.add(regexArr[regexArr.length-1]);

        return result;
    }
}
