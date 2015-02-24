/**
 * 
 */
package simplenote.control;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import simplenote.model.Note;

/**
 * @author Claudio Marangon, Ljubisa Markovic
 *
 */
public class EditNoteController extends RootController {

    private final String HTML_EMPTY = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"></body></html>";
    private final String DEFAULT_URL = "http://";

    private RootController rc;
    private Note note;

    @FXML
    private TextField titleField;

    @FXML
    private HTMLEditor textField;

    @FXML
    private ToggleButton shareButton;

    @FXML
    private VBox pictureList;
    private ArrayList<File> pictureData = new ArrayList<File>();

    @FXML
    private TextField linkField;

    @FXML
    private ListView<URL> linkList;
    private ObservableList<URL> linkData = FXCollections.observableArrayList();

    /**
     * 
     */
    public EditNoteController() {
        this.rc = RootController.getInstance();
    }

    @FXML
    public void initialize() {
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

        /* Context menu for images */
        /*
        ContextMenu contextMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(event);
                System.out.println(event.getTarget());
                System.out.println(event.getSource());
            }
        });
        SeparatorMenuItem sep = new SeparatorMenuItem();
        MenuItem move_up = new MenuItem("Move up");
        MenuItem move_down = new MenuItem("Move down");
        contextMenu.getItems().addAll(delete, sep, move_up, move_down);
        */
        
        // pictures
        for (File file : this.note.getPictureList()) {
            pictureData.add(file);

            ImageView iv = ViewHelper.createImageView(file);

            ImageView deleteIcon = new ImageView();
            deleteIcon.setImage(new Image(getClass().getResourceAsStream("../src/img/delete.png")));
            deleteIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    for (Node node : pictureList.getChildren()) {
                        StackPane img = (StackPane) node;
                        if (img.getChildren().contains(event.getSource())) {
                            removePicture(pictureList.getChildren().indexOf(node));
                            break;
                        }
                    }
                }
            });
            StackPane sp = new StackPane();
            sp.setAlignment(Pos.TOP_RIGHT);
            sp.getChildren().addAll(iv, deleteIcon);
            
            this.pictureList.getChildren().add(sp);
        }
        
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
                        addPictureToList(file);
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
                nTitle = ViewHelper.formatDate(this.note.getCreationDate());
            }

            this.note.setTitle(nTitle);
            this.note.setText(nText);
            this.note.setPictureList(this.pictureData);
            ArrayList<URL> al = new ArrayList<URL>();
            for (URL url : this.linkData) {
                al.add(url);
            }
            this.note.setLinkList(al);

            if (this.rc.getVault().save()) {
                this.rc.showOverview();
            } else {
                System.out.println("ERROR ON SAVING NOTE");
            }
        } else {
            this.textField.getStyleClass().add("error");
        }
    }

    @FXML
    public void addLink() {
        if(this.linkField.getText().startsWith(DEFAULT_URL) && 
           this.linkField.getText().length() > DEFAULT_URL.length()) {
            
            if (this.addLinkToList(this.linkField.getText())) {
                // success
                this.linkField.getStyleClass().remove("error");
                this.linkField.setText(DEFAULT_URL);
            } else {
                this.linkField.getStyleClass().add("error");
            }
        }
    }
    
    private boolean addLinkToList(String string_url) {
        boolean error = true;
        
        try {
            this.linkData.add(new URL(string_url));
            error = false;
        } catch (MalformedURLException e) {
        }
        
        return error;
    }
    
    @FXML
    public void addPicture() {
        FileChooser fileChooser = new FileChooser();
        List<File> pList = fileChooser.showOpenMultipleDialog(this.rc.getStage());
        
        if (pList != null) {
            for(File f : pList) {
                this.addPictureToList(f);
            }
        }
    }
    
    private void addPictureToList(File file) {
        this.pictureData.add(file);
        this.pictureList.getChildren().add(ViewHelper.createImageView(file));
    }
    
    private void removePicture(int index) {
        if (index > -1) {
            this.pictureList.getChildren().remove(index);
            this.pictureData.remove(index);
        }
    }
}
