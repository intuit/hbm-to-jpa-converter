package com.intuit.hbm.converters.task;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * An interface for executing tasks.
 */
public interface TaskExecutor {

    /**
     * Executes a list of tasks.
     *
     * @param tasks A list of tasks represented as Callable<Void> to be executed.
     */
    void executeTasks(List<Callable<Boolean>> tasks) throws InterruptedException;
}
