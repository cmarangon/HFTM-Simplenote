/**
 * simpleNote, a better way to store your notes
 * Abschlussarbeit der HFTM Grenchen
 * Klasse Java Grundlagen II
 */
package simplenote.control;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;

import javax.activation.MimetypesFileTypeMap;

import simplenote.model.Note;

/**
 * Controller class, responsible for note creation or editing.
 * Provides methods used by the AddNoteController and EditNoteController
 * 
 * @author Claudio Marangon, Ljubisa Markovic
 */
public class FXNoteController extends FXController {

    protected RootController rc;

    protected Button saveButton;
    protected Button backButton;

    protected TextField titleField;
    protected HTMLEditor textField;
    protected VBox pictureList;
    protected ArrayList<File> pictureData = new ArrayList<File>();
    protected ListView<URL> linkList;
    protected ObservableList<URL> linkData = FXCollections.observableArrayList();
    protected TextField linkField;
    protected Button addLinkButton;
    protected HBox modifyLinkList;
    protected Button acceptButton;
    protected Button cancelButton;


    /**
     * Constructor
     */
    public FXNoteController() {
        rc = RootController.getInstance();
    }

    /**
     * Initialize the save and back buttons.
     * Sets the icon, removes the text and adds a meaningful tooltip.
     * 
     * @param saveButton
     * @param backButton
     */
    protected void initEditButtons(Button saveButton, Button backButton) {
        this.saveButton = saveButton;
        this.saveButton.setGraphic(new ImageView(SAVE_ICON));
        this.saveButton.setText("");
        this.saveButton.setTooltip(new Tooltip("Änderungen übernehmen"));

        this.backButton = backButton;
        this.backButton.setGraphic(new ImageView(BACK_ICON));
        this.backButton.setText("");
        this.backButton.setTooltip(new Tooltip("Zurück zur Übersicht"));
        this.backButton.setOnAction((event) -> {
            rc.showOverview();
        });
    }

    /**
     * Initialize all the fields which are responsible for the note.
     * Enables drag&drop.
     * 
     * @param titleField
     * @param textField
     * @param pictureList
     * @param linkList
     */
    protected void initNoteFields(TextField titleField, HTMLEditor textField, VBox pictureList, ListView<URL> linkList) {
        this.titleField = titleField;

        this.textField = textField;

        this.pictureList = pictureList;
        this.pictureList.setAlignment(Pos.TOP_RIGHT);

        this.linkList = linkList;

        initDragAndDrop();
    }

    /**
     * Initalize all the elements required to add and modify links.
     * Sets the icon, removes the text and adds a meaningful tooltip.
     * 
     * @param linkField
     * @param modifyLinkList
     * @param addLinkButton
     * @param acceptButton
     * @param cancelButton
     */
    protected void initLinkElements(TextField linkField, HBox modifyLinkList, Button addLinkButton, Button acceptButton, Button cancelButton) {
        this.linkField = linkField;

        this.modifyLinkList = modifyLinkList;
        this.modifyLinkList.getStyleClass().add(CSS_HIDDEN);
        this.modifyLinkList.setMouseTransparent(true);

        this.addLinkButton = addLinkButton;

        this.acceptButton = acceptButton;
        this.acceptButton.setGraphic(new ImageView(DONE_ICON));
        this.acceptButton.setText("");
        this.acceptButton.setTooltip(new Tooltip("Link übernehmen"));

        this.cancelButton = cancelButton;
        this.cancelButton.setGraphic(new ImageView(CLEAR_ICON));
        this.cancelButton.setText("");
        this.cancelButton.setTooltip(new Tooltip("Abbrechen"));

        addContextMenuOnLinkList();
        initLinkCustomActions();
    }

    /**
     * Adds a contextmenu to the link list
     */
    private void addContextMenuOnLinkList() {
        // Context menu for links
        ContextMenu contextMenu = new ContextMenu();
        MenuItem edit = new MenuItem("Edit");
        edit.setOnAction((event) -> {
            // set text in field and give it focus
            linkField.setText(linkList.getSelectionModel().getSelectedItem().toString());
            linkField.requestFocus();

            linkList.setDisable(true);

            activateModifyLink();
        });
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction((event) -> {
            linkData.remove(linkList.getSelectionModel().getSelectedItem());
        });
        contextMenu.getItems().addAll(edit, delete);
        linkList.setContextMenu(contextMenu);
    }

    /**
     * Adds the link from the link text field to the note
     */
    protected void addLink() {
        if (addLinkToList(linkField.getText())) {
            linkField.getStyleClass().remove(CSS_ERROR);
            linkField.setText(DEFAULT_URL);
        } else {
            linkField.getStyleClass().add(CSS_ERROR);
            linkField.requestFocus();
        }
    }

    /**
     * Tries to add a new link
     * 
     * @param string_url
     * @return succes status as boolean
     */
    private boolean addLinkToList(String string_url) {
        return addLinkToList(string_url, -1);
    }

    /**
     * Tries to update a link at a given index of the link list
     * 
     * @param string_url
     * @param index
     * @return success status as boolean
     */
    private boolean addLinkToList(String string_url, int index) {
        boolean success = true;

        try {
            URL url = new URL(string_url);
            if (index > -1) {
                linkData.set(index, url);
            } else {
                linkData.add(url);
            }
        } catch (MalformedURLException e) {
            success = false;
        }

        return success;
    }

    /**
     * Initialize custom actions such as accept or cancel link modification
     */
    private void initLinkCustomActions() {
        addLinkButton.setOnAction((event) -> {
            addLink();
        });
        acceptButton.setOnAction((event) -> {
            if (addLinkToList(linkField.getText(), linkList.getSelectionModel().getSelectedIndex())) {
                // success
                linkField.getStyleClass().remove(CSS_ERROR);
                linkField.setText(DEFAULT_URL);
                linkList.setDisable(false);
                linkList.requestFocus();

                deactivateModifyLink();
            } else {
                linkField.getStyleClass().add(CSS_ERROR);
            }
        });
        cancelButton.setOnAction((event) -> {
            linkField.setText(DEFAULT_URL);
            linkList.setDisable(false);
            linkList.requestFocus();
            
            deactivateModifyLink();
        });
    }

    /**
     * Activates the link modification elements
     */
    private void activateModifyLink() {
        // hide normal save button
        addLinkButton.getStyleClass().add(CSS_HIDDEN);
        addLinkButton.setMouseTransparent(true);

        // show new save and cancel buttons
        modifyLinkList.getStyleClass().remove(CSS_HIDDEN);
        modifyLinkList.setMouseTransparent(false);
    }

    /**
     * Deactivates the link modification elements
     */
    private void deactivateModifyLink() {
        // hide new save and cancel buttons
        modifyLinkList.getStyleClass().add(CSS_HIDDEN);
        modifyLinkList.setMouseTransparent(true);
        
        // show normal save button
        addLinkButton.getStyleClass().remove(CSS_HIDDEN);
        addLinkButton.setMouseTransparent(false);
    }

    /**
     * Loads the picture list on the basis of the picture data
     */
    protected void loadPictureList() {
        // first of all, clear the list 
        pictureList.getChildren().clear();

        // for every element in pictureData create a 
        // new construct:
        //
        // StackPane > ImageView of actual Picture
        //           > HBox > ImageView of RemoveIcon
        //                  > ImageView of MoveUpIcon
        //                  > ImageView of MoveDownIcon
        for (File file : pictureData) {
            StackPane sp = new StackPane();
            sp.setAlignment(Pos.TOP_RIGHT);

            ImageView iv = ViewHelper.createThumbnailImageView(file);

            HBox hoverMenu = new HBox();
            hoverMenu.setMaxHeight(26.0);
            hoverMenu.setPadding(new Insets(2));
            hoverMenu.getStyleClass().add("blurBG");
            ImageView removeIcon = new ImageView();
            removeIcon.setImage(REMOVE_ICON);
            removeIcon.getStyleClass().add(CSS_CLICKABLE);
            removeIcon.setPickOnBounds(true);
            removeIcon.setOnMouseClicked((event) -> {
                removePicture(pictureData.indexOf(file));
            });
            if (pictureData.size() > 1) {
                if (pictureData.indexOf(file) > 0)  {
                    ImageView moveUpIcon = new ImageView();
                    moveUpIcon.setImage(MOVE_UP_ICON);
                    moveUpIcon.getStyleClass().add(CSS_CLICKABLE);
                    moveUpIcon.setPickOnBounds(true);
                    moveUpIcon.setOnMouseClicked((event) -> {
                        int index = pictureData.indexOf(file);
                        File nextFile = pictureData.get(index-1);
                        pictureData.set(index, nextFile);
                        pictureData.set(index-1, file);
                        
                        loadPictureList();
                    });
                    hoverMenu.getChildren().add(moveUpIcon);
                }
                if (pictureData.indexOf(file) != pictureData.size()-1) {
                    ImageView moveDownIcon = new ImageView();
                    moveDownIcon.setImage(MOVE_DOWN_ICON);
                    moveDownIcon.getStyleClass().add(CSS_CLICKABLE);
                    moveDownIcon.setPickOnBounds(true);
                    moveDownIcon.setOnMouseClicked((event) -> {
                        int index = pictureData.indexOf(file);
                        File prevFile = pictureData.get(index+1);
                        pictureData.set(index, prevFile);
                        pictureData.set(index+1, file);
                        
                        loadPictureList();
                    });
                    hoverMenu.getChildren().add(moveDownIcon);
                }
            }
            hoverMenu.getChildren().add(removeIcon);
            hoverMenu.setMaxWidth(hoverMenu.getChildren().size()*26.0);

            sp.getChildren().addAll(iv, hoverMenu);

            pictureList.getChildren().add(sp);
        }
    }

    /**
     * Shows a file chooser dialog and adds the choosen images to the note
     */
    protected void choosePicture() {
        FileChooser fileChooser = new FileChooser();
        List<File> pList = fileChooser.showOpenMultipleDialog(rc.getStage());
        addPictures(pList);
    }

    /**
     * Takes a list of files and adds the pictures of this list to the note
     * 
     * @param pList
     */
    private void addPictures(List<File> pList) {
        if (pList != null) {
            for(File f : pList) {
                String mimetype= new MimetypesFileTypeMap().getContentType(f);
                String type = mimetype.split("/")[0];
                if(type.equals("image")) {
                    addPictureToList(f);
                }
            }
        }
    }

    /**
     * Adds the given file to the note and updates the picture list
     *
     * @param file
     */
    private void addPictureToList(File file) {
        pictureData.add(file);
        loadPictureList();
    }

    /**
     * Removes a picture from the note and updates the picture list 
     * 
     * @param index
     */
    private void removePicture(int index) {
        if (index > -1) {
            pictureList.getChildren().remove(index);
            pictureData.remove(index);
            loadPictureList();
        }
    }

    /**
     * Enables drag&drop on the picture list and link list
     */
    private void initDragAndDrop() {
        // drag & drop for images 
        pictureList.setOnDragOver((event) -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            } else {
                event.consume();
            }
        });
        pictureList.setOnDragDropped((event) -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                for (File file : db.getFiles()) {
                    addPictureToList(file);
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        // drag & drop for links
        linkList.setOnDragOver((event) -> {
            Dragboard db = event.getDragboard();
            if (db.hasUrl()) {
                event.acceptTransferModes(TransferMode.COPY);
            } else {
                event.consume();
            }
        });
        linkList.setOnDragDropped((event) -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasUrl()) {
                success = true;
                addLinkToList(db.getUrl());
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    /**
     * Saves the given note to the vault. If isNew flag is set, adds the note as a new note.
     * 
     * @param note
     * @param isNew
     */
    protected void saveNote(Note note, boolean isNew) {
        // text is required
        if (! textField.getHtmlText().equals(HTML_EMPTY)) {
            String nTitle = titleField.getText();
            String nText = textField.getHtmlText();

            if(nTitle.isEmpty()) {
                Date creationDate = new Date();
                if (! isNew) {
                    creationDate = note.getCreationDate();
                }
                nTitle = ViewHelper.formatDate(creationDate, null);
            }

            note.setTitle(nTitle);
            note.setText(nText);
            if (isNew) {
                note.setModificationDate(new Date());
            }
            note.setPictureList(pictureData);
            ArrayList<URL> al = new ArrayList<URL>();
            for(URL url : linkData) {
                al.add(url);
            }
            note.setLinkList(al); 

            if (isNew) {
                rc.getVault().add(note);
            }
            if(rc.getVault().save()){
                rc.showOverview();
            }
        } else {
            textField.getStyleClass().add(CSS_ERROR);
        }
    }
}
