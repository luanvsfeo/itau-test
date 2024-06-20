package com.itau_test.transfer_api.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class TransferRequestDTO {

	private UUID transferId = UUID.randomUUID();

	@NotNull
	private String receivingClientId;

	@NotNull
	private double amount;

	@NotNull
	private String sendingAccount;

	@NotNull
	private String receivingAccount;

}



