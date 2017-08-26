package me.gavin.svg.editor;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.gavin.svg.editor.util.VectorParser;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void matches() {
        String rex = "[M|m|L|l|H|h|V|v|Q|q|T|t|C|c|S|s|A|a|Z|z][0-9|\\-.\\s]*";
        String path = "M1 21h4V9H1v12zm22-11c0-1.1-.9-2-2-2h-6.31l.95-4.57.03-.32c0-.41-.17-.79-.44-1.06L14.17 1 7.59 7.59C7.22 7.95 7 8.45 7 9v10c0 1.1.9 2 2 2h9c.83 0 1.54-.5 1.84-1.22l3.02-7.05c.09-.23.14-.47.14-.73v-1.91l-.01-.01L23 10z";

        Pattern pattern = Pattern.compile(rex);
        Matcher matcher = pattern.matcher(path.trim().replaceAll(",", " ").replaceAll(" +", " "));
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

    @Test
    public void format() {
        String path = "M1 21h4V9H1v12zm22-11c0-1.1-.9-2-2-2h-6.31l.95-4.57.03-.32c0-.41-.17-.79-.44-1.06L14.17 1 7.59 7.59C7.22 7.95 7 8.45 7 9v10c0 1.1.9 2 2 2h9c.83 0 1.54-.5 1.84-1.22l3.02-7.05c.09-.23.14-.47.14-.73v-1.91l-.01-.01L23 10z";
        System.out.println(VectorParser.pathFormat(path));
    }

}