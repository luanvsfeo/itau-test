package com.itau_test.transfer_api.controller;

import com.itau_test.transfer_api.DTO.TransferRequestDTO;
import com.itau_test.transfer_api.service.TransferService;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/transfer")
@Log4j2
public class TransferController {

	private TransferService transferService;

	@PostMapping
	ResponseEntity<?> createTransfer(@RequestBody TransferRequestDTO transferJson) {

		transferJson.populateId();

		log.info("m=createTransfer; stage=init; transactionUUID= {}; transferJson= {}", transferJson.getTransferId(), transferJson);

		transferService.create(transferJson);

		log.info("m=createTransfer; stage=finished; {}", transferJson);

		return ResponseEntity.ok(null);
	}
}
