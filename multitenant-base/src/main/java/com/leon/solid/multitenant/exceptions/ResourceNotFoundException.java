package com.leon.solid.multitenant.exceptions;

/**
 * This class is the superclass of {@link RuntimeException} that can be thrown during the resource not found.
 *
 * @author Leon.Tang
 * @version 1.0
 *
 */
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     *   Constructs a new runtime exception with the specified detail message.
     * @param message the detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new runtime exception wrap nested exception.
     *
     * @param cause the wrapped cause exception
     */
    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }


    /**
     *   Constructs a new runtime exception with the specified detail message and wrap nested exception.
     *
     * @param message  the detail message
     * @param cause the wrapped cause exception
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }


}
