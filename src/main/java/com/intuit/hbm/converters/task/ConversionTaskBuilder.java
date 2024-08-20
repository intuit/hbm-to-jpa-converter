package com.intuit.hbm.converters.task;

import com.intuit.hbm.converters.converter.ConverterFactory;
import com.intuit.hbm.converters.model.ConverterType;
import com.intuit.hbm.converters.service.ConverterService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Implements TaskBuilder to provide functionality for building conversion tasks.
 */
@Slf4j
public class ConversionTaskBuilder implements TaskBuilder {

    /**
     * Builds a list of tasks where each task is a conversion operation on a file.
     *
     * @param files             An array of files to convert.
     * @param destDirectoryPath The path of the destination directory to store the converted files.
     * @return A list of Callable<Void> tasks. Each task performs the conversion operation on a file.
     */
    @Override
    public List<Callable<Boolean>> buildTasks(File[] files, String destDirectoryPath, ConverterType converterType) {
        List<Callable<Boolean>> tasks = new ArrayList<>();
        ConverterService converterService = ConverterFactory.getConverter(converterType);
        for (File file : files) {
            tasks.add(buildTask(file, destDirectoryPath, converterService));
        }
        return tasks;
    }

    /**
     * Builds a single task that performs a conversion operation on a file.
     *
     * @param file              A file to convert.
     * @param destDirectoryPath The path of the destination directory to store the converted file.
     * @param converterService  {@link ConverterService}
     * @return A Callable<Void> task that performs the conversion operation on the file.
     */
    @Override
    public Callable<Boolean> buildTask(File file, String destDirectoryPath, ConverterService converterService) {
        String fileName = file.getName().split("\\.")[0];
        Callable<Boolean> task = () -> {
            return converterService.performConversion(file, fileName, destDirectoryPath);
        };
        return task;
    }
}
