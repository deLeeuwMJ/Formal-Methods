package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

public class RegExMain {

    public static void main(String[] args) {
        String alphabet = "ab"; // a of b of > |
        String start = "a"; // a ^of b of ab
        String pattern = "bab";
        String end = "a";

        if (!isValid(alphabet, start)) return;

//        if (!isValid(alphabet, start) || !isValid(alphabet, pattern) || !isValid(alphabet, end)) return;

        System.out.println("r = " + start + "(" + formatAlphabet(alphabet) + ")*" + pattern + "(" + formatAlphabet(alphabet) + ")*" + end);
    }

    //todo add row text for start pattern and end

    //todo split alphabet with '|'
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

    //todo contains alphabet in every string
    public static boolean isValid(String alphabet, String input) {
        if (alphabet.isEmpty()) return false;

        for (char i : input.toCharArray()) {
            if (!alphabet.contains(Character.toString(i))) {
                System.out.println("Not in alphabet!");
                return false;
            }
        }

        List<String> alphabetlist = new ArrayList<>();

        for (char i : alphabet.toCharArray()) {
            System.out.println("Before: " + alphabetlist);
            if (alphabetlist.contains(Character.toString(i))) {
                System.out.println("Char already in alphabet!");
                return false;
            }
            else {
                alphabetlist.add(String.valueOf(i));
                System.out.println("After: " + alphabetlist);

            }
            System.out.println("------------------");
        }

        return true;
    }
}
