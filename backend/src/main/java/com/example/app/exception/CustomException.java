package com.example.app.exception;

/**
 * Excepción personalizada para casos de negocio en la aplicación.  Se utiliza para
 * lanzar errores legibles que se convierten en respuestas HTTP coherentes.
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}