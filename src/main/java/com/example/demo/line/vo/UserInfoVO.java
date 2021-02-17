package com.example.demo.line.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class UserInfoVO {

	private String iss;
	private String sub;
	/**
	 * Channel ID
	 */
	private Long aud;
	/**
	 * Token 過期時間
	 */
	private Long exp;
	/**
	 * Token 建立時間
	 */
	private Long iat;
	/**
	 * pwd: Log in with email and password
	 * lineautologin: LINE automatic login (including through LINE SDK)
	 * lineqr: Log in with QR code
	 * linesso: Log in with single sign-on
	 */
	private List<String> amr;
	/**
	 * 使用者名稱
	 */
	private String name;
	/**
	 * 使用者照片url
	 */
	private String picture;
	/**
	 * 信箱
	 */
	private String email;

	/**
	 *     "iss": "https://access.line.me",
	 *     "sub": "Uc4f7f382608a96966f56de57f87ebcf3",
	 *     "aud": "1593850437",
	 *     "exp": 1606209778,
	 *     "iat": 1606206178,
	 *     "amr": [
	 *         "linesso"
	 *     ],
	 *     "name": "黃麗卉 (Lili)",
	 *     "picture": "https://profile.line-scdn.net/0hgYbJ3SSLOHZfMxI4mztHIWN2NhsoHT4-J1IgEH46NRIiV3skZl10FnNhMhR0UyhwY1QkEC5gbkJz",
	 *     "email": "loxa5210@gmail.com"
	 */
}
