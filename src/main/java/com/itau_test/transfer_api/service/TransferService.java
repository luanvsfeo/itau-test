package com.itau_test.transfer_api.service;

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


	public boolean create(UUID uuidTransfer, JSONObject transferDataObject) {

		boolean result = false;

		log.info("m=create; stage=init; transactionUUID= {}; transferJson= {}", uuidTransfer, transferDataObject);

		boolean receivingClientExists = clientExists(transferDataObject.getString("receiveClientId"));

		if (receivingClientExists) {
			// bate na api de contgs
			String sendingClientAccount = getAccountData(transferDataObject.getString("receiveClientAccount"));

			boolean madeTheTransfer = makeTheTransfer(new JSONObject(sendingClientAccount), transferDataObject);

			if (madeTheTransfer) {
				result = true;
				makeNotification();
			}
		}

		log.info("m=create; stage=finish; transactionUUID= {}; transferJson= {} , result={}", uuidTransfer, transferDataObject, result);
		return result;
	}


	boolean makeTheTransfer(JSONObject accountData, JSONObject transferData) {


		if (accountData.getBoolean("ativo") && hasLimitEnough(accountData, transferData) && hasMoneyEnough(accountData, transferData)) {
			// faz a transferencia
			return true;
		} else {
			// nao faz a transferencia
			return false;
		}
	}


	boolean hasLimitEnough(JSONObject accountData, JSONObject transferData) {
		return accountData.getDouble("limiteDiario") < transferData.getDouble("amount");
	}

	boolean hasMoneyEnough(JSONObject accountData, JSONObject transferData) {
		return accountData.getDouble("saldo") < transferData.getDouble("amount");
	}


	boolean clientExists(String clientId) {
		// buscar o cliente e verifica se ele existe
		ResponseEntity<?> stringResponseEntity = requestService.makeRequestWithoutBody("localhost:8080/clientes/" + clientId, HttpMethod.GET, String.class);
		// todo - trocar isso aqui para o DTO

		return stringResponseEntity.getStatusCode().value() == 200;
	}

	String getAccountData(String accountId) {
		// buscar a conta do client que esta fazendo a transferencia
		return (String) requestService.makeRequestWithoutBody("localhost:8080/contas/" + accountId, HttpMethod.GET, String.class).getBody();
		// todo - trocar isso aqui para o dto
	}

	String makeNotification() {
		// faz a notificacao
		return (String) requestService.makeRequestWithoutBody("localhost:8080/notificacoes/", HttpMethod.POST, String.class).getBody();
		// todo - trocar isso aqui para o dto
	}
}
