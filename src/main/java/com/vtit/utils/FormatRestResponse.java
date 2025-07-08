package com.vtit.utils;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.vtit.dto.RestResponseDTO;

import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(
			Object body, 
			MethodParameter returnType, 
			MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, 
			ServerHttpRequest request,
			ServerHttpResponse response) {
		HttpServletResponse serverResponse = ((ServletServerHttpResponse) response).getServletResponse();
		int status = serverResponse.getStatus();
		

		RestResponseDTO<Object> res = new RestResponseDTO<>();
		res.setStatusCode(status);
		if(status >= 400) {
			res.setError("CALL API FALSE");
			res.setMessage(body);
		}else {
			res.setData(body);
			res.setMessage("CALL API SUCCESS");
		}
		return res;
	}

}
