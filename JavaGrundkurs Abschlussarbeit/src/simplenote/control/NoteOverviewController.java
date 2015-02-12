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
    
    @FXML
    private Label lblStatusText;
    
    
    
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
        lblStatusText.setText("Anzahl gespeicherte Notizen: " + this.noteData.size());
        
        
        noteList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Note>() {

            @Override
            public void changed(ObservableValue<? extends Note> observable, Note oldValue, Note newValue) {
                
                WebEngine we = noteText.getEngine();
                currentNote = newValue;
                
                if(newValue != null) {
                    
                    noteTitle.setText(newValue.getTitle());
                    noteDate.setText("" + newValue.getCreationDate()); // TODO: pretty format the date
                    
                    
                    we.loadContent(newValue.getText());
                } else {
                    noteTitle.setText("");
                    noteDate.setText("");
                    we.loadContent("<em>keine Notizen vorhanden</em>");
                }
            }
        });
    }
    
    @FXML
    public void editNote() {
        this.rc.setSelectedNote(this.noteList.getSelectionModel().getSelectedItem());
        this.rc.showEditNote();
    }
    
    @FXML
    public void deleteNote() {
        if(this.currentNote != null && this.noteData.contains(this.currentNote)) {
            
            // remove from storage
            this.rc.getVault().delete(this.currentNote);
            
            // remove from list
            this.noteData.remove(this.currentNote);
            
        }
    }
}
