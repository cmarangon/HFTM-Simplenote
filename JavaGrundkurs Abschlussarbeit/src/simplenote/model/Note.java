/**
 * 
 */
package simplenote.model;

import java.util.Date;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author Claudio Marangon
 *
 */
public class Note {
    
    private Date creation_date;
    private StringProperty title;
    private StringProperty text;
    
    
    
    public Note(String title, String text) {
        this.creation_date = new Date();
        this.title = new SimpleStringProperty(title);
        this.text = new SimpleStringProperty(text);
    }
    
    
    /**
     * @return the creation_date
     */
    public Date getCreation_date() {
        return creation_date;
    }
    /**
     * @param creation_date the creation_date to set
     */
    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }
    /**
     * @return the title
     */
    public StringProperty getTitle() {
        return title;
    }
    /**
     * @param title the title to set
     */
    public void setTitle(StringProperty title) {
        this.title = title;
    }
    /**
     * @return the text
     */
    public StringProperty getText() {
        return text;
    }
    /**
     * @param text the text to set
     */
    public void setText(StringProperty text) {
        this.text = text;
    }
    
}
