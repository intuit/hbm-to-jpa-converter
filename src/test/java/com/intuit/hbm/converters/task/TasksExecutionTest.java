package com.intuit.hbm.converters.task;

import com.intuit.hbm.converters.task.ConcurrentTaskExecutor;
import com.intuit.hbm.converters.task.TaskExecutor;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class TasksExecutionTest {
    @Test
    public void testExecuteTasks() {
        List<Callable<Boolean>> tasks = Arrays.asList(
                () -> {
                    Thread.sleep(1000);
                    return true;
                },
                () -> {
                    Thread.sleep(1000);
                    return true;
                },
                () -> {
                    Thread.sleep(1000);
                    return false;
                });

        // Assuming the class name that have those methods is TasksExecution
        TaskExecutor tasksExecution = new ConcurrentTaskExecutor();

        // Replace the executorService in tasksExecution class by reflection or by using setter method if available.

        try {
            tasksExecution.executeTasks(tasks);
        } catch (InterruptedException e) {
            Assert.fail("The test was interrupted");
        }

    }
}
