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

	ResponseEntity<?> makeRequestWithoutBody(String url, HttpMethod httpMethod, Class classReturn) {
		return makeRequestWithBody(url, httpMethod, classReturn, null);
	}

	ResponseEntity<?> makeRequestWithBody(String url, HttpMethod httpMethod, Class classReturn, Object body) {

		ResponseEntity exchange = null;
		log.info("m=makeRequestWithBody; stage=init; transactionUUID= {}; Object= {}", "", "");

		try {
			HttpEntity<?> request = new HttpEntity<>(body);
			exchange = restTemplate.exchange(url, httpMethod, request, classReturn);
		} catch (Exception ex) {

		}
		log.info("m=makeRequestWithBody; stage=finished; transactionUUID= {}; Object= {}", "", "");
		return exchange;
	}
}
