/**
 * 
 */
package simplenote.control;

import java.io.IOException;
import java.util.prefs.Preferences;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import simplenote.interfaces.IController;
import simplenote.model.Note;
import simplenote.model.Vault;

/**
 * @author Claudio Marangon, Ljubisa Markovic
 *
 */
public class RootController extends FXController {
    // singleton
    private static RootController instance = null;

    private Stage rootStage;
    private Scene rootScene;
    private BorderPane rootLayout;

    private Vault vault;

    private Note selectedNote;

    private Preferences preferences;

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
        // here, I would initialize things... IF I HAD ANY!!
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

    private void loadLayout(String layoutFile, IController controller) {
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

    public void init(Stage rootStage, Scene rootScene, BorderPane rootLayout, Preferences pref) {

        this.rootStage = rootStage;
        this.rootScene = rootScene;
        this.rootLayout = rootLayout;

        this.preferences = pref;

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

    public Preferences getPreferences() {
        return this.preferences;
    }
}
