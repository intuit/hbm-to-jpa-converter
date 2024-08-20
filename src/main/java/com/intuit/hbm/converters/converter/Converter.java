package com.intuit.hbm.converters.converter;

import org.antlr.stringtemplate.StringTemplate;
import org.dom4j.Document;

/**
 * The IConverter interface should be implemented by any
 * class which intends to define the logic of converting a
 * given file into a specified format and storing it in a
 * specified directory.
 */
public interface Converter {

    /**
     * This method is used for converting a given file to a specific
     * format and writing it to a destination directory.
     *
     * @param hbmDoc            The source file which is to be converted.
     * @param fileName          The name of the file after conversion.
     * @param destDirectoryPath The directory path where the converted file would be stored.
     */
    StringTemplate convertFile(Document hbmDoc, String fileName, String destDirectoryPath);
}
