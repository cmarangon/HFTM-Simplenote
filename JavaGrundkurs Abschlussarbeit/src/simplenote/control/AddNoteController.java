/**
 * 
 */
package simplenote.control;

import java.net.URL;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import simplenote.model.Note;

/**
 * @author Claudio Marangon, Ljubisa Markovic
 *
 */
public class AddNoteController extends FXNoteController {

    private Note newNote;

    @FXML
    private Button saveButton;

    @FXML
    private Button backButton;

    @FXML
    private Label modificationDateLabel;

    @FXML
    private TextField titleField;

    @FXML
    private HTMLEditor textField;

    @FXML
    private VBox pictureList;

    @FXML
    private TextField linkField;

    @FXML
    private ListView<URL> linkList;

    @FXML
    private Button addLinkButton;

    @FXML
    private HBox modifyLinkList;

    @FXML
    private Button acceptButton;

    @FXML
    private Button cancelButton;


    /**
     * 
     */
    public AddNoteController() {
    }

    @FXML
    public void initialize() {

        // init all elements
        initEditButtons(saveButton, backButton);
        initNoteFields(titleField, textField, pictureList, linkList);
        initLinkElements(linkField, modifyLinkList, addLinkButton, acceptButton, cancelButton);

        this.newNote = new Note();
        this.linkList.setItems(linkData);
    }


    /* FXML Actions */

    @FXML
    public void saveNote() {
        super.saveNote(newNote, true);
    }
}
