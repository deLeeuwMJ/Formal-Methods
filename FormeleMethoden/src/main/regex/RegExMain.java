package main.regex;

import main.regex.logic.RegExp;

import java.util.ArrayList;
import java.util.List;

public class RegExMain {

    public static void main(String[] args) {
        runBuilder();
        runGenerator();
    }

    private static void runBuilder() {
        System.out.println("[BUILDER]----------------------------------------");

        String alphabet = "ab"; // a of b of > |
        String start = "a"; // a ^of b of ab
        String pattern = "bab";
        String end = "a";

        if (!isValid(alphabet, start) || !isValid(alphabet, pattern) || !isValid(alphabet, end)) return;

        System.out.println("r = " + start + "(" + formatAlphabet(alphabet) + ")*" + pattern + "(" + formatAlphabet(alphabet) + ")*" + end);
    }

    private static void runGenerator() {
        System.out.println("[GENERATOR]----------------------------------------");
        Integer steps = 5;

        RegExp expr1, expr2, expr3, expr4, expr5, a, b, all;

        a = new RegExp("a");
        b = new RegExp("b");

//        // expr1: "baa"
//        expr1 = new RegExp("baa");
//        // expr2: "bb"
//        expr2 = new RegExp("bb");
//        // expr3: "baa | baa"
//        expr3 = expr1.or(expr2);

        // all: "(a|b)*"
        all = (a.or(b)).star();

//        // expr4: "(baa | baa)+"
//        expr4 = expr3.plus();
//        // expr5: "(baa | baa)+ (a|b)*"
//        expr5 = expr4.dot(all);


//        System.out.println("taal van (baa):\n" + expr1.getLanguage(steps));
//        System.out.println("taal van (bb):\n" + expr2.getLanguage(steps));
//        System.out.println("taal van (baa | bb):\n" + expr3.getLanguage(steps));

        System.out.println("taal van (a|b)*:\n" + all.getLanguage(steps));
//        System.out.println("taal van (baa | bb)+:\n" + expr4.getLanguage(steps));
//        System.out.println("taal van (baa | bb)+ (a|b)*:\n" + expr5.getLanguage(steps));
    }

    public static String formatOptions(List<String> list) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            if (i <= (list.size() - 2)) {
                builder.append(list.get(i)).append("|");
            } else {
                builder.append(list.get(i));
            }
        }

        return builder.toString();
    }

    public static String formatAlphabet(String input) {
        StringBuilder formattedString = new StringBuilder();
        char[] chars = input.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (chars.length - 1 == i) {
                formattedString.append(chars[i]);
            } else {
                formattedString.append(chars[i]).append("|");
            }
        }

        return formattedString.toString();
    }

    public static boolean isValid(String alphabet, String input) {
        if (alphabet.isEmpty()) return false;

        for (char i : input.toCharArray()) {
            if (!alphabet.contains(Character.toString(i))) return false;
        }

        List<String> alphabetList = new ArrayList<>();

        for (char i : alphabet.toCharArray()) {
            if (alphabetList.contains(Character.toString(i))) {
                return false;
            } else alphabetList.add(String.valueOf(i));
        }

        return true;
    }
}
