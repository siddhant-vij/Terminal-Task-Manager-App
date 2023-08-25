package task_manager.services;

class InvalidTaskException extends Exception {
  public InvalidTaskException(String message) {
    super(message);
  }
}

class FilePersistenceException extends Exception {
  public FilePersistenceException(String message) {
    super(message);
  }
}
