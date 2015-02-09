/**
 * 
 */
package simplenote.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Claudio Marangon, Ljubisa Markovic
 *
 */
public class Note implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 4680988752581353062L;
    private Date creation_date;
    private String title;
    private String text;
    private Integer owner_id;
    private Integer[] shared_ids;
    
    public Note(String title) {
        this(title, "");
    }
    
    public Note(String title, String text) {
        this.creation_date = new Date();
        this.title = title;
        this.text = text;
    }
    
    
    @Override
    public String toString() {
        return this.title;
    }
    
    /** GETTERS AND SETTERS **/
    
    /**
     * @return the creation_date
     */
    public Date getCreationDate() {
        return this.creation_date;
    }
    
    /**
     * @param creation_date the creation_date to set
     */
    public void setCreationDate(Date creation_date) {
        this.creation_date = creation_date;
    }
    
    /**
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }
    
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * @return the text
     */
    public String getText() {
        return this.text;
    }
    
    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }
}
