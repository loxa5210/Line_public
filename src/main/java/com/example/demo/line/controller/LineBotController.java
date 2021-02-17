package com.example.demo.line.controller;

import com.example.demo.line.config.ApiSettingConfig;
import com.example.demo.line.constant.UrlConstant;
import com.example.demo.line.service.ReplyService;
import com.example.demo.line.utils.ApiUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.CallbackRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@CrossOrigin(origins = { "*" })
@RequestMapping("/bot/")
@RestController
@Slf4j
public class LineBotController {

	@Autowired private ReplyService replyService;

	@Autowired private ApiSettingConfig apiSettingConfig;

	@Autowired private ApiUtil apiUtil;

	/**
	 * 該用戶
	 */
	@RequestMapping("/callback")
	public void getMessage(@RequestBody CallbackRequest callbackRequest){
		List<ReplyMessage> replyInfo = replyService.getReplyInfo(callbackRequest.getEvents());

		if (!CollectionUtils.isEmpty(replyInfo)) {
			sedReply(replyInfo);

		}

	}

	private void sedReply(List<ReplyMessage> replyMessages) {
		ObjectMapper objectMapper = new ObjectMapper();
		RestTemplate restTemplate = new RestTemplate();
		replyMessages.forEach(replyInfo -> {
			try {
				HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity(objectMapper.writeValueAsString(replyInfo),
					apiUtil.geHeaders(apiSettingConfig.getLine_channel_access_token()));
				restTemplate.postForLocation(UrlConstant.REPLY_URL, requestEntity);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				log.error("writeValueAsString is error" + replyMessages.toString());
			}

		});

	}

}
