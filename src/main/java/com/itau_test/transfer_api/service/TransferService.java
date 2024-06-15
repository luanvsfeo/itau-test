package com.itau_test.transfer_api.service;

import com.itau_test.transfer_api.DTO.TransferRequestDTO;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Log4j2
public class TransferService {

	RequestService requestService;

	public TransferService(RequestService requestService) {
		this.requestService = requestService;
	}

	public boolean create(TransferRequestDTO transferDataObject) {

		boolean result = false;

		log.info("m=create; stage=init; transactionUUID= {}; transferJson= {}", transferDataObject.getTransferId(), transferDataObject);

		boolean receivingClientExists = clientExists(transferDataObject.getReceivingClientId());

		if (receivingClientExists) {
			// bate na api de contgs
			String sendingClientAccount = getAccountData(transferDataObject.getSendingAccount());

			boolean madeTheTransfer = makeTheTransfer(new JSONObject(sendingClientAccount), transferDataObject);

			if (madeTheTransfer) {
				result = true;
				makeNotification();
			}
		}

		log.info("m=create; stage=finish; transactionUUID= {}; transferJson= {} , result={}", transferDataObject.getTransferId(), transferDataObject, result);
		return result;
	}


	private boolean makeTheTransfer(JSONObject accountData, TransferRequestDTO transferData) {

		if (accountData.getBoolean("ativo") && hasLimitEnough(accountData, transferData) && hasMoneyEnough(accountData, transferData)) {
			// faz a transferencia
			// colocar a chamada para atualizar o saldo de ambas as contas
			// criar um banco de dados ?
			return true;
		} else {
			// nao faz a transferencia
			return false;
		}
	}


	private boolean hasLimitEnough(JSONObject accountData, TransferRequestDTO transferData) {
		return accountData.getDouble("limiteDiario") < transferData.getAmount();
	}

	private boolean hasMoneyEnough(JSONObject accountData, TransferRequestDTO transferData) {
		return accountData.getDouble("saldo") < transferData.getAmount();
	}


	private boolean clientExists(String clientId) {
		// buscar o cliente e verifica se ele existe
		ResponseEntity<?> stringResponseEntity = requestService.makeRequestWithoutBody("localhost:8080/clientes/" + clientId, HttpMethod.GET, String.class);
		// todo - trocar isso aqui para o DTO

		return stringResponseEntity.getStatusCode().value() == 200;
	}

	private String getAccountData(String accountId) {
		// buscar a conta do client que esta fazendo a transferencia
		return (String) requestService.makeRequestWithoutBody("localhost:8080/contas/" + accountId, HttpMethod.GET, String.class).getBody();
		// todo - trocar isso aqui para o dto
	}

	private String makeNotification() {
		// faz a notificacao
		return (String) requestService.makeRequestWithoutBody("localhost:8080/notificacoes/", HttpMethod.POST, String.class).getBody();
		// todo - trocar isso aqui para o dto
	}
}
