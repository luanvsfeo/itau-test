package com.itau_test.transfer_api.controller;

import com.itau_test.transfer_api.service.TransferService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@Log4j2
public class TransferController {

    private TransferService transferService;

    @PostMapping
    ResponseEntity<?> createTransfer(@RequestBody String transferJson) {

        UUID uuidTransfer = UUID.randomUUID();
        log.info("m=createTransfer; stage=init; transactionUUID= {}; transferJson= {}", uuidTransfer, transferJson);

        transferService.generate(transferJson);

        log.info("m=createTransfer; stage=finished; {}", transferJson);

        return ResponseEntity.ok(null);
    }
}
