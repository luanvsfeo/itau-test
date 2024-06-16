package com.itau_test.transfer_api.DTO;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class ClientResponseDTO {

	private String id;
	private String nome;
	private String telefone;
	private String tipoPessoa;
}
