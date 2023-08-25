package task_manager.services;

import java.util.ArrayList;
import java.util.List;

import task_manager.models.Task;

public class State {
  private List<Task> tasksSnapshot;

  public State(List<Task> tasks) {
    this.tasksSnapshot = new ArrayList<>();
    for (Task task : tasks) {
      // Create a deep copy of each task
      this.tasksSnapshot.add(new Task(task));
    }
  }

  public List<Task> getTasksSnapshot() {
    return tasksSnapshot;
  }
}
