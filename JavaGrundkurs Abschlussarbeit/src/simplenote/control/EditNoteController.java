/**
 * simpleNote, a better way to store your notes
 * Abschlussarbeit der HFTM Grenchen
 * Klasse Java Grundlagen II
 */
package simplenote.control;

import java.io.File;
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
 * Controller class to edit existing notes from the vault
 * 
 * @author Claudio Marangon, Ljubisa Markovic
 */
public class EditNoteController extends FXNoteController {

    private Note note;

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
     * Constructor
     */
    public EditNoteController() {
    }

    /**
     * Gets called when loaded with FXMLLoader
     */
    @FXML
    public void initialize() {

        // init all elements
        initEditButtons(saveButton, backButton);
        initNoteFields(titleField, textField, pictureList, linkList);
        initLinkElements(linkField, modifyLinkList, addLinkButton, acceptButton, cancelButton);

        this.note = this.rc.getSelectedNote();

        // show modification date
        modificationDateLabel.setText(ViewHelper.formatDate(this.note.getModificationDate(), "Zuletzt geändert am ", "nie"));

        // load title & links
        titleField.setText(this.note.getTitle());
        textField.setHtmlText(this.note.getText());

        // load links
        for (URL url : this.note.getLinkList()) {
            linkData.add(url);
        }
        linkList.setItems(linkData);
        
        // load pictures
        for (File file : this.note.getPictureList()) {
            pictureData.add(file);
        }
        loadPictureList();
    }


    /* FXML Actions */
    
    /**
     * Save the current note
     */
    @FXML
    public void saveNote() {
        super.saveNote(this.note, false);
    }
}
