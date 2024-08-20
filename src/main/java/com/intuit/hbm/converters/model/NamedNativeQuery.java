package com.intuit.hbm.converters.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * NamedNativeQuery is a POJO that represents a named native SQL query in an ORM tool configuration.
 * It contains the query name, the SQL query, associated result class and result mapping.
 */
@Getter
public class NamedNativeQuery {
    // Name of the query
    private String name;
    // SQL query
    private String query;
    // Result class associated with the query
    private String resultClass;
    // Name of the result set mapping
    private String resultSetMapping;
    // Result set mappings associated with this query
    @Setter
    private SqlResultSetMapping resultSetMappings;

    /**
     * Creates an instance of NamedNativeQuery with all the attribute values.
     *
     * @param name             The name of the query.
     * @param query            The SQL query.
     * @param resultClass      The result class associated with the query.
     * @param resultSetMapping The result set mapping for the query.
     */
    public NamedNativeQuery(String name, String query, String resultClass, String resultSetMapping) {
        this.name = name;
        this.query = query;
        this.resultClass = resultClass;
        this.resultSetMapping = resultSetMapping;
    }

    /**
     * Checks if the NamedNativeQuery instance has an associated result class.
     *
     * @return boolean - true if there is a resultClass, false otherwise
     */
    public boolean getIsReturnClass() {
        if (resultClass.isEmpty()) return false;
        return true;
    }

    /**
     * Checks if the NamedNativeQuery instance has a resultSetMapping.
     *
     * @return boolean - true if there is a resultSetMapping, false otherwise
     */
    public boolean getIsResultSetMapping() {
        if (resultSetMapping.isEmpty()) return false;
        return true;
    }
}
