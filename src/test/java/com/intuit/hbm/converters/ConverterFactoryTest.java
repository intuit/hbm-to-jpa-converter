package com.intuit.hbm.converters;

import com.intuit.hbm.converters.converter.ConverterFactory;
import com.intuit.hbm.converters.model.ConverterType;
import com.intuit.hbm.converters.service.ConverterService;
import com.intuit.hbm.converters.service.HbmtoJpaService;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ConverterFactoryTest {
    @Test
    public void getConverterReturnsCorrectTypeForHBMToORM() {
        ConverterService converterService = ConverterFactory.getConverter(ConverterType.HBMToORM);
        assertTrue(converterService instanceof HbmtoJpaService);
    }
}
