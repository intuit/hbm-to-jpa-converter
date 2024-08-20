package com.intuit.hbm.converters.converter;

import com.intuit.hbm.converters.common.Constants;
import com.intuit.hbm.converters.exception.FileConversionException;
import com.intuit.hbm.converters.helper.FileHelper;
import com.intuit.hbm.converters.model.ColumnResult;
import com.intuit.hbm.converters.model.NamedNativeQuery;
import com.intuit.hbm.converters.model.NamedQuery;
import com.intuit.hbm.converters.model.SqlResultSetMapping;
import lombok.extern.slf4j.Slf4j;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class responsible for converting a Hibernate XML mapping file (.hbm) of named query to a
 * JPA object-relational mapping.
 */
@Slf4j
public class HbmToJpaConverter implements Converter {
    private static final Map<String, String> TYPE_MAPPING;
    private FileHelper fileHelper;

    static {
        TYPE_MAPPING = new HashMap<>();
        TYPE_MAPPING.put("string", "java.lang.String");
        TYPE_MAPPING.put("double", "java.lang.Double");
        TYPE_MAPPING.put("int", "java.lang.Integer");
        TYPE_MAPPING.put("boolean", "java.lang.Boolean");
        // Add more mappings as necessary
    }

    /**
     * A Constructor of HbmToJpaConverter class which initializes through
     * a given FileOperationsHelper object.
     *
     * @param fileHelper Specifies the current file reader
     */
    public HbmToJpaConverter(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    /**
     * Converts .hbm file to ORM file using a string template.
     *
     * @param hbmDoc            XML Document parsed from the .hbm file.
     * @param fileName          The name of the .hbm file.
     * @param destDirectoryPath Destination directory path for the generated file.
     * @return Template of the converted file.
     * @throws FileConversionException if error occurs while parsing the .hbm file.
     */
    @Override
    public StringTemplate convertFile(Document hbmDoc, String fileName, String destDirectoryPath) {
        StringTemplate t = null;
        log.info("Event=HbmToJpaConversion SubEvent=convertFile Status=Started SourceFileName={}", hbmDoc.getName());
        try {
            StringTemplateGroup templates = new StringTemplateGroup("OrmFile", fileHelper.getTemplateLocation());
            t = templates.getInstanceOf("OrmFile");

            t.setAttribute(Constants.NAMED_QUERIES, getNamedQueries(hbmDoc));
            ArrayList<NamedNativeQuery> namedNativeQueries = getNamedNativeQueries(hbmDoc);
            t.setAttribute(Constants.NAMED_NATIVE_QUERIES, namedNativeQueries);
            t.setAttribute(Constants.PACKAGE, hbmDoc.getRootElement().attributeValue("package"));
            t.setAttribute(Constants.SQL_RESULT_SET_MAPPINGS, getSqlResultSetMappings(namedNativeQueries));
            log.info("Event=HbmToJpaConversion SubEvent=convertFile Status=Done SourceFileName={} DestinationFileName={}", hbmDoc.getName(), fileName + Constants.FILE_ENDS_WITH_ORM_EXT);
        } catch (Exception e) {
            throw new FileConversionException("File conversion fail", e);
        }
        return t;
    }

    /**
     * Retrieves all NamedQuery from .hbm file.
     *
     * @param hbmDoc XML-Document to fetch the NamedQueries from.
     * @return ArrayList containing all named query objects.
     */
    public ArrayList<NamedQuery> getNamedQueries(Document hbmDoc) {
        ArrayList<NamedQuery> namedQueries = new ArrayList<>();
        if (namedQueries.isEmpty()) {
            hbmDoc.getRootElement().elements(Constants.QUERY).forEach(sqlQueryElement -> {
                namedQueries.add(new NamedQuery(sqlQueryElement.attributeValue(Constants.NAME), sqlQueryElement.getText().trim().toString()));
            });
        }
        return namedQueries;
    }

    /**
     * Retrieves all NamedNativeQuery objects from the .hbm file.
     *
     * @param hbmDoc XML Document parsed from the .hbm file.
     * @return ArrayList containing all NamedNativeQuery objects.
     */
    public ArrayList<NamedNativeQuery> getNamedNativeQueries(Document hbmDoc) {
        ArrayList<NamedNativeQuery> nativeNamedQueries = new ArrayList<>();
        if (nativeNamedQueries.isEmpty()) {
            AtomicInteger index = new AtomicInteger();
            hbmDoc.getRootElement().elements(Constants.SQL_QUERY).forEach(sqlQueryElement -> {
                int currentIndex = index.getAndIncrement();
                nativeNamedQueries.add(new NamedNativeQuery(sqlQueryElement.attributeValue(Constants.NAME), sqlQueryElement.getText().trim().toString(),
                        (sqlQueryElement.element(Constants.RETURN_TAG) != null) ? ((sqlQueryElement.element(Constants.RETURN_TAG)).attributeValue(Constants.CLASS_TAG)) : "",
                        (sqlQueryElement.element(Constants.RETURN_SCALAR) != null) ? (sqlQueryElement.attributeValue(Constants.NAME) + Constants.MAPPING) : ""));

                if (sqlQueryElement.element(Constants.RETURN_SCALAR) != null) {
                    SqlResultSetMapping sqlResultSetMapping = new SqlResultSetMapping(sqlQueryElement.attributeValue(Constants.NAME) + Constants.MAPPING, getColumnResults(sqlQueryElement));
                    nativeNamedQueries.get(currentIndex).setResultSetMappings(sqlResultSetMapping);
                }
            });

        }
        return nativeNamedQueries;
    }

    /**
     * Retrieves SqlResultSetMappings objects from the given NamedNativeQuery.
     *
     * @param namedNativeQueries An ArrayList of NamedNativeQuery objects.
     * @return ArrayList of SqlResultSetMapping objects.
     */
    public ArrayList<SqlResultSetMapping> getSqlResultSetMappings(ArrayList<NamedNativeQuery> namedNativeQueries) {
        ArrayList<SqlResultSetMapping> sqlResultSetMappings = new ArrayList<>();
        if (sqlResultSetMappings.isEmpty() && !namedNativeQueries.isEmpty()) {
            for (NamedNativeQuery namedNativeQuery :
                    namedNativeQueries) {
                sqlResultSetMappings.add(namedNativeQuery.getResultSetMappings());
            }
        }
        return sqlResultSetMappings;
    }

    /**
     * Retrieves ColumnResult objects from the given sqlQueryElement.
     *
     * @param sqlQueryElement An xml Element contains sql query information.
     * @return ArrayList of ColumnResult objects.
     */
    public ArrayList<ColumnResult> getColumnResults(Element sqlQueryElement) {
        ArrayList<ColumnResult> columnResults = new ArrayList<>();
        if (columnResults.isEmpty()) {
            for (Element columnResultElement : sqlQueryElement.elements(Constants.RETURN_SCALAR)) {
                columnResults.add(new ColumnResult(columnResultElement.attributeValue(Constants.COLUMN), mapColumnType(columnResultElement.attributeValue(Constants.TYPE))));
            }
        }
        return columnResults;
    }

    /**
     * Maps the column type to its corresponding java type.
     *
     * @param type Column type from the .hbm file.
     * @return Mapped Java data type.
     */
    private String mapColumnType(String type) {
        return TYPE_MAPPING.getOrDefault(type, type);
    }
}
