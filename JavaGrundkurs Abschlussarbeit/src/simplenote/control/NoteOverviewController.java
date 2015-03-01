/**
 * simpleNote, a better way to store your notes
 * Abschlussarbeit der HFTM Grenchen
 * Klasse Java Grundlagen II
 */
package simplenote.control;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;
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
 * Controller class for note overview
 * 
 * @author Claudio Marangon, Ljubisa Markovic
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
        rc = RootController.getInstance();

        for (Note note : rc.getVault().getNotes()) {
            noteData.add(note);
        }
    }

    /**
     * Gets called when loaded with FXMLLoader
     */
    @FXML
    public void initialize() {
        
        // fill listview with data
        noteList.setItems(noteData);

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

        // restore preferences
        searchField.setText(rc.getPreferences().get(PREF_SEARCH_TEXT, ""));
        RadioButton _sortDirection = null;
        if (rc.getPreferences().getInt(PREF_SORT_DIR, ViewHelper.ASC) == ViewHelper.ASC) {
            _sortDirection = sortUpButton;
        } else {
            _sortDirection = sortDownButton;
        }
        RadioMenuItem _sortType = null;
        switch(rc.getPreferences().getInt(PREF_SORT_TYPE, ViewHelper.SORT_TITLE)) {
            case ViewHelper.SORT_CREATION_DATE:
                _sortType = sortOnCreationdate;
                break;
            case ViewHelper.SORT_MODIFICATION_DATE:
                _sortType = sortOnModificationdate;
                break;
            default:
                _sortType = sortOnTitle;
        }

        // handle the functionality when another note gets selected
        noteList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            currentNote = newValue;
            WebEngine we = textField.getEngine();
            
            // save selected item in persistent preferences
            rc.getPreferences().putInt(PREF_SELECTED_NOTE, noteList.getSelectionModel().getSelectedIndex());
            
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
                        ImageView iv = ViewHelper.createThumbnailImageView(file);
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
        FilteredList<Note> filteredData = new FilteredList<>(noteData, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(note -> {
                rc.getPreferences().put(PREF_SEARCH_TEXT, newValue);

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
                        rc.getPreferences().putInt(PREF_SORT_DIR, ViewHelper.DESC);
                    } else {
                        rc.getPreferences().putInt(PREF_SORT_DIR, ViewHelper.ASC);
                    }
                    
                    Toggle selectedToggle = sortType.getSelectedToggle();
                    int sortDir = rc.getPreferences().getInt(PREF_SORT_DIR, ViewHelper.ASC);
                    if (selectedToggle.equals(sortOnCreationdate)) {
                        rc.getPreferences().putInt(PREF_SORT_TYPE, ViewHelper.SORT_CREATION_DATE);
                        sortTypeLabel.setText(sortOnCreationdate.getText());
                        sortedData.setComparator((Note n1, Note n2) -> n1.getCreationDate().compareTo(n2.getCreationDate()) * sortDir);
                    } else if (selectedToggle.equals(sortOnModificationdate)) {
                        rc.getPreferences().putInt(PREF_SORT_TYPE, ViewHelper.SORT_MODIFICATION_DATE);
                        sortTypeLabel.setText(sortOnModificationdate.getText());
                        
                        // since modification date can be null on unmodified notes we need to do a special check
                        sortedData.setComparator(new Comparator<Note>() {
                            @Override
                            public int compare(Note n1, Note n2) {
                                int res = 0;
                                Date d1 = n1.getModificationDate();
                                Date d2 = n2.getModificationDate();
                                if (d1 != null && d2 == null) {
                                    res = 1;
                                } else if (d1 == null && d2 != null) {
                                    res = -1;
                                } else if (d1 == null && d2 == null) {
                                    res = 0;
                                } else {
                                    res = d1.compareTo(d2);
                                }
                                return res * sortDir;
                            }
                        });
                    } else {
                        rc.getPreferences().putInt(PREF_SORT_TYPE, ViewHelper.SORT_TITLE);
                        sortTypeLabel.setText(sortOnTitle.getText());
                        sortedData.setComparator((Note n1, Note n2) -> n1.getTitle().toLowerCase().compareTo(n2.getTitle().toLowerCase()) * sortDir);
                    }
                }
            }
        };

        sortDirection.selectedToggleProperty().addListener(sortToggleListener);
        sortType.selectedToggleProperty().addListener(sortToggleListener);

        // load sorted and filtered data into list
        noteList.setItems(sortedData);
        if (rc.getSelectedNote() == null) {
            int index = rc.getPreferences().getInt(PREF_SELECTED_NOTE, 0);
            noteList.getSelectionModel().select(index);
            Note initialNote = noteList.getSelectionModel().getSelectedItem();
            rc.setSelectedNote(initialNote);
        }
        noteList.getSelectionModel().select(rc.getSelectedNote());
        sortDirection.selectToggle(_sortDirection);
        sortType.selectToggle(_sortType);
    }

    /**
     * Shows the add note layout
     */
    @FXML
    public void showAddNote() {
        rc.showAddNote();
    }

    /**
     * Shows the edit note layout
     */
    @FXML
    public void showEditNote() {
        rc.showEditNote();
    }

    /**
     * Deletes the currently selected note
     */
    @FXML
    public void deleteNote() {
        // check if something is selected and selected note is in data
        if (currentNote != null && noteData.contains(currentNote)) {
            
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Löschen bestätigen");
            alert.setHeaderText("Sie sind dabei eine Notiz zu löschen");
            alert.setContentText("Wollen Sie '" + currentNote.getTitle() + "' wirklich löschen?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // remove from storage
                rc.getVault().delete(currentNote);

                // remove from list
                noteData.remove(currentNote);
                
                // update status
                updateStatus();
            }
        }
    }

    /**
     * Updates the status label. Shows how many notes are currently displayed and
     * how many notes are available in total.
     */
    private void updateStatus() {
        statusLabel.setText("Status: " + noteList.getItems().size() + " / " + noteData.size());
    }
}
