package br.com.raonidev.desafio_todolist.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(
            ResponseStatusException exception,
            HttpServletRequest request) {
        int statusCode = exception.getStatusCode().value();

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                statusCode,
                HttpStatus.valueOf(statusCode).getReasonPhrase(),
                tratarMensagem(exception.getReason()),
                request.getRequestURI());

        return ResponseEntity
                .status(statusCode)
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Existem campos inválidos na requisição. Verifique os dados enviados e tente novamente.",
                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    private String tratarMensagem(String message) {
        if (message == null || message.isBlank()) {
            return "Não foi possível processar a solicitação.";
        }

        if (message.equals("Todo não encontrado")) {
            return "Todo não encontrado. Verifique o ID informado e tente novamente.";
        }

        return message;
    }
}