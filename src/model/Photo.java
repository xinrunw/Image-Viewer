package model;

import Main.AlertBox;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;

public class Photo implements Serializable {

  /** Serialized id */
  private static final long serialVersionUID = 2L;

  /** The history name of this photo */
  private ArrayList<ArrayList<String>> history = new ArrayList<>();

  /** HashMap of name to a list of tags. */
  private HashMap<String, ArrayList<Tag>> historyToTags = new HashMap<>();

  /** The file name of this photo */
  private String name;

  /** The name without a suffix */
  private String partName;

  /** The string formatted path of the photo. */
  private String path;

  /** The type of the photo */
  private String type;

  /** The tags the photo have. */
  private ArrayList<Tag> tags = new ArrayList<>();

  /**
   * Create a new photo.
   *
   * @param path the path of the photo
   * @param name the name of the photo
   */
  public Photo(String path, String name) {
    this.path = path;
    this.name = name;
    this.historyToTags = new HashMap<>();
    if (name.endsWith(".jpeg")) {
      this.partName = name.substring(0, name.length() - 5);
      this.type = name.substring(name.length() - 5, name.length());
    } else {
      this.partName = name.substring(0, name.length() - 4);
      this.type = name.substring(name.length() - 4, name.length());
    }
    if (this.partName.contains(" @")) {
      String[] rawData = this.partName.split("\\s@");
      // Eliminates repeats.
      LinkedHashSet<String> list = new LinkedHashSet<>(
              Arrays.asList(Arrays.copyOfRange(rawData, 1, rawData.length)));
      if (list.size() + 1 != rawData.length) {
        StringBuilder newName = new StringBuilder();
        newName.append(rawData[0]);
        for (String temp : list) {
          newName.append(" @").append(temp);
        }
        this.rename(newName.toString());
        for (String aList : list) {
          Tag tag = new Tag(aList);
          this.tags.add(tag);
        }
      } else {
        for (String tagName : Arrays.copyOfRange(rawData, 1, rawData.length)) {
          Tag tag = new Tag(tagName);
          this.tags.add(tag);
        }
      }
    }
    this.updateHistory();
  }

  /**
   * Return a list of tags the photo has
   *
   * @return the list of tags.
   */
  public ArrayList<Tag> getTags() {
    return this.tags;
  }

  /**
   * Delete the given tag from tags.
   *
   * @param tag the tag that will be deleted but image's name will not be changed.
   */
  void deleteTag(Tag tag) {
    ArrayList<Tag> temp = new ArrayList<>(this.tags);
    for (Tag i : temp) {
      if (i.getName().equals(tag.getName())) {
        this.tags.remove(i);
      }
    }
    tag.removePhoto(this);
  }

  /**
   * Remove the given tag from tags.
   *
   * @param tag the tag that will be removed.
   */
  void removeTag(Tag tag) {
    deleteTag(tag);
    if (Photo.this.partName.contains(" @" + tag.getName())) {
      rename(
          this.partName.substring(0, this.partName.indexOf(" @" + tag.getName()))
              + this.partName.substring(
                  this.partName.indexOf(" @" + tag.getName()) + tag.getName().length() + 2));
    }
  }

  /**
   * Add the given tag to tags but will not change the name of the image.
   *
   * @param tag the tag that will be added.
   */
  void insertTag(Tag tag) {
    for (Tag i : this.tags) {
      if (i.equals(tag)) {
        return;
      }
    }
    this.tags.add(tag);
  }

  /**
   * Add the given tag to tags.
   *
   * @param tag the tag that will be added.
   */
  void addTag(Tag tag) {
    this.tags.add(tag);
    rename(this.partName + " @" + tag.toString());
  }

  /**
   * Rename the photo.
   *
   * @param name the new name of the photo.
   */
  private void rename(String name) {
    File file = new File(path);
    this.partName = name;
    this.path =
        this.path.substring(0, this.path.length() - this.name.length()) + this.partName + this.type;
    this.name = this.partName + this.type;
    if (!file.renameTo(new File(this.path))) {
      AlertBox.show("Error", "Fail to rename the file (maybe name has been occupied)", "OK");
    }
  }

  /** Update history of the Photo. */
  public void updateHistory() {
    if (this.history.size() == 0
        || !this.history.get(history.size() - 1).get(0).equals(this.name)) {
      ArrayList<String> temp = new ArrayList<>();
      String time = LocalDateTime.now().toString().replace("T", " ");
      temp.add(this.name);
      temp.add(time);
      this.history.add(temp);
      ArrayList<Tag> temp2 = new ArrayList<>();
      temp2.addAll(this.tags);
      historyToTags.put(this.name, temp2);
    }
  }

  /**
   * Getter for historyToTags.
   *
   * @return this historyToTags
   */
  HashMap<String, ArrayList<Tag>> getHistoryToTags() {
    return historyToTags;
  }

  /**
   * Getter for path.
   *
   * @return the path of the photo.
   */
  public String getPath() {
    return this.path;
  }

  /**
   * Setter for path.
   *
   * @param path the new path
   */
  void setPath(String path) {
    this.path = path;
  }

  /**
   * Getter for history.
   *
   * @return the naming history of the photo
   */
  public ArrayList<ArrayList<String>> getHistory() {
    return this.history;
  }

  /**
   * Getter for name.
   *
   * @return the name of the photo
   */
  public String getName() {
    return this.name;
  }

  @Override
  public boolean equals(Object item) {
    if (item instanceof Photo) {
      if (((Photo) item).getPath().equals(this.path)) {
        if (((Photo) item).getName().equals(this.name)) {
          if (!(((Photo) item).getTags().size() == this.tags.size())) {
            return false;
          }
          for (Tag tag1 : ((Photo) item).getTags()) {
            boolean match = false;
            for (Tag tag2 : this.tags) {
              if (tag2.getName().equals(tag1.getName())) {
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
    }
    return false;
  }
}
