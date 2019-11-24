package model;

import javafx.scene.image.Image;

import java.io.File;

// ImagePhoto will convert a Photo class to Image class with ability to give back the Photo, only
// for display methods
public class ImagePhoto extends Image {

  /** photo format of this image. */
  private Photo photo;

  /**
   * Create a new image by pass in a photo.
   *
   * @param photo the photo that will be converted to Image
   */
  public ImagePhoto(Photo photo) {
    super(new File(photo.getPath()).toURI().toString());
    this.photo = photo;
  }

  /**
   * Getter for photo.
   *
   * @return this photo
   */
  public Photo getPhoto() {
    return this.photo;
  }
}
