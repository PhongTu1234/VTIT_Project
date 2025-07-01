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

@RestControllerAdvice
public class GlobleException {
	
	@ExceptionHandler(value = IdInvalidException.class)
	public ResponseEntity<RestResponseDTO<Object>> handleIdException(IdInvalidException idException){
		RestResponseDTO<Object> res = new RestResponseDTO<Object>();
		res.setStatusCode(HttpStatus.SC_BAD_REQUEST);
		res.setError(idException.getMessage());
		res.setMessage("IdInvalidException");
		return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(res);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<RestResponseDTO<Object>> handleValidationError(MethodArgumentNotValidException ex) {
	    BindingResult bindingResult = ex.getBindingResult();
	    List<FieldError> fieldErrors = bindingResult.getFieldErrors();

	    // Lấy thông điệp lỗi
	    List<String> errorMessages = fieldErrors.stream()
	        .map(FieldError::getDefaultMessage)
	        .toList();

	    // Tạo response body
	    RestResponseDTO<Object> response = new RestResponseDTO<>();
	    response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
	    response.setError("Validation failed");
	    response.setMessage(errorMessages.size() > 1 ? errorMessages : errorMessages.get(0));

	    return ResponseEntity
	        .status(HttpStatus.SC_BAD_REQUEST)
	        .body(response);
	}

}
