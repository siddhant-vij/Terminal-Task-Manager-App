package task_manager.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import task_manager.models.Task;

interface TaskFilter {
  boolean matches(Task task);
}

class PriorityFilter implements TaskFilter {
  private Priority priority;

  public PriorityFilter(Priority priority) {
    this.priority = priority;
  }

  @Override
  public boolean matches(Task task) {
    return task.getPriority() == priority;
  }
}

class DateFilter implements TaskFilter {
  private Date date;

  public DateFilter(Date date) {
    this.date = date;
  }

  @Override
  public boolean matches(Task task) {
    // Here we're just checking if the task was created on the given date.
    // You can modify this to be more granular if needed.
    Date inputDate = task.getCreatedDate();
    LocalDate myLocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate inputLocalDate = inputDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    return myLocalDate.equals(inputLocalDate);
  }
}
