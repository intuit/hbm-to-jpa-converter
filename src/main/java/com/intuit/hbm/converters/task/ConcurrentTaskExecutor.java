package com.intuit.hbm.converters.task;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Implements TaskExecutor to provide functionality for concurrently executing tasks.
 */
@Slf4j
public class ConcurrentTaskExecutor implements TaskExecutor {
    /**
     * Executes a list of tasks concurrently.
     *
     * @param tasks A list of Callable<Void> tasks to be executed.
     */
    @Override
    public void executeTasks(List<Callable<Boolean>> tasks) throws InterruptedException {
        log.info("Event=HbmToJpaConversion SubEvent=ExecuteTasks InputFilesCount={}", tasks.size());
        List<Future<Boolean>> futureList = new ArrayList<Future<Boolean>>();
        // get the number of available processors
        int processors = Runtime.getRuntime().availableProcessors();
        // set the thread count as twice the number of available processors
        int threadCount = processors * 2;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        try {
            // submit all tasks to the executor service and store futures in the list
            futureList = executorService.invokeAll(tasks);
            waitForCompletion(futureList);
        } finally {
            // At this point, we can guarantee all tasks have finished their execution
            executorService.shutdownNow();
        }
    }

    /**
     * This method waits for all tasks to complete their execution. It iterates through a list of Future objects,
     * each representing a result of an asynchronous computation or task. It blocks the current thread if the computation is not yet complete
     * and gets the completion status of each task.
     * <p>
     * For each task, it increments 'successCount' if the task completed successfully or 'failedCount' if the task failed.
     *
     * @param futureList A list of Future<Void> objects representing each task submitted to the executor service.
     *                   <p>
     *                   It also logs count of how many tasks completed successfully and how many tasks failed.
     */
    private void waitForCompletion(List<Future<Boolean>> futureList) {
        int successCount = 0;
        int failedCount = 0;
        // Wait for all tasks to complete
        try {
            for (Future<Boolean> future : futureList) {
                try {
                    Boolean status = future.get();
                    if (status) {
                        successCount++;
                    } else {
                        failedCount++;
                    }
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Event=HbmToJpaConversion SubEvent=ConcurrentTaskExecutor", e);
                    failedCount++;
                }
            }
        } finally {
            log.info("Event=HbmToJpaConversion SubEvent=ConcurrentTaskExecutor SuccessCount={} FailedCount={}", successCount, failedCount);
        }
    }
}
