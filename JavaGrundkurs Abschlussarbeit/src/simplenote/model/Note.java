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
    private Date creation_date;
    private String title;
    private String text;
    private List<File> pictureList;
    private List<URL> linkList;
    private Integer owner_id;
    private Integer[] shared_ids;
    
    public Note() {
        this("","");
    }
    
    public Note(String title) {
        this(title, "");
    }
    
    public Note(String title, String text) {
        this.creation_date = new Date();
        this.title = title;
        this.text = text;
        this.pictureList = new ArrayList<File>();
    }
    
    /*
    public void addFiles(List<File> fileList) {
        List<File> tmpList = this.getFileList();
        tmpList.addAll(fileList);
        this.setPList(tmpList);
    }
    */
    
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
    
    /**
     * @return the fileList
     */
    public List<File> getPictureList() {
        return this.pictureList;
    }
    
    /**
     * @param pictureList
     */
    public void setPictureList(List<File> pictureList) {
        this.pictureList = pictureList;
    }
    
    /**
     * @return 
     */
    public List<URL> getLinkList() {
        return this.linkList;
    }
    
    /**
     * @param linkList
     */
    public void setLinkList(List<URL> linkList) {
        this.linkList = linkList;
    }
}
