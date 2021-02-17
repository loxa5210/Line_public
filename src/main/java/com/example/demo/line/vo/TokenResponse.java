package com.example.demo.line.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TokenResponse {

	private String access_token;
	private String token_type;
	private String refresh_token;
	private Integer expires_in;
	private String scope;
	private String id_token;
}
