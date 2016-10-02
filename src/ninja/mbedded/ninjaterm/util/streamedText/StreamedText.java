package ninja.mbedded.ninjaterm.util.streamedText;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import ninja.mbedded.ninjaterm.util.debugging.Debugging;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Class which is designed to encapsulate a "unit" of streamed text, which is generated by the ANSI escape
 * code parser. This <code>{@link StreamedText}</code> object is then fed into the filter engine,
 * whose output is another <code>{@link StreamedText}</code> object.
 *
 * @author Geoffrey Hunter <gbmhunter@gmail.com> (www.mbedded.ninja)
 * @since 2016-09-28
 * @last-modified 2016-10-02
 */
public class StreamedText {

    private String text = "";
    private List<TextColour> textColours = new ArrayList<>();
    private Color colorToBeInsertedOnNextChar = null;


    public String getText() {
        return text;
    }

    public List<TextColour> getTextColours () {
        return textColours;
    }

    public Color getColorToBeInsertedOnNextChar() {
        return colorToBeInsertedOnNextChar;
    }

    public void setColorToBeInsertedOnNextChar(Color color) {
        this.colorToBeInsertedOnNextChar = color;
    }

    /**
     * The method extracts the specified number of chars from the input and places them in the output.
     * It extract chars from the "to append" String first, and then starts removing chars from the first of the
     * Text nodes contained within the list.
     * <p>
     * It also shifts any chars from still existing input nodes into the "to append" String
     * as appropriate.
     *
     * @param numChars
     * @return
     */
    public void shiftCharsIn(StreamedText inputStreamedText, int numChars) {

        if(numChars > inputStreamedText.getText().length())
            throw new IllegalArgumentException("numChars is greater than the number of characters in inputStreamedText.");

        for (ListIterator<TextColour> iter = inputStreamedText.textColours.listIterator(); iter.hasNext(); ) {
            TextColour textColour = iter.next();

            // Check if we have reached TextColour objects which index characters beyond the range
            // we are shifting, and if so, break out of this loop
            if(textColour.position < numChars) {
                // We need to offset set the position by the length of the existing text
                textColour.position = textColour.position + text.length();

                // Now add this TextColour object to this objects list, and remove from the input
                textColours.add(textColour);
                iter.remove();
            } else {
                // We are beyond the range that is being shifted, so adjust the position, but
                // don't shift the object to this list (keep in input)
                textColour.position -= numChars;
            }
        }

        text = text + inputStreamedText.text.substring(0, numChars);
        inputStreamedText.text = inputStreamedText.text.substring(numChars, inputStreamedText.text.length());

        // Apply the colour to be inserted on next char, if at least one char was placed in the output
        if((numChars > 0) && (this.colorToBeInsertedOnNextChar != null)) {

            // Check if a color is already assigned to this character. If so, do not
            // overwrite
            if((this.textColours.size() == 0) || (this.textColours.get(0).position != 0)) {
                // Insert new TextColour object at start of list
                this.textColours.add(0, new TextColour(0, this.colorToBeInsertedOnNextChar));
            }

            // We have applied the color to a character, remove the placeholder
            this.colorToBeInsertedOnNextChar = null;
        }

        // Transfer the "color to be inserted on next char", if one exists in input
        // This could overwrite an existing "color to be inserted on next char" in the output, if
        // no chars were shifted
        if(inputStreamedText.getColorToBeInsertedOnNextChar() != null) {
            this.setColorToBeInsertedOnNextChar(inputStreamedText.getColorToBeInsertedOnNextChar());
            inputStreamedText.setColorToBeInsertedOnNextChar(null);
        }

        checkAllColoursAreInOrder();

    }

    public void removeChars(int numChars) {
        StreamedText dummyStreamedText = new StreamedText();
        dummyStreamedText.shiftCharsIn(this, numChars);
        checkAllColoursAreInOrder();
    }

    private enum CopyOrShift {
        COPY,
        SHIFT,
    }

    /**
     * The method copies the specified number of chars from the input into the output.
     * It copies chars from the "to append" String first, and then starts copying chars from the first of the
     * Text nodes contained within the list.
     * <p>
     * It also copies any chars from still existing input nodes into the "to append" String
     * as appropriate.
     *
     * @param numChars
     * @return
     */
    private void copyOrShiftCharsFrom(StreamedText inputStreamedText, int numChars, CopyOrShift copyOrShift) {

        if(numChars > inputStreamedText.getText().length())
            throw new IllegalArgumentException("numChars is greater than the number of characters in inputStreamedText.");

        for (ListIterator<TextColour> iter = inputStreamedText.textColours.listIterator(); iter.hasNext(); ) {
            TextColour oldTextColour = iter.next();
            TextColour newTextColor;

            if(copyOrShift == CopyOrShift.COPY) {
                // Copy text color object
                newTextColor = new TextColour(oldTextColour);
            } else if(copyOrShift == CopyOrShift.SHIFT) {
                // We can just modify the existing object, since we are shifting
                newTextColor = oldTextColour;
            } else {
                throw new RuntimeException("copyOrShift not recognised.");
            }

            // Check if we have reached TextColour objects which index characters beyond the range
            // we are shifting, and if so, break out of this loop
            if(oldTextColour.position < numChars) {

                // We need to offset set the position by the length of the existing text
                newTextColor.position = oldTextColour.position + text.length();
                // Now add this TextColour object to this objects list, and remove from the input
                textColours.add(newTextColor);

                if(copyOrShift == CopyOrShift.SHIFT) {
                    iter.remove();
                }

            } else {
                // We are beyond the range that is being shifted, so adjust the position, but
                // don't shift the object to this list (keep in input)
                if(copyOrShift == CopyOrShift.SHIFT) {
                    newTextColor.position -= numChars;
                }
            }
        }

        text = text + inputStreamedText.text.substring(0, numChars);

        if(copyOrShift == CopyOrShift.SHIFT) {
            inputStreamedText.text = inputStreamedText.text.substring(numChars, inputStreamedText.text.length());
        }

        // Apply the colour to be inserted on next char, if at least one char was placed in the output
        if((numChars > 0) && (this.colorToBeInsertedOnNextChar != null)) {

            // Check if a color is already assigned to this character. If so, do not
            // overwrite
            if((this.textColours.size() == 0) || (this.textColours.get(0).position != 0)) {
                // Insert new TextColour object at start of list
                this.textColours.add(0, new TextColour(0, this.colorToBeInsertedOnNextChar));
            }

            // We have applied the color to a character, remove the placeholder
            this.colorToBeInsertedOnNextChar = null;
        }

        // Transfer the "color to be inserted on next char", if one exists in input
        // This could overwrite an existing "color to be inserted on next char" in the output, if
        // no chars were shifted
        if(inputStreamedText.getColorToBeInsertedOnNextChar() != null) {
            this.setColorToBeInsertedOnNextChar(inputStreamedText.getColorToBeInsertedOnNextChar());

            if(copyOrShift == CopyOrShift.SHIFT) {
                inputStreamedText.setColorToBeInsertedOnNextChar(null);
            }
        }

        checkAllColoursAreInOrder();
    }

    public void copyCharsFrom(StreamedText inputStreamedText, int numChars) {
        copyOrShiftCharsFrom(inputStreamedText, numChars, CopyOrShift.COPY);
    }

    /**
     * Adds the provided text to the stream, using the given <code>addMethod</code>.
     *
     * @param textToAppend
     */
    public void append(String textToAppend) {
        System.out.println("append() called with text = \"" + Debugging.convertNonPrintable(textToAppend) + "\".");

        // Passing in an empty string is not invalid, but we don't have to do anything,
        // so just return.
        if(textToAppend.equals(""))
            return;

        text = text + textToAppend;

        // Apply the "color to be inserted on next char" if there is one to apply.
        // This will never be applied if no chars are inserted because of the return above
        if(colorToBeInsertedOnNextChar != null) {
            addColour(text.length() - textToAppend.length(), colorToBeInsertedOnNextChar);
            colorToBeInsertedOnNextChar = null;
        }

        checkAllColoursAreInOrder();
    }

    public void addColour(int position, Color color) {

        if(position < 0 || position > text.length() - 1)
            throw new IllegalArgumentException("position was either too small or too large.");

        // Make sure all the TextColor objects in the list remain in order
        if(textColours.size() != 0 && textColours.get(textColours.size() - 1).position > position)
            throw new IllegalArgumentException("position was not greater than all existing positions.");

        // Check if we are overwriting the last TextColor object (if they apply to the same text position),
        // or we are needed to create a new TextColor object
        if(textColours.size() != 0 && textColours.get(textColours.size() - 1).position == position) {
            textColours.get(textColours.size() - 1).color = color;
        } else {
            textColours.add(new TextColour(position, color));
        }

        checkAllColoursAreInOrder();
    }

    @Override
    public String toString() {
        return text;
    }

    public void shiftToTextNodes(ObservableList<Node> existingTextNodes) {

        Text lastTextNode = (Text)existingTextNodes.get(existingTextNodes.size() - 1);

        // Copy all text before first TextColour entry into last node

        int indexOfLastCharPlusOne;
        if(getTextColours().size() == 0) {
            indexOfLastCharPlusOne = getText().length();
        } else {
            indexOfLastCharPlusOne = getTextColours().get(0).position;
        }

        lastTextNode.setText(lastTextNode.getText() + getText().substring(0, indexOfLastCharPlusOne));

        // Create new text nodes and copy all text
        // This loop won't run if there is no elements in the TextColors array
        for(int x = 0; x < getTextColours().size(); x++) {
            Text newText = new Text();

            int indexOfFirstCharInNode = getTextColours().get(x).position;

            int indexOfLastCharInNode;
            if(x >= getTextColours().size() - 1) {
                indexOfLastCharInNode = getText().length();
            } else {
                indexOfLastCharInNode = getTextColours().get(x).position;
            }

            newText.setText(getText().substring(indexOfFirstCharInNode, indexOfLastCharInNode));
            newText.setFill(getTextColours().get(x).color);

            existingTextNodes.add(newText);
        }

        if(colorToBeInsertedOnNextChar != null) {
            // Add new node with no text
            Text text = new Text();
            text.setFill(colorToBeInsertedOnNextChar);
            existingTextNodes.add(text);
            colorToBeInsertedOnNextChar = null;
        }

        // Clear all text and the TextColor list
        text = "";
        textColours.clear();

        checkAllColoursAreInOrder();
    }

    private void checkAllColoursAreInOrder() {

        int charIndex = -1;
        for(TextColour textColour : textColours) {
            if(textColour.position <= charIndex)
                throw new RuntimeException("Colours were not in order!");

            charIndex = textColour.position;
        }
    }

}
