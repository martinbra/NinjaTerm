package ninja.mbedded.ninjaterm.util.streamedText;

import javafx.scene.paint.Color;
import ninja.mbedded.ninjaterm.JavaFXThreadingRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the <code>{@link StreamedText}</code> class.
 *
 * @author          Geoffrey Hunter <gbmhunter@gmail.com> (www.mbedded.ninja)
 * @since           2016-09-27
 * @last-modified   2016-10-02
 */
public class ShiftWithColoursTests {

    /**
     * Including this variable in class allows JavaFX objects to be created in tests.
     */
    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    private StreamedText inputStreamedText;
    private StreamedText outputStreamedText;

    @Before
    public void setUp() throws Exception {
        inputStreamedText = new StreamedText();
        outputStreamedText = new StreamedText();
    }

    @Test
    public void extractAppendTextTest() throws Exception {

        inputStreamedText.append("1234");

        outputStreamedText.shiftCharsIn(inputStreamedText, 2);

        assertEquals("34", inputStreamedText.getText());
        assertEquals(0, inputStreamedText.getColourMarkers().size());

        assertEquals("12", outputStreamedText.getText());
        assertEquals(0, outputStreamedText.getColourMarkers().size());
    }

    @Test
    public void shiftWithColoursTest() throws Exception {

        inputStreamedText.append("12345678");
        inputStreamedText.addColour(4, Color.RED);

        outputStreamedText.shiftCharsIn(inputStreamedText, 6);

        // Check input
        assertEquals("78", inputStreamedText.getText());
        assertEquals(0, inputStreamedText.getColourMarkers().size());

        // Check output
        assertEquals("123456", outputStreamedText.getText());
        assertEquals(1, outputStreamedText.getColourMarkers().size());
        assertEquals(4, outputStreamedText.getColourMarkers().get(0).position);
        assertEquals(Color.RED, outputStreamedText.getColourMarkers().get(0).color);

    }

    @Test
    public void extractJustBeforeColorTest() throws Exception {

        inputStreamedText.append("123456789");
        inputStreamedText.addColour(6, Color.RED);

        outputStreamedText.shiftCharsIn(inputStreamedText, 6);

        // Check input
        assertEquals("789", inputStreamedText.getText());
        assertEquals(1, inputStreamedText.getColourMarkers().size());
        assertEquals(0, inputStreamedText.getColourMarkers().get(0).position);
        assertEquals(Color.RED, inputStreamedText.getColourMarkers().get(0).color);

        // Check output
        assertEquals("123456", outputStreamedText.getText());
        assertEquals(0, outputStreamedText.getColourMarkers().size());
    }

    @Test
    public void extractJustAfterColorTest() throws Exception {

        inputStreamedText.append("123456789");
        inputStreamedText.addColour(6, Color.RED);

        outputStreamedText.shiftCharsIn(inputStreamedText, 7);

        // Check input
        assertEquals("89", inputStreamedText.getText());
        assertEquals(0, inputStreamedText.getColourMarkers().size());

        // Check output
        assertEquals("1234567", outputStreamedText.getText());
        assertEquals(1, outputStreamedText.getColourMarkers().size());
        assertEquals(6, outputStreamedText.getColourMarkers().get(0).position);
        assertEquals(Color.RED, outputStreamedText.getColourMarkers().get(0).color);
    }

    @Test
    public void outputAlreadyPopulatedTest() throws Exception {

        inputStreamedText.append("56789");
        inputStreamedText.addColour(3, Color.RED);

        outputStreamedText.append("1234");
        outputStreamedText.addColour(2, Color.GREEN);

        outputStreamedText.shiftCharsIn(inputStreamedText, 4);

        // Check input, should be 1 char left over
        assertEquals("9", inputStreamedText.getText());
        assertEquals(0, inputStreamedText.getColourMarkers().size());

        // Check output
        assertEquals("12345678", outputStreamedText.getText());
        assertEquals(2, outputStreamedText.getColourMarkers().size());

        assertEquals(2, outputStreamedText.getColourMarkers().get(0).position);
        assertEquals(Color.GREEN, outputStreamedText.getColourMarkers().get(0).color);

        assertEquals(7, outputStreamedText.getColourMarkers().get(1).position);
        assertEquals(Color.RED, outputStreamedText.getColourMarkers().get(1).color);
    }

    @Test
    public void removeFromAppendTextTest() throws Exception {

        inputStreamedText.append("123456789");
        inputStreamedText.addColour(5, Color.RED);
        inputStreamedText.addColour(6, Color.GREEN);

        inputStreamedText.removeChars(3);

        assertEquals("456789", inputStreamedText.getText());
        assertEquals(2, inputStreamedText.getColourMarkers().size());

        assertEquals(2, inputStreamedText.getColourMarkers().get(0).position);
        assertEquals(Color.RED, inputStreamedText.getColourMarkers().get(0).color);

        assertEquals(3, inputStreamedText.getColourMarkers().get(1).position);
        assertEquals(Color.GREEN, inputStreamedText.getColourMarkers().get(1).color);
    }

    @Test
    public void removeFromAppendAndNodesTest() throws Exception {

        inputStreamedText.append("1234567");
        inputStreamedText.addColour(2, Color.RED);
        inputStreamedText.addColour(4, Color.GREEN);

        inputStreamedText.removeChars(3);

        assertEquals("4567", inputStreamedText.getText());
        assertEquals(1, inputStreamedText.getColourMarkers().size());

        assertEquals(1, inputStreamedText.getColourMarkers().get(0).position);
        assertEquals(Color.GREEN, inputStreamedText.getColourMarkers().get(0).color);
    }

    @Test
    public void colorNoTextTest() throws Exception {
        inputStreamedText.setColorToBeInsertedOnNextChar(Color.RED);
        assertEquals("", inputStreamedText.getText());
        assertEquals(0, inputStreamedText.getColourMarkers().size());
        assertEquals(Color.RED, inputStreamedText.getColorToBeInsertedOnNextChar());

        outputStreamedText.shiftCharsIn(inputStreamedText, 0);

        assertEquals("", inputStreamedText.getText());
        assertEquals(0, inputStreamedText.getColourMarkers().size());
        assertEquals(null, inputStreamedText.getColorToBeInsertedOnNextChar());

        assertEquals("", outputStreamedText.getText());
        assertEquals(0, outputStreamedText.getColourMarkers().size());
        assertEquals(Color.RED, outputStreamedText.getColorToBeInsertedOnNextChar());
    }

    @Test
    public void colorToAddOnNextCharInOutputTest() throws Exception {
        inputStreamedText.append("123");
        outputStreamedText.setColorToBeInsertedOnNextChar(Color.RED);

        outputStreamedText.shiftCharsIn(inputStreamedText, 3);

        assertEquals("", inputStreamedText.getText());
        assertEquals(0, inputStreamedText.getColourMarkers().size());
        assertEquals(null, inputStreamedText.getColorToBeInsertedOnNextChar());

        assertEquals("123", outputStreamedText.getText());
        assertEquals(1, outputStreamedText.getColourMarkers().size());

        assertEquals(0, outputStreamedText.getColourMarkers().get(0).position);
        assertEquals(Color.RED, outputStreamedText.getColourMarkers().get(0).color);

        assertEquals(null, outputStreamedText.getColorToBeInsertedOnNextChar());

    }

}