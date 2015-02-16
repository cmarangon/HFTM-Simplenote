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
    private ArrayList<File> pictureData = new ArrayList<File>();;
    
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
            pictureData.add(img);
            
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
     // text is required
        if(! this.textField.getHtmlText().equals(HTML_EMPTY)) {

            String nTitle = this.titleField.getText();
            String nText = this.textField.getHtmlText();
            
            if(nTitle.isEmpty()) {
                nTitle = "Notiz vom " + this.note.getCreationDate();
            }
            
            this.note.setTitle(nTitle);
            this.note.setText(nText);
            this.note.setPictureList(this.pictureData);
            ArrayList<URL> al = new ArrayList<URL>();
            for(URL url : this.linkData) {
                al.add(url);
            }
            this.note.setLinkList(al); 
            
            if(this.rc.getVault().save()){
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
        boolean error = false;
        URL url = null;
        
        if(this.linkField.getText().startsWith(DEFAULT_URL) && 
           this.linkField.getText().length() > DEFAULT_URL.length()) {
            try {
                url = new URL(this.linkField.getText()); // TODO: This only checks if protocol is set sooooo.....
            } catch (MalformedURLException e) {
                error = true;
            }
        } else {
            error = true;
        }
        
        
        if(error) {
            System.out.println("keine gültige url");
            this.linkField.getStyleClass().add("error");
        } else {
            this.linkField.getStyleClass().remove("error");
            this.linkData.add(url);
            this.linkField.setText(DEFAULT_URL);
        }
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
