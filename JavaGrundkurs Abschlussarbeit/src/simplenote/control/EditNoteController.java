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
 * @author claudio
 *
 */
public class EditNoteController {

    private RootController rc;
    private Note note;
    
    
    @FXML
    private TextField noteTitle;
    
    @FXML
    private HTMLEditor noteText;
    
    @FXML
    private ToggleButton noteShare;
    
    
    /**
     * 
     */
    public EditNoteController() {
        this.rc = RootController.getInstance();
    }

    
    @FXML
    public void initialize() {
        //this.note = 
    }
    
    /* FXML Actions */

    @FXML
    public void saveNote() {
        String nTitle = noteTitle.getText();
        String nText = noteText.getHtmlText();
        
        this.note.setTitle(nTitle);
        this.note.setText(nText);
        
        this.rc.getVault().add(note);
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
