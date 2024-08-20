package com.intuit.hbm.converters.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * NamedQuery is a simple POJO that represents a named query in an ORM tool configuration.
 * It provides the structure for storing the name of the query and the corresponding SQL query.
 */
@Getter
@AllArgsConstructor
public class NamedQuery {
    // The name of the named query.
    private String name;
    // The SQL query corresponding to the named query.
    private String query;

}
