package org.motechproject.tasks.service;

import org.motechproject.tasks.domain.ActionEvent;
import org.motechproject.tasks.domain.Task;
import org.motechproject.tasks.domain.TaskActionInformation;
import org.motechproject.tasks.domain.TriggerEvent;
import org.motechproject.tasks.ex.ActionNotFoundException;
import org.motechproject.tasks.ex.TriggerNotFoundException;

import java.io.IOException;
import java.util.List;

public interface TaskService {

    void save(final Task task);

    /**
     * @deprecated As of release 0.20, replaced by {@link #getActionEventFor(tasks.domain.TaskActionInformation)}
     */
    @Deprecated
    ActionEvent getActionEventFor(Task task) throws ActionNotFoundException;

    ActionEvent getActionEventFor(TaskActionInformation taskActionInformation) throws ActionNotFoundException;

    List<Task> getAllTasks();

    List<Task> findTasksForTrigger(final TriggerEvent trigger);

    TriggerEvent findTrigger(String subject) throws TriggerNotFoundException;

    Task getTask(String taskId);

    void deleteTask(String taskId);

    String exportTask(String taskId);

    void importTask(String json) throws IOException;
}
