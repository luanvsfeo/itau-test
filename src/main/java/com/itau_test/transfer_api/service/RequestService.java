package com.itau_test.transfer_api.service;


import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Log4j2
public class RequestService {
	RestTemplate restTemplate;

	public RequestService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public ResponseEntity<?> makeRequestWithoutBody(String url, HttpMethod httpMethod, Class classReturn) {
		return makeRequestWithBody(url, httpMethod, classReturn, null);
	}

	public ResponseEntity<?> makeRequestWithBody(String url, HttpMethod httpMethod, Class classReturn, Object body) {

		ResponseEntity responseEntity = null;
		log.info("m=makeRequestWithBody; stage=init; transactionUUID= {}; url= {}; httpMethod={}; Object= {}", "", url, httpMethod, body);

		HttpEntity<?> httpEntity = new HttpEntity<>(body);

		responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, classReturn);

		log.info("m=makeRequestWithBody; stage=finished; transactionUUID= {}; Object= {}", "", responseEntity);
		return responseEntity;
	}
}
