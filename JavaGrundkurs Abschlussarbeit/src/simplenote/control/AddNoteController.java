/**
 * 
 */
package simplenote.control;

import java.io.File;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import simplenote.model.Note;

/**
 * @author Claudio Marangon, Ljubisa Markovic
 *
 */
public class AddNoteController {

    private final String HTML_EMPTY = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"></body></html>";

    private RootController rc;
    private Note newNote;
    
    
    @FXML
    private TextField noteTitle;
    
    @FXML
    private HTMLEditor noteText;
    
    @FXML
    private ToggleButton noteShare;
    
    @FXML
    private VBox pictureList;
    
    @FXML
    private TextField link;
    
    @FXML
    private ListView<String> linkList;
    
    
    private ObservableList<String> linkData = FXCollections.observableArrayList();
    
    /**
     * 
     */
    public AddNoteController() {
        this.rc = RootController.getInstance();
    }

    
    @FXML
    public void initialize() {
        this.newNote = new Note();
        this.linkList.setItems(linkData);
    }
    
    /* FXML Actions */

    @FXML
    public void saveNote() {
        // text is required
        if(! this.noteText.getHtmlText().equals(HTML_EMPTY)) {
            String nTitle = this.noteTitle.getText();
            String nText = this.noteText.getHtmlText();
            
            if(nTitle.isEmpty()) {
                nTitle = "Notiz vom " + this.newNote.getCreationDate();
            }
            
            this.newNote.setTitle(nTitle);
            this.newNote.setText(nText);
            
            this.rc.getVault().add(newNote);
            if(this.rc.getVault().save()){
                this.rc.showOverview();
            } else {
                System.out.println("ERROR ON SAVING NOTE");
            }
        } else {
            this.noteText.getStyleClass().add("error");
        }
    }
    
    @FXML
    public void addLink() {
        this.linkData.add(this.link.getText());
    }
    
    @FXML
    public void addFile() {
        FileChooser fileChooser = new FileChooser();
        List<File> imageList = fileChooser.showOpenMultipleDialog(this.rc.getPrimaryStage());
        
        if (imageList != null) {
            for(File f : imageList) {
                Image img = new Image(f.toURI().toString(), 200, 200, true, true);
                pictureList.getChildren().add(new ImageView(img));
            }
            this.newNote.addFiles(imageList);
        }
    }
}
