package controller;

import model.Database;
import model.Logging;
import model.Photo;
import model.Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class AddTagSceneController {
  /** Database used by user */
  private static Database database;

  /** Photo user is watching */
  private static Photo photo;

  /**
   * Set the current photo.
   *
   * @param photo the current photo
   */
  public static void setPhoto(Photo photo) {
    AddTagSceneController.photo = photo;
  }

  /**
   * Set the current database.
   *
   * @param database the current database
   */
  public static void setDatabase(Database database) {
    AddTagSceneController.database = database;
  }

  /**
   * View tags that are in the database but not in the photo
   *
   * @return list of name of the tags
   */
  public static ArrayList<String> viewTagsNotOnPhoto() {
    ArrayList<Tag> allTags = database.getTags();
    ArrayList<Tag> tags = photo.getTags();
    ArrayList<String> ret = new ArrayList<>();
    for (Tag tag : allTags) {
      if (!tags.contains(tag)) {
        String tagName = tag.getName();
        ret.add(tagName);
      }
    }
    return ret;
  }

  /**
   * Add tag to the photo
   *
   * @param tagNames the ArrayList of name of the tags
   * @return updated photo
   */
  public static Photo addTag(ArrayList<String> tagNames) {
    Photo ret = photo;
    String pastName = photo.getName();
    StringBuilder tags = new StringBuilder();
    for (String tag : tagNames) {
      database.addTag(tag, photo);
      tags.append(tag).append(", ");
    }
    photo.updateHistory();
    try {
      Logging logger = new Logging();
      logger.getLogger().log(Level.FINE, String.format("Added the following tag(s): [%s] to photo \"%s\", " +
                      "name is now changed to \"%s\".",
              tags.substring(0, tags.length() - 2), pastName, photo.getName()));
      logger.getFileHandler().close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return ret;
  }
}
