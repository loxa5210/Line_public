package com.example.demo.line.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginRequest {

	private String response_type;

	private Integer client_id;

	/**
	 * 在Line developer 的設定
	 */
	private String redirect_uri;
	/**
	 * 自訂驗證碼，隨便你填
	 */
	private String state;
	/**
	 * 你想要取得 User 的資料，
	 * 例如想取得 openid 和 profile，填寫 openid%20profile
	 */
	private String scope;

}
