package com.itau_test.transfer_api;

import com.itau_test.transfer_api.service.RequestService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.itau_test.transfer_api.service.TransferService;

import static org.mockito.Mockito.mock;

public class TransferServiceTest {

	TransferService transferService;
	RequestService requestService;

	@BeforeEach
	void init() {
		requestService = mock(requestService);
		transferService = new TransferService(requestService);
	}

	@Test
	void teste() {

	}
}
