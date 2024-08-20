package com.intuit.hbm.converters.handler;

import com.intuit.hbm.converters.exception.FileConversionException;
import com.intuit.hbm.converters.helper.FileHelper;
import com.intuit.hbm.converters.model.InputModel;
import com.intuit.hbm.converters.task.TaskBuilder;
import com.intuit.hbm.converters.task.TaskExecutor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * The FileConversionHandler class is used for managing the conversion of files. It retrieves files from the source directory,
 * builds tasks for conversion, and executes these tasks.
 */
@Slf4j
public class FileConversionHandler {
    private FileHelper fileHelper;
    private TaskBuilder taskBuilder;
    private TaskExecutor taskExecutor;
    private InputModel inputModel;

    /**
     * Constructor for FileConversionHandler.
     * Initializes the FileConversionHandler with the provided parameters.
     *
     * @param inputModel   A InputModel object that contains the input arguments.
     * @param fileHelper   A FileHelper instance to assist the file operations.
     * @param taskBuilder  A TaskBuilder instance to build the tasks for conversion.
     * @param taskExecutor A TaskExecutor instance to execute the tasks.
     */
    public FileConversionHandler(InputModel inputModel, FileHelper fileHelper, TaskBuilder taskBuilder, TaskExecutor taskExecutor) {
        this.fileHelper = fileHelper;
        this.taskBuilder = taskBuilder;
        this.taskExecutor = taskExecutor;
        this.inputModel = inputModel;
    }

    /**
     * This method initiates the conversion of files.
     * First, it retrieves the files from the source directory.
     * It then builds conversion tasks using the taskBuilder and if there are any tasks, executes them using the taskExecutor.
     * Any exceptions encountered during these steps are caught and wrapped into a FileConversionException.
     *
     * @throws FileConversionException If unable to submit conversion tasks.
     */
    public void processTask() {
        try {
            File[] hbmFiles = fileHelper.getFiles(inputModel.getSrcDirectoryPath(), inputModel.getFileEndsWith());
            if (hbmFiles != null && hbmFiles.length > 0) {
                List<Callable<Boolean>> tasks = taskBuilder.buildTasks(hbmFiles, inputModel.getDestDirPath(), inputModel.getConverterType());
                taskExecutor.executeTasks(tasks);
            }
        } catch (Exception e) {
            throw new FileConversionException("Unable to submit conversion tasks", e);
        }
    }
}
