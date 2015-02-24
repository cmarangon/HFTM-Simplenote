/**
 * 
 */
package simplenote.control;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import simplenote.interfaces.IFXController;
import simplenote.model.Note;
import simplenote.model.Settings;
import simplenote.model.Vault;

/**
 * @author Claudio Marangon, Ljubisa Markovic
 *
 */
public class RootController implements IFXController {
    // singleton
    private static RootController instance = null;

    private Stage rootStage;
    private Scene rootScene;
    private BorderPane rootLayout;

    private Vault vault;

    private Note selectedNote;

    private Settings settings;

    public RootController() {
    }

    public static RootController getInstance() {
        // lazy singleton
        if (null == instance) {
            instance = new RootController();
        }
        return instance;
    }

    /* FXML ACTIONS */

    @FXML
    private void initialize() {
        this.settings = new Settings();
    }

    protected void showOverview() {
        this.loadNoteOverviewLayout();
    }

    protected void showAddNote() {
        this.loadAddNoteLayout();
    }

    protected void showEditNote(Note selectedNote) {
        this.setSelectedNote(selectedNote);
        if (selectedNote != null) {
            this.loadEditNotelayout();
        }
    }

    /* LAYOUTS & CONTROLLERS */

    private void loadNoteOverviewLayout() {
        loadLayout("../view/NoteOverviewLayout.fxml", new NoteOverviewController());
    }

    private void loadAddNoteLayout() {
        loadLayout("../view/EditNoteLayout.fxml", new AddNoteController());
    }

    private void loadEditNotelayout() {
        loadLayout("../view/EditNoteLayout.fxml", new EditNoteController());
    }

    private void loadLayout(String layoutFile, IFXController controller) {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(layoutFile));
            loader.setController(controller);

            // Set person overview into the center of root layout.
            this.getLayout().setCenter((AnchorPane) loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(Stage rootStage, Scene rootScene, BorderPane rootLayout) {
        
        this.rootStage = rootStage;
        this.rootScene = rootScene;
        this.rootLayout = rootLayout;

        this.rootStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent ev) {
                settings.setHeight(rootStage.getHeight());
                settings.setWidth(rootStage.getWidth());
                settings.setPosX(rootStage.getX());
                settings.setPosY(rootStage.getY());

                settings.save();
            }
        });
        /*
        ChangeListener<Number> resizeListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if(rootStage.widthProperty() != null && rootStage.heightProperty() != null) {
                    System.out.println("Height: " + rootStage.heightProperty().get());
                    System.out.println("Width: " + rootStage.widthProperty().get());
                    System.out.println("-----------------");
                }
            }
        };
        this.rootStage.widthProperty().addListener(resizeListener);
        this.rootStage.heightProperty().addListener(resizeListener);
        */
        this.rootStage.setMinHeight(450);
        this.rootStage.setMinWidth(860);
        this.showOverview();
    }

    /** GETTERS AND SETTERS **/

    public BorderPane getLayout() {
        return this.rootLayout;
    }

    public Stage getStage() {
        return this.rootStage;
    }

    public Scene getScene() {
        return this.rootScene;
    }

    public void setVault(Vault vault) {
        this.vault = vault;
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

    public Settings getSettings() {
        return this.settings;
    }
}
