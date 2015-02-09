/**
 * 
 */
package simplenote.control;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.web.HTMLEditor;
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
    
    
    /**
     * 
     */
    public AddNoteController() {
        this.rc = RootController.getInstance();
    }

    
    @FXML
    public void initialize() {}
    
    /* FXML Actions */

    @FXML
    public void saveNote() {
        String nTitle = noteTitle.getText();
        String nText = noteText.getHtmlText();
        
        newNote = new Note(nTitle);
        newNote.setText(nText);
        
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
        
    }
}
