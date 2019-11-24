package controller;

import Main.AlertBox;
import model.Database;
import model.Logging;
import model.Photo;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class MoveFileSceneController {

  /** Photo user is watching */
  private static Photo photo;

  /** Database used by user */
  private static Database database;

  /**
   * Set the current photo.
   * @param photo the current photo
   */
  public static void setPhoto(Photo photo) {
    MoveFileSceneController.photo = photo;
  }

  /**
   * Set the current database.
   * @param database the current database
   */
  public static void setDatabase(Database database) {
    MoveFileSceneController.database = database;
  }

  /**
   * Move file to another directory
   * @param newPath the new path for the file
   */
  public static void moveFile(String newPath) {
    File file = new File(photo.getPath());
    String name = photo.getName();
    String pastPath = photo.getPath();
    database.movePhoto(photo, newPath);
    if (!file.renameTo(new File(newPath + File.separator + photo.getName())))
      AlertBox.show("Error", "Failed to move the file.", "OK");
    String currentPath = photo.getPath();
    try {
      Logging logger = new Logging();
      logger.getLogger().log(Level.FINE, String.format("Moved the photo \"%s\" from \"%s\" to \"%s\".",
              name, pastPath, currentPath));
      logger.getFileHandler().close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
