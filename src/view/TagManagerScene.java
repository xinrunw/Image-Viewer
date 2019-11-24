package view;

import Main.AlertBox;
import controller.TagManagerSceneController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TagManagerScene {

    /**
     * show an TagManagerScene which allows user to add and delete tags for the whole application
     * regardless to directories.
     */
    public static void show() {
    TagManagerSceneController.setDatabase();
    Stage TagManager = new Stage();
    TagManager.initModality(Modality.APPLICATION_MODAL);

    TextField newTag = new TextField();
    newTag.setPromptText("Please Input New Tag");

    Button addTag = new Button("Add Tag");
    addTag.setOnAction(
        e -> {
          try {
            if (newTag.getText().trim().length() == 0) {
              AlertBox.show("Empty Tag", "New tag is empty", "OK");
            } else {
              TagManagerSceneController.addTag(newTag.getText());
            }
          } catch (NullPointerException npe) {
            AlertBox.show("Error", "New tag is not typed in", "OK");
          }
          TagManager.close();
          TagManagerScene.show();
        });

    HBox addTagPart = new HBox(10);
    addTagPart.getChildren().addAll(newTag, addTag);

    ListView<String> existTags = new ListView<>();
    for (String tag : TagManagerSceneController.viewTags()) {
      existTags.getItems().addAll(tag);
    }
    existTags.getSelectionModel().selectFirst();

    Button deleteTag = new Button("Delete Tag");
    deleteTag.setOnAction(
        e -> {
          TagManagerSceneController.deleteTag(existTags.getSelectionModel().getSelectedItem());
          TagManager.close();
          TagManagerScene.show();
        });

    HBox deleteTagPart = new HBox(10);
    deleteTagPart.getChildren().addAll(existTags, deleteTag);

    VBox layout = new VBox(10);
    layout.getChildren().addAll(addTagPart, deleteTagPart);
    layout.setAlignment(Pos.CENTER);
    layout.setPadding(new Insets(10, 10, 10, 10));
    Scene TagManagerScene = new Scene(layout);
    TagManager.setTitle("TagManager");
    TagManager.setScene(TagManagerScene);
    TagManager.show();
  }
}
