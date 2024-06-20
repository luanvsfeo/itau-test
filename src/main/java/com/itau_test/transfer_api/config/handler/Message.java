package com.itau_test.transfer_api.config.handler;

import lombok.*;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Message {

	HashMap<String,String> errors;
}
