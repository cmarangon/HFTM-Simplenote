/**
 * 
 */
package simplenote.model;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Claudio Marangon, Ljubisa Markovic
 *
 */
public class Note implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4680988752581353062L;
    private Date creationDate;
    private Date modificationDate;
    private String title;
    private String text;
    private ArrayList<File> pictureList;
    private ArrayList<URL> linkList;

    public Note() {
        this("", "");
    }

    public Note(String title) {
        this(title, "");
    }

    public Note(String title, String text) {
        this.creationDate = new Date();
        this.modificationDate = new Date();
        this.title = title;
        this.text = text;
        this.linkList = new ArrayList<URL>();
        this.pictureList = new ArrayList<File>();
    }

    @Override
    public String toString() {
        return this.title;
    }

    /** GETTERS AND SETTERS **/

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return this.creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the modificationDate
     */
    public Date getModificationDate() {
        return this.modificationDate;
    }

    /**
     * @param modificationDate the modificationDate to set
     */
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
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

    /**
     * @return the fileList
     */
    public List<File> getPictureList() {
        return this.pictureList;
    }

    /**
     * @param pictureList
     */
    public void setPictureList(ArrayList<File> pictureList) {
        this.pictureList = pictureList;
    }

    /**
     * @return the linkList
     */
    public ArrayList<URL> getLinkList() {
        return this.linkList;
    }

    /**
     * @param linkList
     */
    public void setLinkList(ArrayList<URL> linkList) {
        this.linkList = linkList;
    }
}
