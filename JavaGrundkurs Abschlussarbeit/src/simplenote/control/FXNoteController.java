/**
 * 
 */
package simplenote.control;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * @author Claudio Marangon, Ljubisa Markovic
 *
 */
public class FXNoteController extends FXController {

    protected ArrayList<File> pictureData = new ArrayList<File>();
    protected ObservableList<URL> linkData = FXCollections.observableArrayList();

    protected void addContextMenuToLinkList(ListView<URL> linkList, TextField linkField, Button addLink, HBox modifyLink) {
        /* Context menu for links */
        ContextMenu contextMenu = new ContextMenu();
        MenuItem edit = new MenuItem("Edit");
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                linkField.setText(linkList.getSelectionModel().getSelectedItem().toString());
                modifyLink.getStyleClass().remove(CSS_HIDDEN);
                modifyLink.setMouseTransparent(false);

                addLink.getStyleClass().add(CSS_HIDDEN);
                addLink.setMouseTransparent(true);
            }
        });
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                linkData.remove(linkList.getSelectionModel().getSelectedItem());
            }
        });
        contextMenu.getItems().addAll(edit, delete);
        linkList.setContextMenu(contextMenu);
    }

    protected void initButtons(Button saveButton, Button backButton) {
        saveButton.setGraphic(new ImageView(SAVE_ICON));
        saveButton.setText("");
        saveButton.setTooltip(new Tooltip("Änderungen übernehmen"));
        backButton.setGraphic(new ImageView(BACK_ICON));
        backButton.setText("");
        backButton.setTooltip(new Tooltip("Zurück zur Übersicht"));
    }

    protected boolean addLinkToList(String string_url) {
        boolean success = true;

        try {
            this.linkData.add(new URL(string_url));
        } catch (MalformedURLException e) {
            success = false;
        }

        return success;
    }

    protected void loadPictureList(VBox pictureList) {
        // first of all, clear the list 
        pictureList.getChildren().clear();

        for (File file : this.pictureData) {
            ImageView iv = ViewHelper.createImageView(file);

            HBox hoverMenu = new HBox();
            hoverMenu.setMaxHeight(26.0);
            hoverMenu.setPadding(new Insets(2));
            hoverMenu.getStyleClass().add("blurBG");
            ImageView removeIcon = new ImageView();
            removeIcon.setImage(REMOVE_ICON);
            removeIcon.getStyleClass().add(CSS_CLICKABLE);
            removeIcon.setPickOnBounds(true);
            removeIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    removePicture(pictureList, pictureData.indexOf(file));
                    loadPictureList(pictureList);
                }
            });
            if (this.pictureData.size() > 1) {
                if (this.pictureData.indexOf(file) > 0)  {
                    ImageView moveUpIcon = new ImageView();
                    moveUpIcon.setImage(MOVE_UP_ICON);
                    moveUpIcon.getStyleClass().add(CSS_CLICKABLE);
                    moveUpIcon.setPickOnBounds(true);
                    moveUpIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            int index = pictureData.indexOf(file);
                            File nextFile = pictureData.get(index-1);
                            pictureData.set(index, nextFile);
                            pictureData.set(index-1, file);
                            
                            loadPictureList(pictureList);
                        }
                    });
                    hoverMenu.getChildren().add(moveUpIcon);
                }
                if (this.pictureData.indexOf(file) != this.pictureData.size()-1) {
                    ImageView moveDownIcon = new ImageView();
                    moveDownIcon.setImage(MOVE_DOWN_ICON);
                    moveDownIcon.getStyleClass().add(CSS_CLICKABLE);
                    moveDownIcon.setPickOnBounds(true);
                    moveDownIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            int index = pictureData.indexOf(file);
                            File prevFile = pictureData.get(index+1);
                            pictureData.set(index, prevFile);
                            pictureData.set(index+1, file);
                            
                            loadPictureList(pictureList);
                        }
                    });
                    hoverMenu.getChildren().add(moveDownIcon);
                }
            }
            hoverMenu.getChildren().add(removeIcon);
            hoverMenu.setMaxWidth(hoverMenu.getChildren().size()*26.0);
            StackPane sp = new StackPane();
            sp.setAlignment(Pos.TOP_RIGHT);
            sp.getChildren().addAll(iv, hoverMenu);
            
            pictureList.getChildren().add(sp);
        }
    }

    public void addPictures(VBox pictureList, List<File> pList) {
        if (pList != null) {
            for(File f : pList) {
                String mimetype= new MimetypesFileTypeMap().getContentType(f);
                String type = mimetype.split("/")[0];
                if(type.equals("image")) {
                    this.addPictureToList(pictureList, f);
                }
            }
        }
    }

    protected void addPictureToList(VBox pictureList, File file) {
        this.pictureData.add(file);
        loadPictureList(pictureList);
    }

    protected void removePicture(VBox pictureList, int index) {
        if (index > -1) {
            pictureList.getChildren().remove(index);
            this.pictureData.remove(index);
        }
    }
}
