package com.itau_test.transfer_api.DTO;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class TransferRequestDTO {

	private UUID transferId;
	private String receivingClientId;
	private double amount;
	private String sendingAccount;
	private String receivingAccount;

	public void populateId() {
		this.transferId = UUID.randomUUID();
	}
}



