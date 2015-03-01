/**
 * simpleNote, a better way to store your notes
 * Abschlussarbeit der HFTM Grenchen
 * Klasse Java Grundlagen II
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
 * Model for the vault.
 * He stores all the notes, loads and saves them when needed.
 * 
 * @author Claudio Marangon, Ljubisa Markovic
 */
public class Vault {

    private static final String FILENAME = "notizen.dat";
    private ArrayList<Note> notes;
    private File vaultFile;

    /**
     * Constructor
     */
    public Vault() {
        this.notes = new ArrayList<Note>();
        this.vaultFile = new File(FILENAME);
        if (!this.vaultFile.exists()) {
            try {
                this.vaultFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Fehler beim erstellen des Tresors!");
                e.printStackTrace();
            }
        }
        this.read();
    }

    /**
     * Reads the storage file
     */
    public void read() {
        ObjectInputStream inputStream;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(
                    this.vaultFile));
            Note note = null;
            while ((note = (Note) inputStream.readObject()) != null) {
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

    /**
     * Adds a new note to the vault
     * 
     * @param note
     */
    public void add(Note note) {
        this.notes.add(note);
    }

    /**
     * Saves the vault and all it's notes to the storage file
     * 
     * @return
     */
    public boolean save() {
        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(
                    this.vaultFile));
            for (Note note : this.notes) {
                if (note != null) {
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

    /**
     * Deletes a given note
     * 
     * @param deleteNote
     */
    public void delete(Note deleteNote) {
        for (Note note : this.getNotes()) {
            if (deleteNote.equals(note)) {
                this.notes.remove(this.notes.indexOf(note));
                this.save();
                break;
            }
        }
    }

    /**
     * Returns a list of all the notes
     * 
     * @return all notes as ArrayList<Note>
     */
    public ArrayList<Note> getNotes() {
        return this.notes;
    }
}
