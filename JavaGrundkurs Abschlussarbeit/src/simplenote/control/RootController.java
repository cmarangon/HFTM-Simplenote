/**
 * simpleNote, a better way to store your notes
 * Abschlussarbeit der HFTM Grenchen
 * Klasse Java Grundlagen II
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
 * Controller class which holds all the necessary information
 * such as the vault which manages all the notes, the currently selected
 * note, the user preferences and all the layout elements. 
 * 
 * @author Claudio Marangon, Ljubisa Markovic
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

    /**
     * Constructor
     */
    public RootController() {
    }

    /**
     * Singleton inplementation.
     * Always returns the same RootController instance.
     * 
     * @return RootController
     */
    public static RootController getInstance() {
        // lazy singleton
        if (null == instance) {
            instance = new RootController();
        }
        return instance;
    }

    /* FXML ACTIONS */

    /**
     * Gets called when loaded with FXMLLoader
     */
    @FXML
    private void initialize() {
        // here, I would initialize things... IF I HAD ANY!!
    }

    /**
     * Shows the overview layout
     */
    protected void showOverview() {
        this.loadNoteOverviewLayout();
    }

    /**
     * Shows the add note layout
     */
    protected void showAddNote() {
        this.loadAddNoteLayout();
    }

    /**
     * Shows the edit note layout but only when a note is selected
     */
    protected void showEditNote() {
        if (getSelectedNote() != null) {
            this.loadEditNotelayout();
        }
    }

    /* LAYOUTS & CONTROLLERS */

    /**
     * Loads the NoteOverviewLayout
     */
    private void loadNoteOverviewLayout() {
        loadLayout("../view/NoteOverviewLayout.fxml", new NoteOverviewController());
    }

    /**
     * Loads the AddNoteLayout
     */
    private void loadAddNoteLayout() {
        loadLayout("../view/EditNoteLayout.fxml", new AddNoteController());
    }

    /**
     * Loads the EditNoteLayout
     */
    private void loadEditNotelayout() {
        loadLayout("../view/EditNoteLayout.fxml", new EditNoteController());
    }

    /**
     * Loads the given layout and sets the given controller
     * 
     * @param layoutFile
     * @param controller
     */
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

    /**
     * Initialize the layout elements and the preferences
     * 
     * @param rootStage
     * @param rootScene
     * @param rootLayout
     * @param pref
     */
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

    /**
     * Returns the root layout
     * 
     * @return rootlayout as BorderPane
     */
    public BorderPane getLayout() {
        return this.rootLayout;
    }

    /**
     * Returns the root stage
     * 
     * @return stage as Stage
     */
    public Stage getStage() {
        return this.rootStage;
    }

    /**
     * Returns the root scene
     * 
     * @return scene as Scene
     */
    public Scene getScene() {
        return this.rootScene;
    }

    /**
     * Sets the vault
     * 
     * @param vault
     */
    public void setVault(Vault vault) {
        this.vault = vault;
    }

    /**
     * Returns the vault
     * 
     * @return vault as Vault
     */
    public Vault getVault() {
        return this.vault;
    }

    /**
     * Sets the selected note
     * 
     * @param note
     */
    public void setSelectedNote(Note note) {
        this.selectedNote = note;
    }

    /**
     * Returns the selected note
     * 
     * @return selected note as Note
     */
    public Note getSelectedNote() {
        return this.selectedNote;
    }

    /**
     * Returns the preferences
     * 
     * @return user preferences as Preferences
     */
    public Preferences getPreferences() {
        return this.preferences;
    }
}
