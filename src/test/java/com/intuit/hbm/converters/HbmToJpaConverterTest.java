package com.intuit.hbm.converters;

import com.intuit.hbm.converters.common.Constants;
import com.intuit.hbm.converters.converter.HbmToJpaConverter;
import com.intuit.hbm.converters.exception.FileConversionException;
import com.intuit.hbm.converters.helper.FileHelper;
import com.intuit.hbm.converters.model.ColumnResult;
import com.intuit.hbm.converters.model.NamedNativeQuery;
import com.intuit.hbm.converters.model.NamedQuery;
import com.intuit.hbm.converters.model.SqlResultSetMapping;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;

public class HbmToJpaConverterTest {
    private HbmToJpaConverter converter;
    private FileHelper fileHelper;
    private StringTemplate mockTemplate;
    private ArrayList<NamedNativeQuery> namedNativeQueries;
    private ArrayList<NamedQuery> namedQueries;

    @Before
    public void setup() {
        fileHelper = Mockito.mock(FileHelper.class);
        converter = new HbmToJpaConverter(fileHelper);
    }

    @Test
    public void testConvertFile() {
        mockTemplate = Mockito.mock(StringTemplate.class);
        namedNativeQueries = new ArrayList<>();
        namedQueries = new ArrayList<>();
        // Arrange
        StringTemplateGroup mockTemplateGroup = Mockito.mock(StringTemplateGroup.class);
        Mockito.when(mockTemplateGroup.getInstanceOf("OrmFile")).thenReturn(mockTemplate);

        Document mockDocument = Mockito.mock(Document.class);
        Element mockRootElement = Mockito.mock(Element.class);
        Mockito.when(mockDocument.getRootElement()).thenReturn(mockRootElement);
        doNothing().when(mockTemplate).setAttribute(Constants.NAMED_QUERIES, namedQueries);
        doNothing().when(mockTemplate).setAttribute(Constants.NAMED_NATIVE_QUERIES, namedNativeQueries);

        // Act
        StringTemplate actualTemplate = converter.convertFile(mockDocument, "dummy.hbm", "/dummy/path");

        // Assert
        assertNotNull(actualTemplate);
    }

    @Test
    public void testConvertFileException() {
        // Arrange
        //Mockito.when(fileHelper.getTemplateLocation()).thenThrow(new IllegalStateException()); // Causes an exception in your method

        Document mockDocument = Mockito.mock(Document.class);

        // Assert
        Exception exception = assertThrows(FileConversionException.class, () -> {
            // Act
            converter.convertFile(mockDocument, "dummy.hbm", "/dummy/path");
        });

        assertNotNull(exception);
        assertEquals("File conversion fail", exception.getMessage());
    }

    @Test
    public void testGetNamedQueries() {
        // given
        Document mockDocument = Mockito.mock(Document.class);
        Element mockRootElement = Mockito.mock(Element.class);
        Element mockQueryElement = Mockito.mock(Element.class);
        NamedQuery expectedNamedQuery = new NamedQuery("testQuery", "SELECT * from Test");

        // then
        Mockito.when(mockDocument.getRootElement()).thenReturn(mockRootElement);
        Mockito.when(mockRootElement.elements(Constants.QUERY)).thenReturn(Collections.singletonList(mockQueryElement));
        Mockito.when(mockQueryElement.attributeValue(Constants.NAME)).thenReturn(expectedNamedQuery.getName());
        Mockito.when(mockQueryElement.getText()).thenReturn(expectedNamedQuery.getQuery());

        // when
        ArrayList<NamedQuery> actualNamedQueries = converter.getNamedQueries(mockDocument);

        // verify
        assertEquals(1, actualNamedQueries.size());
        assertEquals(expectedNamedQuery.getQuery(), actualNamedQueries.get(0).getQuery());
        assertEquals(expectedNamedQuery.getName(), actualNamedQueries.get(0).getName());
    }

    @Test
    public void testGetNamedNativeQueries() {
        // Setup
        Document mockDocument = Mockito.mock(Document.class);
        Element mockRootElement = Mockito.mock(Element.class);
        Element mockSqlQueryElement = Mockito.mock(Element.class);
        Element mockReturnTagElement = Mockito.mock(Element.class);
        Element mockReturnScalarElement = Mockito.mock(Element.class);

        Mockito.when(mockDocument.getRootElement()).thenReturn(mockRootElement);
        Mockito.when(mockRootElement.elements(Constants.SQL_QUERY)).thenReturn(Collections.singletonList(mockSqlQueryElement));
        Mockito.when(mockSqlQueryElement.attributeValue(Constants.NAME)).thenReturn("testNamedQuery");
        Mockito.when(mockSqlQueryElement.getText()).thenReturn("SELECT * FROM Test");
        Mockito.when(mockSqlQueryElement.element(Constants.RETURN_TAG)).thenReturn(mockReturnTagElement);
        Mockito.when(mockReturnTagElement.attributeValue(Constants.CLASS_TAG)).thenReturn("TestClass");
        Mockito.when(mockSqlQueryElement.element(Constants.RETURN_SCALAR)).thenReturn(mockReturnScalarElement);

        // Execute
        ArrayList<NamedNativeQuery> actualNamedNativeQueries = converter.getNamedNativeQueries(mockDocument);

        // verify
        assertEquals(1, actualNamedNativeQueries.size());
        NamedNativeQuery namedNativeQuery = actualNamedNativeQueries.get(0);
        assertEquals("testNamedQuery", namedNativeQuery.getName());
        assertEquals("SELECT * FROM Test", namedNativeQuery.getQuery());
        assertEquals("TestClass", namedNativeQuery.getResultClass());
    }

    @Test
    public void testGetSqlResultSetMappings() {
        // Setup
        SqlResultSetMapping resultSetMapping1 = Mockito.mock(SqlResultSetMapping.class);
        SqlResultSetMapping resultSetMapping2 = Mockito.mock(SqlResultSetMapping.class);
        NamedNativeQuery namedNativeQuery1 = Mockito.mock(NamedNativeQuery.class);
        NamedNativeQuery namedNativeQuery2 = Mockito.mock(NamedNativeQuery.class);
        Mockito.when(namedNativeQuery1.getResultSetMappings()).thenReturn(resultSetMapping1);
        Mockito.when(namedNativeQuery2.getResultSetMappings()).thenReturn(resultSetMapping2);
        namedNativeQueries = new ArrayList<>(Arrays.asList(namedNativeQuery1, namedNativeQuery2));

        // Execute
        ArrayList<SqlResultSetMapping> actualResultSetMappings = converter.getSqlResultSetMappings(namedNativeQueries);

        // Verify
        assertEquals(2, actualResultSetMappings.size());
        Assert.assertSame(resultSetMapping1, actualResultSetMappings.get(0));
        Assert.assertSame(resultSetMapping2, actualResultSetMappings.get(1));
    }

    @Test
    public void testGetSqlResultSetMappingsWithEmptyList() {
        // Setup
        namedNativeQueries = new ArrayList<>();

        // Execute
        ArrayList<SqlResultSetMapping> actualResultSetMappings = converter.getSqlResultSetMappings(namedNativeQueries);

        // Verify
        assertEquals(0, actualResultSetMappings.size());
    }

    @Test
    public void testGetColumnResults() {
        // Setup
        Element mockSqlQueryElement = Mockito.mock(Element.class);
        Element mockColumnResultElement1 = Mockito.mock(Element.class);
        Element mockColumnResultElement2 = Mockito.mock(Element.class);

        Mockito.when(mockSqlQueryElement.elements(Constants.RETURN_SCALAR)).thenReturn(Arrays.asList(mockColumnResultElement1, mockColumnResultElement2));
        Mockito.when(mockColumnResultElement1.attributeValue(Constants.COLUMN)).thenReturn("column1");
        Mockito.when(mockColumnResultElement1.attributeValue(Constants.TYPE)).thenReturn("string");
        Mockito.when(mockColumnResultElement2.attributeValue(Constants.COLUMN)).thenReturn("column2");
        Mockito.when(mockColumnResultElement2.attributeValue(Constants.TYPE)).thenReturn("int");

        // Execute
        ArrayList<ColumnResult> actualColumnResults = converter.getColumnResults(mockSqlQueryElement);

        // Verify
        assertEquals(2, actualColumnResults.size());
        assertEquals("column1", actualColumnResults.get(0).getName());
        assertEquals("java.lang.String", actualColumnResults.get(0).getClasses());
        assertEquals("column2", actualColumnResults.get(1).getName());
        assertEquals("java.lang.Integer", actualColumnResults.get(1).getClasses());
    }
}
