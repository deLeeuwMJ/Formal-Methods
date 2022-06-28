package test;

import main.model.RegExp;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class WordGeneratorTest {

    @Test
    public void parsePlainPlus()
    {
        // (a|b)+
        RegExp step1 = new RegExp("a");
        RegExp step2 = new RegExp("b");
        RegExp step3 = step2.or(step1);
        RegExp step4 = step3.plus();

        assertEquals("[a, b, aa, ab, ba, bb, aaa, aab, aba, abb, baa, bab, bba, bbb]", step4.getLanguage(3).toString());
    }

    @Test
    public void parsePlainDoubleCharPlus()
    {
        // (a|bc)+
        RegExp step1 = new RegExp("a");
        RegExp step2 = new RegExp("b");
        RegExp step3 = new RegExp("c");
        RegExp step4 = step2.dot(step3);
        RegExp step5 = step1.or(step4);
        RegExp step6 = step5.plus();

        assertEquals("[a, aa, bc, aaa, abc, bca, aaaa, aabc, abca, bcaa, bcbc, aaabc, aabca, abcaa, abcbc, bcaaa, bcabc, bcbca, aabcbc, abcabc, abcbca, bcaabc, bcabca, bcbcaa, bcbcbc, abcbcbc, bcabcbc, bcbcabc, bcbcbca, bcbcbcbc]", step6.getLanguage(4).toString());
    }

    @Test
    public void parseUsingPostfixStar()
    {
//        RegExParser regExParser = new RegExParser();
//        PostfixNotationParser postfixParser = new PostfixNotationParser();
//        WordGenerator wordGenerator = new WordGenerator();
//
//        RegexOperationSequence sequence = regExParser.parse("(a|b)*");
//        Stack<String> result = postfixParser.parse(sequence);
//
//        SortedSet<String> words = wordGenerator.generateValidWords(result, 4);
//
//        assertEquals("[, a, b, aa, ab, ba, bb, aaa, aab, aba, abb, baa, bab, bba, bbb, aaaa, aaab, aaba, aabb, abaa, abab, abba, abbb, baaa, baab, baba, babb, bbaa, bbab, bbba, bbbb]", words.toString());
    }

    @Test
    public void parseUsingPostfixPlus()
    {
//        RegExParser regExParser = new RegExParser();
//        PostfixNotationParser postfixParser = new PostfixNotationParser();
//        WordGenerator wordGenerator = new WordGenerator();
//
//        RegexOperationSequence sequence = regExParser.parse("(a|b)+");
//        Stack<String> result = postfixParser.parse(sequence);
//
//        SortedSet<String> words = wordGenerator.generateValidWords(result, 4);
//
//        assertEquals("[a, b, aa, ab, ba, bb, aaa, aab, aba, abb, baa, bab, bba, bbb, aaaa, aaab, aaba, aabb, abaa, abab, abba, abbb, baaa, baab, baba, babb, bbaa, bbab, bbba, bbbb]", words.toString());
    }
}
