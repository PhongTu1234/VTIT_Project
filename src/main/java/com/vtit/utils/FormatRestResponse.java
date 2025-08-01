package com.vtit.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vtit.dto.common.RestResponseDTO;
import com.vtit.utils.annotation.ApiMessage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    private static final ObjectMapper mapper = new ObjectMapper();

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
    	
    	if (body instanceof org.springframework.core.io.Resource) {
            return body;
        }

        if (body instanceof RestResponseDTO) {
            return body;
        }

        HttpServletResponse serverResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = serverResponse.getStatus();

        RestResponseDTO<Object> res = new RestResponseDTO<>();
        res.setStatusCode(status);

        if (status >= 400) {
            res.setError(body != null ? body.toString() : "Unknown error");
            res.setMessage(null);
            res.setData(null);
        } else {
            res.setError(null);
            ApiMessage message = returnType.getMethodAnnotation(ApiMessage.class);
            res.setMessage(message != null ? message.value() : "CALL API SUCCESS");
            res.setData(body);
        }

        // ⚠️ Nếu body gốc là String, Spring mong muốn String, nên phải tự serialize
        if (body instanceof String) {
            try {
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return mapper.writeValueAsString(res);
            } catch (Exception e) {
                throw new RuntimeException("Lỗi khi convert RestResponseDTO thành JSON string", e);
            }
        }

        return res;
    }
}
