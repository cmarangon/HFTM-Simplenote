/**
 * 
 */
package simplenote.control;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import simplenote.model.Note;

/**
 * @author Claudio Marangon, Ljubisa Markovic
 *
 */
public class AddNoteController extends FXNoteController {

    private RootController rc;
    private Note newNote;

    @FXML
    private Button saveButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField titleField;

    @FXML
    private HTMLEditor textField;

    @FXML
    private ToggleButton shareButton;

    @FXML
    private VBox pictureList;

    @FXML
    private TextField linkField;

    @FXML
    private ListView<URL> linkList;

    @FXML
    private Button addLink;

    @FXML
    private HBox modifyLink;


    /**
     * 
     */
    public AddNoteController() {
        this.rc = RootController.getInstance();
    }

    @FXML
    public void initialize() {
        // update buttons
        super.initButtons(saveButton, backButton);

        modifyLink.getStyleClass().add(CSS_HIDDEN);
        modifyLink.setMouseTransparent(true);

        this.newNote = new Note();
        this.linkList.setItems(linkData);
        addContextMenuToLinkList(linkList, linkField, addLink, modifyLink);

        pictureList.setAlignment(Pos.TOP_RIGHT);

        // drag & drop for images 
        pictureList.setOnDragOver(new EventHandler<DragEvent>(){
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                } else {
                    event.consume();
                }
            }
        });
        pictureList.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    for (File file : db.getFiles()) {
                        addPictureToList(pictureList, file);
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });

        // drag & drop for links
        linkList.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasUrl()) {
                    event.acceptTransferModes(TransferMode.COPY);
                } else {
                    event.consume();
                }
            }
        });
        linkList.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasUrl()) {
                    success = true;
                    addLinkToList(db.getUrl());
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
        
    }


    /* FXML Actions */

    @FXML
    protected void showOverview() {
        this.rc.showOverview();
    };

    @FXML
    public void saveNote() {
        // text is required
        if(! this.textField.getHtmlText().equals(HTML_EMPTY)) {
            String nTitle = this.titleField.getText();
            String nText = this.textField.getHtmlText();

            if(nTitle.isEmpty()) {
                nTitle = ViewHelper.formatDate(new Date(), null);
            }

            this.newNote.setTitle(nTitle);
            this.newNote.setText(nText);
            this.newNote.setPictureList(this.pictureData);
            ArrayList<URL> al = new ArrayList<URL>();
            for(URL url : this.linkData) {
                al.add(url);
            }
            this.newNote.setLinkList(al); 

            this.rc.getVault().add(newNote);
            if(this.rc.getVault().save()){
                this.rc.showOverview();
            } else {
            }
        } else {
            this.textField.getStyleClass().add(CSS_ERROR);
        }
    }

    @FXML
    public void addLink() {
        if (this.addLinkToList(this.linkField.getText())) {
            // success
            this.linkField.getStyleClass().remove(CSS_ERROR);
            this.linkField.setText(DEFAULT_URL);
        } else {
            // error
            this.linkField.getStyleClass().add(CSS_ERROR);
        }
    }

    @FXML
    public void saveLink() {
        
    }

    @FXML
    public void choosePicture() {
        FileChooser fileChooser = new FileChooser();
        List<File> pList = fileChooser.showOpenMultipleDialog(this.rc.getStage());
        addPictures(this.pictureList, pList);
    }
}
