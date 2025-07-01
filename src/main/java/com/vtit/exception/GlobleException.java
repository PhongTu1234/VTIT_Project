package com.vtit.exception;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vtit.dto.RestResponseDTO;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobleException {

    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponseDTO<Object>> handleIdException(IdInvalidException ex) {
        RestResponseDTO<Object> res = new RestResponseDTO<>();
        res.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        res.setError("IdInvalidException");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(res);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<RestResponseDTO<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        RestResponseDTO<Object> res = new RestResponseDTO<>();
        res.setStatusCode(HttpStatus.SC_NOT_FOUND);
        res.setError("ResourceNotFoundException");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(res);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<RestResponseDTO<Object>> handleDuplicate(DuplicateResourceException ex) {
        RestResponseDTO<Object> res = new RestResponseDTO<>();
        res.setStatusCode(HttpStatus.SC_CONFLICT);
        res.setError("DuplicateResourceException");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.SC_CONFLICT).body(res);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<RestResponseDTO<Object>> handleValidation(ValidationException ex) {
        RestResponseDTO<Object> res = new RestResponseDTO<>();
        res.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        res.setError("ValidationException");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(res);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<RestResponseDTO<Object>> handleUnauthorized(UnauthorizedException ex) {
        RestResponseDTO<Object> res = new RestResponseDTO<>();
        res.setStatusCode(HttpStatus.SC_UNAUTHORIZED);
        res.setError("UnauthorizedException");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body(res);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<RestResponseDTO<Object>> handleBusiness(BusinessException ex) {
        RestResponseDTO<Object> res = new RestResponseDTO<>();
        res.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        res.setError("BusinessException");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(res);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<RestResponseDTO<Object>> handleEntityNotFound(EntityNotFoundException ex) {
        RestResponseDTO<Object> response = new RestResponseDTO<>();
        response.setStatusCode(HttpStatus.SC_NOT_FOUND);
        response.setError("EntityNotFoundException");
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponseDTO<Object>> handleValidationError(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        List<String> errorMessages = fieldErrors.stream()
            .map(FieldError::getDefaultMessage)
            .toList();

        RestResponseDTO<Object> response = new RestResponseDTO<>();
        response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        response.setError("Validation failed");
        response.setMessage(errorMessages.size() > 1 ? errorMessages : errorMessages.get(0));

        return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponseDTO<Object>> handleGenericException(Exception ex) {
        RestResponseDTO<Object> response = new RestResponseDTO<>();
        response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        response.setError("Internal Server Error");
        response.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(response);
    }
}
