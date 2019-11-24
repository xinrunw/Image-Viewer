package model;

import Main.AlertBox;

import java.io.Serializable;
import java.util.ArrayList;

public class Tag implements Serializable {

  /** Serialized id */
  private static final long serialVersionUID = 3L;
  /** The corresponding photos for this tag. */
  private ArrayList<Photo> photos = new ArrayList<>();
  /** The name of this tag. */
  private String name;

  /** Create a new tag with given name */
  public Tag(String name) {
    this.name = name;
  }

  /** Getter for photos */
  public ArrayList<Photo> getPhotos() {
    return this.photos;
  }

  /**
   * Add a photo to photos.
   *
   * @param photo the photo that is added to photos.
   */
  public void addPhoto(Photo photo) {
    for (Photo i : this.photos) {
      if (i.getPath().equals(photo.getPath())) {
        AlertBox.show("Error", "This tag is already added to the photo.", "OK");
        return;
      }
    }
    this.photos.add(photo);
  }

  /**
   * Add a photo to photos without warning.
   *
   * @param photo the photo that is added to photos.
   */
  void silentAddPhoto(Photo photo) {
    for (Photo i : this.photos) {
      if (i.getPath().equals(photo.getPath())) {
        return;
      }
    }
    this.photos.add(photo);
  }

  /**
   * Remove a photo to photos.
   *
   * @param photo the photo that is removed from photos.
   */
  public void removePhoto(Photo photo) {
    ArrayList<Photo> temp = new ArrayList<>(this.photos);
    for (Photo i : temp) {
      if (i.equals(photo)) {
        this.photos.remove(i);
        return;
      }
    }
  }

  @Override
  public String toString() {
    return this.name;
  }

  /**
   * Getter for the name of this tag
   *
   * @return the name of the tag.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Set the name of this tag
   *
   * @param name the name that want to be updated to.
   */
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object item) {
    if (item instanceof Tag) {
      if (item.toString().equals(this.name)) {
        if (!(((Tag) item).getPhotos().size() == this.getPhotos().size())) {
          return false;
        }
        for (Photo photo1 : ((Tag) item).getPhotos()) {
          boolean match = false;
          for (Photo photo2 : this.photos) {
            if (photo2.getPath().equals(photo1.getPath())) {
              match = true;
            }
          }
          if (!match) {
            return false;
          }
        }
        return true;
      }
    }
    return false;
  }
}
