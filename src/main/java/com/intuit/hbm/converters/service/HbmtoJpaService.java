package com.intuit.hbm.converters.service;

import com.intuit.hbm.converters.exception.FileConversionException;
import com.intuit.hbm.converters.helper.FileHelper;
import com.intuit.hbm.converters.converter.HbmToJpaConverter;
import org.antlr.stringtemplate.StringTemplate;
import org.dom4j.Document;

/**
 * This class extends ConverterService. It represents a converter service specifically for
 * converting Hibernate mapping files (HBM) to ORM.
 **/
public class HbmtoJpaService extends ConverterService {
    /**
     * Constructor that calls the parent's constructor with FileOperationsHelper.
     *
     * @param fileHelper Helps with file operations such as reading and writing.
     */
    public HbmtoJpaService(FileHelper fileHelper, HbmToJpaConverter hbmToJpaConverter) {
        super(fileHelper, hbmToJpaConverter);
    }

    /**
     * Overridden convertFile method for converting Hibernate mapping documents to ORM.
     *
     * @param hbmDoc            Document representing the input Hibernate mapping file.
     * @param fileName          Name of the file to be converted.
     * @param destDirectoryPath Destination directory path for the converted file.
     * @return StringTemplate representing the converted ORM document.
     * @throws FileConversionException If there is an error while processing the Document.
     */
    @Override
    StringTemplate convertFile(Document hbmDoc, String fileName, String destDirectoryPath) {
        return converter.convertFile(hbmDoc, fileName, destDirectoryPath);
    }
}
