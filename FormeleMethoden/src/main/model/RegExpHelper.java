package main.model;

import java.util.*;

public class RegExpHelper {

    private final List<String> alphabetList;
    private final List<String> startList;
    private final List<String> patternList;
    private final List<String> endList;
    private int MAX_STEPS = 3;

    public RegExpHelper(List<String> alphabetList, List<String> startList, List<String> patternList, List<String> endList) {
        this.alphabetList = alphabetList;
        this.startList = startList;
        this.patternList = patternList;
        this.endList = endList;
    }

    public String buildResult() {
        System.out.println("[BUILDER]----------------------------------------");
        StringBuilder builder = new StringBuilder();

        if (!isListEmpty(startList)) {
            builder.append(formatOutput(startList));
            if (isListEmpty(patternList)) builder.append(getFormattedAlphabet());
        }

        if (!isListEmpty(patternList)) {
            builder.append(getFormattedAlphabet());
            builder.append(formatOutput(patternList));
            builder.append(getFormattedAlphabet());
        }

        if (!isListEmpty(endList)) {
            if (isListEmpty(patternList) && isListEmpty(startList)) builder.append(getFormattedAlphabet());
            builder.append(formatOutput(endList));
        }

        return builder.toString();
    }

    private String getFormattedAlphabet() {
        return "(" + formatOutput(alphabetList) + ")*";
    }

    public List<String> generateAlphabet(List<String> list, int extraSteps) {
        List<String> wordList = new ArrayList<>();
        MAX_STEPS = list.size() + extraSteps;

        wordList.addAll(generateSameKind(list));
        wordList.addAll(generateDifferentKind(list));
        wordList.add("");
        Collections.sort(wordList);

        return wordList;
    }

    public List<String> generateDifferentKind(List<String> list) {
        String[] ml = list.toArray(new String[0]);

        for (int z = 0; z < MAX_STEPS - 1; z++) {
            Vector<String> tmp = new Vector<String>();

            for (int i = 0; i < list.size(); i++) {
                for (int k = 0; k < ml.length; k++) {
                    if (list.get(i) != ml[k]) {
                        tmp.add(ml[k] + list.get(i));
                    }
                }
            }

            ml = tmp.toArray(new String[tmp.size()]);
        }
        return Arrays.asList(ml);
    }

    public List<String> generateSameKind(List<String> list) {
        List<String> temp = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            String currentWord = "";

            for (int j = 0; j < MAX_STEPS; j++) {
                currentWord += list.get(i);
                temp.add(currentWord);
            }
        }
        return temp;
    }

    public List<String> generateWords(int steps) {
        System.out.println("[GENERATOR]----------------------------------------");
        List<String> alphabetPossibilitiesList = generateAlphabet(alphabetList, steps);
        List<String> generatedList = new ArrayList<>();

        if (!isListEmpty(startList)) {
            for (int i = 0; i < alphabetPossibilitiesList.size(); i++) {
                for (String s : startList) {
                    String word = s;
                    word += alphabetPossibilitiesList.get(i);
                    generatedList.add(word);
                }
            }
        }

        System.out.println(generatedList);

        if (!isListEmpty(patternList)) {
            List<String> list = new ArrayList<>();
            List<String> temp;

            if (generatedList.isEmpty() && isListEmpty(startList)) {
                temp = alphabetPossibilitiesList;
            } else {
                temp = generatedList;
            }

            for (String s : temp) {
                for (String p : patternList) {
                    String word = s;
                    word += p;
                    list.add(word);
                }
            }

            // After pattern
            List<String> temp2 = new ArrayList<>();
            if (isListEmpty(endList) || (!isListEmpty(startList) && !isListEmpty(endList))) {
                for (int i = 0; i < alphabetPossibilitiesList.size(); i++) {
                    for (String s : list) {
                        String word = s;
                        word += alphabetPossibilitiesList.get(i);
                        temp2.add(word);
                    }
                }
            } else {
                temp2.addAll(list);
            }

            generatedList.clear();
            generatedList.addAll(temp2);
        }

        System.out.println(generatedList);

        if (!isListEmpty(endList)) {
            List<String> list = new ArrayList<>();
            List<String> temp;

            if (generatedList.isEmpty() && isListEmpty(startList)) {
                temp = alphabetPossibilitiesList;
            } else {
                temp = generatedList;
            }

            for (String s : temp) {
                for (String e : endList) {
                    String word = s;
                    word += e;
                    list.add(word);
                }
            }

            generatedList.clear();
            generatedList.addAll(list);
        }

        System.out.println(generatedList);

        Collections.sort(generatedList);

        return generatedList;
    }

    private boolean isListEmpty(List<String> list) {
        return list.size() == 1 && "".equals(list.get(0));
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
