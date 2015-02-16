package simplenote;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.event.EventHandler;
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
    
    // Keeping the notes together and save
    private Vault vault;
    
    // LoadingScreen elements
    private VBox loadingPane;
    private ProgressBar loadProgress;
    private Label progressText;
    
    
    /**
     * Main method to start application but beforehand show loadingscreen
     * 
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        final File file = new File("black_flag");
        final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        final FileLock fileLock = randomAccessFile.getChannel().tryLock();
        
        if (fileLock != null) {
            // normal starting procedure
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent arg0) {
                    try {
                        fileLock.release();
                        randomAccessFile.close();
                    } catch (Exception ex) {
                        System.out.print(ex.getMessage()); // TODO: Logging?
                    }
                }
            });
            
            showLoadingScreen(
                primaryStage,
                loadNotesTask,
                () -> showMainStage()
            );
            
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
        ImageView splash = new ImageView(new Image(
                getClass().getResourceAsStream("src/img/small_armadillo.png")
        ));
        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(300);
        progressText = new Label("Lade Notizen");
        loadingPane = new VBox();
        loadingPane.getChildren().addAll(splash, loadProgress, progressText);
        progressText.setAlignment(Pos.CENTER);
        loadingPane.setStyle(
                "-fx-padding: 20; " +
                "-fx-background-color: #fff; "
        );
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
            Thread.sleep(500);
            
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
    private void showLoadingScreen(
            final Stage primaryStage,
            Task<?> task,
            InitCompletionHandler initCompletionHandler
    ) {
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
            RootController rc = RootController.getInstance();
            FXMLLoader loader = new FXMLLoader();
            BorderPane rootLayout;
            Scene scene;
            
            rc.setVault(this.vault);
            loader.setController(rc);
            loader.setLocation(getClass().getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            rc.setRootLayout(rootLayout);
            rc.showOverview();

            scene = new Scene(rootLayout);
            // Add stylesheet to scene
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            
            Stage mainStage = new Stage();
            mainStage.setTitle("simpleNote");
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
	public static void main(String[] args) {
		launch(args);
	}
}
