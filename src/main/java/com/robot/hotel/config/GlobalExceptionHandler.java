package com.robot.hotel.config;

import com.robot.hotel.error.AppError;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.GuestsQuantityException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.exception.WrongDatesException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }


    @ExceptionHandler
    public ResponseEntity<AppError> catchDuplicateObjectException(DuplicateObjectException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_IMPLEMENTED.value(), e.getMessage()), HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchNoSuchElementException(NoSuchElementException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchNotEmptyObjectException(NotEmptyObjectException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_IMPLEMENTED.value(), e.getMessage()), HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchGuestsQuantityException(GuestsQuantityException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_IMPLEMENTED.value(), e.getMessage()), HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchWrongDatesException(WrongDatesException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_IMPLEMENTED.value(), e.getMessage()), HttpStatus.NOT_IMPLEMENTED);
    }
}
