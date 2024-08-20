package com.intuit.hbm.converters.service;

import com.intuit.hbm.converters.common.Constants;
import com.intuit.hbm.converters.exception.FileConversionException;
import com.intuit.hbm.converters.helper.FileHelper;
import com.intuit.hbm.converters.converter.Converter;
import lombok.extern.slf4j.Slf4j;
import org.antlr.stringtemplate.StringTemplate;
import org.dom4j.Document;

import java.io.File;

/**
 * Abstract class that provides the base for all file converter services.
 * It provides methods for reading, writing and validating files.
 * It uses Template Method design pattern where performConversion is your template method and
 * convertFile is your primitive(step) which is implemented by subclasses.
 */
@Slf4j
public abstract class ConverterService {
    // Helper for file operations
    private FileHelper fileHelper;
    protected Converter converter;

    /**
     * Constructor for ConverterService
     *
     * @param fileHelper Helper for reading, writing and validating files
     */
    public ConverterService(FileHelper fileHelper, Converter converter) {
        this.fileHelper = fileHelper;
        this.converter = converter;
    }

    /**
     * Reads the contents of a file into a Document
     *
     * @param hbmFile File to read from
     * @return Document containing the contents of the file
     */
    Document readInputFile(File hbmFile) {
        return fileHelper.readInputFile(hbmFile);
    }

    /**
     * Validates the input file
     *
     * @param hbmFile File to be validated
     */
    void validateInputFile(File hbmFile) {
        fileHelper.validateInputFile(hbmFile);
    }

    /**
     * Converts the input file into a StringTemplate.
     * Implementation to be provided by subclasses.
     *
     * @param hbmDoc            Document representing the file
     * @param fileName          Name of the file
     * @param destDirectoryPath Path of the destination directory
     * @return StringTemplate representing the converted file
     * @throws FileConversionException When there is an error processing the Document
     */
    abstract StringTemplate convertFile(Document hbmDoc, String fileName, String destDirectoryPath);

    /**
     * Writes a StringTemplate to a file
     *
     * @param t                 StringTemplate to write
     * @param destDirectoryPath Path of the destination directory
     * @param fileName          Name of the file
     * @throws FileConversionException When an error occurs
     */
    void writeFile(StringTemplate t, String destDirectoryPath, String fileName) throws FileConversionException {
        fileHelper.writeFile(t, destDirectoryPath, fileName);
    }

    /**
     * Template method for performing the conversion process
     *
     * @param hbmFile           File to convert
     * @param filename          Name of the file
     * @param destDirectoryPath Path of the destination directory
     * @throws FileConversionException When an error occurs
     */
    public final boolean performConversion(File hbmFile, String filename, String destDirectoryPath) {
        try {
            log.info("Event=HbmToJpaConversion SubEvent=FileConversion Status=Started SourceFileName={}", hbmFile.getName());
            //Validate File
            validateInputFile(hbmFile);
            //Read Input File
            Document hbmDoc = readInputFile(hbmFile);
            //Convert File to Target Format
            StringTemplate t = convertFile(hbmDoc, filename, destDirectoryPath);
            //Write Output File
            writeFile(t, destDirectoryPath, filename);
            log.info("Event=HbmToJpaConversion SubEvent=FileConversion Status=Done SourceFileName={} DestinationFileName={}", hbmFile.getName(), filename + Constants.FILE_ENDS_WITH_ORM_EXT);
        } catch (Exception e) {
            log.error("Event=HbmToJpaConversion SubEvent=FileConversion Status=Failed SourceFileName={}", filename, e);
            return false;
        }
        return true;
    }
}
