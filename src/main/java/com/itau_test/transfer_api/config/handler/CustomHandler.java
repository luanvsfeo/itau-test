package com.itau_test.transfer_api.config.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;

@Log4j2
@ControllerAdvice
public class CustomHandler extends ResponseEntityExceptionHandler {

	@Override
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

		log.info("m=handleMethodArgumentNotValid | message={} ", ex.getMessage());

		HashMap<String, String> errors = new HashMap<>();
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {

			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}

		log.info("m=handleMethodArgumentNotValid | errors={} ", errors);

		return ResponseEntity.badRequest().body(new Message(errors));
	}

	@ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class, HttpClientErrorException.class})
	protected ResponseEntity<Object> handleIllegalArgumentException(RuntimeException ex, WebRequest request) {

		log.info("m=handleIllegalArgumentException | message={} ", ex.getMessage());

		HashMap<String, String> errors = new HashMap<>();
		errors.put("message", resizeMessage(ex.getMessage()));

		return ResponseEntity.badRequest().body(new Message(errors));
	}

	private String resizeMessage(String message) {
		if (message.length() >= 50) {
			return message.substring(0, 50);
		} else {
			return message;
		}
	}
}