package com.itau_test.transfer_api.controller;

import com.itau_test.transfer_api.DTO.TransferRequestDTO;
import com.itau_test.transfer_api.service.TransferService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/transfer")
@Log4j2
public class TransferController {

	@Autowired
	private TransferService transferService;

	@Value("${url.get-client}")
	String URL_GET_CLIENT;

	@Value("${url.post-notification}")
	String URL_POST_NOTIFICATION;

	@Value("${url.get-account}")
	String URL_GET_ACCOUNT;

	@Value("${url.put-account}")
	String URL_PUT_ACCOUNT;


	@PostMapping
	ResponseEntity<?> createTransfer( @Valid @RequestBody TransferRequestDTO transferJson) {

		log.info("m=createTransfer; stage=init; transactionUUID= {}; transferJson= {}", transferJson.getTransferId(), transferJson);

		transferService.create(
				transferJson,
				URL_GET_CLIENT,
				URL_POST_NOTIFICATION,
				URL_GET_ACCOUNT,
				URL_PUT_ACCOUNT
		);

		log.info("m=createTransfer; stage=finished; {}", transferJson);

		return ResponseEntity.ok(null);
	}
}
