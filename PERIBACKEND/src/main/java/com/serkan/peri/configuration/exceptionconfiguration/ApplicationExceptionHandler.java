package com.serkan.peri.configuration.exceptionconfiguration;



import jakarta.validation.ConstraintViolationException;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice




public class ApplicationExceptionHandler {

private  final ErrorMessage errorMessage;

public ApplicationExceptionHandler(ErrorMessage errorMessage) {
    this.errorMessage = errorMessage;
}


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> runTimeExceptionHandler(RuntimeException exception) {
        System.out.println( exception);
        return errorMessage.errorMessageReceived(exception , ErrorType.UYGULAMA_ÇALIŞMA_HATASI,HttpStatus.SERVICE_UNAVAILABLE,null);
 }



    @ExceptionHandler(ApplicationSpesificException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> applicationSpesificException(ApplicationSpesificException exception){
        return errorMessage.errorMessageReceived(exception , exception.getErrorType(),exception.getErrorType().getHttpStatusCode(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> methodArgumentNotValid(MethodArgumentNotValidException exception){
        List<String> fields = new ArrayList<>();
        exception.getBindingResult().getFieldErrors().forEach(field -> {
            fields.add(field.getField()+ " : "+ field.getDefaultMessage());
        });
        return errorMessage.errorMessageReceived(exception,ErrorType.PARAMETRE_HATASI, HttpStatus.FORBIDDEN,fields);
    }



    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Object> handleTransactionSystemException(TransactionSystemException ex) {
        Throwable cause = ex.getRootCause();
        if (cause instanceof ConstraintViolationException constraintViolationException) {
            String mesaj = constraintViolationException.getConstraintViolations().stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body("Doğrulama Hatası: " + mesaj);
        }
        return ResponseEntity.internalServerError().body("Beklenmeyen hata oluştu");
    }




}