package simplenote;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.prefs.Preferences;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import simplenote.control.RootController;
import simplenote.interfaces.InitCompletionHandler;
import simplenote.model.Vault;

/**
 * 
 * @author Claudio Marangon, Ljubisa Markovic
 *
 */
public class Main extends Application {
    private final String APP_NAME = "simpleNote";
    private final String CSS_FILE = "src/css/styles.css";
    private final String LOCK_FILE = "black_flag"; // Arrrrr!
    private final String LOADING_FILE = "src/img/armadillo_loading.png";
    private final String ICON_FILE = "src/img/armadillo_icon_32.png";
    private final String LAYOUT_FILE = "view/RootLayout.fxml";
    
    private final String WINDOW_HEIGHT = "window.height";
    private final String WINDOW_WIDTH = "window.width";
    private final String WINDOW_POSX = "window.posX";
    private final String WINDOW_POSY = "window.posY";
    

    // Keeping the notes together and save
    private Vault vault;
    
    // RootController object
    private RootController rc;

    // Preferences
    private Preferences preferences;

    // LoadingScreen elements
    private VBox loadingPane;
    private ProgressBar loadProgress;
    private Label progressText;
    private Stage stage;

    /**
     * Main method to start application but beforehand show loadingscreen
     * 
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        final File file = new File(LOCK_FILE);
        final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        final FileLock fileLock = randomAccessFile.getChannel().tryLock();

        // check if application is already running
        if (fileLock != null) {
            // normal starting procedure
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent arg0) {
                    try {
                        fileLock.release();
                        randomAccessFile.close();
                    } catch (Exception ex) {
                    }
                }
            });

            showLoadingScreen(primaryStage, loadNotesTask, () -> showMainStage());

            new Thread(loadNotesTask).start();

        } else {
            // exit right away
            Platform.exit();
        }
    }

    /**
     * Generate LoadingScreen
     */
    @Override
    public void init() {
        ImageView splash = new ImageView(new Image(getClass().getResource(LOADING_FILE).toExternalForm()));
        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(300);
        progressText = new Label("Lade Notizen");
        loadingPane = new VBox();
        loadingPane.getChildren().addAll(splash, loadProgress, progressText);
        progressText.setAlignment(Pos.CENTER);
        loadingPane.setStyle("-fx-padding: 20; -fx-background-color: #fff; ");
        loadingPane.setEffect(new DropShadow());
    }

    /**
     * Seperate task to load all notes
     */
    final Task<Integer> loadNotesTask = new Task<Integer>() {
        @Override
        protected Integer call() throws InterruptedException {

            updateMessage("Lade Notizen...");
            vault = new Vault();

            updateMessage(String.valueOf(vault.getNotes().size()) + " Notizen geladen");
            
            // thread needs to sleep, otherwise we wont see the beautiful loadingscreen ;)
            Thread.sleep(750); 

            return vault.getNotes().size();
        }
    };

    /**
     * Show loading screen
     * 
     * @param primaryStage
     * @param task
     * @param initCompletionHandler
     */
    private void showLoadingScreen(final Stage primaryStage, Task<?> task, InitCompletionHandler initCompletionHandler) {
        progressText.textProperty().bind(task.messageProperty());
        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {

                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);

                // Bring initStage to front
                primaryStage.toFront();

                // Add Fade out transition to loadingScreen
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), loadingPane);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> primaryStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            }
            // todo add code to gracefully handle other task states.
        });

        Scene loadingScene = new Scene(loadingPane);
        primaryStage.setScene(loadingScene);

        // place stage at center of primary screen
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        primaryStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - 400 / 2);
        primaryStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - 200 / 2);

        primaryStage.show();
    }

    /**
     * Show main stage
     * 
     */
    private void showMainStage() {
        try {
            // Load root layout from fxml file.
            this.rc = RootController.getInstance();
            FXMLLoader loader = new FXMLLoader();
            BorderPane layout;
            Scene scene;

            this.rc.setVault(this.vault);
            loader.setController(this.rc);
            loader.setLocation(getClass().getResource(LAYOUT_FILE));
            layout = (BorderPane) loader.load();
            scene = new Scene(layout);
            
            // Add stylesheet to scene
            scene.getStylesheets().add(getClass().getResource(CSS_FILE).toExternalForm());

            preferences = Preferences.userNodeForPackage(getClass());

            stage = new Stage();
            stage.setX(preferences.getDouble(WINDOW_POSX, 0));
            stage.setY(preferences.getDouble(WINDOW_POSY, 0));
            stage.setWidth(preferences.getDouble(WINDOW_WIDTH, 800));
            stage.setHeight(preferences.getDouble(WINDOW_HEIGHT, 600));
            stage.setTitle(APP_NAME);
            stage.setScene(scene);
            // TODO: this image gets messed up :<
            //stage.getIcons().add(new Image(getClass().getResource(ICON_FILE).toExternalForm()));
            stage.show();

            this.rc.init(stage, scene, layout, preferences);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void stop() {
        // check if preferences not equals null because this
        // might be the case if you start this since instance app
        // more than one time
        if (preferences != null) {
            preferences.putDouble(WINDOW_HEIGHT, stage.getHeight());
            preferences.putDouble(WINDOW_WIDTH, stage.getWidth());
            preferences.putDouble(WINDOW_POSX, stage.getX());
            preferences.putDouble(WINDOW_POSY, stage.getY());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
