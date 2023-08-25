package task_manager.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import task_manager.models.Task;

public class TaskStatistics {
  private List<Task> tasks;

  public TaskStatistics(List<Task> tasks) {
    this.tasks = tasks;
  }

  public Map<Priority, Integer> countTasksByPriority() {
    Map<Priority, Integer> countByPriority = new HashMap<>();

    for (Task task : tasks) {
      countByPriority.put(task.getPriority(),
          countByPriority.getOrDefault(task.getPriority(), 0) + 1);
    }

    return countByPriority;
  }

  // You can add more statistical methods in the future - ifneeded.
}
