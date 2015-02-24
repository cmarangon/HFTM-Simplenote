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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import simplenote.model.Note;
import simplenote.model.Settings;

/**
 * @author Claudio Marangon, Ljubisa Markovic
 *
 */
public class NoteOverviewController extends RootController {

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

    @FXML
    private ToggleGroup sortDirection;

    @FXML
    private RadioButton sortUpButton;

    @FXML
    private RadioButton sortDownButton;

    @FXML
    private ToggleGroup sortType;

    @FXML
    private MenuButton sortTypeLabel;
    
    @FXML
    private RadioMenuItem sortOnTitle;

    @FXML
    private RadioMenuItem sortOnCreationdate;

    @FXML
    private RadioMenuItem sortOnModificationdate;

    /**
     * Constructor
     * 
     * Using a lazy singleton implementation
     */
    public NoteOverviewController() {
        this.rc = RootController.getInstance();

        for (Note note : this.rc.getVault().getNotes()) {
            this.noteData.add(note);
        }
    }

    @FXML
    public void initialize() {
        // fill listview with data
        this.noteList.setItems(this.noteData);

        // initialize the statustext
        updateStatus();

        // handle the functionality when another note gets selected
        noteList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            currentNote = newValue;
            WebEngine we = textField.getEngine();
            
            // save selected item in persistent settings
            rc.getSettings().setSelectedElementIndex(noteList.getSelectionModel().getSelectedIndex());
            
            // load values of selected item into view
            if (currentNote != null) {

                titleLabel.setText(currentNote.getTitle());
                dateLabel.setText(ViewHelper.formatDate(currentNote.getCreationDate()));
                // clear and rebuild link list
                linkData.clear();
                if (currentNote.getLinkList() != null) {
                    linkData.addAll(currentNote.getLinkList());
                }
                linkList.setItems(linkData);

                // clear and rebuild picture list
                pictureList.getChildren().clear();
                if (currentNote.getPictureList() != null) {
                    for (File file : currentNote.getPictureList()) {
                        pictureList.getChildren().add(ViewHelper.createImageView(file));
                    }
                }
                
                we.loadContent(ViewHelper.makeContentUnwritable(currentNote.getText()));
            } else {
                titleLabel.setText("");
                dateLabel.setText("");
                we.loadContent("<em>keine Notizen vorhanden</em>");
            }
        });

        // handle search
        FilteredList<Note> filteredData = new FilteredList<>(this.noteData, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(note -> {
                rc.getSettings().setSearchText(newValue);

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (note.getTitle().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (note.getText().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }
                return false;
            });

            // update status
            updateStatus();
        });

        SortedList<Note> sortedData = new SortedList<>(filteredData);

        // handle sorting
        ChangeListener<Toggle> sortToggleListener = new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (sortDirection.getSelectedToggle() != null && sortType.getSelectedToggle() != null) {
                    
                    if (sortDirection.getSelectedToggle().equals(sortDownButton)) {
                        rc.getSettings().setSortDirection(ViewHelper.DESC);
                    } else {
                        rc.getSettings().setSortDirection(ViewHelper.ASC);
                    }
                    
                    Toggle selectedToggle = sortType.getSelectedToggle(); 
                    if (selectedToggle.equals(sortOnCreationdate)) {
                        sortTypeLabel.setText(sortOnCreationdate.getText());
                        sortedData.setComparator((Note n1, Note n2) -> n1.getCreationDate().compareTo(n2.getCreationDate()) * rc.getSettings().getSortDirection());
                    } else if (selectedToggle.equals(sortOnModificationdate)) {
                        sortTypeLabel.setText(sortOnModificationdate.getText());
                        sortedData.setComparator((Note n1, Note n2) -> n1.getModificationDate().compareTo(n2.getModificationDate()) * rc.getSettings().getSortDirection());
                    } else {
                        sortTypeLabel.setText(sortOnTitle.getText());
                        sortedData.setComparator((Note n1, Note n2) -> n1.getTitle().toLowerCase().compareTo(n2.getTitle().toLowerCase()) * rc.getSettings().getSortDirection());
                    }
                }
            }
        };
        sortDirection.selectedToggleProperty().addListener(sortToggleListener);
        sortType.selectedToggleProperty().addListener(sortToggleListener);

        // load sorted and filtered data into list
        noteList.setItems(sortedData);

        // save settings persisten for future use
        Settings settings = new Settings();
        this.searchField.setText(settings.getSearchText());
        if (settings.getSortDirection() == ViewHelper.ASC) {
            this.sortUpButton.setSelected(true);
        } else {
            this.sortDownButton.setSelected(true);
        }
        noteList.getSelectionModel().select(settings.getSelectedElementIndex());
    }

    @FXML
    public void showAddNote() {
        this.rc.showAddNote();
    }

    @FXML
    public void showEditNote() {
        this.rc.showEditNote(this.noteList.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void deleteNote() {
        // check if something is selected and selected note is in data
        if (this.currentNote != null && this.noteData.contains(this.currentNote)) {

            // remove from storage
            this.rc.getVault().delete(this.currentNote);

            // remove from list
            this.noteData.remove(this.currentNote);
            
            // update status
            updateStatus();
        }
    }
    
    private void updateStatus() {
        this.statusLabel.setText("Status: " + this.noteList.getItems().size() + " / " + this.noteData.size());
    }
}
