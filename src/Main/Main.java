package Main;

import controller.StartingSceneController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import view.Library;
import view.TagManagerScene;

import java.awt.*;
import java.io.File;

public class Main extends Application {

  /** the Scene we are using */
  public static Scene startScene;
  /** path get from user input */
  private TextField path;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    primaryStage.setTitle("PhotoManager");

    // initiate searchButton
    Button searchButton = new Button();
    searchButton.setText("search");

    // set event to searchButton
    searchButton.setOnAction(
        e -> {
          DirectoryChooser directoryChooser = new DirectoryChooser();
          File directory = directoryChooser.showDialog(primaryStage);
          if (directory != null) {
            path.setText(directory.getAbsolutePath());
          }
        });

    // initiate open Button
    Button open = new Button();
    open.setText("open directory");
    open.setMinSize(120,20);

    // set event to open Button
    open.setOnAction(
        e -> {
          if (path.getText().isEmpty()) {
            AlertBox.show("No Directory Path", "There is no directory path", "OK");
          } else {
            File directory = new File(path.getText());
            if (directory.isDirectory()) {
              Library.show(
                  primaryStage, StartingSceneController.openDirectory(directory.getAbsolutePath()));
            } else {
              AlertBox.show("Not A Directory Path", "This is an illegal directory path", "OK");
            }
          }
        });

    // initiate the path TextField for user input path
    path = new TextField();
    path.setPromptText("URL here");
    path.setMinWidth(100);

    // add our logo for display
    Image logopic = new Image(new File("logo.png").toURI().toString());
    ImageView logo = new ImageView(logopic);
    logo.setFitHeight(150);
    logo.setFitWidth(150);

    // HBox for path and searchButton
    HBox pathPart = new HBox(10);
    pathPart.getChildren().addAll(path, searchButton);
    pathPart.setAlignment(Pos.CENTER);
    pathPart.setPadding(new Insets(10, 10, 10, 10));

    //Button for open Tag Manager
    Button tagManager = new Button("Tag Manager");
    tagManager.setMinSize(120,20);
    tagManager.setOnAction(e-> TagManagerScene.show());

    //Button for open log
    Button log = new Button("Log");
    log.setMinSize(120,20);
    log.setOnAction(e-> {
      File temp = new File("log.txt");
      if (temp.exists()) {
        if (Desktop.isDesktopSupported()) {
          new Thread(() -> {
            Desktop desktop = Desktop.getDesktop();
            try {
              desktop.open(new File("log.txt"));
            } catch (Exception Exc) {
              AlertBox.show("No Log", "No change has been done, so there is no such a log file", "OK");
            }
          }).start();
        }
      } else {
        AlertBox.show("No Log", "No change has been done, so there is no such a log file", "OK");
      }
    });

    // initiate the layout by using VBox
    VBox layout = new VBox(20);
    layout.setPadding(new Insets(10, 10, 10, 10));
    layout.setAlignment(Pos.CENTER);
    layout.getChildren().addAll(logo, pathPart, open, tagManager, log);

    // initiate the startScene
    startScene = new Scene(layout, 300, 400);

    // set startScene to stage and show
    primaryStage.setScene(startScene);
    primaryStage.show();
  }
}
