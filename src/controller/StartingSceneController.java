package controller;

import model.Database;

public class StartingSceneController {

  /**
   * Initiate Database.
   * @param path the path of the chosen directory
   * @return the current database
   */
  public static Database openDirectory(String path) {
    Database database = new Database(path);
    DirectorySceneController.setDatabase(database);
    return database;
  }
}
