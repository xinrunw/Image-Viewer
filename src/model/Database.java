package model;

import Main.AlertBox;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database implements Serializable {

  /** Serialization id */
  private static final long serialVersionUID = 1L;

  /** The photos the directory have. */
  private ArrayList<Photo> photos;

  /** The tags the directory have. */
  private ArrayList<Tag> tags;

  /** The photos that is currently occupying. */
  private ArrayList<Photo> occupyingPhotos;

  /** The tags that are currently occupying. */
  private ArrayList<Tag> occupyingTags;

  /** Currently working path. */
  private String path;

  /** The accepted types of images. */
  private List<String> acceptedType = Arrays.asList(".jpg", ".jpeg", ".gif", ".png", ".bmp");

  /**
   * Create a Database which can deserialize the data.ser and save all the data of this program.
   *
   * @param path the path of the directory user is currently working at.
   */
  public Database(String path) {
    deserialize();
    check();
    this.occupyingPhotos = new ArrayList<>();
    this.occupyingTags = new ArrayList<>();
    read(path);
    this.path = path;
  }

  /** Create a default Database with only datas (no path chosen). */
  public Database() {
    deserialize();
    check();
    this.occupyingPhotos = new ArrayList<>();
    this.occupyingTags = new ArrayList<>();
  }

  /** Deserialize a Database. */
  private void deserialize() {
    this.photos = new ArrayList<>();
    this.tags = new ArrayList<>();
    try {
      FileInputStream fileIn = new FileInputStream("data.ser");
      ObjectInputStream objectIn = new ObjectInputStream(fileIn);
      Database temp = (Database) objectIn.readObject();
      if (temp.photos != null) {
        this.photos = temp.photos;
      }
      if (temp.tags != null) {
        this.tags = temp.tags;
      }
      objectIn.close();
      fileIn.close();
    } catch (FileNotFoundException e) {
      serialize();
    } catch (Exception e1) {
      e1.printStackTrace();
    }
  }

  /** Travel through all photos in Database and check whether they still exist. */
  private void check() {
    ArrayList<Photo> temp = new ArrayList<>(this.photos);
    for (Photo photo : temp) {
      if (!new File(photo.getPath()).exists()) {
        this.photos.remove(photo);
        for (Tag tag : photo.getTags()) {
          tag.removePhoto(photo);
          if (tag.getPhotos().isEmpty()) {
            this.tags.remove(tag);
          }
        }
      }
    }
    serialize();
  }

  /**
   * Analysis all the image files under the path and add them to Database.
   *
   * @param path the path of the directory user wants to analysis.
   */
  private void read(String path) {
    File file = new File(path);
    if (file.exists()) {
      File[] files = file.listFiles();
      if (!(files == null || files.length == 0)) {
        for (File file2 : files) {
          String fileName = file2.getName();
          if (file2.isDirectory()) {
            read(file2.getPath());
          } else if (fileName.lastIndexOf(".") != -1
              && acceptedType.contains(fileName.substring(fileName.lastIndexOf(".")))) {
            boolean isExisted = false;
            for (Photo i : this.photos) {
              if (i.getPath().equals(file2.getPath())) {
                this.occupyingPhotos.add(i);
                for (Tag tag : i.getTags()) {
                  if (!this.occupyingTags.contains(tag)) {
                    this.occupyingTags.add(tag);
                  }
                }

                isExisted = true;
              }
            }
            if (!isExisted) {
              Photo photo = new Photo(file2.getPath(), fileName);
              this.photos.add(photo);
              readPhoto(photo);
              this.occupyingPhotos.add(photo);
            }
          }
        }
      }
    }
    serialize();
  }

  /**
   * Return all the photos under the tag.
   *
   * @param tag the tag that will be transferred to photos
   * @return the photos under the tag
   */
  public ArrayList<Photo> tagToPhoto(Tag tag) {
    ArrayList<Photo> result = new ArrayList<>();
    for (Photo photo : tag.getPhotos()) {
      if (this.occupyingPhotos.contains(photo)) {
        result.add(photo);
      }
    }
    return result;
  }

  /**
   * Rename from history.
   *
   * @param historyName the historyName want to change to
   * @param photo the photo that will change name to
   */
  public void renameHistory(String historyName, Photo photo) {
    ArrayList<Tag> historyTags = photo.getHistoryToTags().get(historyName);
    ArrayList<Tag> photoTags = new ArrayList<>(photo.getTags());
    for (Tag tagH : historyTags) {
      boolean isExisted = false;
      for (Tag tagP : photoTags) {
        if (tagH.toString().equals(tagP.toString())) {
          isExisted = true;
        }
      }
      if (!isExisted) {
        addTag(tagH.toString(), photo);
      }
    }
    for (Tag tagP : photoTags) {
      boolean isExisted = false;
      for (Tag tagH : historyTags) {
        if (tagP.toString().equals(tagH.toString())) {
          isExisted = true;
        }
      }
      if (!isExisted) {
        removeTag(tagP, photo);
      }
    }
  }

  /**
   * Add a tag to a photo.
   *
   * @param name the name of the tag
   * @param photo the photo to add a tag on
   */
  public void addTag(String name, Photo photo) {
    boolean is_existed = false;
    boolean is_shown = false;
    for (Tag tag : photo.getTags()) {
      if (tag.getName().equals(name)) {
        AlertBox.show("Error", "Your selected new tag has already existed.", "OK");
        return;
      }
    }
    for (Tag i : tags) {
      if (i.getName().equals(name)) {
        i.addPhoto(photo);
        photo.addTag(i);
        is_existed = true;
        break;
      }
    }

    if (!is_existed) {
      Tag tag = new Tag(name);
      tag.addPhoto(photo);
      photo.addTag(tag);
      this.tags.add(tag);
    }

    for (Tag k : occupyingTags) {
      if (k.getName().equals(name)) {
        is_shown = true;
      }
    }

    if (!is_shown) {
      Tag tag = new Tag(name);
      this.occupyingTags.add(tag);
    }
    serialize();
  }

  /**
   * Add a tag to the database.
   *
   * @param name the name of the tag
   */
  public void addTag(String name) {
    boolean is_existed = false;
    for (Tag i : tags) {
      if (i.getName().equals(name)) {
        is_existed = true;
        break;
      }
    }
    if (!is_existed) {
      Tag tag = new Tag(name);
      this.tags.add(tag);
    } else {
      AlertBox.show("Error", "Exist such a tag", "OK");
    }
    serialize();
  }

  /**
   * Remove a tag from the photo.
   *
   * @param tag the name of the tag
   * @param photo the photo to remove a tag from
   * @return the resulting photo
   */
  public Photo removeTag(Tag tag, Photo photo) {
    photo.removeTag(tag);
    boolean existed = false;
    for (Photo tempPhoto : tag.getPhotos()) {
      if (this.occupyingPhotos.contains(tempPhoto)) existed = true;
    }
    if (!existed) {
      this.occupyingTags.remove(tag);
    }
    serialize();
    return photo;
  }

  /**
   * Remove a tag that is not associated with any photo.
   *
   * @param tag the name of the tag
   */
  public void removeTag(Tag tag) {
    this.tags.remove(tag);
    serialize();
  }

  /**
   * Delete a photo from occupyingPhotos.
   *
   * @param photo the photo that will be deleted.
   */
  private void deletePhoto(Photo photo) {
    this.occupyingPhotos.remove(photo);
    for (Tag tag : photo.getTags()) {
      boolean existed = false;
      for (Photo tempPhoto : this.occupyingPhotos) {
        if (tempPhoto.getTags().contains(tag)) existed = true;
      }
      if (!existed) {
        ArrayList<Tag> tempTags = new ArrayList<>(this.occupyingTags);
        for (Tag temp : tempTags) {
          if (temp.getName().equals(tag.getName())) this.occupyingTags.remove(temp);
        }
      }
    }
    serialize();
  }

  /**
   * Move a photo to a new path (Keep it in occupyingPhotos only when it is within the path of the
   * chosen directory.
   *
   * @param photo the photo to move
   * @param newPath the new path
   */
  public void movePhoto(Photo photo, String newPath) {
    photo.setPath(newPath + File.separator + photo.getName());
    if (!newPath.contains(path)) {
      deletePhoto(photo);
    }
    serialize();
  }

  /** Serialize the item */
  public void serialize() {
    try {
      FileOutputStream fileOut = new FileOutputStream("data.ser");
      ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
      objectOut.writeObject(this);
      objectOut.close();
      fileOut.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Update photo to database.
   *
   * @param photo the photo that is updated for
   */
  private void readPhoto(Photo photo) {
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> occupyingNames = new ArrayList<>();
    ArrayList<Tag> delete = new ArrayList<>();
    ArrayList<Tag> insert = new ArrayList<>();
    for (Tag t : tags) names.add(t.getName());
    for (Tag t : occupyingTags) occupyingNames.add(t.getName());
    for (Tag tag1 : photo.getTags()) {
      if (!names.contains(tag1.getName())) {
        this.tags.add(tag1);
        names.add(tag1.getName());
        tag1.silentAddPhoto(photo);
        if (!occupyingNames.contains(tag1.getName())) {
          occupyingTags.add(tag1);
          occupyingNames.add(tag1.getName());
        }
      } else {
        for (Tag tag2 : tags) {
          if (tag1.getName().equals(tag2.getName())) {
            tag2.silentAddPhoto(photo);
            insert.add(tag2);
            delete.add(tag1);
            if (!occupyingNames.contains(tag2.getName())) {
              occupyingTags.add(tag2);
              occupyingNames.add(tag2.getName());
            }
          }
        }
      }
    }
    for (Tag tag : delete) photo.deleteTag(tag);
    for (Tag tag : insert) photo.insertTag(tag);
  }

  /**
   * Getter for occupyingTags.
   *
   * @return this occupyingTags
   */
  public ArrayList<Tag> getOccupyingTags() {
    return this.occupyingTags;
  }

  /**
   * Getter for tags.
   *
   * @return this tags
   */
  public ArrayList<Tag> getTags() {
    return this.tags;
  }

  /**
   * Getter for occupyingPhotos.
   *
   * @return this occupyingPhotos
   */
  public ArrayList<Photo> getOccupyingPhotos() {
    return this.occupyingPhotos;
  }

  /**
   * Getter for path.
   *
   * @return this path
   */
  public String getPath() {
    return this.path;
  }

  /**
   * Getter for photos.
   *
   * @return this photos
   */
  public ArrayList<Photo> getPhotos() {
    return this.photos;
  }
}
