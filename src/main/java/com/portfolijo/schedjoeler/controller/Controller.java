package com.portfolijo.schedjoeler.controller;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.NoSuchElementException;

/**
 * A base class for all Controllers
 */
public abstract class Controller {

    /**
     * Handles {@link ValidationException}s
     *
     * @param e A ValidationException
     * @return A 400 BAD REQUEST response entity
     */
    @ExceptionHandler({ValidationException.class})
    private ResponseEntity<Response> handleException(ValidationException e) {
        return responseCodeBadRequest(e.getMessage());
    }

    /**
     * Handles {@link NoSuchElementException}s
     *
     * @param e A NoSuchElementException
     * @return A 404 NOT FOUND response entity
     */
    @ExceptionHandler({NoSuchElementException.class})
    private ResponseEntity<Response> handleException(NoSuchElementException e) {
        return responseCodeNotFound(e.getMessage());
    }

    /**
     * Creates a response entity indicating that the request successful
     *
     * @param data The data to be returned to the client
     * @return A 200 OK response entity
     */
    public ResponseEntity<Response> responseCodeOk(Object data) {
        HttpStatus status = HttpStatus.OK;
        return responseEntity(status, data, status.name(), null);
    }

    /**
     * Creates a response entity indicating that a new resource has been created
     *
     * @param data The data to be returned to the client
     * @param path The URI path of the new resource
     * @return A 201 CREATED response entity containing a location header with the resource URI
     */
    public ResponseEntity<Response> responseCodeCreated(Object data, String path) {
        HttpStatus status = HttpStatus.CREATED;
        return responseEntity(
                status,
                data,
                status.name(),
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .toUriString()
                        +path);
    }

    /**
     * Creates a response entity indicating that there is no content at the resource location
     *
     * @return A 204 NO CONTENT response entity
     */
    public ResponseEntity<Response> responseCodeNoContent() {
        HttpStatus status = HttpStatus.NO_CONTENT;
        return responseEntity(status, null, status.name(), null);
    }

    /**
     * Creates a response entity indicating a bad request
     *
     * @param message An error message
     * @return A 400 BAD REQUEST response entity
     */
    public ResponseEntity<Response> responseCodeBadRequest(String message) {
        return responseEntity(HttpStatus.BAD_REQUEST, null, message, null);
    }

    /**
     * Creates a response entity indicating that the requested resource does not exist
     *
     * @param message An error message
     * @return A 404 NOT FOUND response entity
     */
    public ResponseEntity<Response> responseCodeNotFound(String message) {
        return responseEntity(HttpStatus.NOT_FOUND, null, message, null);
    }

    /**
     * Creates a response entity
     *
     * @param status An HTTP status
     * @param data The data to be returned to the client
     * @param message A response message
     * @param path The URI path of a new or updated resource
     * @return A response entity
     */
    private ResponseEntity<Response> responseEntity(HttpStatus status, Object data, String message, String path) {
        if (path != null) {
            return ResponseEntity
                    .status(status)
                    .header("Location", path)
                    .body(response(data, message));
        }
        return ResponseEntity
                .status(status)
                .body(response(data, message));
    }

    /**
     * Creates a response
     *
     * @param data The data to be returned to the client
     * @param message A response message
     * @return A response
     */
    private Response response(Object data, String message) {
        return Response
                .builder()
                .data(data)
                .message(message)
                .build();
    }
}
