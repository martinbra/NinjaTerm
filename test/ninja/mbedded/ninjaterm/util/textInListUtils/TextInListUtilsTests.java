package ninja.mbedded.ninjaterm.util.textInListUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.text.Text;
import ninja.mbedded.ninjaterm.JavaFXThreadingRule;
import ninja.mbedded.ninjaterm.util.stringFilter.StringFilter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the <code>TextInListUtils</code> class.
 *
 * @author          Geoffrey Hunter <gbmhunter@gmail.com> (www.mbedded.ninja)
 * @since           2016-09-27
 * @last-modified   2016-09-27
 */
public class TextInListUtilsTests {

    /**
     * Including this variable in class allows JavaFX objects to be created in tests.
     */
    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void oneNodeTest() throws Exception {

        ObservableList<Node> observableList = FXCollections.observableArrayList();
        observableList.add(new Text("1234"));

        TextInListUtils.trimTextNodesFromStart(observableList, 1);
        assertEquals("234", ((Text)observableList.get(0)).getText());
    }

    @Test
    public void twoNodeSmallTrimTest() throws Exception {

        ObservableList<Node> observableList = FXCollections.observableArrayList();
        observableList.add(new Text("1234"));
        observableList.add(new Text("5678"));

        TextInListUtils.trimTextNodesFromStart(observableList, 1);
        assertEquals(2, observableList.size());
        assertEquals("234", ((Text)observableList.get(0)).getText());
        assertEquals("5678", ((Text)observableList.get(1)).getText());
    }

    @Test
    public void twoNodeLargeTrimTest() throws Exception {

        ObservableList<Node> observableList = FXCollections.observableArrayList();
        observableList.add(new Text("1234"));
        observableList.add(new Text("5678"));

        TextInListUtils.trimTextNodesFromStart(observableList, 5);
        assertEquals(1, observableList.size());
        assertEquals("678", ((Text)observableList.get(0)).getText());
    }

    @Test
    public void removeAllCharsTest() throws Exception {

        ObservableList<Node> observableList = FXCollections.observableArrayList();
        observableList.add(new Text("1234"));
        observableList.add(new Text("5678"));

        TextInListUtils.trimTextNodesFromStart(observableList, 8);
        assertEquals(0, observableList.size());
    }

}