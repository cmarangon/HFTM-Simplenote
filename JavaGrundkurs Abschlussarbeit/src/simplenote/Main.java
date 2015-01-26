package simplenote;
	
import simplenote.control.RootController;
import simplenote.interfaces.InitCompletionHandler;
import simplenote.model.Note;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;


public class Main extends Application {
    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private Stage mainStage;
    
    
    /**
     * Main method to start application but beforehand show loadingscreen
     * 
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        showLoadingScreen(
            primaryStage,
            loadNotesTask,
            () -> showMainStage()
        );
        
        new Thread(loadNotesTask).start();
    }

	/**
	 * Seperate task to load all notes 
	 */
    final Task<ObservableList<Note>> loadNotesTask = new Task<ObservableList<Note>>() {
        @Override
        protected ObservableList<Note> call() throws InterruptedException {
            
            ObservableList<Note> foundNotes =
                    FXCollections.<Note>observableArrayList();
            
            // load notes here
            //Thread.sleep(2000);
            return foundNotes;
        }
    };
    
    /**
     * Show loading screen
     * 
     * @param initStage
     * @param task
     * @param initCompletionHandler
     */
    private void showLoadingScreen(
            final Stage initStage,
            Task<?> task,
            InitCompletionHandler initCompletionHandler
    ) {
        progressText.textProperty().bind(task.messageProperty());
        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            } // todo add code to gracefully handle other task states.
        });

        Scene splashScene = new Scene(splashLayout);
        initStage.initStyle(StageStyle.UNDECORATED);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - 400 / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - 200 / 2);
        initStage.show();
    }
	
    /**
     * Show main stage
     */
    private void showMainStage() {
        mainStage = new Stage(StageStyle.DECORATED);
        mainStage.setTitle("simpleNote");
        /*
        final ListView<String> peopleView = new ListView<>();
        peopleView.itemsProperty().bind(friends);

        mainStage.setScene(new Scene(peopleView));
        mainStage.show();
        */
        RootController rc = RootController.getInstance();
        rc.setMainApp(this);
        rc.setPrimaryStage(mainStage);
        rc.initRootLayout();
    }
    
	public static void main(String[] args) {
		launch(args);
	}
}
