package controller;

import java.util.ArrayList;
import model.Database;
import model.Photo;
import model.Tag;

public class DirectorySceneController {

  /** Database used by user */
  private static Database database;

  /**
   * Set the database.
   * @param database the current database
   */
  public static void setDatabase(Database database) {
    DirectorySceneController.database = database;
  }

  /**
   * View all the occupying tags under the directory.
   * @return all the occupying tags
   */
  public static ArrayList<String> viewAllTags() {
    ArrayList<Tag> tags = database.getOccupyingTags();
    ArrayList<String> ret = new ArrayList<>();
    for (Tag tag : tags) {
      ret.add(tag.getName());
    }
    return ret;
  }

  /**
   * View all the occupying photos under the directory.
   * @return all the occupying photos
   */
  public static ArrayList<Photo> viewAllPhotos() {
    return database.getOccupyingPhotos();
  }

  public static ArrayList<Photo> tagToPhoto(String tag) {
    Tag chosenTag = null;
    for (Tag tags : database.getTags()) {
      if (tags.getName().equals(tag)) {
        chosenTag = tags;
      }
    }
    return database.tagToPhoto(chosenTag);
  }
}
