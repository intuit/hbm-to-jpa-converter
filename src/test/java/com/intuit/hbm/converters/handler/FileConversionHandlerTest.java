package com.intuit.hbm.converters.handler;

import com.intuit.hbm.converters.handler.FileConversionHandler;
import com.intuit.hbm.converters.helper.FileHelper;
import com.intuit.hbm.converters.model.ConverterType;
import com.intuit.hbm.converters.model.InputModel;
import com.intuit.hbm.converters.task.TaskBuilder;
import com.intuit.hbm.converters.task.TaskExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import static org.mockito.Mockito.*;

public class FileConversionHandlerTest {
    private FileHelper fileHelper;
    private TaskBuilder taskBuilder;
    private InputModel inputModel;
    private TaskExecutor taskExecutor;

    private FileConversionHandler fileConversionHandler;

    @Before
    public void setUp() {
        inputModel = Mockito.mock(InputModel.class);
        fileHelper = Mockito.mock(FileHelper.class);
        taskBuilder = Mockito.mock(TaskBuilder.class);
        taskExecutor = Mockito.mock(TaskExecutor.class);
        // Initialize your classes under test
        fileConversionHandler = new FileConversionHandler(inputModel, fileHelper, taskBuilder, taskExecutor);
    }

    @Test
    public void processTask() throws InterruptedException {
        // Given
        File[] files = new File[1];
        files[0] = mock(File.class);

        Callable<Boolean> task = () -> true;
        List<Callable<Boolean>> tasks = Collections.singletonList(task);

        when(inputModel.getSrcDirectoryPath()).thenReturn("srcDirPath");
        when(inputModel.getFileEndsWith()).thenReturn("endsWith");
        when(inputModel.getDestDirPath()).thenReturn("destDirPath");
        when(inputModel.getConverterType()).thenReturn(ConverterType.HBMToORM);

        when(fileHelper.getFiles("srcDirPath", "endsWith")).thenReturn(files);
        when(taskBuilder.buildTasks(any(File[].class), anyString(), any(ConverterType.class))).thenReturn(tasks);

        // When
        fileConversionHandler.processTask();

        // Then
        verify(fileHelper).getFiles(anyString(), anyString());
    }

}
