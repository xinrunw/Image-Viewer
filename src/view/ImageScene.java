package view;

import Main.AlertBox;
import controller.ImageSceneController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Database;
import model.ImagePhoto;
import model.Photo;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ImageScene {

  /**
   * show an ImageScene which allows user to view the selected Photo, and editing the photo by
   * clicking the buttons.
   *
   * @param window the Stage where the photo is shown.
   * @param directory the Database which the photo belongs to.
   * @param image the Photo we are viewing.
   */
  public static void show(Stage window, Database directory, Photo image) {

    // connect to Controller
    ImageSceneController.setPhoto(image);

    // initiate the center part with ScrollPane, set property of the ScrollPane
    ScrollPane center = new ScrollPane();
    ImagePhoto ipic = new ImagePhoto(image);
    ImageView showpic = new ImageView(ipic);
    showpic.setPreserveRatio(true);
    showpic.setFitWidth(500);
    showpic.setFitHeight(500);
    center.setContent(showpic);
    center.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    center.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

    // top part as Label display name
    Label top = new Label();
    top.setPadding(new Insets(10, 10, 10, 10));
    top.setText(image.getPath());

    // Buttons for right part
    Button add = new Button("Add Tags");
    add.setMinWidth(120);
    add.setOnAction(e -> AddTagScene.show(window, directory, image));

    Button delete = new Button("Delete Tags");
    delete.setMinWidth(120);
    delete.setOnAction(
        e -> {
          if (ImageSceneController.viewTags().size() == 0) {
            AlertBox.show("No Tags", "There is no tag being added to this photo", "OK");
          } else {
            // open DeleteTagScene
            DeleteTagScene.show(window, directory, image);
          }
        });

    Button rename = new Button("Rename Photo");
    rename.setMinWidth(120);
    rename.setOnAction(
        e -> {
          if (image.getHistory().size() == 0) {
            AlertBox.show(
                "No History Names", "The photo has not been changed to other names yet", "OK");
          } else {
            // open RenameScene
            RenameScene.show(window, directory, image);
          }
        });

    Button move = new Button("Move");
    move.setMinWidth(120);
    move.setOnAction(
        e -> {
          // open MoveFileScene
          MoveFileScene.show(window, directory, image);
        });

    Button openDirc = new Button("Open Directory");
    openDirc.setMinWidth(120);
    openDirc.setOnAction(
        e -> {
          new Thread(() -> {
            Desktop desktop = Desktop.getDesktop();
            File pic = new File(image.getPath());
            File dirc = new File(pic.getParent());
            try {
              desktop.open(dirc);
            } catch (IOException ignored) {
            }
          }).start();
        });

    // right part as VBox
    VBox right = new VBox(30);
    right.setPadding(new Insets(10, 10, 10, 10));
    right.setAlignment(Pos.CENTER);
    right.getChildren().addAll(add, delete, rename, move, openDirc);

    // left part as ListView show list of tags of the Photo and History of renaming
    VBox left = new VBox(10);
    left.setAlignment(Pos.CENTER_LEFT);
    left.setPadding(new Insets(10, 10, 10, 10));

    Label picTagstile = new Label("Tags of this photo:");

    ListView<String> picTags = new ListView<>();
    for (String tags : ImageSceneController.viewTags()) {
      picTags.getItems().add(tags);
    }
    picTags.setMaxHeight(150);
    picTags.setFocusTraversable(false);

    Label picHistitle = new Label("Naming history of this photo:");

    ListView<String> picHis = new ListView<>();
    for (ArrayList<String> list : image.getHistory()) {
      String hist = "";
      hist = hist + list.get(0) + ", since " + list.get(1);
      picHis.getItems().add(hist);
    }
    Collections.reverse(picHis.getItems());
    picHis.setFocusTraversable(false);

    picHis.setMaxHeight(150);

    left.getChildren().addAll(picTagstile, picTags, picHistitle, picHis);

    // Buttons for bottom part
    Button backButton = new Button();
    backButton.setText("back");
    backButton.setOnAction(e -> Library.show(window, directory));

    Button closeButton = new Button();
    closeButton.setText("close");
    closeButton.setOnAction(e -> window.close());

    // set bottom part as HBox, include back and close buttom
    HBox bottom = new HBox(10);
    bottom.setAlignment(Pos.BOTTOM_RIGHT);
    bottom.setPadding(new Insets(10, 10, 10, 10));
    bottom.getChildren().addAll(backButton, closeButton);

    // initialize layout with BorderPane
    BorderPane layout = new BorderPane();
    layout.setMinSize(500, 500);
    layout.setTop(top);
    layout.setBottom(bottom);
    layout.setLeft(left);
    layout.setRight(right);
    layout.setCenter(center);

    Scene scene = new Scene(layout);
    window.setScene(scene);
  }
}
