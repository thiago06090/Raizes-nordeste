package com.raizes.backend.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // erros de validação (ex: campo obrigatório vazio)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        ApiError error = new ApiError(
                "ERRO_VALIDACAO",
                "Erro de validação nos campos enviados",
                details,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    // credenciais inválidas no login
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                "CREDENCIAIS_INVALIDAS",
                "E-mail ou senha inválidos",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    // regras de negócio (ex: estoque insuficiente)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntime(
            RuntimeException ex,
            HttpServletRequest request) {

        // define o status baseado na mensagem
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (ex.getMessage().contains("não encontrado") ||
                ex.getMessage().contains("não encontrada")) {
            status = HttpStatus.NOT_FOUND;
        }

        if (ex.getMessage().contains("insuficiente") ||
                ex.getMessage().contains("já cadastrado")) {
            status = HttpStatus.CONFLICT;
        }

        if (ex.getMessage().contains("não autorizado")) {
            status = HttpStatus.FORBIDDEN;
        }

        ApiError error = new ApiError(
                "ERRO_NEGOCIO",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(error);
    }

    // qualquer outro erro inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                "ERRO_INTERNO",
                "Ocorreu um erro interno. Tente novamente.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}