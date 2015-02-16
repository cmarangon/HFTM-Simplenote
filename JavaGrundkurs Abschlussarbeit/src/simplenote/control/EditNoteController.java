/**
 * 
 */
package simplenote.control;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import simplenote.model.Note;

/**
 * @author Claudio Marangon, Ljubisa Markovic
 *
 */
public class EditNoteController {

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
    private ArrayList<File> pictureData;
    
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
        for(URL url : this.note.getLinkList()) {
            linkData.add(url);
        }
        linkList.setItems(linkData);
        
        // pictures
        for(File img : this.note.getPictureList()) {
            ImageView iv = new ImageView();
            iv.setImage(new Image(img.toURI().toString()));
            iv.setFitHeight(200);
            iv.setFitWidth(200);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
            this.pictureList.getChildren().add(iv);
        }
    }
    
    /* FXML Actions */

    @FXML
    public void saveNote() {
        String nTitle = titleField.getText();
        String nText = textField.getHtmlText();
        
        this.note.setTitle(nTitle);
        this.note.setText(nText);
        
        if(this.rc.getVault().save()){
            this.rc.showOverview();
        } else {
            System.out.println("ERROR ON SAVING NOTE");
        }
    }
    
    @FXML
    public void addLink() {
        
    }
    
    @FXML
    public void addPicture() {
        FileChooser fileChooser = new FileChooser();
        List<File> pList = fileChooser.showOpenMultipleDialog(this.rc.getPrimaryStage());
        
        if (pList != null) {
            for(File f : pList) {
                this.pictureData.add(f);
                
                ImageView iv = new ImageView();
                iv.setImage(new Image(f.toURI().toString()));
                iv.setFitHeight(200);
                iv.setFitWidth(200);
                iv.setPreserveRatio(true);
                iv.setSmooth(true);
                this.pictureList.getChildren().add(iv);
            }
        }
    }
}
