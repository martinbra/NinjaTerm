package ninja.mbedded.ninjaterm.view.mainWindow.terminal.txRx.filters;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jfxtras.scene.control.ToggleGroupValue;
import ninja.mbedded.ninjaterm.model.Model;
import ninja.mbedded.ninjaterm.model.terminal.Terminal;
import ninja.mbedded.ninjaterm.model.terminal.txRx.filters.Filters;

/**
 * Backend for the filters pop-up window.
 *
 * @author Geoffrey Hunter <gbmhunter@gmail.com> (www.mbedded.ninja)
 * @since 2016-09-21
 * @last-modified 2016-10-02
 */
public class FiltersViewController {

    //================================================================================================//
    //========================================== FXML BINDINGS =======================================//
    //================================================================================================//

    @FXML
    private TextField filterTextTextField;

    @FXML
    private RadioButton applyToNewRxDataOnlyCheckBox;

    @FXML
    private RadioButton applyToBothBufferedAndNewRxDataCheckBox;

    //================================================================================================//
    //=========================================== CLASS FIELDS =======================================//
    //================================================================================================//

    private Model model;
    private Terminal terminal;

    //================================================================================================//
    //========================================== CLASS METHODS =======================================//
    //================================================================================================//

    public FiltersViewController() {
    }

    public void init(Model model, Terminal terminal) {

        //==============================================//
        //======= ATTACH LISTENER TO FILTER TEXT =======//
        //==============================================//

        // Bind the text in the filter text textfield to the string in the model
        Bindings.bindBidirectional(filterTextTextField.textProperty(), terminal.txRx.filters.filterText);

        //==============================================//
        //=============== RADIOBUTTON SETUP ============//
        //==============================================//

        ToggleGroupValue<Filters.FilterApplyTypes> filterApplyTypesTGV = new ToggleGroupValue<>();
        filterApplyTypesTGV.add(applyToNewRxDataOnlyCheckBox, Filters.FilterApplyTypes.APPLY_TO_NEW_RX_DATA_ONLY);
        filterApplyTypesTGV.add(applyToBothBufferedAndNewRxDataCheckBox, Filters.FilterApplyTypes.APPLY_TO_BUFFERED_AND_NEW_RX_DATA);

        Bindings.bindBidirectional(filterApplyTypesTGV.valueProperty(), terminal.txRx.filters.filterApplyType);

    }
}
