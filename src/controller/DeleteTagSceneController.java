package controller;

import model.Database;
import model.Logging;
import model.Photo;
import model.Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class DeleteTagSceneController {

  /** Photo user is watching */
  private static Photo photo;

  /** Database used by user */
  private static Database database;

  /**
   * Set the photo.
   * @param photo the current photo
   */
  public static void setPhoto(Photo photo) {
    DeleteTagSceneController.photo = photo;
  }

  /**
   * Set the Database.
   * @param database the current database
   */
  public static void setDatabase(Database database) {
    DeleteTagSceneController.database = database;
  }

  /**
   * View all the tags under the photo.
   * @return all the tags that the photo has
   */
  public static ArrayList<String> viewTags() {
    ArrayList<Tag> tags = photo.getTags();
    ArrayList<String> ret = new ArrayList<>();
    for (Tag tag : tags) {
      String tagName = tag.getName();
      ret.add(tagName);
    }
    return ret;
  }

  /**
   * Delete tags from the photo
   * @param tagNames the name of the tags
   * @return new photo
   */
  public static Photo deleteTags(ArrayList<String> tagNames) {
    Photo ret = photo;
    String pastName = photo.getName();
    StringBuilder tags = new StringBuilder();
    for (String name : tagNames) {
      tags.append(name).append(", ");
      ArrayList<Tag> temp = new ArrayList<>(photo.getTags());
      for (Tag tag : temp) {
        if (tag.getName().equals(name)) {
          ret = database.removeTag(tag, photo);
        }
      }
    }
    if (tags.length() != 0) {
      try {
        Logging logger = new Logging();
        logger.getLogger().log(Level.FINE, String.format("Removed the following tag(s): [%s] from photo \"%s\", " +
                        "name is now changed to \"%s\".",
                tags.substring(0, tags.length() - 2), pastName, photo.getName()));
        logger.getFileHandler().close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    photo.updateHistory();
    return ret;
  }
}
