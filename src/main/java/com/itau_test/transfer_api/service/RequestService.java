package com.itau_test.transfer_api.service;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RequestService {

    RestTemplate restTemplate;

    ResponseEntity<String> makeRequestWithoutBody(String url, HttpMethod httpMethod) {

        HttpEntity<String> request = new HttpEntity<>(null);
        return restTemplate.exchange(url, httpMethod, request, String.class);
    }

    ResponseEntity<String> makeRequestWithBody(String url, HttpMethod httpMethod, Object body) {

        HttpEntity<Object> request = new HttpEntity<>(body);
        return restTemplate.exchange(url, httpMethod, request, String.class);
    }
}
