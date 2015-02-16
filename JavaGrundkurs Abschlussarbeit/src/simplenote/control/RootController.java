/**
 * 
 */
package simplenote.control;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import simplenote.Main;
import simplenote.model.Note;
import simplenote.model.Vault;

/**
 * @author Claudio Marangon, Ljubisa Markovic
 *
 */
public class RootController {
    // singleton
    private static RootController instance = null;
    
    private Stage primaryStage;
    private Main mainApp;
    private BorderPane rootLayout;
    
    private Vault vault;
    
    private Note selectedNote;
    
    
    public RootController() {
    }
    
    public static RootController getInstance() {
        // lazy singleton
        if (null == instance) {
            instance = new RootController();
        }
        return instance;
    }

    @FXML
    public void initialize() {
        // init
    }
    
    
    /* FXML ACTIONS  */
    
    @FXML
    public void showOverview() {
        this.loadNoteOverviewLayout();
    }
    
    public void showAddNote() {
        this.loadAddNoteLayout();
    }
    
    @FXML
    public void showEditNote() {
        this.setSelectedNote(selectedNote);
        
        this.loadEditNotelayout();
    }
    
    @FXML
    public void closeApplication() {
        System.exit(0);
    }
    
    
    /* LAYOUTS & CONTROLLERS */
    
    public void loadNoteOverviewLayout() {
        loadLayout("../view/NoteOverviewLayout.fxml");
    }
    
    public void loadAddNoteLayout() {
        loadLayout("../view/AddNoteLayout.fxml");
    }
    
    public void loadEditNotelayout() {
        loadLayout("../view/EditNoteLayout.fxml");
    }
    
    private void loadLayout(String layoutFile) {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(layoutFile));

            // Set person overview into the center of root layout.
            this.getRootLayout().setCenter((AnchorPane) loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /** GETTERS AND SETTERS **/
    
    public void setRootLayout(BorderPane rootLayout) {
        this.rootLayout = rootLayout;
    }
    
    public BorderPane getRootLayout() {
        return this.rootLayout;
    }
    
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
    
    public Main getMainApp(){
        return this.mainApp;
    }
    
    public void setPrimaryStage(Stage pStage) {
        this.primaryStage = pStage;
    }
    
    public Stage getPrimaryStage() {
        return this.primaryStage;
    }
    
    public void setVault(Vault v) {
        this.vault = v;
    }
    
    public Vault getVault() {
        return this.vault;
    }
    
    public void setSelectedNote(Note note) {
        this.selectedNote = note;
    }
    
    public Note getSelectedNote() {
        return this.selectedNote;
    }
}
