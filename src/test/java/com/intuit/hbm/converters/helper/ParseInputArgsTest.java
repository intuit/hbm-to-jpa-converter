package com.intuit.hbm.converters.helper;

import com.intuit.hbm.converters.helper.ArgsParser;
import com.intuit.hbm.converters.model.InputModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ParseInputArgsTest {
    // Assuming the helper class name is 'InputHelper' where the methods are defined
    private ArgsParser argsParser;

    @Before
    public void setup() {
        argsParser = new ArgsParser();
    }

    @Test
    public void testParseInputArgs() throws IOException {

        // Create two temporary files as source and destination
        File src = File.createTempFile("tempSrcFile", ".txt");
        File dest = File.createTempFile("tempDestFile", ".txt");

        String[] args = {src.getPath(), "txt", dest.getPath()};

        // Test case: valid input arguments
        InputModel inputModel = argsParser.parseInputArgs(args);
        Assert.assertNotNull(inputModel);
        Assert.assertEquals(src.getPath(), inputModel.getSrcDirectoryPath());
        Assert.assertEquals("txt", inputModel.getFileEndsWith());
        Assert.assertEquals(dest.getPath(), inputModel.getDestDirPath());

        logFilePathAndDeleteFile(src, dest);

        // Test case: arguments are invalid
        try {
            args = new String[]{"invalid"}; // less than 3 args
            argsParser.parseInputArgs(args);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Source path, extension and Destination path arguments are required.", e.getMessage());
        }
    }

    private void logFilePathAndDeleteFile(File... files) {
        // Display temp files paths and delete them
        for (File file : files) {
            System.out.println(file.getPath());
            file.deleteOnExit();
        }
    }
}
