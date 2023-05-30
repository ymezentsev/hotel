package com.robot.hotel.rest;

import com.robot.hotel.error.AppError;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.GuestsQuantityException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.exception.WrongDatesException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
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
