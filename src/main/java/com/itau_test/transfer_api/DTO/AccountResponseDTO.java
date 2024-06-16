package com.itau_test.transfer_api.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class AccountResponseDTO {

	private String id;
	private double saldo;
	private double limiteDiario;
	private boolean ativo;
}
