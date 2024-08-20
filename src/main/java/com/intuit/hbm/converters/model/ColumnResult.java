package com.intuit.hbm.converters.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ColumnResult is a POJO that represent a result column in an ORM tool configuration.
 * It provides structure for the name of the column and corresponding class type.
 */
@Getter
@AllArgsConstructor
public class ColumnResult {
    // name of the column
    private String name;
    // class name associated with the column
    private String classes;

}
