package view;

import Main.AlertBox;
import Main.ConfirmBox;
import controller.MoveFileSceneController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Database;
import model.Photo;

import java.io.File;

public class MoveFileScene {

  /**
   * show a MoveFileScene which allows user to move the photo to a selected directory.
   *
   * @param window the Stage where the photo is shown.
   * @param directory the Database which the photo belongs to.
   * @param image the Photo we are going to move.
   */
  public static void show(Stage window, Database directory, Photo image) {

    // initiate the stage
    Stage MoveStage = new Stage();
    MoveStage.setTitle("Move Photo");
    MoveStage.initModality(Modality.APPLICATION_MODAL);

    // connect to Controller
    MoveFileSceneController.setDatabase(directory);
    MoveFileSceneController.setPhoto(image);

    // set TextField for user input path
    TextField path = new TextField();
    path.setPromptText("Direction Path here");
    path.setMinWidth(100);

    // search Button for directoryChooser
    Button search = new Button();
    search.setText("search");
    search.setOnAction(
        e -> {
          DirectoryChooser directoryChooser = new DirectoryChooser();
          File direction = directoryChooser.showDialog(MoveStage);
          if (direction != null) {
            path.setText(direction.getAbsolutePath());
          }
        });

    HBox pathPart = new HBox(10);
    pathPart.getChildren().addAll(path, search);

    // Button move to move photo
    Button move = new Button("Move");
    move.setMinWidth(100);
    move.setOnAction(
        e -> {
          // check if no path given
          if (path.getText().isEmpty()) {
            AlertBox.show("No Directory Path", "There is no directory path", "OK");
          } else {
            File direction = new File(path.getText());
            // check if valid directory
            if (direction.isDirectory()) {
              Boolean confirm =
                  ConfirmBox.show(
                      "Move Photo", "Are you sure you want to move this photo?", "Yes", "No");
              if (confirm) {
                MoveFileSceneController.moveFile(direction.getAbsolutePath());
                // return to Library scene
                Library.show(window, directory);
                MoveStage.close();
              }
            } else {
              AlertBox.show("Not A Directory Path", "This is an illegal directory path", "OK");
            }
          }
        });

    // cancel Button, back to ImageScene
    Button close = new Button("Cancel");
    close.setMinWidth(100);
    close.setOnAction(e -> MoveStage.close());

    VBox layout = new VBox(20);
    layout.setPadding(new Insets(10, 10, 10, 10));
    layout.setAlignment(Pos.CENTER);
    layout.getChildren().addAll(pathPart, move, close);
    Scene moveScene = new Scene(layout);
    MoveStage.setScene(moveScene);
    MoveStage.showAndWait();
  }
}
