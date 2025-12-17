package com.example.projeto_test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler Global de Exceções.
 *
 * Esta classe centraliza o tratamento de todas as exceções da aplicação,
 * garantindo respostas HTTP consistentes e padronizadas para erros.
 *
 * @author Sistema de Gestão de Restaurante
 * @version 1.0
 * @since 2025-01-01
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções genéricas do tipo RuntimeException.
     *
     * Captura exceções lançadas pelos serviços quando recursos
     * não são encontrados (produtos, recibos, etc.).
     *
     * @param ex Exceção lançada
     * @return Resposta HTTP 404 com detalhes do erro
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put("status", String.valueOf(HttpStatus.NOT_FOUND.value()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Trata erros de validação de dados (Bean Validation).
     *
     * Captura falhas de validação quando dados inválidos são enviados
     * via JSON (ex: campos obrigatórios vazios, valores negativos).
     *
     * @param ex Exceção contendo detalhes dos erros de validação
     * @return Resposta HTTP 400 com lista de erros por campo
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Processa cada erro de campo individualmente
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        errors.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}

