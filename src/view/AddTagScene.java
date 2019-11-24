package view;

import Main.AlertBox;
import controller.AddTagSceneController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Database;
import model.Photo;

import java.util.ArrayList;

public class AddTagScene {

  /** Stage we will back to */
  private static Stage mainWindow;

  /** VBox of tags CheckBoxes */
  private static VBox tags;

  /**
   * show an AddTagScene which allows user to add selected tags and new tags to the photo.
   *
   * @param window the Stage where the photo is shown.
   * @param directory the Database which the photo belongs to.
   * @param photo the Photo we are adding tags to.
   */
  public static void show(Stage window, Database directory, Photo photo) {
    mainWindow = window;

    // initiate the AddTagStage, make ImageScene untouchable
    Stage AddTag = new Stage();
    AddTag.setTitle("Add Tags");
    AddTag.initModality(Modality.APPLICATION_MODAL);

    // connect to Controller
    AddTagSceneController.setPhoto(photo);
    AddTagSceneController.setDatabase(directory);

    // tansfer all tags to CheckBoxes and add to VBox
    tags = new VBox(10);

    // set the first row of the VBox as a editable TextField to add non-existing new tag
    TextField newTag = new TextField();
    newTag.setPromptText("New Tag Here");
    CheckBox selfTagCheck = new CheckBox();
    HBox addSelfTag = new HBox(10);
    addSelfTag.getChildren().addAll(selfTagCheck, newTag);

    tags.getChildren().add(addSelfTag);

    // set the following rows of the VBox as list of existing tags excluding tags of the Photo
    for (String tag : AddTagSceneController.viewTagsNotOnPhoto()) {
      CheckBox box = new CheckBox(tag);
      tags.getChildren().add(box);
    }
    tags.setAlignment(Pos.CENTER_LEFT);
    tags.setPadding(new Insets(10, 10, 10, 10));

    // right part for the Button add
    VBox right = new VBox();
    right.setAlignment(Pos.CENTER);

    Button add = new Button("Add");
    add.setAlignment(Pos.CENTER);

    // set event to add Button
    add.setOnAction(
        e -> {
          ArrayList<String> selectedTags = new ArrayList<>();
          HBox firstRow = (HBox) tags.getChildren().get(0);
          if (((CheckBox) firstRow.getChildren().get(0)).isSelected()) {
            TextField inputTag = (TextField) firstRow.getChildren().get(1);
            try {
              if (inputTag.getText().trim().length() == 0) {
                AlertBox.show("Empty Tag", "Your new tag is empty", "OK");
              } else {
                selectedTags.add(inputTag.getText());
              }
            } catch (NullPointerException npe) {
              AlertBox.show("Empty Tag", "You selected new tag but nothing was typed in", "OK");
            }
          }

          for (int i = 1; i < tags.getChildren().size(); i++) {
            if (((CheckBox) tags.getChildren().get(i)).isSelected()) {
              selectedTags.add(((CheckBox) tags.getChildren().get(i)).getText());
            }
          }
          if (selectedTags.size() == 0) {
            AlertBox.show("No Tags Added", "No tag has been selected", "OK");
          } else {
            Photo changedPhoto = AddTagSceneController.addTag(selectedTags);
            AddTag.close();
            ImageScene.show(mainWindow, directory, changedPhoto);
          }
        });

    right.getChildren().add(add);

    // bottom part for close Button
    HBox bottom = new HBox();
    bottom.setAlignment(Pos.BOTTOM_RIGHT);

    Button close = new Button("Close");
    close.setAlignment(Pos.BOTTOM_RIGHT);
    close.setOnAction(e -> AddTag.close());

    bottom.getChildren().add(close);

    // initiate the layout
    BorderPane layout = new BorderPane();
    layout.setMinSize(200, 200);
    layout.setPadding(new Insets(10, 10, 10, 10));
    layout.setCenter(tags);
    layout.setRight(right);
    layout.setBottom(bottom);

    Scene addScene = new Scene(layout);
    AddTag.setScene(addScene);
    AddTag.showAndWait();
  }
}
