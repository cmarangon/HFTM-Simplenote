/**
 * 
 */
package simplenote.control;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import simplenote.model.Note;

/**
 * @author Claudio Marangon, Ljubisa Markovic
 *
 */
public class AddNoteController {

    private RootController rc;
    private Note newNote;
    
    
    @FXML
    private TextField noteTitle;
    
    @FXML
    private HTMLEditor noteText;
    
    @FXML
    private ToggleButton noteShare;
    
    @FXML
    private ListView<Object> attachmentList;
    
    
    private ObservableList<Object> attachmentData = FXCollections.observableArrayList();
    
    /**
     * 
     */
    public AddNoteController() {
        this.rc = RootController.getInstance();
    }

    
    @FXML
    public void initialize() {
        this.newNote = new Note();
        this.attachmentList.setItems(this.attachmentData);
    }
    
    /* FXML Actions */

    @FXML
    public void saveNote() {
        String nTitle = this.noteTitle.getText();
        String nText = this.noteText.getHtmlText();
        
        this.newNote.setTitle(nTitle);
        this.newNote.setText(nText);
        
        this.rc.getVault().add(newNote);
        if(this.rc.getVault().save()){
            this.rc.showOverview();
        } else {
            System.out.println("ERROR ON SAVING NOTE");
        }
    }
    
    @FXML
    public void addLink() {
        
    }
    
    @FXML
    public void addFile() {
        FileChooser fileChooser = new FileChooser();
        List<File> imageList = fileChooser.showOpenMultipleDialog(this.rc.getPrimaryStage());
        
        if (imageList != null) {
            this.newNote.addFiles(imageList);
            this.attachmentData.addAll(imageList);
        }
    }
}
