package com.itau_test.transfer_api.DTO;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Data
public class NotificationRequestDTO {

	private double valor;
	private Conta conta;

	@Getter
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Conta {
		private String idOrigem;
		private String idDestino;
	}

	public NotificationRequestDTO(double valor, String contaOrigem, String contaDestino) {
		this.valor = valor;
		this.conta = new Conta(contaOrigem, contaDestino);
	}
}
