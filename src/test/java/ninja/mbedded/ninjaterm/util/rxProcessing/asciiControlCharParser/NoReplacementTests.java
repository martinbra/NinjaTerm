package ninja.mbedded.ninjaterm.util.rxProcessing.asciiControlCharParser;

import ninja.mbedded.ninjaterm.util.rxProcessing.streamedData.StreamedData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the <code>AsciiControlCharParser</code> class.
 *
 * @author          Geoffrey Hunter <gbmhunter@gmail.com> (www.mbedded.ninja)
 * @since           2016-10-13
 * @last-modified   2016-10-16
 */
public class NoReplacementTests {

    private AsciiControlCharParser asciiControlCharParser;

    private StreamedData input;
    private StreamedData releasedText;

    @Before
    public void setUp() throws Exception {
        asciiControlCharParser = new AsciiControlCharParser();

        input = new StreamedData();
        releasedText = new StreamedData();
    }

    @Test
    public void singleControlCharAtStartTest() throws Exception {

        input.append("\rabc");
        asciiControlCharParser.parse(input, releasedText);
        assertEquals("abc", releasedText.getText());
    }

    @Test
    public void singleControlCharInMiddleTest() throws Exception {
        input.append("a\rb");
        asciiControlCharParser.parse(input, releasedText);
        assertEquals("ab", releasedText.getText());
    }

    @Test
    public void singleControlCharAtEndTest() throws Exception {
        input.append("abc\r");
        asciiControlCharParser.parse(input, releasedText);
        assertEquals("abc", releasedText.getText());
    }

    @Test
    public void singleControlChar2Test() throws Exception {
        input.append("abc\n");
        asciiControlCharParser.parse(input, releasedText);
        assertEquals("abc", releasedText.getText());
    }

    @Test
    public void twoControlCharsTest() throws Exception {
        input.append("abc\rdef\r");
        asciiControlCharParser.parse(input, releasedText);
        assertEquals("abcdef", releasedText.getText());
    }

    @Test
    public void onlyAControlCharTest() throws Exception {
        input.append("\r");
        asciiControlCharParser.parse(input, releasedText);
        assertEquals("", releasedText.getText());
    }

    @Test
    public void multipleControlCharsTest() throws Exception {
        input.append("a\rb\nc");
        asciiControlCharParser.parse(input, releasedText);
        assertEquals("abc", releasedText.getText());
    }

}