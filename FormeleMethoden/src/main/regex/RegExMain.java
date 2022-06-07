package main.regex;

import java.util.ArrayList;
import java.util.List;

public class RegExMain {

    public static void main(String[] args) {
        runBuilder();
    }

    private static void runBuilder() {
        System.out.println("[BUILDER]----------------------------------------");

        String alphabet = "ab"; // a of b of > |
        String start = "a"; // a ^of b of ab
        String pattern = "bab";
        String end = "a";

        if (!isValid(alphabet, start) || !isValid(alphabet, pattern) || !isValid(alphabet, end)) return;

        System.out.println("r = " + start + "(" + formatOutput(extractIntoCharList(alphabet)) + ")*" + pattern + "(" + formatOutput(extractIntoCharList(alphabet)) + ")*" + end);
    }

    // Return empty list on double occurrence
    public static List<String> extractIntoCharList(String text) {
        List<String> list = new ArrayList<>();

        for (char c : text.toCharArray()){
            list.add(String.valueOf(c));
        }

        return list;
    }

    public static String formatOutput(List<String> list) {
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
