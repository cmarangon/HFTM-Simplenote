/**
 * 
 */
package simplenote.control;

import java.io.File;
import java.util.List;

import javafx.fxml.FXML;
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
public class EditNoteController {

    private RootController rc;
    private Note note;
    
    
    @FXML
    private TextField noteTitle;
    
    @FXML
    private HTMLEditor noteText;
    
    @FXML
    private ToggleButton noteShare;
    
    @FXML
    private VBox attachmentList;
    
    /**
     * 
     */
    public EditNoteController() {
        this.rc = RootController.getInstance();
    }

    
    @FXML
    public void initialize() {
        this.note = this.rc.getSelectedNote();
        noteTitle.setText(this.note.getTitle());
        noteText.setHtmlText(this.note.getText());
        for(File f : this.note.getFileList()) {
            Image img = new Image(f.toURI().toString(), 200, 200, true, true);
            attachmentList.getChildren().add(new ImageView(img));
        }
    }
    
    /* FXML Actions */

    @FXML
    public void saveNote() {
        String nTitle = noteTitle.getText();
        String nText = noteText.getHtmlText();
        
        this.note.setTitle(nTitle);
        this.note.setText(nText);
        
        //this.rc.getVault().add(note);
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
            for(File f : imageList) {
                Image img = new Image(f.toURI().toString(), 200, 200, true, true);
                attachmentList.getChildren().add(new ImageView(img));
            }
            this.note.addFiles(imageList);
        }
    }
}
