package ninja.mbedded.ninjaterm.util.asciiControlCharParser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the <code>AsciiControlCharDisplayer</code> class.
 *
 * @author          Geoffrey Hunter <gbmhunter@gmail.com> (www.mbedded.ninja)
 * @since           2016-10-13
 * @last-modified   2016-10-14
 */
public class NoReplacementTests {

    private AsciiControlCharDisplayer asciiControlCharDisplayer;

    @Before
    public void setUp() throws Exception {
        asciiControlCharDisplayer = new AsciiControlCharDisplayer();
    }

    @Test
    public void singleControlCharAtStartTest() throws Exception {
        String output = asciiControlCharDisplayer.parse("\rabc");
        assertEquals("abc", output);
    }

    @Test
    public void singleControlCharInMiddleTest() throws Exception {
        String output = asciiControlCharDisplayer.parse("a\rb");
        assertEquals("ab", output);
    }

    @Test
    public void singleControlCharAtEndTest() throws Exception {
        String output = asciiControlCharDisplayer.parse("abc\r");
        assertEquals("abc", output);
    }

    @Test
    public void singleControlChar2Test() throws Exception {
        String output = asciiControlCharDisplayer.parse("abc\n");
        assertEquals("abc", output);
    }

    @Test
    public void twoControlCharsTest() throws Exception {
        String output = asciiControlCharDisplayer.parse("abc\rdef\r");
        assertEquals("abcdef", output);
    }

    @Test
    public void onlyAControlCharTest() throws Exception {
        String output = asciiControlCharDisplayer.parse("\r");
        assertEquals("", output);
    }

    @Test
    public void multipleControlCharsTest() throws Exception {
        String output = asciiControlCharDisplayer.parse("a\rb\nc");
        assertEquals("abc", output);
    }


}