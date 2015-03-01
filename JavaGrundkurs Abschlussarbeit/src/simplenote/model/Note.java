/**
 * simpleNote, a better way to store your notes
 * Abschlussarbeit der HFTM Grenchen
 * Klasse Java Grundlagen II
 */
package simplenote.model;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model for a note.
 * Stores all the necessary information.
 * 
 * @author Claudio Marangon, Ljubisa Markovic
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

    /**
     * Constructor
     */
    public Note() {
        this("", "");
    }

    /**
     * Constructor
     * 
     * @param title
     */
    public Note(String title) {
        this(title, "");
    }

    /**
     * Constructor
     * 
     * @param title
     * @param text
     */
    public Note(String title, String text) {
        this.creationDate = new Date();
        this.modificationDate = null;
        this.title = title;
        this.text = text;
        this.linkList = new ArrayList<URL>();
        this.pictureList = new ArrayList<File>();
    }

    /**
     * Converts the note into a string representation
     */
    @Override
    public String toString() {
        return this.getTitle();
    }

    /** GETTERS AND SETTERS **/

    /**
     * Returns the creation date
     * 
     * @return creationdate as Date
     */
    public Date getCreationDate() {
        return this.creationDate;
    }

    /**
     * Sets the creation date
     * 
     * @param creationDate
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Returns the modification date
     * 
     * @return modificationDate as Date
     */
    public Date getModificationDate() {
        return this.modificationDate;
    }

    /**
     * Sets the modification date
     * 
     * @param modificationDate
     */
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    /**
     * Returns the title
     * 
     * @return title as String
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the title
     * 
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the text
     * 
     * @return text as String
     */
    public String getText() {
        return this.text;
    }

    /**
     * Sets the text
     * 
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the list of pictures
     * 
     * @return fileList as List<File>
     */
    public List<File> getPictureList() {
        return this.pictureList;
    }

    /**
     * Sets the list of pictures
     * 
     * @param pictureList
     */
    public void setPictureList(ArrayList<File> pictureList) {
        this.pictureList = pictureList;
    }

    /**
     * Returns the list of links
     * 
     * @return linkList as ArrayList<URL>
     */
    public ArrayList<URL> getLinkList() {
        return this.linkList;
    }

    /**
     * Sets the list of links
     * 
     * @param linkList
     */
    public void setLinkList(ArrayList<URL> linkList) {
        this.linkList = linkList;
    }
}
