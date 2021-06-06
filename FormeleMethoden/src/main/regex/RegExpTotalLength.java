package main.regex;

public class RegExpTotalLength extends RegExpOperator {


    @Override
    RegExpOperatorType getType() {
        return RegExpOperatorType.TOTAL_LENGTH;
    }


    public void GetLength(String[] regex) {
        int length = 0;

        for (String words : regex)
        {
            length = words.length();
            System.out.println(length);
        }
    }
}