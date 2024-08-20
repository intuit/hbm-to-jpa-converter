package com.intuit.hbm.converters.task;

import com.intuit.hbm.converters.converter.ConverterFactory;
import com.intuit.hbm.converters.model.ConverterType;
import com.intuit.hbm.converters.service.ConverterService;
import com.intuit.hbm.converters.task.ConversionTaskBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ConversionTaskBuilderTest {
    @Test
    public void testBuildTasks() throws Exception {
        ConversionTaskBuilder conversionTaskBuilder = new ConversionTaskBuilder();
        // Create a sample file
        File file = new File("Paycheck.query.hbm.xml");

        // Build the tasks
        List<Callable<Boolean>> tasks = conversionTaskBuilder.buildTasks(new File[]{file}, "dest", ConverterType.HBMToORM);

        Assert.assertEquals(1, tasks.size());

        // Execute one of the tasks and test the result
        Callable<Boolean> task = tasks.get(0);
        Boolean result = task.call();
        Assert.assertTrue(result);
    }
}
