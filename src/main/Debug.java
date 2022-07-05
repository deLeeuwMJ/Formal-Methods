package main;

import main.logic.AutomataBuilder;
import main.logic.PostfixNotationParser;
import main.logic.RegExParser;
import main.logic.WordGenerator;
import main.model.LanguageMode;
import main.model.automata.AutomataType;
import main.model.automata.DFA;
import main.model.automata.FA;
import main.model.regex.ParsedRegex;

import java.util.LinkedHashSet;
import java.util.SortedSet;

public class Debug {

    public static void main(String[] args) {
        String input = "aab";
        String sLetters = generateOrKleeneFromLetter(input);
        input = input + sLetters; //r aab(a|b)*

        // Validate input
//        ParsedRegex parsedRegex = new RegExParser().parse("(ab)*(c|de)+");
        ParsedRegex parsedRegex = new RegExParser().parse(input);
        if (parsedRegex == null) return;
        System.out.println(parsedRegex.getInfixSequence());

        // Parse into postfix notation to remove parenthesis
        PostfixNotationParser postfixParser = new PostfixNotationParser();
        parsedRegex.setPostfixSequence(postfixParser.parse(parsedRegex));
        System.out.println(parsedRegex.getPostfixSequence());

        // Generate (in)valid words with postfix
        WordGenerator wordGenerator = new WordGenerator();
        SortedSet<String> validWords = wordGenerator.generateValidWords(parsedRegex, 5);
        SortedSet<String> invalidWords = wordGenerator.generateFaultyWords(validWords, 5);
        System.out.println(validWords);

        // Build Automata
        AutomataBuilder automataBuilder = new AutomataBuilder();
        FA fa = automataBuilder.build(AutomataType.DFA, LanguageMode.NONE, parsedRegex);
        fa.printTransitions();

        // Check if valid
        if (fa instanceof DFA) {
            DFA dfa = (DFA) fa;
            System.out.println(dfa.isAccepted("abaab"));
        }
    }

    private static String generateOrKleeneFromLetter(String input) {
        StringBuilder builder = new StringBuilder("(");
        LinkedHashSet<Character> seenLetters = new LinkedHashSet<>();

        for (int i = 0; i < input.length(); i++) {
            char inputChar = input.charAt(i);
            String inputCharString = String.valueOf(inputChar);

            if (!seenLetters.contains(inputCharString)) {
                if (seenLetters.add(inputChar)){
                    seenLetters.add(inputChar);
                    builder.append(inputCharString);
                    if (i < input.length() - 1) builder.append("|");
                }
            }
        }

        builder.append(")*");
        return builder.toString();
    }
}
