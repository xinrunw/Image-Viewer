package controller;

import Main.AlertBox;
import Main.ConfirmBox;
import model.Database;
import model.Logging;
import model.Photo;
import model.Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class TagManagerSceneController {

  /** Database used by user */
  private static Database database;

  /** Set the Database. */
  public static void setDatabase() {
    TagManagerSceneController.database = new Database();
  }

  /**
   * Show all the tags in this database.
   *
   * @return an Arraylist of the tags in database in String form
   */
  public static ArrayList<String> viewTags() {
    ArrayList<String> ret = new ArrayList<>();
    for (Tag tag : database.getTags()) {
      ret.add(tag.getName());
    }
    return ret;
  }

  /**
   * Add the tag to this database.
   *
   * @param newTag input name String of a new tag
   */
  public static void addTag(String newTag) {
    database.addTag(newTag);
    try {
      Logging logger = new Logging();
      logger.getLogger().log(Level.FINE, String.format("Added a new tag @%s to Database.",
              newTag));
      logger.getFileHandler().close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Delete the tag from the database, and rename all the photos with this tag.
   *
   * @param tagName name String of a tag which user want to delete
   */
  public static void deleteTag(String tagName) {
    ArrayList<Photo> listPhoto;
    Tag deletedTag = null;
    Boolean confirm;
    for (Tag tag : database.getTags()) {
      if (tag.getName().equals(tagName)) deletedTag = tag;
    }
    if (deletedTag == null) {
      AlertBox.show("Error", "No such a tag can be delete", "OK");
    } else {
      listPhoto = deletedTag.getPhotos();
      if (listPhoto.size() != 0) {
        StringBuilder warning = new StringBuilder("You will rename the following photos: \n");
        for (Photo pic : listPhoto) {
          warning.append(pic.getName()).append("\n");
        }
        confirm = ConfirmBox.show("Warning", warning.toString(), "OK", "Cancel");
        if (confirm) {
          StringBuilder photos = new StringBuilder();
          ArrayList<Photo> temp = new ArrayList<>(listPhoto);
          for (Photo pic : temp) {
            photos.append(pic.getName()).append(", ");
            database.removeTag(deletedTag, pic);
            pic.updateHistory();
          }
          database.removeTag(deletedTag);
          try {
            Logging logger = new Logging();
            logger.getLogger().log(Level.FINE, String.format("Deleted the tag @%s from Database, the photos " +
                            "with that tag (which will be deleted from that photo) are following: [%s].",
                      tagName, photos.substring(0, photos.length() - 2)));
            logger.getFileHandler().close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      } else {
        database.removeTag(deletedTag);
        try {
          Logging logger = new Logging();
          logger.getLogger().log(Level.FINE, String.format("Deleted the tag @%s from Database.", tagName));
          logger.getFileHandler().close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
