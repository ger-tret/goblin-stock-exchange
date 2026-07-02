package me.ger_tret.goblin_stock_exchange.exception.handler;


import lombok.extern.slf4j.Slf4j;
import me.ger_tret.goblin_stock_exchange.exception.GseException;
import me.ger_tret.goblin_stock_exchange.exception.InsufficientGoldException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(GseException.class)
    public ProblemDetail handleGseException(GseException ex) {
        log.warn("Business logic violation: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        problemDetail.setTitle("Business Rule Violation");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }


    @ExceptionHandler(InsufficientGoldException.class)
    public ProblemDetail handleInsufficientGold(InsufficientGoldException ex) {
        log.warn("Financial error: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.PAYMENT_REQUIRED,
                ex.getMessage()
        );
        problemDetail.setTitle("Insufficient Gold Balance");
        problemDetail.setProperty("errorCode", "GOLD_001");

        return problemDetail;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Validation failed for request");

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Your request parameters are invalid."
        );
        problemDetail.setTitle("Constraint Violation");

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        problemDetail.setProperty("invalid_params", errors);

        return problemDetail;
    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAllExceptions(Exception ex) {
        log.error("Unhandled exception caught: ", ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred on the goblin servers."
        );
        problemDetail.setTitle("Server Error");

        return problemDetail;
    }
}
