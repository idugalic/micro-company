package com.idugalic.commandside.blog;

import com.idugalic.commandside.blog.aggregate.exception.PublishBlogPostException;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.model.ConcurrencyException;
import org.axonframework.messaging.interceptors.JSR303ViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    public RestResponseEntityExceptionHandler() {
        super();
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers, final HttpStatus status,
                                                                  final WebRequest request) {
        final String bodyOfResponse = "HttpMessageNotReadableException";
        LOG.error(bodyOfResponse, ex);
        return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status,
                                                                  final WebRequest request) {
        final String bodyOfResponse = "MethodArgumentNotValidException";
        LOG.error(bodyOfResponse, ex);
        return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    // TODO Consider handling validation and other errors/exceptions here
    @ExceptionHandler({ CommandExecutionException.class })
    protected ResponseEntity<Object> handleCommandExecution(final RuntimeException cex, final WebRequest request) {
        final String bodyOfResponse = "CommandExecutionException";
        if (null != cex.getCause()) {
            LOG.error("CAUSED BY: {} {}", cex.getCause().getClass().getName(), cex.getCause().getMessage());

            if (cex.getCause() instanceof ConcurrencyException) {
                return handleExceptionInternal(cex, bodyOfResponse + " - Concurrency issue", new HttpHeaders(), HttpStatus.CONFLICT, request);
            }
            if (cex.getCause() instanceof PublishBlogPostException) {
                return handleExceptionInternal(cex, bodyOfResponse + " - " + cex.getCause().getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
            }

        }
        return handleExceptionInternal(cex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ InvalidDataAccessApiUsageException.class, DataAccessException.class })
    protected ResponseEntity<Object> handleConflict(final RuntimeException ex, final WebRequest request) {
        final String bodyOfResponse = "DataAccessException";
        LOG.error(bodyOfResponse, ex);
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleBadRequest(final DataIntegrityViolationException ex, final WebRequest request) {
        final String bodyOfResponse = "DataIntegrityViolationException";
        LOG.error(bodyOfResponse, ex);
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        final String bodyOfResponse = "Internal Error";
        LOG.error(bodyOfResponse, ex);
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({ JSR303ViolationException.class })
    public ResponseEntity<Object> handleValidation(final JSR303ViolationException ex, final WebRequest request) {
        // TODO do it properly !!!!
        final String bodyOfResponse = ex.getViolations().toString();
        LOG.error("Validation error", ex);
        return new ResponseEntity<Object>(bodyOfResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
        return new ResponseEntity<Object>("Access denied message here", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

}