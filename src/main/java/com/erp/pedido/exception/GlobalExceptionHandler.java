package com.erp.pedido.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.erp.pedido.dto.ErroResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RestControllerAdvice 
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErroResponse> handleNotFound(NotFoundException ex) {
        return criarResposta(HttpStatus.NOT_FOUND,"NOT_FOUND",ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErroResponse> handleConflict(ConflictException ex) {
        return criarResposta(HttpStatus.CONFLICT,"CONFLICT",ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErroResponse> handleBusiness(BusinessException ex) {
        return criarResposta(HttpStatus.UNPROCESSABLE_CONTENT,"BUSINESS_ERROR",ex.getMessage());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MissingRequestHeaderException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErroResponse> handleInvalidRequest(Exception ex) {
        return criarResposta(HttpStatus.BAD_REQUEST,"INVALID_REQUEST","Requisição inválida");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidation(MethodArgumentNotValidException ex) {
        var erro = ex.getBindingResult().getFieldError();
        var mensagem = erro != null ? erro.getField() + ": " + erro.getDefaultMessage() : "Dados inválidos";
        return criarResposta(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", mensagem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnexpected(Exception ex) {
        if (ex instanceof ErrorResponse errorResponse) {
            return ResponseEntity.status(errorResponse.getStatusCode()).headers(errorResponse.getHeaders()).body(errorResponse.getBody());
        }
        return criarResposta(HttpStatus.INTERNAL_SERVER_ERROR,"INTERNAL_ERROR","Ocorreu um erro interno na aplicação");
    }

    private ResponseEntity<ErroResponse> criarResposta(HttpStatus status, String codigo, String mensagem) {
        var erro = new ErroResponse();
        erro.setCodigo(codigo);
        erro.setMensagem(mensagem);
        return ResponseEntity.status(status).body(erro);
    }
}



