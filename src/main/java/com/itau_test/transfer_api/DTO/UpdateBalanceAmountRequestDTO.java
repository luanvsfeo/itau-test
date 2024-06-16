package com.itau_test.transfer_api.DTO;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class UpdateBalanceAmountRequestDTO {

	private double amount;
	private String sendingAccount;
	private String receivingAccount;

	public UpdateBalanceAmountRequestDTO(TransferRequestDTO transferRequestDTO) {
		this.amount = transferRequestDTO.getAmount();
		this.sendingAccount = transferRequestDTO.getSendingAccount();
		this.receivingAccount = transferRequestDTO.getReceivingAccount();
	}
}
