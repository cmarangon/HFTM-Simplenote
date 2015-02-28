package simplenote.control;

import javafx.scene.image.Image;
import simplenote.Main;
import simplenote.interfaces.IController;

public class FXController implements IController {
    private final String MOVE_UP = "src/img/arrow-drop-up.png";
    private final String MOVE_DOWN = "src/img/arrow-drop-down.png";
    private final String REMOVE = "src/img/highlight-remove.png";
    private final String DELETE = "src/img/delete.png";
    private final String EDIT = "src/img/edit.png";
    private final String VIEW = "src/img/visibility.png";
    private final String SAVE = "src/img/save.png";
    private final String BACK = "src/img/arrow-back.png";
    
    protected final Image MOVE_UP_ICON = new Image(Main.class.getResourceAsStream(MOVE_UP));
    protected final Image MOVE_DOWN_ICON = new Image(Main.class.getResourceAsStream(MOVE_DOWN));
    protected final Image REMOVE_ICON = new Image(Main.class.getResourceAsStream(REMOVE));
    protected final Image DELETE_ICON = new Image(Main.class.getResourceAsStream(DELETE));
    protected final Image EDIT_ICON = new Image(Main.class.getResourceAsStream(EDIT));
    protected final Image VIEW_ICON = new Image(Main.class.getResourceAsStream(VIEW));
    protected final Image SAVE_ICON = new Image(Main.class.getResourceAsStream(SAVE));
    protected final Image BACK_ICON = new Image(Main.class.getResourceAsStream(BACK));
    
    protected final String HTML_EMPTY = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"></body></html>";
    protected final String DEFAULT_URL = "http://";
    
    protected final String CSS_HIDDEN = "hidden";
    protected final String CSS_ERROR = "error";
    protected final String CSS_CLICKABLE = "clickable";
    
    public FXController() {
     // luke, im your father
    }
}
