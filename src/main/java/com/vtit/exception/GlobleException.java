package com.vtit.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vtit.dto.common.RestResponseDTO;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobleException {

	@ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class, IdInvalidException.class})
	public ResponseEntity<RestResponseDTO<Object>> handleLoginFail(Exception ex) {
	    RestResponseDTO<Object> res = new RestResponseDTO<>();
	    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
	    res.setError(ex.getMessage());
	    res.setMessage("Exception occurs...");
	    res.setData(null);
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
	}

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<RestResponseDTO<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        RestResponseDTO<Object> res = new RestResponseDTO<>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setError(ex.getMessage() != null ? ex.getMessage() : "Resource not found");
        res.setMessage("404 Not Found. URL not found or resource does not exist.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<RestResponseDTO<Object>> handleDuplicate(DuplicateResourceException ex) {
        RestResponseDTO<Object> res = new RestResponseDTO<>();
        res.setStatusCode(HttpStatus.CONFLICT.value());
        res.setError("DuplicateResourceException");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(res);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<RestResponseDTO<Object>> handleValidation(ValidationException ex) {
        RestResponseDTO<Object> res = new RestResponseDTO<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("ValidationException");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(res);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<RestResponseDTO<Object>> handleUnauthorized(UnauthorizedException ex) {
        RestResponseDTO<Object> res = new RestResponseDTO<>();
        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        res.setError("UnauthorizedException");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(res);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<RestResponseDTO<Object>> handleBusiness(BusinessException ex) {
        RestResponseDTO<Object> res = new RestResponseDTO<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("BusinessException");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(res);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<RestResponseDTO<Object>> handleEntityNotFound(EntityNotFoundException ex) {
        RestResponseDTO<Object> response = new RestResponseDTO<>();
        response.setStatusCode(HttpStatus.NOT_FOUND.value());
        response.setError("EntityNotFoundException");
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponseDTO<Object>> handleValidationError(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        List<String> errorMessages = fieldErrors.stream()
            .map(FieldError::getDefaultMessage)
            .toList();

        RestResponseDTO<Object> response = new RestResponseDTO<>();
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setError("Validation failed");
        response.setMessage(errorMessages.size() > 1 ? errorMessages : errorMessages.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<RestResponseDTO<Object>> handleGenericException(Exception ex) {
//        // Nếu là lỗi xác thực, ném lại để Spring xử lý bằng entrypoint
//        if (ex instanceof org.springframework.security.core.AuthenticationException ||
//            ex.getCause() instanceof org.springframework.security.core.AuthenticationException) {
//            throw (org.springframework.security.core.AuthenticationException) ex;
//        }
//
//        // Nếu là lỗi decode JWT (Malformed, Expired,...), ném lại để Spring xử lý
//        if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("jwt")) {
//            throw new org.springframework.security.authentication.BadCredentialsException("Invalid JWT");
//        }
//
//        RestResponseDTO<Object> response = new RestResponseDTO<>();
//        response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
//        response.setError("Internal Server Error");
//        response.setMessage("Có lỗi xảy ra trong hệ thống."); // ❗ Không nên show message gốc
//        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(response);
//    }
    
    @ExceptionHandler(RefreshTokenMissingException.class)
    public ResponseEntity<RestResponseDTO<Object>> handleRefreshException(RefreshTokenMissingException ex) {
        RestResponseDTO<Object> res = new RestResponseDTO<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage(ex.getMessage());
        res.setData(null);
        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, Object>> handleAppException(AppException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }
}
