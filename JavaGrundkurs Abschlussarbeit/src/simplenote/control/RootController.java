/**
 * 
 */
package simplenote.control;

import java.io.IOException;

import simplenote.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author Claudio Marangon
 *
 */
public class RootController {
    private static RootController instance = null;
    
    private Stage primaryStage;
    protected Main mainApp;
    private BorderPane rootLayout;
    
    /*
    public CourseOverviewController coc;
    public AddCourseController acc;
    public TrainingController tc;
    */
    
    public RootController() {
    }
    
    public static RootController getInstance() {
        if (null == instance) {
            instance = new RootController();
        }
        return instance;
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
    

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/RootLayout.fxml"));
            this.rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            this.primaryStage.setTitle("simpleNote");
            this.primaryStage.setScene(scene);
            this.primaryStage.show();
            
            // init Overview
            /*
            coc = new CourseOverviewController();
            coc.initCourseOverviewLayout();
            */
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /* Getters & Setters */
    
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
    
}
