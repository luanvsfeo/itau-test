package com.itau_test.transfer_api.service;

import com.itau_test.transfer_api.DTO.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class TransferService {

	RequestService requestService;

	public TransferService(RequestService requestService) {
		this.requestService = requestService;
	}

	public boolean create(
			TransferRequestDTO transferDataObject,
			String urlGetClient,
			String urlPostNotification,
			String urlGetAccount,
			String urlPutAccount
	) {

		boolean result = false;

		log.info("m=create; stage=init; transactionUUID= {}; transferJson= {}", transferDataObject.getTransferId(), transferDataObject);

		boolean receivingClientExists = clientExists(urlGetClient, transferDataObject.getReceivingClientId());

		if (receivingClientExists) {

			AccountResponseDTO sendingClientAccount = getAccountData(urlGetAccount, transferDataObject.getSendingAccount());

			boolean response = couldMakeTheTransfer(urlPutAccount, sendingClientAccount, transferDataObject);

			if (response) {
				result = true;

				makeNotification(
						urlPostNotification,
						new NotificationRequestDTO(
								transferDataObject.getAmount(),
								transferDataObject.getSendingAccount(),
								transferDataObject.getReceivingAccount()
						)
				);
			}
		}

		log.info("m=create; stage=finish; transactionUUID= {}; transferJson= {} , result={}", transferDataObject.getTransferId(), transferDataObject, result);
		return result;
	}


	private boolean couldMakeTheTransfer(String urlPutAccount, AccountResponseDTO accountData, TransferRequestDTO transferData) {

		if (accountData.isAtivo() && hasLimitEnough(accountData, transferData) && hasMoneyEnough(accountData, transferData)) {

			updateAccountsBalance(urlPutAccount, transferData);
			return true;
		} else {
			return false;
		}
	}


	private boolean hasLimitEnough(AccountResponseDTO accountData, TransferRequestDTO transferData) {
		return accountData.getLimiteDiario() > transferData.getAmount();
	}


	private boolean hasMoneyEnough(AccountResponseDTO accountData, TransferRequestDTO transferData) {
		return accountData.getSaldo() > transferData.getAmount();
	}


	private boolean clientExists(String urlGetClient, String clientId) {

		ResponseEntity<?> stringResponseEntity = requestService.makeRequestWithoutBody(urlGetClient.replace("{uuid}", clientId), HttpMethod.GET, ClientResponseDTO.class);
		return stringResponseEntity.getStatusCode().is2xxSuccessful();
	}


	private AccountResponseDTO getAccountData(String urlGetAccount, String accountId) {

		return (AccountResponseDTO) requestService.makeRequestWithoutBody(urlGetAccount.replace("{uuid}", accountId), HttpMethod.GET, AccountResponseDTO.class).getBody();
	}


	private void updateAccountsBalance(String urlPutAccount, TransferRequestDTO transferRequestDTO) {
		requestService.makeRequestWithBody(urlPutAccount, HttpMethod.PUT, String.class, new UpdateBalanceAmountRequestDTO(transferRequestDTO));
	}


	private boolean makeNotification(String urlPostNotification, NotificationRequestDTO notificationRequestDTO) {
		ResponseEntity<?> responseEntity = requestService.makeRequestWithBody(urlPostNotification, HttpMethod.POST, String.class, notificationRequestDTO);

		return responseEntity.getStatusCode().is2xxSuccessful();
	}
}
