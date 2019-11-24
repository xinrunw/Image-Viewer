package controller;

import java.util.ArrayList;
import model.Photo;
import model.Tag;

public class ImageSceneController {

  /** Photo user is watching */
  private static Photo photo;

  /**
   * Set the current photo.
   *
   * @param photo the current photo
   */
  public static void setPhoto(Photo photo) {
    ImageSceneController.photo = photo;
  }

  /**
   * Return the current photo
   *
   * @return this photo
   */
  public static Photo getPhoto() {
    return photo;
  }

  /**
   * View the name of all tags that the photo has.
   *
   * @return the list of name of tags
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
}
