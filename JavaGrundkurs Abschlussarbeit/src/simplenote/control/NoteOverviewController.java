/**
 * 
 */
package simplenote.control;

import java.io.File;
import java.net.URL;
import java.util.Comparator;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
    private StringProperty statusText;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private RadioButton sortingUpButton;
    
    @FXML
    private RadioButton sortingDownButton;
    
    @FXML
    private ToggleGroup sortingGroup;
    
    
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
        
        statusLabel.setText("Status: " + this.noteData.size() + " / " + this.noteData.size());
        
        
        /* Notes overview */
        noteList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Note>() {

            @Override
            public void changed(ObservableValue<? extends Note> observable, Note oldValue, Note newValue) {
                
                WebEngine we = textField.getEngine();
                currentNote = newValue;
                if(newValue != null) {
                    
                    titleLabel.setText(newValue.getTitle());
                    dateLabel.setText("" + newValue.getCreationDate()); // TODO: pretty format the date
                    
                    // clear and rebuild link list
                    linkData.clear();
                    if(newValue.getLinkList() != null) {
                        linkData.addAll(newValue.getLinkList());
                    }
                    linkList.setItems(linkData);
                    
                    // clear and rebuild picture list
                    pictureList.getChildren().clear();
                    if(newValue.getPictureList() != null) {
                        ImageView iv;
                        for(File img : newValue.getPictureList()) {
                            iv = new ImageView();
                            iv.setImage(new Image(img.toURI().toString()));
                            iv.setFitHeight(200);
                            iv.setFitWidth(200);
                            iv.setPreserveRatio(true);
                            iv.setSmooth(true);
                            pictureList.getChildren().add(iv);
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
        
        /* Notes overview search field */
        FilteredList<Note> filteredData = new FilteredList<>(this.noteData, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(note -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (note.getTitle().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches first name.
                } else if (note.getText().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
            
            statusLabel.setText("Status: " + filteredData.size() + " / " + this.noteData.size());
        });
        
        SortedList<Note> sortedData = new SortedList<>(filteredData);
        
        
        
        /* Notes overview sorting */
        sortingGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(sortingDownButton)) {
                sortedData.setComparator((Note n1, Note n2) -> n1.getTitle().toLowerCase().compareTo(n2.getTitle().toLowerCase()) * -1);
            } else if(newValue.equals(sortingUpButton)) {
                sortedData.setComparator((Note n1, Note n2) -> n1.getTitle().toLowerCase().compareTo(n2.getTitle().toLowerCase()));
            }
        });
        
        noteList.setItems(sortedData);
    }
    
    @FXML
    public void addNote() {
      this.rc.showAddNote();
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
