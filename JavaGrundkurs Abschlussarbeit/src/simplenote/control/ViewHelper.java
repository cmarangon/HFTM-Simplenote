/**
 * simpleNote, a better way to store your notes
 * Abschlussarbeit der HFTM Grenchen
 * Klasse Java Grundlagen II
 */
package simplenote.control;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * View helper which provides methods for often used elements
 * 
 * @author Claudio Marangon, Ljubisa Markovic
 */
public class ViewHelper {
    // Sortierungskonstanten
    public static final int ASC = 1;
    public static final int DESC = -1;

    public static final int SORT_TITLE = 1;
    public static final int SORT_CREATION_DATE = 2;
    public static final int SORT_MODIFICATION_DATE = 3;

    /**
     * Formats the given date into an predefined string
     * 
     * @param date
     * @param prefix
     * @param text
     * @return formatted date as String
     */
    public static String formatDate(Date date, String prefix, String text) {
        if (date != null) {
            String defaultPrefix = "Erstellt am ";
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy' um 'HH:mm:ss");
            if (prefix == null || prefix.equals("")) {
                prefix = defaultPrefix;
            }
            text = dateFormat.format(date);
        }
        return prefix + text;
    }

    /**
     * Formats the given date into an predefined string
     * 
     * @param date
     * @param prefix
     * @return formatted date as String
     */
    public static String formatDate(Date date, String prefix) {
        return formatDate(date, prefix, "");
    }

    /**
     * Sets the content of a note to uneditable
     * 
     * @param content
     * @return uneditable string representation as String
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
     * Creates thumbnail ImageView from file
     * 
     * @param file
     * @return image as ImageView
     */
    public static ImageView createThumbnailImageView(File file) {
        ImageView iv = new ImageView();
        iv.setImage(new Image(file.toURI().toString()));
        iv.setFitHeight(200);
        iv.setFitWidth(200);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        return iv;
    }
}
