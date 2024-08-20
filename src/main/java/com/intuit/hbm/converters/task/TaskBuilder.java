package com.intuit.hbm.converters.task;

import com.intuit.hbm.converters.model.ConverterType;
import com.intuit.hbm.converters.service.ConverterService;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * An interface for building tasks used by the TaskExecutor.
 */
public interface TaskBuilder {

    /**
     * Builds a list of tasks based on the given array of files, IConverter, and destination directory path.
     *
     * @param files             An array of files to convert.
     * @param destDirectoryPath The path of the destination directory to store the converted files.
     * @param converterType     {@link ConverterType}
     * @return A list of Callable<Void> tasks.
     */
    List<Callable<Boolean>> buildTasks(File[] files, String destDirectoryPath, ConverterType converterType);

    /**
     * Builds a single task based on the given file, IConverter, and destination directory path.
     *
     * @param file              A file to convert.
     * @param destDirectoryPath The path of the destination directory to store the converted file.
     * @return A Callable<Void> task.
     */
    Callable<Boolean> buildTask(File file, String destDirectoryPath, ConverterService service);

}
