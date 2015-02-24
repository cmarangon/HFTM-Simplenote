package simplenote.control;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ViewHelper {
    // Sortierungskonstanten
    public static int ASC = 1;
    public static int DESC = -1;

    public static int SORT_TITLE = 1;
    public static int SORT_CREATION_DATE = 2;
    public static int SORT_MODIFICATION_DATE = 3;
    
    /**
     * Formats the given date into an predefined string
     * 
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("'Erstellt am 'dd.MM.yyyy' um 'HH:mm:ss");
        return dateFormat.format(date);
    }
    
    
    /**
     * Sets the content of a note to uneditable
     * 
     * @param content
     * @return
     */
    public static String makeContentUnwritable(String content) {
        String contentWritable = "contenteditable=\"true\"";
        String contentUnwritable = "contenteditable=\"false\"";
        
        // change content to noneditable mode
        if (content.contains(contentWritable)) {
            content = content.replace(contentWritable, contentUnwritable);
        }
        return content;
    }
    
    /**
     * Creates standard ImageView from file
     * 
     * @param file
     * @return an ImageView
     */
    public static ImageView createImageView(File file) {
        ImageView iv = new ImageView();
        iv.setImage(new Image(file.toURI().toString()));
        iv.setFitHeight(200);
        iv.setFitWidth(200);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        return iv;
    }
}
