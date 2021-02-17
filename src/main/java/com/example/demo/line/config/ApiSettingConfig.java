package com.example.demo.line.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:apiSetting.properties")
public class ApiSettingConfig {

	@Value("${imgur_client_id}")
	private String imgur_client_id;
	@Value("${imgur_client_secret}")
	private String imgur_client_secret;
	@Value("${imgur_access_token}")
	private String imgur_access_token;


	@Value("${line_client_id}")
	private String line_client_id;

	@Value("${line_client_secret}")
	private String line_client_secret;

	@Value("${line_channel_access_token}")
	private String line_channel_access_token;

	@Value("${callback_url}")
	private String callback_url;

}
