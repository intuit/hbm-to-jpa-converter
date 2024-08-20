package com.intuit.hbm.converters.helper;

import com.intuit.hbm.converters.model.InputModel;
import com.intuit.hbm.converters.model.ConverterType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * This class is used to parse arguments for the conversion process.
 * It has methods for parsing, validation, and determining the converter type.
 */
@Slf4j
public class ArgsParser {

    /**
     * Parses input arguments and constructs an InputModel instance from them.
     * It validates the arguments before constructing the InputModel.
     * Also, logs the start and end of the parsing process.
     *
     * @param args An array of input arguments.
     * @return A InputModel object if the arguments are valid, otherwise null.
     */
    public InputModel parseInputArgs(String[] args) {
        log.info("Event=HbmToJpaConversion SubEvent=ParseAndValidateArgs Status=Started");

        validateMandatoryArgs(args);
        validateFileLocation(args);

        ConverterType converterType = getConverterType(args);
        InputModel inputModel = new InputModel(args[0], args[1], args[2], converterType);

        log.info("Event=HbmToJpaConversion SubEvent=ParseAndValidateArgs Status=Done InputModel={}", inputModel);
        return inputModel;
    }

    /**
     * Validates the mandatory arguments including source path, extension, and destination path.
     * Throws an IllegalArgumentException if any of these arguments is missing.
     *
     * @param args An array of input arguments.
     */
    private static void validateMandatoryArgs(String[] args) {
        if (args.length < 3 || StringUtils.isEmpty(args[0]) || StringUtils.isEmpty(args[1]) || StringUtils.isEmpty(args[2])) {
            throw new IllegalArgumentException("Source path, extension and Destination path arguments are required.");
        }
    }

    /**
     * Validates the location of the source and destination files.
     * Throws an IllegalArgumentException if the source and destination are not both files or
     * both directories or if the source is a file and the destination a directory.
     *
     * @param args An array of input arguments.
     */
    private static void validateFileLocation(String[] args) {
        File src = new File(args[0]);
        File dest = new File(args[2]);
        // Valid condition: source and destination are both files
        if (!((src.isFile() && dest.isFile()) || (src.isDirectory() && dest.isDirectory()) ||
                (src.isFile() && dest.isDirectory()))) {
            throw new IllegalArgumentException("Arguments are invalid.");
        }
    }

    /**
     * Determines the ConverterType based on the arguments.
     * If there are four arguments, it uses the fourth to determine the type.
     * If there are less than four arguments, it defaults to HBMToORM.
     *
     * @param args An array of input arguments.
     * @return The determined ConverterType.
     */
    private ConverterType getConverterType(String[] args) {
        if (args.length == 4) {
            return ConverterType.valueOf(args[4]);
        } else {
            return ConverterType.HBMToORM;
        }
    }
}
