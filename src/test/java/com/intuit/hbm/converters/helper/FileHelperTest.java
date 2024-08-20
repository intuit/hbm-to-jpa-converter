package com.intuit.hbm.converters.helper;

import com.intuit.hbm.converters.exception.FileConversionException;
import com.intuit.hbm.converters.helper.FileHelper;
import org.antlr.stringtemplate.StringTemplate;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FileHelperTest {
    private FileHelper fileHelper;
    @Mock
    StringTemplate template;
    @Mock
    File mockFile;

    @Before
    public void setUp() {
        fileHelper = FileHelper.getInstance();
    }

    @Test
    public void testReadInputFile() throws DocumentException, FileNotFoundException {
        File file = new File("Paycheck.query.hbm.xml");

        Document result = fileHelper.readInputFile(file);
        assertNotNull(result);
        assertEquals(result.getName(),"Paycheck.query.hbm.xml");
        assertNotNull(result.getRootElement());
    }

    @Test
    public void testReadInputFile_Exception() {
        File file = new File("file1.xml");
        Document result = null;
        try {
            result = fileHelper.readInputFile(file);
            Assert.fail("Expected an RuntimeException to be thrown");
       } catch (FileConversionException anEx) {
            Assert.assertThat(anEx.getMessage(), containsString("ReadInputFile Status=Failed"));
        }

        assertNull(result);

    }

    @Test(expected = FileConversionException.class)
    public void validateInputFile_FileDoesNotExist_ThrowsException() {
        // arrange
        when(mockFile.exists()).thenReturn(false);

        fileHelper.validateInputFile(mockFile);
    }

    @Test(expected = FileConversionException.class)
    public void validateInputFile_FileIsNotAFile_ThrowsException() {
        // arrange
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isFile()).thenReturn(false);

        // act
        fileHelper.validateInputFile(mockFile);
    }

    @Test(expected = FileConversionException.class)
    public void validateInputFile_FileCannotBeRead_ThrowsException() {
        // arrange
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isFile()).thenReturn(true);
        when(mockFile.canRead()).thenReturn(false);

        // act
        fileHelper.validateInputFile(mockFile);
    }

    @Test(expected = FileConversionException.class)
    public void validateInputFile_FileCannotBeWritten_ThrowsException() {
        // arrange
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isFile()).thenReturn(true);
        when(mockFile.canRead()).thenReturn(true);
        when(mockFile.canWrite()).thenReturn(false);

        // act
        fileHelper.validateInputFile(mockFile);
    }

    @Test
    public void validateInputFile_FileIsValid_NoExceptionThrown() {
        // arrange
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isFile()).thenReturn(true);
        when(mockFile.canRead()).thenReturn(true);
        when(mockFile.canWrite()).thenReturn(true);


        fileHelper.validateInputFile(mockFile);

        // verify
        verify(mockFile, times(1)).exists();
        verify(mockFile, times(1)).isFile();
        verify(mockFile, times(1)).canRead();
        verify(mockFile, times(1)).canWrite();
    }

    @Test
    public void testWriteFile_DesDirectory() {
        String currentDirectory = System.getProperty("user.dir");
        String destDirectoryPath = currentDirectory + "/src/test/java/com/intuit/hbm/converters/";
        String fileName = "Paycheck";

        // Mock the template's toString method
        when(template.toString()).thenReturn("String to write to file");

        // execute the method under test
        fileHelper.writeFile(template, destDirectoryPath, fileName);
        File file = new File(destDirectoryPath + fileName + ".xml");
        assertTrue(file.exists());
    }

    @Test
    public void testWriteFile_DesFile() {
        String destDirectoryPath = "Paycheck.xml";
        String fileName = "Paycheck";

        // Mock the template's toString method
        when(template.toString()).thenReturn("String to write to file");

        // execute the method under test
        fileHelper.writeFile(template, destDirectoryPath, fileName);
        File file = new File(destDirectoryPath);
        assertTrue(file.exists());
    }

    @Test
    public void testGetFiles() throws IOException {
        String directoryPath = "directory";
        String fileName = "test.txt";
        String filePath = directoryPath + File.separator + fileName;

        // Create a temporary directory and a file inside it
        Path tempDirectoryPath = Files.createTempDirectory(directoryPath);
        Path tempFilePath = Paths.get(tempDirectoryPath.toString(), fileName);
        Files.createFile(tempFilePath);

        // Test case: directory exists and file with matching pattern exists
        File[] files = fileHelper.getFiles(tempDirectoryPath.toString(), ".txt");
        Assert.assertNotNull(files);
        Assert.assertEquals(1, files.length);
        Assert.assertEquals(tempFilePath.toString(), files[0].getPath());

        new File(tempFilePath.toString()).delete();

        // Test case: directory is actually a file matching the pattern
        Files.createFile(tempFilePath);
        files = fileHelper.getFiles(tempFilePath.toString(), ".txt");
        Assert.assertNotNull(files);
        Assert.assertEquals(1, files.length);
        Assert.assertEquals(tempFilePath.toString(), files[0].getPath());

        new File(tempFilePath.toString()).delete();

        // Test case: no file matches the pattern in directory
        files = fileHelper.getFiles(tempDirectoryPath.toString(), ".txt");
        Assert.assertNotNull(files);
        Assert.assertEquals(0, files.length);

        new File(tempDirectoryPath.toString()).delete();
    }

    @Test
    public void testGetFilesWithEmptyAndNonexistentDirectory() throws IOException {
        String nonExistentDirectory = "nonExistentDirectory";

        // Test case: directory does not exist
        try {
            fileHelper.getFiles(nonExistentDirectory, ".txt");
            Assert.fail("Expected an RuntimeException to be thrown");
        } catch (RuntimeException anEx) {
            Assert.assertThat(anEx.getMessage(), containsString("destination file and file ends ext should match"));
        }

        // Create a temporary directory and an unmatched file inside it
        String directoryPath = "directory";
        String unmatchedFileName = "test.abc";
        Path tempDirectoryPath = Files.createTempDirectory(directoryPath);
        Path tempFilePath = Paths.get(tempDirectoryPath.toString(), unmatchedFileName);
        Files.createFile(tempFilePath);

        // Test case: no file matches the pattern in directory
        File[] files = fileHelper.getFiles(tempDirectoryPath.toString(), ".txt");
        Assert.assertNotNull(files);
        Assert.assertEquals(0, files.length);

        new File(tempFilePath.toString()).delete();
        new File(tempDirectoryPath.toString()).delete();
    }

    @After
    public void tearDown() {
        fileHelper = null;
    }
}
