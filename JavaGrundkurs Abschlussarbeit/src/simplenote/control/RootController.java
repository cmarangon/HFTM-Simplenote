/**
 * 
 */
package simplenote.control;

import java.io.IOException;

import simplenote.Main;
import simplenote.model.Note;
import simplenote.model.Vault;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author Claudio Marangon
 *
 */
public class RootController {
    private static RootController instance = null;
    
    private Stage primaryStage;
    private Main mainApp;
    private BorderPane rootLayout;
    
    private Vault vault;
    
    private Note selectedNote;
    
    
    public RootController() {
        //System.out.println("RC");
    }
    
    public static RootController getInstance() {
        if (null == instance) {
            System.out.println("new");
            instance = new RootController();
        }
        System.out.println("return RC");
        return instance;
    }

    @FXML
    public void initialize() {
        
        System.out.println("init");
    }
    
    /*
    public RootController(MainApp mApp, Stage pStage) {
        System.out.println(this.hashCode());
        setMainApp(mApp);
        setPrimaryStage(pStage);
        
        initRootLayout();
        
        new CourseOverviewController(this);
        //this.addCourseLayoutController = new AddCourseLayoutController(this);
    }
    */
    
    
    /* FXML Actions */
    
    @FXML
    public void showOverview() {
        System.out.println("show overview");
        this.loadNoteOverviewLayout();
    }
    
    @FXML
    public void showAddNote() {
        System.out.println("show add note");
        this.loadAddNoteLayout();
    }
    
    @FXML
    public void showEditNote() {
        System.out.println("show edit note");
    }
    
    @FXML
    public void closeApplication() {
        System.out.println("close app");
        System.exit(0);
    }
    
    
    /* Layouts & Controllers */
    
    public void loadNoteOverviewLayout() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/NoteOverviewLayout.fxml"));

            // Set person overview into the center of root layout.
            this.getRootLayout().setCenter((AnchorPane) loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadAddNoteLayout() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/AddNoteLayout.fxml"));

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
}
