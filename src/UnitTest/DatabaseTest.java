package UnitTest;

import Main.AlertBox;
import model.Database;
import model.Photo;
import model.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

  private String path = "TestPhotos";
  private Database database = new Database(path);
  private Photo logo1 = new Photo(path + File.separator + "logo1 @1.png", "logo1 @1.png");
  private Photo logo2 = new Photo(path + File.separator + "logo2 @2 @3.png", "logo2 @2 @3.png");
  private Photo logo3 = new Photo(path + File.separator + "logo3.png", "logo3.png");
  private Photo logo4 =
      new Photo(path + File.separator + "Logo" + File.separator + "logo4.png", "logo4.png");

  @BeforeEach
  void init() {
    database = new Database(path);
  }

  @AfterEach
  void delete() {
    File file = new File("data.ser");
    if (!file.delete()) {
      AlertBox.show("Error", "Fail to delete." , "OK");
    }
  }

  @Test
  void testPhotos() {
    ArrayList<Photo> result = database.getPhotos();
    ArrayList<Photo> photos = new ArrayList<>();
    photos.add(logo4);
    photos.add(logo1);
    photos.add(logo2);
    photos.add(logo3);
    assertEquals(photos, result);
  }

  @Test
  void testOccupyingPhotos() {
    ArrayList<Photo> result = database.getOccupyingPhotos();
    ArrayList<Photo> photos = new ArrayList<>();
    photos.add(logo4);
    photos.add(logo1);
    photos.add(logo2);
    photos.add(logo3);
    assertEquals(photos, result);
  }

  @Test
  void testOccupyingTags() {
    ArrayList<Tag> result = database.getOccupyingTags();
    ArrayList<Tag> tags = new ArrayList<>();
    Tag tag1 = new Tag("1");
    tag1.addPhoto(logo1);
    Tag tag2 = new Tag("2");
    tag2.addPhoto(logo2);
    Tag tag3 = new Tag("3");
    tag3.addPhoto(logo2);
    tags.add(tag1);
    tags.add(tag2);
    tags.add(tag3);
    for (int i = 0; i < tags.size(); i++) {
      assertEquals(tags.get(i), result.get(i));
    }
  }

  @Test
  void testTags() {
    ArrayList<Tag> result = database.getTags();
    ArrayList<Tag> tags = new ArrayList<>();
    Tag tag1 = new Tag("1");
    tag1.addPhoto(logo1);
    Tag tag2 = new Tag("2");
    tag2.addPhoto(logo2);
    Tag tag3 = new Tag("3");
    tag3.addPhoto(logo2);
    tags.add(tag1);
    tags.add(tag2);
    tags.add(tag3);
    for (int i = 0; i < tags.size(); i++) {
      assertEquals(tags.get(i), result.get(i));
    }
  }

  @Test
  void testPath() {
    String result = database.getPath();
    String path = "TestPhotos";
    assertEquals(path, result);
  }

  @Test
  void tagToPhoto() {
    ArrayList<Tag> tags = database.getTags();
    Tag tag = null;
    for (Tag t : tags) {
      if (t.getName().equals("1")) {
        tag = t;
      }
    }
    ArrayList result = database.tagToPhoto(tag);
    ArrayList<Photo> photos = new ArrayList<>();
    photos.add(logo1);
    assertEquals(photos, result);
  }

  @Test
  void renameHistory() {
    Photo dataLogo = null;
    for (Photo p : database.getOccupyingPhotos()) {
      if (p.equals(logo1)) dataLogo = p;
    }
    database.addTag("3", dataLogo);
    database.renameHistory("logo1 @1.png", dataLogo);
    assertEquals(logo1, dataLogo);
  }

  @Test
  void addExistingTag() {
    Photo dataLogo = null;
    for (Photo p : database.getOccupyingPhotos()) {
      if (p.equals(logo1)) dataLogo = p;
    }
    database.addTag("2", dataLogo);
    Photo photo = new Photo(path + File.separator + "logo1 @1 @2.png", "logo1 @1 @2.png");
    assertEquals(photo, dataLogo);

    // Rename the file name to its original name and tags
    File oldFile = new File(path + File.separator + "logo1 @1 @2.png");
    File newFile = new File(path + File.separator + "logo1 @1.png");
    if (!oldFile.renameTo(newFile)) {
      AlertBox.show("Error", "Fail to rename." , "OK");
    }
  }

  @Test
  void addNewTag() {
    Photo dataLogo = null;
    for (Photo p : database.getOccupyingPhotos()) {
      if (p.equals(logo3)) dataLogo = p;
    }
    database.addTag("4", dataLogo);
    Photo photo = new Photo(path + File.separator + "logo3 @4.png", "logo3 @4.png");
    assertEquals(photo, dataLogo);

    // Rename the file name to its original name and tags
    File oldFile = new File(path + File.separator + "logo3 @4.png");
    File newFile = new File(path + File.separator + "logo3.png");
    if (!oldFile.renameTo(newFile)) {
      AlertBox.show("Error", "Fail to rename." , "OK");
    }
  }

  @Test
  void removeTag() {
    Photo dataLogo = null;
    for (Photo p : database.getOccupyingPhotos()) {
      if (p.equals(logo1)) dataLogo = p;
    }
    Photo photo = new Photo(path + File.separator + "logo1.png", "logo1.png");
    ArrayList<Tag> tags = database.getTags();
    Tag tag = null;
    for (Tag t : tags) {
      if (t.getName().equals("1")) {
        tag = t;
      }
    }
    database.removeTag(tag, dataLogo);
    assertEquals(photo, dataLogo);

    // Rename the file name to its original name and tags
    File oldFile = new File(path + File.separator + "logo1.png");
    File newFile = new File(path + File.separator + "logo1 @1.png");
    if (!oldFile.renameTo(newFile)) {
      AlertBox.show("Error", "Fail to rename." , "OK");
    }
  }

  @Test
  void removeSeveralTags() {
    Photo dataLogo = null;
    for (Photo p : database.getOccupyingPhotos()) {
      if (p.equals(logo2)) dataLogo = p;
    }
    Photo photo = new Photo(path + File.separator + "logo2.png", "logo2.png");
    ArrayList<Tag> tags = database.getTags();
    Tag tag1 = null;
    Tag tag2 = null;
    for (Tag t : tags) {
      if (t.getName().equals("2")) {
        tag1 = t;
      }
      if (t.getName().equals("3")) {
        tag2 = t;
      }
    }
    database.removeTag(tag1, dataLogo);
    database.removeTag(tag2, dataLogo);
    assertEquals(photo, dataLogo);

    // Rename the file name to its original name and tags
    File oldFile = new File(path + File.separator + "logo2.png");
    File newFile = new File(path + File.separator + "logo2 @2 @3.png");
    if (!oldFile.renameTo(newFile)) {
      AlertBox.show("Error", "Fail to rename." , "OK");
    }
  }

  @Test
  void moveUntaggedPhoto() {
    Photo dataLogo = null;
    for (Photo p : database.getOccupyingPhotos()) {
      if (p.equals(logo3)) dataLogo = p;
    }
    database.movePhoto(dataLogo, path + File.separator + "Logo");
    Photo photo =
        new Photo(path + File.separator + "Logo" + File.separator + "logo3.png", "logo3.png");
    assertEquals(photo, dataLogo);

    // Test OccupyingPhotos
    ArrayList<Photo> result = database.getOccupyingPhotos();
    ArrayList<Photo> photos = new ArrayList<>();
    logo3 = new Photo(path + File.separator + "Logo" + File.separator + "logo3.png", "logo3.png");
    photos.add(logo4);
    photos.add(logo1);
    photos.add(logo2);
    photos.add(logo3);
    assertEquals(photos, result);

    // Move the file to its original place
    database.movePhoto(dataLogo, path);
  }

  @Test
  void moveTaggedPhoto() {
    Photo dataLogo = null;
    for (Photo p : database.getOccupyingPhotos()) {
      if (p.equals(logo1)) dataLogo = p;
    }
    database.movePhoto(dataLogo, path + File.separator + "Logo");
    Photo photo =
        new Photo(path + File.separator + "Logo" + File.separator + "logo1 @1.png", "logo1 @1.png");
    assertEquals(photo, dataLogo);

    // Test OccupyingPhotos
    ArrayList<Photo> result = database.getOccupyingPhotos();
    ArrayList<Photo> photos = new ArrayList<>();
    logo1 =
        new Photo(path + File.separator + "Logo" + File.separator + "logo1 @1.png", "logo1 @1.png");
    photos.add(logo4);
    photos.add(logo1);
    photos.add(logo2);
    photos.add(logo3);
    assertEquals(photos, result);

    // Test OccupyingTags
    testOccupyingTags();

    // Move the file to its original place
    database.movePhoto(dataLogo, path);
  }

  @Test
  void serializeAndDeserialize() {
    database.serialize();
    database = null;
    database = new Database();
    testTags();
    testPhotos();
  }
}
