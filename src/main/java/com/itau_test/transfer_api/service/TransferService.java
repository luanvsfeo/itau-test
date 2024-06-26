package com.itau_test.transfer_api.service;

import com.itau_test.transfer_api.DTO.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

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
		String transferUuid = transferDataObject.getTransferId().toString();

		log.info("m=create; stage=init; transactionUUID= {}; transferJson= {}", transferDataObject.getTransferId(), transferDataObject);

		boolean receivingClientExists = clientExists(urlGetClient, transferDataObject.getReceivingClientId(), transferUuid);

		if (receivingClientExists) {

			AccountResponseDTO sendingClientAccount = getAccountData(urlGetAccount, transferDataObject.getSendingAccount(), transferUuid);

			boolean response = couldMakeTheTransfer(urlPutAccount, sendingClientAccount, transferDataObject, transferUuid);

			if (response) {
				result = true;

				makeNotification(
						urlPostNotification,
						new NotificationRequestDTO(
								transferDataObject.getAmount(),
								transferDataObject.getSendingAccount(),
								transferDataObject.getReceivingAccount()
						),
						transferUuid
				);
			}
		}

		log.info("m=create; stage=finish; transactionUUID= {}; transferJson= {} , result={}", transferDataObject.getTransferId(), transferDataObject, result);
		return result;
	}


	private boolean couldMakeTheTransfer(String urlPutAccount, AccountResponseDTO accountData, TransferRequestDTO transferData, String transferUuid) {

		if (isActive(accountData) && hasLimitEnough(accountData, transferData) && hasMoneyEnough(accountData, transferData)) {

			updateAccountsBalance(urlPutAccount, transferData, transferUuid);
			return true;
		}

		return false;
	}

	private boolean isActive(AccountResponseDTO accountData) {
		boolean result = accountData.isAtivo();

		if (result) {
			return true;
		} else {
			log.info("m=isActive; stage=invalid; accountData= {}", accountData);
			throw new IllegalStateException("Account is not available");
		}
	}

	private boolean hasLimitEnough(AccountResponseDTO accountData, TransferRequestDTO transferData) {

		boolean result = accountData.getLimiteDiario() > transferData.getAmount();

		if (result) {
			return true;
		} else {
			log.info("m=hasLimitEnough; stage=invalid; transactionUUID= {}; transferJson= {}", transferData.getTransferId(), transferData);
			throw new IllegalStateException("Not enough Limit today, try again tomorrow");
		}
	}


	private boolean hasMoneyEnough(AccountResponseDTO accountData, TransferRequestDTO transferData) {

		boolean result = accountData.getSaldo() > transferData.getAmount();

		if (result) {
			return true;
		} else {
			log.info("m=hasMoneyEnough; stage=invalid; transactionUUID= {}; transferJson= {}", transferData.getTransferId(), transferData);
			throw new IllegalStateException("Not enough money");
		}
	}


	private boolean clientExists(String urlGetClient, String clientId, String transferUuid) {

		try {
			ResponseEntity<?> stringResponseEntity = requestService.makeRequestWithoutBody(
					urlGetClient.replace("{uuid}", clientId),
					HttpMethod.GET,
					ClientResponseDTO.class,
					transferUuid
			);

			if (stringResponseEntity != null) {
				if (stringResponseEntity.getStatusCode().is2xxSuccessful()) {
					return true;
				} else {
					throw new NotFoundException("Client not found");
				}
			}
		} catch (Exception ex) {
			throw new IllegalStateException("Something went wrong | GET CLIENT API | ".concat(ex.getMessage()), ex.getCause());
		}

		return false;
	}


	private AccountResponseDTO getAccountData(String urlGetAccount, String accountId, String transferUuid) {

		try {
			ResponseEntity<?> responseEntity = requestService.makeRequestWithoutBody(
					urlGetAccount.replace("{uuid}", accountId),
					HttpMethod.GET,
					AccountResponseDTO.class,
					transferUuid
			);

			if (responseEntity != null) {
				if (responseEntity.getStatusCode().is2xxSuccessful()) {
					return (AccountResponseDTO) responseEntity.getBody();
				} else {
					throw new NotFoundException("Account not found");
				}
			}
		} catch (Exception ex) {
			throw new IllegalStateException("Something went wrong | GET ACCOUNTS API | ".concat(ex.getMessage()), ex.getCause());
		}

		return null;
	}


	private boolean updateAccountsBalance(String urlPutAccount, TransferRequestDTO transferRequestDTO, String transferUuid) {


		try {
			ResponseEntity<?> responseEntity = requestService.makeRequestWithBody(
					urlPutAccount,
					HttpMethod.PUT,
					String.class,
					new UpdateBalanceAmountRequestDTO(transferRequestDTO),
					transferUuid
			);

			if (responseEntity != null) {
				if (responseEntity.getStatusCode().is2xxSuccessful()) {
					return true;
				} else {
					throw new NotFoundException("Cloud not update accounts");
				}
			}
		} catch (Exception ex) {
			throw new IllegalStateException("Something went wrong | PUT ACCOUNTS API | ".concat(ex.getMessage()), ex.getCause());
		}

		return false;
	}


	private boolean makeNotification(String urlPostNotification, NotificationRequestDTO notificationRequestDTO, String transferUuid) {

		try {
			ResponseEntity<?> responseEntity = requestService.makeRequestWithBody(
					urlPostNotification,
					HttpMethod.POST,
					String.class,
					notificationRequestDTO,
					transferUuid
			);

			if (responseEntity != null) {
				if (responseEntity.getStatusCode().is2xxSuccessful()) {
					return true;
				} else {
					throw new NotFoundException("Cloud send notification");
				}
			}
		} catch (Exception ex) {
			throw new IllegalStateException("Something went wrong | POST NOTIFICATIONS API | ".concat(ex.getMessage()), ex.getCause());
		}

		return false;
	}
}
