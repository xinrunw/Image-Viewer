package UnitTest;

import model.Photo;
import model.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

  @Test
  void tagParameter(){
  Tag tag = new Tag("Tag!");
  String string = tag.toString();
  String name = tag.getName();
  assertEquals("Tag!", string);
  assertEquals("Tag!", name);
  }

  @Test
  void addOnePhoto() {
    Tag tag = new Tag("Tag!");
    Photo photo = new Photo("path", "photo1");
    tag.addPhoto(photo);
    ArrayList<Photo> list = new ArrayList<>();
    list.add(photo);
    assertEquals(list, tag.getPhotos());
  }

  @Test
  void addSeveralPhoto() {
    Tag tag = new Tag("Tag!");
    Photo photo1 = new Photo("path1", "photo1");
    Photo photo2 = new Photo("path2", "photo2");
    Photo photo3 = new Photo("path3", "photo3");
    tag.addPhoto(photo1);
    tag.addPhoto(photo2);
    tag.addPhoto(photo3);
    ArrayList<Photo> list = new ArrayList<>();
    list.add(photo1);
    list.add(photo2);
    list.add(photo3);
    assertEquals(list, tag.getPhotos());
  }

  @Test
  void removePhoto() {
    Tag tag = new Tag("Tag!");
    Photo photo1 = new Photo("path1", "photo1");
    Photo photo2 = new Photo("path2", "photo2");
    Photo photo3 = new Photo("path3", "photo3");
    tag.addPhoto(photo1);
    tag.addPhoto(photo2);
    tag.addPhoto(photo3);
    tag.removePhoto(photo2);
    ArrayList<Photo> list = new ArrayList<>();
    list.add(photo1);
    list.add(photo3);
    assertEquals(list, tag.getPhotos());
  }

  @Test
  void setName() {
    Tag tag = new Tag("Tag!");
    tag.setName("Good!");
    assertEquals("Good!", tag.getName());
  }

  @Test
  void equals() {
    Tag tag1 = new Tag("Tag!");
    Tag tag2 = new Tag("Tag!");
    Photo photo1 = new Photo("path1", "photo1");
    Photo photo2 = new Photo("path2", "photo2");
    Photo photo3 = new Photo("path3", "photo3");
    tag1.addPhoto(photo1);
    tag1.addPhoto(photo2);
    tag1.addPhoto(photo3);
    tag2.addPhoto(photo3);
    tag2.addPhoto(photo2);
    tag2.addPhoto(photo1);
    assertEquals(tag1, tag2);
  }
}