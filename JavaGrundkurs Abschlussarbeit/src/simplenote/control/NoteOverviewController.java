/**
 * 
 */
package simplenote.control;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import simplenote.model.Note;

/**
 * @author Claudio Marangon, Ljubisa Markovic
 *
 */
public class NoteOverviewController extends FXController {

    private RootController rc;
    private Note currentNote;

    @FXML
    private Label titleLabel;

    @FXML
    private Label creationDateLabel;
    
    @FXML
    private Label modificationDateLabel;

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
    private Label newNoteLabel;
    
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

        // update buttons
        newNoteLabel.setTooltip(new Tooltip("Neue Notiz erstellen"));
        editButton.setGraphic(new ImageView(EDIT_ICON));
        editButton.setText("");
        editButton.setTooltip(new Tooltip("Notiz bearbeiten"));
        deleteButton.setGraphic(new ImageView(DELETE_ICON));
        deleteButton.setText("");
        deleteButton.setTooltip(new Tooltip("Notiz löschen"));
        sortTypeLabel.setTooltip(new Tooltip("Sortieren nach"));
        sortUpButton.setTooltip(new Tooltip("Aufsteigend"));
        sortDownButton.setTooltip(new Tooltip("Absteigend"));

        // handle the functionality when another note gets selected
        noteList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            currentNote = newValue;
            WebEngine we = textField.getEngine();
            
            // save selected item in persistent preferences
            rc.getPreferences().putInt("notes.selectedNote", noteList.getSelectionModel().getSelectedIndex());
            
            // load values of selected item into view
            if (currentNote != null) {

                titleLabel.setText(currentNote.getTitle());
                creationDateLabel.setText(ViewHelper.formatDate(currentNote.getCreationDate(), "Erstellt: "));
                creationDateLabel.getStyleClass().removeAll(CSS_HIDDEN);
                creationDateLabel.setMouseTransparent(false);
                
                modificationDateLabel.setText(ViewHelper.formatDate(currentNote.getModificationDate(), "Geändert: ", "nie"));
                modificationDateLabel.getStyleClass().add(CSS_HIDDEN);
                modificationDateLabel.setMouseTransparent(true);

                creationDateLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        creationDateLabel.getStyleClass().add(CSS_HIDDEN);
                        creationDateLabel.setMouseTransparent(true);
                        
                        modificationDateLabel.getStyleClass().removeAll(CSS_HIDDEN);
                        modificationDateLabel.setMouseTransparent(false);
                    }
                });

                modificationDateLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        modificationDateLabel.getStyleClass().add(CSS_HIDDEN);
                        modificationDateLabel.setMouseTransparent(true);
                        
                        creationDateLabel.getStyleClass().removeAll(CSS_HIDDEN);
                        creationDateLabel.setMouseTransparent(false);
                    }
                });

                // clear and rebuild link list
                linkData.clear();
                if (currentNote.getLinkList() != null) {
                    linkData.addAll(currentNote.getLinkList());
                    linkList.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            URL url = linkList.getSelectionModel().getSelectedItem();
                            
                            // Just open it
                            try {
                                Desktop.getDesktop().browse(url.toURI());
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                linkList.setItems(linkData);

                // clear and rebuild picture list
                pictureList.getChildren().clear();
                pictureList.setAlignment(Pos.TOP_RIGHT);
                if (currentNote.getPictureList() != null) {
                    for (File file : currentNote.getPictureList()) {
                        ImageView iv = ViewHelper.createImageView(file);
                        iv.getStyleClass().add(CSS_CLICKABLE);
                        iv.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                                    Desktop desktop = Desktop.getDesktop();
                                    try {
                                        desktop.open(file);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        pictureList.getChildren().add(iv);
                    }
                }
                
                we.loadContent(ViewHelper.makeContentUnwritable(currentNote.getText()));
            } else {
                titleLabel.setText("");
                creationDateLabel.setText("");
                pictureList.getChildren().clear();
                linkList.getItems().clear();
                we.loadContent("<em>keine Notiz ausgewählt</em>");
            }
        });

        // handle search
        FilteredList<Note> filteredData = new FilteredList<>(this.noteData, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(note -> {
                rc.getPreferences().put("search.text", newValue);

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
                        rc.getPreferences().putInt("sort.direction", ViewHelper.DESC);
                    } else {
                        rc.getPreferences().putInt("sort.direction", ViewHelper.ASC);
                    }
                    
                    Toggle selectedToggle = sortType.getSelectedToggle(); 
                    if (selectedToggle.equals(sortOnCreationdate)) {
                        sortTypeLabel.setText(sortOnCreationdate.getText());
                        sortedData.setComparator((Note n1, Note n2) -> n1.getCreationDate().compareTo(n2.getCreationDate()) * rc.getPreferences().getInt("sort.direction", ViewHelper.ASC));
                    } else if (selectedToggle.equals(sortOnModificationdate)) {
                        sortTypeLabel.setText(sortOnModificationdate.getText());
                        sortedData.setComparator((Note n1, Note n2) -> n1.getModificationDate().compareTo(n2.getModificationDate()) * rc.getPreferences().getInt("sort.direction", ViewHelper.ASC));
                    } else {
                        sortTypeLabel.setText(sortOnTitle.getText());
                        sortedData.setComparator((Note n1, Note n2) -> n1.getTitle().toLowerCase().compareTo(n2.getTitle().toLowerCase()) * rc.getPreferences().getInt("sort.direction", ViewHelper.ASC));
                    }
                }
            }
        };
        sortDirection.selectedToggleProperty().addListener(sortToggleListener);
        sortType.selectedToggleProperty().addListener(sortToggleListener);

        // load sorted and filtered data into list
        noteList.setItems(sortedData);

        // save preferences
        this.searchField.setText(this.rc.getPreferences().get("search.text", ""));
        if (this.rc.getPreferences().getInt("sort.direction", 1) == ViewHelper.ASC) {
            this.sortUpButton.setSelected(true);
        } else {
            this.sortDownButton.setSelected(true);
        }
        noteList.getSelectionModel().select(this.rc.getPreferences().getInt("notes.selectedNote", 0));
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
            
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Löschen bestätigen");
            alert.setHeaderText("Sie sind dabei eine Notiz zu löschen");
            alert.setContentText("Wollen Sie '" + this.currentNote.getTitle() + "' wirklich löschen?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // remove from storage
                this.rc.getVault().delete(this.currentNote);

                // remove from list
                this.noteData.remove(this.currentNote);
                
                // update status
                updateStatus();
            }
        }
    }
    
    private void updateStatus() {
        this.statusLabel.setText("Status: " + this.noteList.getItems().size() + " / " + this.noteData.size());
    }
}
