package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import model.Database;
import model.Logging;
import model.Photo;

public class RenameSceneController {

  /** Database used by user */
  private static Database database;

  /** Photo user is watching */
  private static Photo photo;

  /**
   * Set the photo.
   * @param photo the current photo
   */
  public static void setPhoto(Photo photo) {
    RenameSceneController.photo = photo;
  }

  /**
   * Set the database.
   * @param database the current database
   */
  public static void setDatabase(Database database) {
    RenameSceneController.database = database;
  }

  /**
   * return HistoryNames of photo
   * @return list of historyNames
   */
  public static ArrayList<String> getHistoryName() {
    ArrayList<String> names = new ArrayList<>();
    for (ArrayList<String> list : photo.getHistory()) {
      // get rid of same-look history names
      if (!names.contains(list.get(0))) {
        names.add(list.get(0));
      }
    }
    return names;
  }

  /**
   * Rename the photo by given name from history.
   * @param historyName the give history name
   * @return updated photo
   */
  public static Photo rename(String historyName) {
    String pastName = photo.getName();
    database.renameHistory(historyName, photo);
    String currentName = photo.getName();
    if (!pastName.equals(currentName)) {
      try {
        Logging logger = new Logging();
        logger.getLogger().log(Level.FINE, String.format("Renamed using history name: photo's name is changed " +
                        "from \"%s\" to \"%s\".",
                pastName, currentName));
        logger.getFileHandler().close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    photo.updateHistory();
    return photo;
  }
}
