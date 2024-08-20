package com.intuit.hbm.converters;

import com.intuit.hbm.converters.helper.ArgsParser;
import com.intuit.hbm.converters.helper.FileHelper;
import com.intuit.hbm.converters.model.InputModel;
import com.intuit.hbm.converters.handler.FileConversionHandler;
import com.intuit.hbm.converters.task.ConcurrentTaskExecutor;
import com.intuit.hbm.converters.task.ConversionTaskBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

/**
 * HbmToJpaTool is a tool used for converting Hibernate Mapping files (HBM files) to Java Persistence API (JPA) standard ORM files.
 */
@Slf4j
public class HbmToJpaTool {

    /**
     * The main method that is first invoked at runtime.
     * Creates an instance of HbmToJpaTool and starts the HBM to JPA conversion process with the input arguments.
     *
     * @param args Command line arguments
     */
    public static void main(String args[]) {
        new HbmToJpaTool().process(args);
    }

    /**
     * Begins the process of converting Hibernate Mapping files (HBM) to Java Persistence API (JPA).
     * It first logs the beginning of the conversion, then attempts to parse the input arguments.
     * If the input arguments are not empty, it creates a FileConversionHandler instance to handle the processing of the task.
     * Logs any exceptions that occur during the process.
     * Finally, logs the completion of the process, along with the total time taken for the process to complete in milliseconds.
     *
     * @param args The input parameters for HBM to JPA conversion, received from the main method.
     */
    private void process(String[] args) {
        long start = System.currentTimeMillis();
        log.info("Event=HbmToJpaConversion Status=Started");
        try {
            InputModel inputModel = new ArgsParser().parseInputArgs(args);
            if (ObjectUtils.isNotEmpty(inputModel)) {
                //create handler instance and process task
                FileConversionHandler fileConversionHandler = new FileConversionHandler(inputModel, FileHelper.getInstance(), new ConversionTaskBuilder(), new ConcurrentTaskExecutor());
                fileConversionHandler.processTask();
            }
        } catch (Exception e) {
            log.error("Event=HbmToJpaConversion Status=Failed", e);
        } finally {
            long end = System.currentTimeMillis();
            log.info("Event=HbmToJpaConversion Status=Done totalTimeTaken={}ms", end - start);
        }
    }
}
