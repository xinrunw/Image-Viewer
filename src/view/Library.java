package view;

import Main.AlertBox;
import Main.Main;
import controller.DirectorySceneController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Database;
import model.ImagePhoto;
import model.Photo;

import java.util.ArrayList;

public class Library {

  /**
   * show a Library which allows user to check and selected tags and photos from the directory.
   *
   * @param window the Stage where the photos from directory is shown.
   * @param directory the Database which the photos and directory belongs to.
   */
  public static void show(Stage window, Database directory) {

    // connect to Controller
    DirectorySceneController.setDatabase(directory);

    // set the center part as ListView
    ListView<HBox> center = new ListView<>();
    center.setPadding(new Insets(10, 10, 10, 10));
    center.setPrefSize(400, 500);

    // set top part a label showing directory path
    Label top = new Label();
    top.setText("Directory from " + directory.getPath());

    // Buttons for bottom part
    Button backButton = new Button();
    backButton.setText("back");
    backButton.setOnAction(e -> window.setScene(Main.startScene));

    Button closeButton = new Button();
    closeButton.setText("close");
    closeButton.setOnAction(e -> window.close());

    // set bottom part as HBox, include back and close buttom
    HBox bottom = new HBox(10);
    bottom.setAlignment(Pos.BOTTOM_RIGHT);
    bottom.setPadding(new Insets(10, 10, 10, 10));
    bottom.getChildren().addAll(backButton, closeButton);

    // set left part as ListView, display exist tags in this directory
    ListView<String> left = new ListView<>();

    // set the first item in list
    left.getItems().add("All photos");
    for (String tag : DirectorySceneController.viewAllTags()) {
      left.getItems().add(tag);
    }
    left.setPrefSize(100, 500);

    // add listener to ListView, make it has the ability to get photos of the chosen tag
    left.getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (listener, previous, chosen) -> {
              // if use choose All photos, show all the photos in the directory
              if (chosen.equals("All photos")) {
                // clear the center
                center.getItems().remove(0, center.getItems().size());
                // get photos from controller
                ArrayList<Photo> photos = DirectorySceneController.viewAllPhotos();
                for (Photo pic : photos) {
                  // transfer photo to Image's subclass
                  ImagePhoto ipic = new ImagePhoto(pic);

                  // initiate the ImageView to show the image
                  ImageView showpic = new ImageView();
                  showpic.setFitHeight(200);
                  showpic.setFitWidth(200);
                  showpic.setPreserveRatio(true);
                  showpic.setImage(ipic);

                  // set the name of the photo
                  Label name = new Label();
                  name.setText(pic.getName());

                  // initate the HBox contains
                  HBox picWtihName = new HBox(20);
                  picWtihName.getChildren().addAll(showpic, name);

                  center.getItems().add(picWtihName);
                }
              } else {
                // clear the center
                center.getItems().remove(0, center.getItems().size());
                // get photos from controller
                ArrayList<Photo> photos = DirectorySceneController.tagToPhoto(chosen);
                for (Photo pic : photos) {
                  // transfer photo to Image's subclass
                  ImagePhoto ipic = new ImagePhoto(pic);

                  // initiate the ImageView to show the image
                  ImageView showpic = new ImageView();
                  showpic.setFitHeight(200);
                  showpic.setFitWidth(200);
                  showpic.setPreserveRatio(true);
                  showpic.setImage(ipic);

                  // set the name of the photo
                  Label name = new Label();
                  name.setText(pic.getName());

                  // initate the HBox contains
                  HBox picWtihName = new HBox(20);
                  picWtihName.getChildren().addAll(showpic, name);

                  center.getItems().add(picWtihName);
                }
              }
            });

    // make default selection to All photos
    left.getSelectionModel().selectFirst();

    // set right part with VBox contains a button to open the chosen image
    Button open = new Button();
    open.setText("Open Image");
    open.setMinSize(100, 30);
    open.setOnAction(
        e -> {
          try {
            HBox chosenBox = center.getSelectionModel().getSelectedItems().get(0);
            ImageView selected = (ImageView) chosenBox.getChildren().get(0);
            ImageScene.show(window, directory, ((ImagePhoto) selected.getImage()).getPhoto());
          } catch (Exception npe) {
            AlertBox.show("Unchosen Image", "You have not chosen an image!", "OK");
          }
        });

    VBox right = new VBox(30);
    right.getChildren().addAll(open);
    right.setAlignment(Pos.CENTER);
    right.setPadding(new Insets(10, 10, 10, 10));

    // initialize the layout with BorderPane
    BorderPane layout = new BorderPane();
    layout.setBottom(bottom);
    layout.setLeft(left);
    layout.setRight(right);
    layout.setCenter(center);
    layout.setTop(top);

    // set the Scene
    Scene libraryScene = new Scene(layout);
    layout.setMinSize(500, 500);

    // make the stage show this scene
    window.setScene(libraryScene);
  }
}
