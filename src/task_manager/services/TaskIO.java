package task_manager.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import task_manager.models.Task;

public class TaskIO {

  private static final String FILENAME = "./src/task_manager/data/tasks.txt";

  public static void saveTasks(List<Task> tasks) throws FilePersistenceException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(FILENAME))) {
      for (Task task : tasks) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getTitle()).append("|")
            .append(task.getDescription()).append("|")
            .append(task.getPriority().toString()).append("|")
            .append(task.getCreatedDate().toString());
        writer.println(sb.toString());
      }
    } catch (IOException e) {
      throw new FilePersistenceException("Error saving tasks: " + e.getMessage());
    }
  }

  public static List<Task> loadTasks() throws FilePersistenceException {
    List<Task> tasks = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split("\\|");
        Task task = new Task(parts[0], parts[1], Priority.valueOf(parts[2]));
        tasks.add(task);
      }
    } catch (IOException e) {
      throw new FilePersistenceException("Error loading tasks: " + e.getMessage());
    }

    return tasks;
  }
}