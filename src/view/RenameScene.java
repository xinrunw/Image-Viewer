package view;

import Main.ConfirmBox;
import controller.RenameSceneController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Database;
import model.Photo;

public class RenameScene {

  /**
   * show a RenameScene which allows user to rename the photo to a selected history name.
   *
   * @param window the Stage where the photo is shown.
   * @param directory the Database which the photo belongs to.
   * @param image the Photo we are going to rename.
   */
  public static void show(Stage window, Database directory, Photo image) {

    // connect ot Controller
    RenameSceneController.setPhoto(image);
    RenameSceneController.setDatabase(directory);

    // initiate the stage
    Stage RenameStage = new Stage();
    RenameStage.setTitle("Rename Photo");
    RenameStage.initModality(Modality.APPLICATION_MODAL);

    // add all History names to the ChoiceBox
    ChoiceBox<String> names = new ChoiceBox<>();
    for (String hisName : RenameSceneController.getHistoryName()) {
      names.getItems().add(hisName);
    }
    // set default choice
    names.getSelectionModel().selectFirst();

    // renameButton
    Button renameButton = new Button("Rename");
    renameButton.setOnAction(
        e -> {
          // call a confirm Box
          Boolean confirm =
              ConfirmBox.show(
                  "Change Name", "Are you sure you want to rename this photo?", "Yes", "No");
          try {
            if (confirm) {
              // pass input to controller
              Photo changedPhoto =
                  RenameSceneController.rename(names.getSelectionModel().getSelectedItem());
              // back to ImageScene
              ImageScene.show(window, directory, changedPhoto);
              RenameStage.close();
            }
          } catch (NullPointerException ignored) {
          }
        });

    // HBox combine name ChoiceBox and renameButton
    HBox renamePart = new HBox(20);
    renamePart.getChildren().addAll(names, renameButton);

    // closeButton, back to ImageScene
    Button close = new Button("Cancel");
    close.setOnAction(e -> RenameStage.close());

    VBox layout = new VBox(10);
    layout.setAlignment(Pos.CENTER);
    layout.setPadding(new Insets(10, 10, 10, 10));
    layout.getChildren().addAll(renamePart, close);

    Scene renameScene = new Scene(layout);
    RenameStage.setScene(renameScene);
    RenameStage.showAndWait();
  }
}
