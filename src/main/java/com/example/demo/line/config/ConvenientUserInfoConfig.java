package com.example.demo.line.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource(value = "classpath:convenientUserInfo.properties", encoding = "utf-8")
public class ConvenientUserInfoConfig {

	@Value("${ESHOPID}")
	private String eshopId;
	@Value("${PARENT_ID}")
	private String parentId;
	@Value("${PrintType}")
	private String printType;
	@Value("${rderAmount}")
	private String orderAmount;
	@Value("${receiverEmail}")
	private String receiverEmail;
	@Value("${receiverName}")
	private String receiverName;
	@Value("${receiverPhone}")
	private String receiverPhone;
	@Value("${returnStore}")
	private String returnStore;
	@Value("${returnStoreAddr}")
	private String returnStoreAddr;
	@Value("${returnStoreId}")
	private String returnStoreId;
	@Value("${sendStore}")
	private String sendStore;
	@Value("${sendStoreAddr}")
	private String sendStoreAddr;
	@Value("${sendStoreId}")
	private String sendStoreId;
	@Value("${senderEmail}")
	private String senderEmail;
	@Value("${enderName}")
	private String senderName;
	@Value("${senderPhone}")
	private String senderPhone;
}

