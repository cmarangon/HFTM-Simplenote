/**
 * 
 */
package simplenote.control;

import java.io.File;
import java.net.URL;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
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
    private Label titleLabel;
    
    @FXML
    private Label dateLabel;
    
    @FXML
    private WebView textField;
    
    @FXML
    private VBox pictureList;
    
    @FXML
    private ListView<URL> linkList;
    private ObservableList<URL> linkData = FXCollections.observableArrayList();
    
    @FXML
    private ListView<Note> noteList;
    private ObservableList<Note> noteData = FXCollections.observableArrayList();
    
    @FXML
    private Button editButton;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private TextField searchField;
    
    
    
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
        statusLabel.setText("Status: " + this.noteData.size() + " / 100");
        
        
        noteList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Note>() {

            @Override
            public void changed(ObservableValue<? extends Note> observable, Note oldValue, Note newValue) {
                
                WebEngine we = textField.getEngine();
                currentNote = newValue;
                if(newValue != null) {
                    
                    titleLabel.setText(newValue.getTitle());
                    dateLabel.setText("" + newValue.getCreationDate()); // TODO: pretty format the date
                    if(newValue.getLinkList() != null) {
                        linkData.addAll(newValue.getLinkList());
                        linkList.setItems(linkData);
                    }
                    if(newValue.getPictureList() != null) {
                        for(File f : newValue.getPictureList()) {
                            Image img = new Image(f.toURI().toString(), 200, 200, true, true);
                            pictureList.getChildren().add(new ImageView(img));
                        }
                    }
                    
                    // change content to noneditable mode
                    String html = newValue.getText();
                    if(html.contains("contenteditable=\"true\"")){
                        html=html.replace("contenteditable=\"true\"", "contenteditable=\"false\"");
                    }
                    
                    we.loadContent(html);
                } else {
                    titleLabel.setText("");
                    dateLabel.setText("");
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
        // check if something is selected and selected note is in data
        if(this.currentNote != null && this.noteData.contains(this.currentNote)) {
            
            // remove from storage
            this.rc.getVault().delete(this.currentNote);
            
            // remove from list
            this.noteData.remove(this.currentNote);
        }
    }
    
    @FXML
    public void change() {
        //System.out.println(this.searchText.getText());
        //this.noteData.f
    }
}
