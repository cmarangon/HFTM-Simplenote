/**
 * 
 */
package simplenote.control;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import simplenote.model.Note;

/**
 * @author Claudio Marangon, Ljubisa Markovic
 *
 */
public class NoteOverviewController {

    private RootController rc;
    private Note currentNote;
    
    
    @FXML
    private Label noteTitle;
    
    @FXML
    private Label noteDate;
    
    @FXML
    private WebView noteText;
    
    @FXML
    private ListView<Note> noteList;
    
    @FXML
    private Button editBtn;
    
    @FXML
    private Button deleteBtn;
    
    
    
    private ObservableList<Note> noteData = FXCollections.observableArrayList();
    
    
    /**
     * 
     */
    public NoteOverviewController() {
        this.rc = RootController.getInstance();
        
        for(Note note : this.rc.getVault().getNotes()) {
            this.noteData.add(note);
        }
    }
    
    @FXML
    public void initialize() {
        this.noteList.setItems(this.noteData);
        
        
        noteList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Note>() {

            @Override
            public void changed(ObservableValue<? extends Note> observable, Note oldValue, Note newValue) {
                currentNote = newValue;
                noteTitle.setText(newValue.getTitle());
                noteDate.setText("" + newValue.getCreationDate()); // TODO: pretty format the date
                
                WebEngine we = noteText.getEngine();
                we.loadContent(newValue.getText());
            }
        });
    }
    
    @FXML
    public void editNote() {
        // show edit layout for this note
    }
    
    @FXML
    public void deleteNote() {
        this.rc.getVault().delete(this.currentNote);
        this.rc.showOverview();
    }
}
