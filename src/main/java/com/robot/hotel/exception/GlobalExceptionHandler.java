package com.robot.hotel.exception;

import com.robot.hotel.error.AppError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("errorCode", HttpStatus.BAD_REQUEST.value());
        body.put("timestamp", LocalDateTime.now());
        List<String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(this::getErrorMessage)
                .toList();
        body.put("errorText", errors);

        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(body, headers, status);
    }

    private String getErrorMessage(ObjectError e) {
        if (e instanceof FieldError) {
            String field = ((FieldError) e).getField();
            String message = e.getDefaultMessage();
            return field + " " + message;
        }
        return e.getDefaultMessage();
    }

    @ExceptionHandler(DuplicateObjectException.class)
    public ResponseEntity<AppError> catchDuplicateObjectException(DuplicateObjectException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(HttpStatus.CONFLICT.value(), LocalDateTime.now(), e.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<AppError> catchNoSuchElementException(NoSuchElementException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotEmptyObjectException.class)
    public ResponseEntity<AppError> catchNotEmptyObjectException(NotEmptyObjectException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(HttpStatus.CONFLICT.value(), LocalDateTime.now(), e.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(GuestsQuantityException.class)
    public ResponseEntity<AppError> catchGuestsQuantityException(GuestsQuantityException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongDatesException.class)
    public ResponseEntity<AppError> catchWrongDatesException(WrongDatesException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotEnoughInformationException.class)
    public ResponseEntity<AppError> catchNotEnoughInformationException(NotEnoughInformationException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FailedToSendEmailException.class)
    public ResponseEntity<AppError> catchFailedToSendEmailException(FailedToSendEmailException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}