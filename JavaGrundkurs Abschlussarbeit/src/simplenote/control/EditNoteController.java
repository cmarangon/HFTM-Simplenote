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
public class EditNoteController extends FXNoteController {

    private RootController rc;
    private Note note;

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
    public EditNoteController() {
        this.rc = RootController.getInstance();
    }

    @FXML
    public void initialize() {
        // update buttons
        initButtons(saveButton, backButton);

        modifyLink.getStyleClass().add(CSS_HIDDEN);
        modifyLink.setMouseTransparent(true);

        this.note = this.rc.getSelectedNote();

        // title
        titleField.setText(this.note.getTitle());

        // text
        textField.setHtmlText(this.note.getText());

        // links
        for (URL url : this.note.getLinkList()) {
            linkData.add(url);
        }
        linkList.setItems(linkData);
        addContextMenuToLinkList(linkList, linkField, addLink, modifyLink);
        
        // pictures
        pictureList.setAlignment(Pos.TOP_RIGHT);
        for (File file : this.note.getPictureList()) {
            pictureData.add(file);
        }
        loadPictureList(this.pictureList);
        
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
                    for (File file:db.getFiles()) {
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
        if (!this.textField.getHtmlText().equals(HTML_EMPTY)) {

            String nTitle = this.titleField.getText();
            String nText = this.textField.getHtmlText();

            if (nTitle.isEmpty()) {
                nTitle = ViewHelper.formatDate(this.note.getCreationDate(), null);
            }

            this.note.setTitle(nTitle);
            this.note.setText(nText);
            this.note.setModificationDate(new Date());
            this.note.setPictureList(this.pictureData);
            ArrayList<URL> al = new ArrayList<URL>();
            for (URL url : this.linkData) {
                al.add(url);
            }
            this.note.setLinkList(al);

            if (this.rc.getVault().save()) {
                this.rc.showOverview();
            } else {
            }
        } else {
            this.textField.getStyleClass().add(CSS_ERROR);
        }
    }

    @FXML
    public void addLink() {
        if(this.linkField.getText().startsWith(DEFAULT_URL) && 
           this.linkField.getText().length() > DEFAULT_URL.length()) {
            
            if (this.addLinkToList(this.linkField.getText())) {
                // success
                this.linkField.getStyleClass().remove(CSS_ERROR);
                this.linkField.setText(DEFAULT_URL);
            } else {
                this.linkField.getStyleClass().add(CSS_ERROR);
            }
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
