package dev.lpa.goutbackend.commons;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import dev.lpa.goutbackend.commons.exceptions.EntityNotFound;

//NOTE: use to handle all exception in application
@RestControllerAdvice
public class ResponseAdviceHandler extends ResponseEntityExceptionHandler{
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException err,
        HttpHeaders headers,HttpStatusCode status, WebRequest request) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            err.getMessage()
        );
        Map<String,Object> invalidArgumentMap = new HashMap<>();
        var invalidArgumentList = err.getBindingResult().getFieldErrors();
        for(var invalidArgument : invalidArgumentList) {
            invalidArgumentMap.put(invalidArgument.getField(), invalidArgument.getDefaultMessage());
        }
        detail.setProperty("argument", invalidArgumentMap);
        logger.info(detail.getDetail());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(detail);
    }

    @ExceptionHandler(EntityNotFound.class)
    protected ResponseEntity<Object> entityNotFound(EntityNotFound err) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            err.getMessage()
        );
        logger.info(detail.getDetail());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detail);
    }

    //* Grobal exception */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> grobalException(Exception err) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
             err.getMessage()
        );
        logger.error(err.getMessage());
        err.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(detail);
    }
}
