package ninja.mbedded.ninjaterm.view.mainwindow.terminal.rxtx.layout;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import ninja.mbedded.ninjaterm.model.terminal.txRx.layout.Layout;

import java.io.IOException;

/**
 * Controller for the layout pop-up window.
 *
 * @author Geoffrey Hunter <gbmhunter@gmail.com> (www.mbedded.ninja)
 * @since 2016-09-16
 * @last-modified 2016-09-16
 */
public class LayoutView extends VBox {

    //================================================================================================//
    //========================================== FXML BINDINGS =======================================//
    //================================================================================================//

    @FXML
    public ComboBox<Layout.LayoutOptions> layoutOptionsComboBox;

    //================================================================================================//
    //=========================================== CLASS FIELDS =======================================//
    //================================================================================================//


    //================================================================================================//
    //========================================== CLASS METHODS =======================================//
    //================================================================================================//

    public LayoutView() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "LayoutView.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    public void init(Layout layout) {

        // Populate decoding options combobox
        layoutOptionsComboBox.getItems().setAll(Layout.LayoutOptions.values());

        // Add listener to combobox
        layoutOptionsComboBox.setOnAction(event -> {

            // Bind the decoder decoding option to what has been selected in the
            // combobox
            layout.selectedLayoutOption.set(layoutOptionsComboBox.getSelectionModel().getSelectedItem());
        });

        // Set default
        layoutOptionsComboBox.getSelectionModel().select(layout.selectedLayoutOption.get());
    }

}
