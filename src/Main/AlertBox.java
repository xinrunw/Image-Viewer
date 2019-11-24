package Main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

  /**
   * show a AlertBox, display information of the related events.
   *
   * @param title the title of the AlertBox stage
   * @param info information we want to display to the user
   * @param buttonNote String shows on the notice Button
   */
  public static void show(String title, String info, String buttonNote) {

    // initiate the Stage
    Stage alertBox = new Stage();
    alertBox.setTitle(title);
    alertBox.setMinWidth(300);
    alertBox.setMinHeight(100);

    // make previous stage untouchable before close this window
    alertBox.initModality(Modality.APPLICATION_MODAL);

    // message to user
    Label message = new Label();
    message.setText(info);

    // Button for check noticed
    Button notice = new Button();
    notice.setText(buttonNote);
    notice.setOnAction(e -> alertBox.close());

    // initiate the layout with VBox
    VBox layout = new VBox(20);
    layout.getChildren().addAll(message, notice);
    layout.setPadding(new Insets(20, 10, 20, 10));
    layout.setAlignment(Pos.CENTER);

    Scene scene = new Scene(layout);

    alertBox.setScene(scene);
    alertBox.showAndWait();
  }
}
