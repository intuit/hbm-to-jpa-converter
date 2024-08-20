package com.intuit.hbm.converters.converter;

import com.intuit.hbm.converters.helper.FileHelper;
import com.intuit.hbm.converters.model.ConverterType;
import com.intuit.hbm.converters.service.ConverterService;
import com.intuit.hbm.converters.service.HbmtoJpaService;

/**
 * A factory class to provide instances of different converter types based on the input fileType.
 * Currently, supports only HbmToJpaConverter.
 */
public class ConverterFactory {
    /**
     * A factory method that provides instances of IConverter implementations.
     * Throws IllegalArgumentException in case of unsupported file types.
     *
     * @param converterType The type of converter to be returned.
     * @return an instance of the specified converter type.
     * @throws IllegalArgumentException when the fileType passed is not supported.
     */
    public static ConverterService getConverter(ConverterType converterType) {
        switch (converterType) {
            case HBMToORM:
                return new HbmtoJpaService(FileHelper.getInstance(), new HbmToJpaConverter(FileHelper.getInstance()));
            default:
                throw new IllegalArgumentException("Invalid converter type.");
        }
    }
}
