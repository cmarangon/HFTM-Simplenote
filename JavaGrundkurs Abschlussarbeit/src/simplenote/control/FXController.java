/**
 * simpleNote, a better way to store your notes
 * Abschlussarbeit der HFTM Grenchen
 * Klasse Java Grundlagen II
 */
package simplenote.control;

import javafx.scene.image.Image;
import simplenote.Main;
import simplenote.interfaces.IController;
/**
 * Controller class. Acts as father of all used controllers.
 * Implements the IController interface. 
 * 
 * @author Claudio Marangon, Ljubisa Markovic
 */
public class FXController implements IController {
    private static final String MOVE_UP = "src/img/arrow-drop-up.png";
    private static final String MOVE_DOWN = "src/img/arrow-drop-down.png";
    private static final String REMOVE = "src/img/highlight-remove.png";
    private static final String DELETE = "src/img/delete.png";
    private static final String EDIT = "src/img/edit.png";
    private static final String VIEW = "src/img/visibility.png";
    private static final String SAVE = "src/img/save.png";
    private static final String BACK = "src/img/arrow-back.png";
    private static final String DONE = "src/img/done.png";
    private static final String CLEAR = "src/img/clear.png";

    protected static final Image MOVE_UP_ICON = new Image(Main.class.getResourceAsStream(MOVE_UP));
    protected static final Image MOVE_DOWN_ICON = new Image(Main.class.getResourceAsStream(MOVE_DOWN));
    protected static final Image REMOVE_ICON = new Image(Main.class.getResourceAsStream(REMOVE));
    protected static final Image DELETE_ICON = new Image(Main.class.getResourceAsStream(DELETE));
    protected static final Image EDIT_ICON = new Image(Main.class.getResourceAsStream(EDIT));
    protected static final Image VIEW_ICON = new Image(Main.class.getResourceAsStream(VIEW));
    protected static final Image SAVE_ICON = new Image(Main.class.getResourceAsStream(SAVE));
    protected static final Image BACK_ICON = new Image(Main.class.getResourceAsStream(BACK));
    protected static final Image DONE_ICON = new Image(Main.class.getResourceAsStream(DONE));
    protected static final Image CLEAR_ICON = new Image(Main.class.getResourceAsStream(CLEAR));

    protected static final String HTML_EMPTY = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"></body></html>";
    protected static final String DEFAULT_URL = "http://";

    protected static final String CSS_HIDDEN = "hidden";
    protected static final String CSS_ERROR = "error";
    protected static final String CSS_CLICKABLE = "clickable";

    public static final String PREF_WINDOW_HEIGHT = "window.height";
    public static final String PREF_WINDOW_WIDTH = "window.width";
    public static final String PREF_WINDOW_POSX = "window.posX";
    public static final String PREF_WINDOW_POSY = "window.posY";
    public static final String PREF_SORT_TYPE = "sort.type";
    public static final String PREF_SORT_DIR = "sort.direction";
    public static final String PREF_SEARCH_TEXT = "search.text";
    public static final String PREF_SELECTED_NOTE = "notes.selectedNote";

    /**
     * Constructor
     */
    public FXController() {
     // luke, im your father
    }
}
