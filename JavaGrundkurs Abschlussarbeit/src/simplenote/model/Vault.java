/**
 * @author Claudio Marangon, Ljubisa Markovic
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
import java.util.ArrayList;


/**
 * @author claudio
 *
 */
public class Vault {

    private static final String FILENAME = "notizen.dat";
    private ArrayList<Note> notes;
    private File vaultFile; 
    
    
    /**
     * 
     */
    public Vault() {
        this.notes = new ArrayList<Note>();
        this.vaultFile = new File(FILENAME);
        if(!this.vaultFile.exists()) {
            try {
                this.vaultFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Fehler beim erstellen des Tresors!");
                e.printStackTrace();
            }
        }
        this.read();
    }

    public void read() {
        ObjectInputStream inputStream;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(this.vaultFile));
            Note note = null;
            while ((note = (Note) inputStream.readObject()) != null) {
                System.out.println(note);
                this.add(note);
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("Tresordatei nicht gefunden!");
            e.printStackTrace();
        } catch (EOFException e) {
            // End of file reached, simple as that
        } catch (IOException e) {
            System.out.println("Fehler beim lesen der Tresordatei!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Konnte Notizen Klasse nicht finden");
            e.printStackTrace();
        }
    }

    public void add(Note note) {
        this.notes.add(note);
    }

    public boolean save() {
        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(this.vaultFile));
            for(Note note : this.notes) {
                if(note != null) {
                    outputStream.writeObject(note);
                }
            }
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("Tresordatei nicht gefunden!");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println("Fehler beim schreiben in die Tresordatei!");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public void delete(Note deleteNote) {
        for(Note note : this.getNotes()) {
            if(deleteNote.equals(note)) {
                int indexOfNote = this.notes.indexOf(note);
                System.out.println(indexOfNote);
                this.notes.remove(indexOfNote);
                this.save();
                break;
            }
        }
    }

    public ArrayList<Note> getNotes() {
        return this.notes;
    }
}
