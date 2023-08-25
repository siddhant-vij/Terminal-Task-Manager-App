package task_manager.services;

import task_manager.models.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;


public class TaskManager {
  private List<Task> tasks;
  private Stack<State> states = new Stack<>();

  public TaskManager() {
    try {
      this.tasks = TaskIO.loadTasks(); // Load tasks from file on startup
    } catch (FilePersistenceException e) {
      System.out.println(e.getMessage());
    }

    // Ensure tasks is initialized even if there was an exception
    if (this.tasks == null) {
      this.tasks = new ArrayList<>();
    }
  }

  public void createTask(String title, String description, Priority priority) {
    Task task = new Task(title, description, priority);

    // Ensure tasks is not null before adding
    if (this.tasks == null) {
      this.tasks = new ArrayList<>();
    }

    tasks.add(task);
  }

  public boolean deleteTask(Task task) {
    return tasks.remove(task);
  }

  public Task getTaskByIndex(int index) {
    return tasks.get(index);
  }

  public List<Task> getAllTasks() {
    return tasks;
  }

  public List<Task> getTasksSortedByPriority() {
    tasks.sort(Comparator.comparingInt(task -> task.getPriority().ordinal()));
    return tasks;
  }

  public List<Task> getTasksSortedByDate() {
    tasks.sort(Comparator.comparing(Task::getCreatedDate));
    return tasks;
  }

  public List<Task> filterTasks(TaskFilter filter) {
    return tasks.stream()
        .filter(filter::matches)
        .collect(Collectors.toList());
  }

  public void saveState() {
    states.push(new State(tasks));
  }

  public void undoLastAction() {
    if (!states.isEmpty()) {
      State prevState = states.pop();
      tasks = prevState.getTasksSnapshot();
    } else {
      System.out.println("No actions to undo.");
    }
  }

  public void saveTasksToFile() {
    try {
      TaskIO.saveTasks(tasks);
      System.out.println("Tasks saved successfully!");
    } catch (FilePersistenceException e) {
      System.out.println(e.getMessage());
    }
  }

  public class BulkOperations {
    public void markTasksAsComplete(List<Integer> taskIndices) {
      for (Integer index : taskIndices) {
        if (index >= 0 && index < tasks.size()) {
          Task task = tasks.get(index);
          task.markAsComplete();
        }
      }
    }

    // Add more bulk operations methods here in the future as needed
  }
}