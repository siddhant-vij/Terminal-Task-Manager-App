package task_manager.services;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import task_manager.models.Task;

public class TaskManagerSystem {

  private static final TaskManager taskManager = new TaskManager();
  private static final Scanner SCANNER = new Scanner(System.in);

  private static String getUserInput(String prompt) {
    System.out.print(prompt);
    String input = "";
    while (true) {
      input = SCANNER.nextLine();
      if (input.isEmpty()) {
        System.out.println("Input error. Please try again.");
      } else {
        break;
      }
    }
    return input;
  }

  private static void createTask() {
    taskManager.saveState();

    String title = "";
    String description = "";
    Priority priority = null;

    while (title.isEmpty()) {
      title = getUserInput("Enter task title: ");
      if (title.isEmpty()) {
        System.out.println("Title cannot be empty!");
      }
    }

    while (description.isEmpty()) {
      description = getUserInput("Enter task description: ");
      if (description.isEmpty()) {
        System.out.println("Description cannot be empty!");
      }
    }

    while (priority == null) {
      try {
        String priorityInput = getUserInput("Enter task priority (LOW, MEDIUM, HIGH): ");
        priority = Priority.valueOf(priorityInput.toUpperCase());
      } catch (IllegalArgumentException e) {
        System.out.println("Invalid priority. Please choose from LOW, MEDIUM, or HIGH.");
      }
    }

    taskManager.createTask(title, description, priority);
  }

  private static void viewAllTasks() {
    System.out.println("Select sorting method: ");
    System.out.println("1. By Priority");
    System.out.println("2. By Creation Date");
    System.out.print("Enter choice: ");

    int choice = 0;
    try {
      choice = Integer.parseInt(SCANNER.nextLine());
    } catch (NumberFormatException e) {
      System.out.println("Invalid input. Please enter a number.");
    }

    List<Task> tasks;

    switch (choice) {
      case 1:
        tasks = taskManager.getTasksSortedByPriority();
        break;
      case 2:
        tasks = taskManager.getTasksSortedByDate();
        break;
      default:
        System.out.println("Invalid choice. Sorting by creation date.");
        tasks = taskManager.getTasksSortedByDate();
    }

    if (tasks.isEmpty()) {
      System.out.println("No tasks available.");
      return;
    }

    for (Task task : tasks) {
      System.out.println(task);
    }
  }

  private static void updateTask() {
    taskManager.saveState();

    if (taskManager.getAllTasks().isEmpty()) {
      System.out.println("No tasks available to update.");
      return;
    }

    System.out.println("Select a task to update:");

    List<Task> tasks = taskManager.getAllTasks();
    for (int i = 0; i < tasks.size(); i++) {
      System.out.println((i + 1) + ". " + tasks.get(i));
    }

    int taskIndex = 0;
    try {
      taskIndex = Integer.parseInt(SCANNER.nextLine());
    } catch (NumberFormatException e) {
      System.out.println("Invalid input. Please enter a number.");
      return; // Return to main menu or you can prompt again
    }

    Task selectedTask = tasks.get(taskIndex - 1);
    Task.TaskUpdater updater = selectedTask.getUpdater();

    boolean continueUpdating = true;

    while (continueUpdating) {
      System.out.println("Choose an attribute to update:");
      System.out.println("1. Title");
      System.out.println("2. Description");
      System.out.println("3. Priority");
      System.out.println("0. Go back");

      int choice = 0;
      try {
        choice = Integer.parseInt(SCANNER.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a number.");
      }

      switch (choice) {
        case 1:
          System.out.println("Enter new title:");
          String newTitle = SCANNER.nextLine();
          updater.updateTitle(newTitle);
          break;
        case 2:
          System.out.println("Enter new description:");
          String newDescription = SCANNER.nextLine();
          updater.updateDescription(newDescription);
          break;
        case 3:
          System.out.println("Select new priority (1. LOW, 2. MEDIUM, 3. HIGH):");
          int priorityChoice = 0;
          try {
            priorityChoice = Integer.parseInt(SCANNER.nextLine());
          } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
          }
          switch (priorityChoice) {
            case 1:
              updater.updatePriority(Priority.LOW);
              break;
            case 2:
              updater.updatePriority(Priority.MEDIUM);
              break;
            case 3:
              updater.updatePriority(Priority.HIGH);
              break;
            default:
              System.out.println("Invalid priority choice.");
          }
          break;
        case 0:
          continueUpdating = false;
          break;
        default:
          System.out.println("Invalid choice. Please try again.");
      }
    }
  }

  private static void deleteTask() {
    taskManager.saveState();

    if (taskManager.getAllTasks().isEmpty()) {
      System.out.println("No tasks available to delete.");
      return;
    }

    System.out.println("Select a task to delete:");

    List<Task> tasks = taskManager.getAllTasks();
    for (int i = 0; i < tasks.size(); i++) {
      System.out.println((i + 1) + ". " + tasks.get(i));
    }

    int taskIndex = 0;
    try {
      taskIndex = Integer.parseInt(SCANNER.nextLine());
    } catch (NumberFormatException e) {
      System.out.println("Invalid input. Please enter a number.");
      return;
    }

    Task selectedTask = taskManager.getTaskByIndex(taskIndex - 1);
    if (taskManager.deleteTask(selectedTask)) {
      System.out.println("Task deleted successfully!");
    } else {
      System.out.println("Failed to delete the task.");
    }
  }

  private static void filterTasks() {
    System.out.println("Filter by: ");
    System.out.println("1. Priority");
    System.out.println("2. Date created");

    int choice = Integer.parseInt(getUserInput("Enter choice: "));
    List<Task> filteredTasks = new ArrayList<>();

    switch (choice) {
      case 1:
        System.out.println("Select Priority (LOW, MEDIUM, HIGH): ");
        Priority priority = Priority.valueOf(getUserInput("").toUpperCase());
        filteredTasks = taskManager.filterTasks(new PriorityFilter(priority));
        break;
      case 2:
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
          Date date = sdf.parse(getUserInput("Enter date (yyyy-MM-dd): "));
          filteredTasks = taskManager.filterTasks(new DateFilter(date));
        } catch (ParseException e) {
          System.out.println("Invalid date format. Please use yyyy-MM-dd format.");
          return; // Return to the main menu
        }
        break;
      default:
        System.out.println("Invalid choice! Please choose a valid option.");
        return;
    }

    // Display the filtered tasks (this is a simple print, you can enhance the
    // display as needed)
    for (Task task : filteredTasks) {
      System.out.println(task.getTitle() + " - " + task.getDescription());
    }
  }

  private static void displayTaskStatistics() {
    TaskStatistics statistics = new TaskStatistics(taskManager.getAllTasks());
    Map<Priority, Integer> countsByPriority = statistics.countTasksByPriority();

    System.out.println("Task counts by priority:");
    for (Map.Entry<Priority, Integer> entry : countsByPriority.entrySet()) {
      System.out.printf("%s: %d tasks\n", entry.getKey(), entry.getValue());
    }
  }

  private static List<Integer> getMultipleTaskIndices() {
    System.out.println("Enter task indices separated by commas (e.g., 0,2,3): ");
    String[] indexStrings = getUserInput("").split(",");
    List<Integer> indices = new ArrayList<>();
    for (String indexString : indexStrings) {
      indices.add(Integer.parseInt(indexString.trim()));
    }
    return indices;
  }

  private static void performBulkOperations() {
    System.out.println("1. Mark multiple tasks as complete");
    // Add more bulk operation options as needed in the future

    int choice = Integer.parseInt(getUserInput("Select an operation: "));
    switch (choice) {
      case 1:
        taskManager.saveState();
        List<Integer> indices = getMultipleTaskIndices();
        taskManager.new BulkOperations().markTasksAsComplete(indices);
        System.out.println("Selected tasks have been marked as complete.");
        break;

      // Handle other bulk operations in future cases
    }
  }

  private static void clearScreen() {
    try {
      if (System.getProperty("os.name").contains("Windows")) {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } else {
        new ProcessBuilder("bash", "-c", "clear").inheritIO().start().waitFor();
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static boolean shouldClearScreen() {
    String response = getUserInput("Would you like to clear the screen? (yes/no): ");
    return response.trim().equalsIgnoreCase("yes");
  }

  private static void displayMenu() {
    System.out.println("------ Task Manager ------");
    System.out.println("1. Create a new task");
    System.out.println("2. View all tasks");
    System.out.println("3. Update a task");
    System.out.println("4. Delete a task");
    System.out.println("5. Save tasks to file");
    System.out.println("6. Filter tasks");
    System.out.println("7. Display task statistics");
    System.out.println("8. Perform bulk operations on tasks");
    System.out.println("9. Undo last action");
    // ... you can add other options here, like deleting a task, marking as
    // completed, etc. ...
    System.out.println("0. Exit");
  }

  public static void run() {
    boolean continueRunning = true;
    while (continueRunning) {
      if (TaskManagerSystem.shouldClearScreen())
        TaskManagerSystem.clearScreen();
      TaskManagerSystem.displayMenu();

      int choice;
      try {
        choice = Integer.parseInt(TaskManagerSystem.getUserInput("Please choose an option: "));
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a number.");
        continue; // Move to the next iteration of the loop to prompt the user again
      }

      switch (choice) {
        case 1:
          TaskManagerSystem.createTask();
          break;
        case 2:
          TaskManagerSystem.viewAllTasks();
          break;
        case 3:
          TaskManagerSystem.updateTask();
          break;
        case 4:
          TaskManagerSystem.deleteTask();
          break;
        case 5:
          taskManager.saveTasksToFile();
          break;
        case 6:
          TaskManagerSystem.filterTasks();
          break;
        case 7:
          TaskManagerSystem.displayTaskStatistics();
          break;
        case 8:
          TaskManagerSystem.performBulkOperations();
          break;
        case 9:
          taskManager.undoLastAction();
          System.out.println("Last action undone.");
          break;
        // ... other cases ...
        case 0:
          continueRunning = false;
          break;
        default:
          System.out.println("Invalid choice. Please try again.");
      }
    }
  }
}
