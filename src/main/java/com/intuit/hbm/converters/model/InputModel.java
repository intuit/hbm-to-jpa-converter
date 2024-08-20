package com.intuit.hbm.converters.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * InputModel is a class which stores the target directory path and
 * the destination directory path which were parsed from input arguments.
 */
@ToString
@Getter
@AllArgsConstructor
public class InputModel {
    private String srcDirectoryPath;
    private String fileEndsWith;
    private String destDirPath;
    private ConverterType converterType;

}
