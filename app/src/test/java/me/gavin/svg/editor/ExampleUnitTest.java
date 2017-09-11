package me.gavin.svg.editor;

import org.junit.Test;

import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.gavin.svg.editor.util.L;

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
        String rex = "[M|m|L|l|H|h|V|v|Q|q|T|t|C|c|S|s|A|a|Z|z]\\s+";
        String path = "M80 80 A \n  45 45, 0, 0, 0, 125 125 L 125 80 Z";

        Pattern pattern = Pattern.compile(rex);
        Matcher matcher = pattern.matcher(path);
        while (matcher.find()) {
            String group = matcher.group();
            System.out.println(group + "~");
            path = path.replaceFirst(group, group.charAt(0) + "");
//            path = matcher.replaceFirst(group.charAt(0) + "");
        }
        System.out.println(path);
    }

    @Test
    public void format() {

    }

    @Test
    public void split() {
        String s = "a   b c";
        String[] ss = s.split("\\s+");
        System.out.println(Arrays.toString(ss));
        for (int i = 0; i < ss.length; i++) {
            System.out.println(i + ":" + ss[i]);
        }
    }

    @Test
    public void num() {
        a(20);
    }

    private void a(int n) {
        for (int v = 0; v < n; v++) {
            for (int h = 0; h < n; h++) {
                if (n % 2 == 0) { // 偶数
                    int c = d(h, v, n);
                    int lineCount = c * 2;
                    int rc = n / 2 - c;
                    int start = s(rc, n);
                    System.out.print(String.format(Locale.getDefault(), "%03d", b(lineCount, start, h - rc, v - rc)));
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    /**
     * 计算最外圈
     */
    private int b(int lineCount, int start, int h, int v) {
        int result = start;
        if (h == 0) {
            result += v;
        } else if (v == lineCount - 1) {
            result += lineCount + h - 1;
        } else if (h == lineCount - 1) {
            result += lineCount * 3 - v - 3;
        } else {
            result += lineCount * 4 - h - 4;
        }
        return result;
    }

    /**
     * 倒数第几圈
     */
    private int d(int h, int v, int n) {
        return Math.max(Math.abs(((int) (h - (n - 1.0) / 2))) + 1,
                Math.abs(((int) (v - (n - 1.0) / 2))) + 1);
    }

    /**
     * 倒数第几圈
     */
    private int s(int c, int n) {
        int start = 1;
        for (int i = 0; i < c; i++) {
            start += (n - c) * 4;
        }
        return start;
    }

}