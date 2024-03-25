package org.task.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.task.responses.MessageResponse;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for handling exceptions across the application.
 * This class is annotated with `@ControllerAdvice` to handle exceptions
 * thrown by methods annotated with `@RequestMapping` and similar.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Exception handler for ConstraintViolationException.
     * This method is annotated with `@ExceptionHandler` to handle ConstraintViolationException.
     * It also sets the response status to BAD_REQUEST.
     *
     * @param ex The ConstraintViolationException to handle.
     * @return A ResponseEntity with a MessageResponse containing the validation errors and a BAD_REQUEST status.
     * @see MessageResponse
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<MessageResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return new ResponseEntity<>(
                new MessageResponse("Validation error.", errors), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler for MethodArgumentNotValidException.
     * This method is annotated with `@ExceptionHandler` to handle MethodArgumentNotValidException.
     * It also sets the response status to BAD_REQUEST.
     *
     * @param ex The MethodArgumentNotValidException to handle.
     * @return A ResponseEntity with a MessageResponse containing the validation errors and a BAD_REQUEST status.
     * @see MessageResponse
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MessageResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return new ResponseEntity<>(
                new MessageResponse("Validation error.", errors), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler for InvalidFormatException.
     * This method is annotated with `@ExceptionHandler` to handle InvalidFormatException.
     *
     * @param ex The InvalidFormatException to handle.
     * @return A ResponseEntity with a MessageResponse containing the error message and a BAD_REQUEST status.
     * @see MessageResponse
     */
    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MessageResponse> handleInvalidFormatException(InvalidFormatException ex) {
        // Extract the field name from the path reference
        String pathReference = ex.getPathReference();
        int startIndex = pathReference.lastIndexOf("[\"") + 2;
        int endIndex = pathReference.lastIndexOf("\"]");

        String fieldName =
                (startIndex > 0 && endIndex > startIndex) ? pathReference.substring(startIndex, endIndex) : "unknown";

        String errorMessage = "Invalid format for field: '"
                + fieldName
                + "'. Expected: "
                + ex.getTargetType().getSimpleName();
        return new ResponseEntity<>(
                new MessageResponse("Validation error.", errorMessage), HttpStatus.BAD_REQUEST);
    }
}