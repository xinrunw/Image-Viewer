package model;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Logging {

  /** my logger */
  private Logger logger = Logger.getLogger(Logging.class.getName());

  /** the fileHandler for the logger */
  private FileHandler fileHandler;

  /**
   * Create new Logging class.
   *
   * @throws IOException Handled exception involving input/output error.
   */
  public Logging() throws IOException {
    fileHandler = new FileHandler("log.txt", true);
    logger.setLevel(Level.ALL);
    fileHandler.setLevel(Level.FINE);
    fileHandler.setFormatter(new java.util.logging.SimpleFormatter());
    logger.addHandler(fileHandler);
  }

  /**
   * Getter for logger.
   *
   * @return this logger
   */
  public Logger getLogger() {
    return logger;
  }

  /**
   * getter for fileHandler.
   *
   * @return this fileHandler
   */
  public FileHandler getFileHandler() {
    return fileHandler;
  }
}
