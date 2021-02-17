package com.example.demo.line.utils;

import com.example.demo.line.config.ApiSettingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class ApiUtil {
	@Autowired ApiSettingConfig apiSettingConfig;

	/**
	 * 取得headers
	 * Authorization, Bearer +token
	 *
	 * @return
	 */
	public HttpHeaders geHeaders(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Bearer " + token);
		return headers;
	}

	/**
	 * 取得headers
	 * Authorization, Bearer +token
	 *
	 * @return
	 */
	public HttpHeaders geHeadersContentTypeFile(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add("Authorization", "Bearer " + token);

		return headers;
	}

	/**
	 * 取得headers
	 * Authorization, Bearer +token
	 *
	 * @return
	 */
	public HttpHeaders geHeadersByImgur() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add("Authorization", "Client-ID " + apiSettingConfig.getImgur_client_id());
//		headers.add("Authorization", "Bearer " + apiSettingVO.getImgur_access_token());

		return headers;
	}
}
