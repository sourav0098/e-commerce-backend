package com.quickpik.exception;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.quickpik.payload.ApiResponse;

/**

GlobalExceptionHandler is a class that implements the exception handling
mechanism of the application. We are handling ResourceNotFoundException, ConstraintViolationException
and sending custom error messages in response
@author Sourav Choudhary
*/

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String,Object> body=new LinkedHashMap<>();
		body.put("timestamp", System.currentTimeMillis());
		body.put("status",status.value());
		
		List<String> errors=ex.getBindingResult().getFieldErrors().stream()
				.map(x->x.getDefaultMessage()).collect(Collectors.toList());
		body.put("errors",errors);
		return new ResponseEntity<Object>(body,status);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> handlerResponseNotFoundException(ResourceNotFoundException ex){
		String message=ex.getMessage();
		ApiResponse response=ApiResponse.builder().message(message).status(HttpStatus.NOT_FOUND.value()).timestamp(System.currentTimeMillis()).build();
		return new ResponseEntity<ApiResponse>(response,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse> handlerConstraintVoilationException(ConstraintViolationException ex){
		String message=ex.getMessage();
		ApiResponse response=ApiResponse.builder().message(message).status(HttpStatus.BAD_REQUEST.value()).timestamp(System.currentTimeMillis()).build();
		return new ResponseEntity<ApiResponse>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(BadApiRequestException.class)
	public ResponseEntity<ApiResponse> handleBadApiRequest(BadApiRequestException ex){
		String message=ex.getMessage();
		ApiResponse response=ApiResponse.builder().message(message).status(HttpStatus.BAD_REQUEST.value()).timestamp(System.currentTimeMillis()).build();
		return new ResponseEntity<ApiResponse>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ApiResponse> handleUnauthorizedRequest(UnauthorizedException ex){
		String message=ex.getMessage();
		ApiResponse response=ApiResponse.builder().message(message).status(HttpStatus.UNAUTHORIZED.value()).timestamp(System.currentTimeMillis()).build();
		return new ResponseEntity<ApiResponse>(response,HttpStatus.UNAUTHORIZED);
	}
}