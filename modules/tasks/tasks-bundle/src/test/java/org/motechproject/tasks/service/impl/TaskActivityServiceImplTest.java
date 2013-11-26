package org.motechproject.tasks.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.tasks.domain.Task;
import org.motechproject.tasks.domain.TaskActivity;
import org.motechproject.tasks.domain.TaskActivityType;
import org.motechproject.tasks.ex.TaskHandlerException;
import org.motechproject.tasks.repository.AllTaskActivities;
import org.motechproject.tasks.service.TaskActivityService;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.tasks.domain.TaskActivityType.ERROR;
import static org.motechproject.tasks.domain.TaskActivityType.SUCCESS;
import static org.motechproject.tasks.domain.TaskActivityType.WARNING;
import static org.motechproject.tasks.events.constants.TaskFailureCause.TRIGGER;

public class TaskActivityServiceImplTest {
    private static final String TASK_ID = "12345";
    private static final String[] ERROR_FIELD = new String[]{"phone"};

    private List<TaskActivity> activities;

    @Mock
    AllTaskActivities allTaskActivities;

    TaskActivityService activityService;

    Task task;

    @Before
    public void setup() throws Exception {
        initMocks(this);

        activityService = new TaskActivityServiceImpl(allTaskActivities);
        activities = createTaskActivities();

        task = new Task();
        task.setId(TASK_ID);
    }

    @Test
    public void shouldReturnTaskActivitiesForTaskFromLastErrorActivity() {
        when(allTaskActivities.byTaskId(TASK_ID)).thenReturn(activities);

        List<TaskActivity> errors = activityService.errorsFromLastRun(task);

        assertNotNull(errors);

        for (TaskActivity error : errors) {
            assertActivity(ERROR.getValue(), ERROR_FIELD, TASK_ID, ERROR, null, error);
        }
    }

    @Test
    public void shouldReturnEmptyListWhenTaskHasNotActivities() {
        when(allTaskActivities.byTaskId(TASK_ID)).thenReturn(new ArrayList<TaskActivity>());

        List<TaskActivity> errors = activityService.errorsFromLastRun(task);

        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void shouldAddErrorActivityWithMessage() {
        String messageKey = "error.notFoundTrigger";

        ArgumentCaptor<TaskActivity> captor = ArgumentCaptor.forClass(TaskActivity.class);

        activityService.addError(task, messageKey);

        verify(allTaskActivities).add(captor.capture());

        TaskActivity actual = captor.getValue();
        assertActivity(messageKey, new String[0], TASK_ID, TaskActivityType.ERROR, actual.getStackTraceElement(), actual);
    }

    @Test
    public void shouldAddErrorActivityWithTaskException() {
        String messageKey = "error.notFoundTrigger";
        TaskHandlerException exception = new TaskHandlerException(TRIGGER, messageKey, ERROR_FIELD);

        ArgumentCaptor<TaskActivity> captor = ArgumentCaptor.forClass(TaskActivity.class);

        activityService.addError(task, exception);

        verify(allTaskActivities).add(captor.capture());

        assertActivity(messageKey, ERROR_FIELD, TASK_ID, TaskActivityType.ERROR, getStackTrace(exception), captor.getValue());
    }


    @Test
    public void shouldAddTaskSuccessActivity() {
        String messageKey = "task.success.ok";

        ArgumentCaptor<TaskActivity> captor = ArgumentCaptor.forClass(TaskActivity.class);

        activityService.addSuccess(task);

        verify(allTaskActivities).add(captor.capture());

        assertActivity(messageKey, new String[0], TASK_ID, TaskActivityType.SUCCESS, null, captor.getValue());
    }

    @Test
    public void shouldAddTaskWarningActivity() {
        String messageKey = "task.warning.taskDisabled";

        ArgumentCaptor<TaskActivity> captor = ArgumentCaptor.forClass(TaskActivity.class);

        activityService.addWarning(task);

        verify(allTaskActivities).add(captor.capture());

        assertActivity(messageKey, new String[0], TASK_ID, TaskActivityType.WARNING, null, captor.getValue());
    }

    @Test
    public void shouldAddTaskWarningActivityWithGivenKeyAndField() {
        String messageKey = "warning.manipulation";

        ArgumentCaptor<TaskActivity> captor = ArgumentCaptor.forClass(TaskActivity.class);

        activityService.addWarning(task, messageKey, ERROR_FIELD[0]);

        verify(allTaskActivities).add(captor.capture());

        assertActivity(messageKey, ERROR_FIELD, TASK_ID, TaskActivityType.WARNING, null, captor.getValue());
    }

    @Test
    public void shouldAddTaskWarningActivityWithGivenException() {
        TaskHandlerException exception = new TaskHandlerException(TRIGGER, "trigger.exception", new TaskHandlerException(TRIGGER, "task.exception"));
        String messageKey = "warning.manipulation";

        ArgumentCaptor<TaskActivity> captor = ArgumentCaptor.forClass(TaskActivity.class);

        activityService.addWarning(task, messageKey, ERROR_FIELD[0], exception);

        verify(allTaskActivities).add(captor.capture());

        assertActivity(messageKey, ERROR_FIELD, TASK_ID, TaskActivityType.WARNING, getStackTrace(exception.getCause()), captor.getValue());
    }

    @Test
    public void shouldDeleteAllTaskActivitiesForGivenTask() {
        when(allTaskActivities.byTaskId(TASK_ID)).thenReturn(activities);

        activityService.deleteActivitiesForTask(TASK_ID);

        verify(allTaskActivities, times(activities.size())).remove(any(TaskActivity.class));
    }

    @Test
    public void shouldNotRemoveAnyActivitiesWhenTaskHasNotActivities() {
        when(allTaskActivities.byTaskId(TASK_ID)).thenReturn(new ArrayList<TaskActivity>());

        activityService.deleteActivitiesForTask(TASK_ID);

        verify(allTaskActivities, never()).remove(any(TaskActivity.class));
    }

    @Test
    public void shouldReturnAllActivities() {
        when(allTaskActivities.getAll()).thenReturn(activities);

        List<TaskActivity> actual = activityService.getAllActivities();

        assertNotNull(actual);
        assertEquals(activities, actual);
    }

    @Test
    public void shouldReturnAllActivitiesForGivenTask() {
        when(allTaskActivities.byTaskId(TASK_ID)).thenReturn(activities);

        List<TaskActivity> actual = activityService.getTaskActivities(TASK_ID);

        assertNotNull(actual);
        assertEquals(activities, actual);
    }

    private void assertActivity(String messageKey, String[] field, String task, TaskActivityType activityType,
                                String stackTraceElement, TaskActivity activity) {
        assertNotNull(activity);

        assertEquals(messageKey, activity.getMessage());
        assertEquals(task, activity.getTask());
        assertEquals(activityType, activity.getActivityType());
        assertEquals(stackTraceElement, activity.getStackTraceElement());

        assertArrayEquals(field, activity.getFields());
    }

    private List<TaskActivity> createTaskActivities() {
        List<TaskActivity> messages = new ArrayList<>();
        messages.add(createError());
        messages.add(createError());
        messages.add(createSuccess());
        messages.add(createError());
        messages.add(createError());
        messages.add(createWarning());
        messages.add(createSuccess());
        messages.add(createError());
        messages.add(createError());
        messages.add(createError());
        messages.add(createError());

        return messages;
    }

    private TaskActivity createError() {
        return new TaskActivity(ERROR.getValue(), ERROR_FIELD, TASK_ID, ERROR);
    }

    private TaskActivity createSuccess() {
        return new TaskActivity(SUCCESS.getValue(), TASK_ID, SUCCESS);
    }

    private TaskActivity createWarning() {
        return new TaskActivity(WARNING.getValue(), TASK_ID, WARNING);
    }
}
