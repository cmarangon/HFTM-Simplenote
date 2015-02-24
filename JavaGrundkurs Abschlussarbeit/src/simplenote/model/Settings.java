/**
 * 
 */
package simplenote.model;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author claudio
 *
 */
public class Settings {
    private static final String FILENAME = "settings.dat";
    private File settingsFile;

    // breite und höhe
    private double height = 530.0;
    private double width = 1000.0;

    // position
    private double posX = 0;
    private double posY = 0;

    // sortierung und filterung
    private String searchText = "";
    private int sortDirection = 1;
    private int sortType = 1;

    // aktuell ausgewählte notiz
    private int selectedElementIndex = 0;

    public Settings() {
        this.settingsFile = new File(FILENAME);
        if (!this.settingsFile.exists()) {
            try {
                this.settingsFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Fehler beim erstellen des Tresors!");
                e.printStackTrace();
            }
        }
        this.read();
    }

    /**
     * @
     */
    public boolean save() {
        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(
                    this.settingsFile));
            outputStream.writeObject(this.height);
            outputStream.writeObject(this.width);
            outputStream.writeObject(this.posX);
            outputStream.writeObject(this.posY);
            outputStream.writeObject(this.searchText);
            outputStream.writeObject(this.sortDirection);
            outputStream.writeObject(this.sortType);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("Einstellungsdatei nicht gefunden!");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out
                    .println("Fehler beim schreiben in die Einstellungsdatei!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 
     */
    private void read() {
        ObjectInputStream inputStream;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(
                    this.settingsFile));
            this.setHeight((double) inputStream.readObject());
            this.setWidth((double) inputStream.readObject());
            this.setPosX((double) inputStream.readObject());
            this.setPosY((double) inputStream.readObject());
            this.setSearchText((String) inputStream.readObject());
            this.setSortDirection((int) inputStream.readObject());
            this.setSort_type((int) inputStream.readObject());
            this.setSelectedElementIndex((int) inputStream.readObject());
            inputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("Einstellungsdatei nicht gefunden!");
            e.printStackTrace();
        } catch (EOFException e) {
            // End of file reached, simple as that
        } catch (IOException e) {
            System.out.println("Fehler beim lesen der Einstellungsdatei!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Konnte Settings Klasse nicht finden");
            e.printStackTrace();
        }
    }

    /**
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * @return the posX
     */
    public double getPosX() {
        return this.posX;
    }

    /**
     * @param posX the posX to set
     */
    public void setPosX(double posX) {
        this.posX = posX;
    }

    /**
     * @return the posY
     */
    public double getPosY() {
        return this.posY;
    }

    /**
     * @param posY the posY to set
     */
    public void setPosY(double posY) {
        this.posY = posY;
    }

    /**
     * @return the searchText
     */
    public String getSearchText() {
        return this.searchText;
    }

    /**
     * @param searchText the searchText to set
     */
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    /**
     * @return the sortDirection
     */
    public int getSortDirection() {
        return this.sortDirection;
    }

    /**
     * @param sortDirection the sortDirection to set
     */
    public void setSortDirection(int sortDirection) {
        this.sortDirection = sortDirection;
    }

    /**
     * @return the sortType
     */
    public int getSortType() {
        return this.sortType;
    }

    /**
     * @param sortType the sortType to set
     */
    public void setSort_type(int sortType) {
        this.sortType = sortType;
    }

    /**
     * @return the selectedElementIndex
     */
    public int getSelectedElementIndex() {
        return this.selectedElementIndex;
    }

    /**
     * @param selectedElementIndex the selectedElementIndex to set
     */
    public void setSelectedElementIndex(int selectedElementIndex) {
        this.selectedElementIndex = selectedElementIndex;
    }
}
