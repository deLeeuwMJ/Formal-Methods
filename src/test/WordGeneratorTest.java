package test;

import main.logic.PostfixNotationParser;
import main.logic.RegExParser;
import main.logic.WordGenerator;
import main.model.RegExp;
import main.model.RegexOperationSequence;
import org.junit.Test;

import java.util.SortedSet;
import java.util.Stack;

import static junit.framework.TestCase.assertEquals;

public class WordGeneratorTest {

    @Test
    public void parsePlain()
    {
        // (a|b)+
        RegExp step1 = new RegExp("a");
        RegExp step2 = new RegExp("b");
        RegExp step3 = step2.or(step1);
        RegExp step4 = step3.plus();

        assertEquals("[a, b, aa, ab, ba, bb, aaa, aab, aba, abb, baa, bab, bba, bbb]", step4.getLanguage(3).toString());
    }

    @Test
    public void parseUsingPostfix()
    {
        RegExParser regExParser = new RegExParser();
        PostfixNotationParser postfixParser = new PostfixNotationParser();
        WordGenerator wordGenerator = new WordGenerator();

        RegexOperationSequence sequence = regExParser.parse("(a|b)*");
        Stack<String> result = postfixParser.parse(sequence);

        SortedSet<String> words = wordGenerator.generate(result, 4);

        assertEquals("[, a, b, aa, ab, ba, bb, aaa, aab, aba, abb, baa, bab, bba, bbb, aaaa, aaab, aaba, aabb, abaa, abab, abba, abbb, baaa, baab, baba, babb, bbaa, bbab, bbba, bbbb]", words.toString());
    }
}
