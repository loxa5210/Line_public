package com.example.demo.line.controller;

import com.example.demo.line.config.ApiSettingConfig;
import com.example.demo.line.constant.UrlConstant;
import com.example.demo.line.vo.TokenResponse;
import com.example.demo.line.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = { "*" })
@RequestMapping("/login/")
@Controller
@Slf4j
public class LoginController {
	@Autowired private ApiSettingConfig apiSettingConfig;

	/**
	 * 該用戶
	 *
	 * @param code  授權碼 code
	 * @param state 自定義的驗證碼
	 */
	@GetMapping("/callback")
	public String getLineToken(String code, String state, Model model) {
		log.error("授權碼 " + code + "/n 驗證碼" + state);

		if (StringUtils.isNotBlank(code)) {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			ResponseEntity<TokenResponse> tokenResponseResponseEntity = addGetTokenParameters(code, headers);

			if (200 == tokenResponseResponseEntity.getStatusCode().value() && null != tokenResponseResponseEntity.getBody()) {
				ResponseEntity<UserInfoVO> userInfoVOResponseEntity =
					addGetUserInfoParameters(tokenResponseResponseEntity.getBody(), headers);
				model.addAttribute("userInfo", userInfoVOResponseEntity.getBody());
			} else {
				log.error("getToke is fail StatusCode " + tokenResponseResponseEntity.getStatusCode().value());
			}
		} else {

			log.error("授權碼 code" + code + "state" + state);

		}

		return "userInfo";
	}

	private ResponseEntity<TokenResponse> addGetTokenParameters(String code, HttpHeaders headers) {
		MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
		postParameters.add("grant_type", "authorization_code");
		postParameters.add("client_id", apiSettingConfig.getLine_client_id());
		postParameters.add("client_secret", apiSettingConfig.getLine_client_secret());
		postParameters.add("code", code);
		postParameters.add("redirect_uri", apiSettingConfig.getCallback_url());

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(postParameters, headers);

		return restTemplate.postForEntity(UrlConstant.TOKEN_URL, requestEntity, TokenResponse.class);

	}

	private ResponseEntity<UserInfoVO> addGetUserInfoParameters(TokenResponse tokenResponse, HttpHeaders headers) {
		MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
		postParameters.add("id_token", tokenResponse.getId_token());
		postParameters.add("client_id", apiSettingConfig.getLine_client_id());
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(postParameters, headers);
		return restTemplate.postForEntity(UrlConstant.VERIFY_URL, requestEntity, UserInfoVO.class);
	}
}
