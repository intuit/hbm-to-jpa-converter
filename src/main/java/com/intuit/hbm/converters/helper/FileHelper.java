package com.intuit.hbm.converters.helper;

import com.intuit.hbm.converters.common.Constants;
import com.intuit.hbm.converters.exception.FileConversionException;
import lombok.extern.slf4j.Slf4j;
import org.antlr.stringtemplate.StringTemplate;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.URL;

/**
 * This class provides helper methods to handle file related operations.
 * It follows a singleton pattern and provides methods for getting an instance of the class,
 * fetching files from a directory, reading an input file, validating an input file, writing to a file,
 * and getting the location of templates.
 */
@Slf4j
public class FileHelper {

    private static FileHelper fileHelper = new FileHelper();

    // Private constructor to enforce singleton pattern
    private FileHelper() {

    }

    /**
     * This method returns a singleton instance of FileHelper.
     *
     * @return The singleton instance of the FileHelper.
     */
    public static FileHelper getInstance() {
        return fileHelper;
    }

    /**
     * Returns an array of files from the given directory that end with a specific string.
     *
     * @param directoryPath the path to the directory to read
     * @param fileEndsWith  string that files must end with to be included in the returned array
     * @return an array of File objects; all files from directoryPath that end with fileEndsWith
     */
    public File[] getFiles(String directoryPath, String fileEndsWith) {
        File directory = new File(directoryPath);
        // If it's a directory, get all files that match the pattern
        if (directory.isDirectory()) {
            return directory.listFiles(file -> file.getName().endsWith(fileEndsWith));
        } else {
            // If it's a file, check if it matches the pattern
            if (directory.getName().toLowerCase().endsWith(fileEndsWith.toLowerCase())) {
                return new File[]{directory};
            } else {
                throw new FileConversionException("destination file and file ends ext should match");
            }
        }
    }

    /**
     * This method reads an input file and returns a Document object.
     *
     * @param hbmFile The input file to be read.
     * @return A Document representing the content of the input file.
     */
    public Document readInputFile(File hbmFile) {
        Document document = null;
        try {
            log.info("Event=HbmToJpaConversion SubEvent=ReadInputFile Status=Started SourceFileName={}", hbmFile.getName());
            InputStream inputStream = new FileInputStream(hbmFile);
            SAXReader reader = new SAXReader();
            document = reader.read(inputStream);
            document.setName(hbmFile.getName());
            log.info("Event=HbmToJpaConversion SubEvent=ReadInputFile Status=Done SourceFileName={}", hbmFile.getName());
            return document;
        } catch (DocumentException | FileNotFoundException e) {
            log.error("Event=HbmToJpaConversion SubEvent=ReadInputFile Status=Failed", e);
            throw new FileConversionException("ReadInputFile Status=Failed", e);
        }
    }

    /**
     * This method validates the input file before operating on it.
     *
     * @param file The input file to be validated.
     * @throws RuntimeException if the file is invalid.
     */
    public void validateInputFile(File file) {
        log.info("Event=HbmToJpaConversion SubEvent=ValidateInputFile Status=Started");
        if (!file.exists() || !file.isFile() || !file.canRead() || !file.canWrite()) {
            log.error("Event=HbmToJpaConversion SubEvent=ValidateInputFile Status=Failed Reason=IncorrectFilePath/InvalidPermissions");
            throw new FileConversionException("Ensure File Existence and Permissions");
        }
        log.info("Event=HbmToJpaConversion SubEvent=ValidateInputFile Status=Done SourceFileName={}", file.getName());
    }

    /**
     * This method writes the processed content into the orm file.
     *
     * @param t                 The StringTemplate object containing the processed content.
     * @param destDirectoryPath The destination directory path where the file has to be written.
     * @param fileName          The filename including which the content has to be written.
     * @throws FileConversionException if an error occurs.
     */
    public void writeFile(StringTemplate t, String destDirectoryPath, String fileName) throws FileConversionException {
        log.info("Event=HbmToJpaConversion SubEvent=WriteOutputFile Status=Started DestinationFileName={}", fileName + Constants.FILE_ENDS_WITH_ORM_EXT);
        try {
            File hbmFolder = new File(destDirectoryPath);
            String hbmFullFileName = null;
            if (hbmFolder.isDirectory()) {
                hbmFullFileName = destDirectoryPath + fileName + Constants.FILE_ENDS_WITH_ORM_EXT;
            } else {
                hbmFullFileName = destDirectoryPath;
            }
            //deleting existing file if any
            File file = new File(hbmFullFileName);
            file.delete();
            try (BufferedWriter outputFile = new BufferedWriter(new FileWriter(hbmFullFileName))) {
                outputFile.write(t.toString());
            } catch (Exception e) {
                throw new FileConversionException("Failed to write to file at " + hbmFullFileName, e);
            }
        } catch (Exception e) {
            throw new FileConversionException("Failed to write to file due to an exception", e);
        }
        log.info("Event=HbmToJpaConversion SubEvent=WriteOutputFile Status=Done DestinationFileName={}", fileName + Constants.FILE_ENDS_WITH_ORM_EXT);
    }

    /**
     * This method gets the location of the templates available in the classpath
     * and throws an IllegalStateException if the resource path cannot be located.
     *
     * @return the absolute path of the template location.
     * @throws IllegalStateException if unable to locate the resource path.
     */
    public final String getTemplateLocation() {
        String templateLocation = "templates";
        URL resourceUrl = this.getClass().getClassLoader().getResource(templateLocation);
        if (resourceUrl == null) {
            throw new IllegalStateException("Unable to locate resource path: " + templateLocation);
        }
        String resourcePath = resourceUrl.getPath();
        return resourcePath;
    }
}
