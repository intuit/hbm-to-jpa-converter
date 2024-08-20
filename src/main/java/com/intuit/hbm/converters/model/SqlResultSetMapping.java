package com.intuit.hbm.converters.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

/**
 * SqlResultSetMapping is a POJO that represents a SQL result set mapping in an ORM tool configuration.
 * It provides the structure for storing the name of the mapping and the corresponding column results.
 */
@Getter
@AllArgsConstructor
public class SqlResultSetMapping {
    // The name of the SQL result set mapping.
    private String name;
    // The list of column results corresponding to the SQL result set mapping.
    private ArrayList<ColumnResult> columnResults;

}
