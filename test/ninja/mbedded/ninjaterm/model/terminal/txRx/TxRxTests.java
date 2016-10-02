package ninja.mbedded.ninjaterm.model.terminal.txRx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import ninja.mbedded.ninjaterm.JavaFXThreadingRule;
import ninja.mbedded.ninjaterm.util.ansiECParser.AnsiECParserV2;
import ninja.mbedded.ninjaterm.util.streamedText.StreamFilterV2;
import ninja.mbedded.ninjaterm.util.streamedText.StreamedTextV2;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the <code>TextInListUtils</code> class.
 *
 * @author          Geoffrey Hunter <gbmhunter@gmail.com> (www.mbedded.ninja)
 * @since           2016-09-27
 * @last-modified   2016-10-02
 */
public class TxRxTests {

    /**
     * Including this variable in class allows JavaFX objects to be created in tests.
     */
    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    private AnsiECParserV2 ansiECParser;
    private StreamedTextV2 ansiECParserOutput;

    private StreamFilterV2 streamFilter;
    private StreamedTextV2 streamFilterOutput;

    private ObservableList<Node> textNodes;

    @Before
    public void setUp() throws Exception {
        ansiECParser = new AnsiECParserV2();
        ansiECParserOutput = new StreamedTextV2();

        streamFilter = new StreamFilterV2();
        streamFilterOutput = new StreamedTextV2();

        textNodes = FXCollections.observableArrayList();
        Text text = new Text();
        text.setFill(Color.BLACK);
        textNodes.add(text);
    }

    private void runOneIteration(String inputData) {
        ansiECParser.parse(inputData, ansiECParserOutput);
        streamFilter.streamFilter(ansiECParserOutput, streamFilterOutput, "a");
        streamFilterOutput.shiftToTextNodes(textNodes);
    }

    @Test
    public void extractAppendTextTest() throws Exception {

        runOneIteration("\u001B");
        assertEquals(1, textNodes.size());
        assertEquals("", ((Text)textNodes.get(0)).getText());
        assertEquals(Color.BLACK, ((Text)textNodes.get(0)).getFill());

        runOneIteration("[");
        assertEquals(1, textNodes.size());
        assertEquals("", ((Text)textNodes.get(0)).getText());
        assertEquals(Color.BLACK, ((Text)textNodes.get(0)).getFill());

        runOneIteration("3");
        assertEquals(1, textNodes.size());
        assertEquals("", ((Text)textNodes.get(0)).getText());
        assertEquals(Color.BLACK, ((Text)textNodes.get(0)).getFill());

        runOneIteration("1");
        assertEquals(1, textNodes.size());
        assertEquals("", ((Text)textNodes.get(0)).getText());
        assertEquals(Color.BLACK, ((Text)textNodes.get(0)).getFill());

        runOneIteration("m");
        assertEquals(1, textNodes.size());
        assertEquals("", ((Text)textNodes.get(0)).getText());
        assertEquals(Color.BLACK, ((Text)textNodes.get(0)).getFill());

        // This will make a match
        runOneIteration("a");
        assertEquals(2, textNodes.size());
        assertEquals("", ((Text)textNodes.get(0)).getText());
        assertEquals(Color.BLACK, ((Text)textNodes.get(0)).getFill());
        assertEquals("a", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("b");
        assertEquals(2, textNodes.size());
        assertEquals("ab", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("\r");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("\n");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("c");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("d");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("\r");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("\n");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("\u001B");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("[");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("3");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("2");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("m");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("e");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("f");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("\r");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("\n");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("\u001B");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("[");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("3");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("2");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("m");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("\u001B");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("[");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("3");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("2");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("m");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("b");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("\r");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("\n");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("\u001B");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("[");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("3");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("2");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("m");
        assertEquals(2, textNodes.size());
        assertEquals("ab\r\n", ((Text)textNodes.get(1)).getText());
        assertEquals(Color.rgb(170, 0, 0), ((Text)textNodes.get(1)).getFill());

        runOneIteration("a");
        assertEquals(3, textNodes.size());
        assertEquals("a", ((Text)textNodes.get(2)).getText());
        assertEquals(Color.rgb(0, 170, 0), ((Text)textNodes.get(2)).getFill());
    }

}