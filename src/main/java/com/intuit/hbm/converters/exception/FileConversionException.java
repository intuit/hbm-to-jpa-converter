package com.intuit.hbm.converters.exception;

/**
 * A custom exception class for file conversion errors.
 * Extends the RuntimeException class to indicate that it is an unchecked exception.
 */
public class FileConversionException extends RuntimeException {
    /**
     * Constructs a new FileConversionException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public FileConversionException(String message) {
        super(message);
    }

    /**
     * Constructs a new FileConversionException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     * @param e       the cause (which is saved for later retrieval by the getCause() method)
     */
    public FileConversionException(String message, Throwable e) {
        super(message, e);
    }
}
