package view;

import controller.DeleteTagSceneController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Database;
import model.Photo;

import java.util.ArrayList;

public class DeleteTagScene {

  /** Stage we will back to */
  private static Stage mainWindow;

  /** VBox of tags CheckBoxes */
  private static VBox tags;

  /**
   * show a DeleteTagScene which allows user to delete selected tags.
   *
   * @param window the Stage where the photo is shown.
   * @param directory the Database which the photo belongs to.
   * @param photo the Photo we are deleting tags from.
   */
  public static void show(Stage window, Database directory, Photo photo) {
    mainWindow = window;

    // initiate the Stage, make ImageScene untouchable
    Stage DeleteTag = new Stage();
    DeleteTag.setTitle("Delete Tags");
    DeleteTag.initModality(Modality.APPLICATION_MODAL);

    // connect to Controller
    DeleteTagSceneController.setPhoto(photo);
    DeleteTagSceneController.setDatabase(directory);

    // tansfer all tags to CheckBoxes and add to VBox
    tags = new VBox(10);
    for (String tag : DeleteTagSceneController.viewTags()) {
      CheckBox box = new CheckBox(tag);
      tags.getChildren().add(box);
    }
    tags.setAlignment(Pos.CENTER_LEFT);
    tags.setPadding(new Insets(10, 10, 10, 10));

    // right part for the Button delete
    VBox right = new VBox();
    right.setAlignment(Pos.CENTER);

    Button delete = new Button("Delete");
    delete.setAlignment(Pos.CENTER);

    // set event to delete Button
    delete.setOnAction(
        e -> {
          // get all checked CheckBoxes of tag
          ArrayList<String> checkTags = new ArrayList<>();
          for (Object box : tags.getChildren().toArray()) {
            if (((CheckBox) box).isSelected()) {
              checkTags.add(((CheckBox) box).getText());
            }
          }
          // pass tags to Controller
          Photo image = DeleteTagSceneController.deleteTags(checkTags);
          // back to ImageScene
          DeleteTag.close();
          ImageScene.show(mainWindow, directory, image);
        });

    right.getChildren().add(delete);

    // bottom part for close Button
    HBox bottom = new HBox();
    bottom.setAlignment(Pos.BOTTOM_RIGHT);

    Button close = new Button("Close");
    close.setAlignment(Pos.BOTTOM_RIGHT);
    close.setOnAction(e -> DeleteTag.close());

    bottom.getChildren().add(close);

    // initiate the layout
    BorderPane layout = new BorderPane();
    layout.setMinSize(200, 200);
    layout.setPadding(new Insets(10, 10, 10, 10));
    layout.setCenter(tags);
    layout.setRight(right);
    layout.setBottom(bottom);

    Scene deleteScene = new Scene(layout);
    DeleteTag.setScene(deleteScene);
    DeleteTag.showAndWait();
  }
}
