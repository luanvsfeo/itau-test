package com.itau_test.transfer_api;

import com.itau_test.transfer_api.DTO.AccountResponseDTO;
import com.itau_test.transfer_api.DTO.ClientResponseDTO;
import com.itau_test.transfer_api.DTO.TransferRequestDTO;
import com.itau_test.transfer_api.service.RequestService;
import com.itau_test.transfer_api.service.TransferService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransferServiceTest {

	TransferService transferService;
	RequestService requestService;

	RestTemplate restTemplate;

	@BeforeEach
	void init() {
		restTemplate = mock(RestTemplate.class);
		requestService = new RequestService(restTemplate);
		transferService = new TransferService(requestService);
	}

	@Test
	void transferValid() {

		when(restTemplate.exchange(contains("/clientes"), any(), any(), eq(ClientResponseDTO.class))).thenReturn(ResponseEntity.ok().body(responseClientApi()));
		when(restTemplate.exchange(contains("/contas"), eq(HttpMethod.GET), any(), eq(AccountResponseDTO.class))).thenReturn(ResponseEntity.ok().body(responseAccountApi()));
		when(restTemplate.exchange(contains("/saldos"), any(), any(), eq(String.class))).thenReturn(ResponseEntity.ok().body(null));
		when(restTemplate.exchange(contains("/notificacoes"), any(), any(), eq(String.class))).thenReturn(ResponseEntity.ok().body(null));


		boolean response = transferService.create(
				new TransferRequestDTO(
						UUID.randomUUID(),
						"2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f",
						50,
						"41313d7b-bd75-4c75-9dea-1f4be434007f",
						"d0d32142-74b7-4aca-9c68-838aeacef96b"
				)
		);

		Assertions.assertTrue(response);

	}


	private AccountResponseDTO responseAccountApi() {
		return new AccountResponseDTO(
				"d32142-74b7-4aca-9c68-838aeacef96b",
				5000.00,
				500.00,
				true
		);
	}

	private ClientResponseDTO responseClientApi() {
		return new ClientResponseDTO(
				"bcdd1048-a501-4608-bc82-66d7b4db3600",
				"João Silva",
				"912348765",
				"Fisica"
		);
	}
}
