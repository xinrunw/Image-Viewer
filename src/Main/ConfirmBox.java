package Main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

  /** the return confirm value we want get from user */
  private static Boolean result = false;

  /**
   * show a AlertBox, display information of the related events.
   *
   * @param title the title of the AlertBox stage
   * @param info information we want to display to the user
   * @param trueButtonMess message shows on the Button representing true
   * @param falseButtonMess message shows on the Button representing false
   */
  public static Boolean show(
      String title, String info, String trueButtonMess, String falseButtonMess) {

    // initiate the Stage
    Stage alertBox = new Stage();
    alertBox.setTitle(title);
    alertBox.setMinWidth(300);
    alertBox.setMinHeight(100);

    // make previous stage untouchable before close this window
    alertBox.initModality(Modality.APPLICATION_MODAL);

    // message to show
    Label message = new Label();
    message.setText(info);

    // trueButton, click to return true
    Button trueButton = new Button();
    trueButton.setText(trueButtonMess);
    trueButton.setOnAction(
        e -> {
          alertBox.close();
          result = true;
        });

    // falseButton, click to return false
    Button falseButton = new Button();
    falseButton.setText(falseButtonMess);
    falseButton.setOnAction(
        e -> {
          alertBox.close();
          result = false;
        });

    // initiate the layout by using VBox
    VBox layout = new VBox(20);
    layout.getChildren().addAll(message, trueButton, falseButton);
    layout.setPadding(new Insets(20, 10, 20, 10));
    layout.setAlignment(Pos.CENTER);

    Scene scene = new Scene(layout);

    alertBox.setScene(scene);
    alertBox.showAndWait();

    return result;
  }
}
