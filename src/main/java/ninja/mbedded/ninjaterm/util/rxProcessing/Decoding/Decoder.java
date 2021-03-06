package ninja.mbedded.ninjaterm.util.rxProcessing.Decoding;

import javafx.beans.property.SimpleObjectProperty;

import java.io.UnsupportedEncodingException;

/**
 * Parsing engine for converting raw bytes received through the COM port into
 * displayable data to the user. The user can change the decoding type (
 * UTF-8, hex, ...).
 *
 * @author          Geoffrey Hunter <gbmhunter@gmail.com> (www.mbedded.ninja)
 * @since           2016-08-25
 * @last-modified   2016-10-07
 */
public class Decoder {

    public SimpleObjectProperty<DecodingOptions> decodingOption = new SimpleObjectProperty<>(DecodingOptions.ASCII);

    public Decoder() {

    }

    public String parse(byte[] data) {

        String output;

        // All ASCII variants have the same decoding
        // (ASCII control chars will be processed later)
        if(decodingOption.get() == DecodingOptions.ASCII ||
                decodingOption.get() == DecodingOptions.ASCII_WITH_CONTROL_CHARS) {
            try {
                output = new String(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        } else if(decodingOption.get() == DecodingOptions.HEX) {
            output = BytesToString.bytesToHex(data);
        } else {
            throw new RuntimeException("formatting option was not recognised.");
        }

        return output;
    }

}
