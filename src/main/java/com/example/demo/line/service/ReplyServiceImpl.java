package com.example.demo.line.service;

import com.example.demo.line.config.ApiSettingConfig;
import com.example.demo.line.config.ConvenientUserInfoConfig;
import com.example.demo.line.config.OtherInfoConfig;
import com.example.demo.line.constant.UrlConstant;
import com.example.demo.line.utils.ApiUtil;
import com.example.demo.line.utils.QRCodeUtil;
import com.example.demo.line.vo.ImgurResponse;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
@Slf4j
public class ReplyServiceImpl implements ReplyService {

	@Autowired private QRCodeUtil qrCodeUtil;
	@Autowired private ApiUtil apiUtil;
	@Autowired private ApiSettingConfig apiSettingConfig;
	@Autowired private ConvenientUserInfoConfig convenientUserInfoConfig;
	@Autowired private OtherInfoConfig otherInfoConfig;

	@Override
	public List<ReplyMessage> getReplyInfo(List<Event> events) {
		List<ReplyMessage> messageEvents = new ArrayList<>();
		events.forEach(event -> {
			if (event.getClass().getName().equals(MessageEvent.class.getName())) {

				messageEvents.add(checkMessageType((MessageEvent) event));
			}
		});

		return messageEvents;
	}

	/**
	 * 確認使用者傳入的訊息種類
	 *
	 * @param messageEvent;
	 * @return ReplyMessage
	 */
	private ReplyMessage checkMessageType(MessageEvent messageEvent) {
		if (messageEvent.getMessage() instanceof TextMessageContent) {
			return handleSendMessageByText(messageEvent.getReplyToken(), (TextMessageContent) messageEvent.getMessage());
		}
		return sedReplyContextByText(messageEvent.getReplyToken(), "訊息錯誤");
	}

	/**
	 * 設定回覆內容
	 *
	 * @param replyToken;
	 * @param textMessageContent 傳入訊息的資訊
	 * @return List<MessageEvent> 回覆訊息內容清單
	 */
	private ReplyMessage handleSendMessageByText(String replyToken, TextMessageContent textMessageContent) {
		switch (textMessageContent.getText()) {
			case "寄貨":
				return getConvenientOrderShipInfo(replyToken);
			case "確認產生單號":
				String orderNo = handleOrderShip(getConvenientStoreOrderShip());
				if (StringUtils.isNotBlank(orderNo)) {
					ReplyMessage replyMessage = sedReplyContextByText(replyToken, "寄件編號：" + orderNo);
					List<Message> messages = new ArrayList<>(replyMessage.getMessages());
					messages.add(getQRCode(orderNo));
					return replyMessage;
				}
			default:
				return handleAddress(replyToken, textMessageContent);
		}
	}

	/**
	 * 設定回覆內容
	 *
	 * @param replyToken;
	 * @param textMessageContent 傳入訊息的資訊
	 * @return ReplyMessage 回覆訊息內容物件
	 */
	private ReplyMessage handleAddress(String replyToken, TextMessageContent textMessageContent) {
		if (textMessageContent.getText().equals(otherInfoConfig.getSecondName())) {
			return sedReplyContextByText(replyToken, otherInfoConfig.getSecond());
		}
		if (textMessageContent.getText().equals(otherInfoConfig.getOldestName())) {
			return sedReplyContextByText(replyToken, otherInfoConfig.getOldest());
		}
		if (textMessageContent.getText().equals(otherInfoConfig.getYoungestName())) {
			return sedReplyContextByText(replyToken, otherInfoConfig.getYoungest());
		}
		return sedReplyContextByText(replyToken, "訊息錯誤");
	}

	/**
	 * 設定回覆內容
	 *
	 * @param replyToken;
	 * @param messageContext 訊息內容
	 * @return ReplyMessage 回覆訊息內容物件
	 */
	private ReplyMessage sedReplyContextByText(String replyToken, String messageContext) {
		return new ReplyMessage(replyToken, new TextMessage(messageContext));
	}

	//	/**
	//	 * 設定回覆內容
	//	 *
	//	 * @param event;
	//	 * @param imageUrl 圖片超連結
	//	 * @return MessageEvent 回覆訊息內容物件
	//	 */
	//	private ReplyMessage sedReplyContextByImage(MessageEvent event, String imageUrl) {
	//		URI uri = stringToUrl(imageUrl);
	//		return new ReplyMessage(event.getReplyToken(), new ImageMessage(uri, uri));
	//	}

	/**
	 * 店到店單號產出QRCODE
	 *
	 * @param orderNo 超商單號;
	 * @return MessageEvent 回覆訊息內容物件
	 */
	private ImageMessage getQRCode(String orderNo) {
		InputStreamSource qrCodeStream = qrCodeUtil.getQrCodeStream(orderNo);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		try {
			body.add("image", IOUtils.toByteArray(qrCodeStream.getInputStream()));
			body.add("title", orderNo);

			HttpEntity<MultiValueMap<String, Object>> requestEntity
				= new HttpEntity<>(body, apiUtil.geHeadersContentTypeFile(apiSettingConfig.getImgur_access_token()));

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<ImgurResponse> response = restTemplate.postForEntity(UrlConstant.IMGUR_URL, requestEntity, ImgurResponse.class);

			if (200 == response.getStatusCode().value() && null != response.getBody()) {
				ImgurResponse imgurResponse = response.getBody();
				if (imgurResponse.getStatus().equals(200) && imgurResponse.getSuccess().equals(Boolean.TRUE)) {
					URI uri = stringToUrl(imgurResponse.getData().getLink());
					return new ImageMessage(uri, uri);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error("upData to imgur is error");
		}

		return null;
	}

	/**
	 * 取得店到店取貨人資訊
	 *
	 * @param replyToken;
	 * @return MessageEvent 回覆訊息內容物件
	 */
	private ReplyMessage getConvenientOrderShipInfo(String replyToken) {
		ButtonsTemplate buttonsTemplate = new ButtonsTemplate(
			stringToUrl(UrlConstant.LOGO_URL), "店到店資訊", getReceiveInfo(), Collections.singletonList(new MessageAction("確認產生單號", "確認產生單號")));
		TemplateMessage template = new TemplateMessage("請使用手持裝置查看此訊息", buttonsTemplate);
		return new ReplyMessage(replyToken, template);

		//
		//		{
		//			"type": "template",
		//			"altText": "this is a buttons template",
		//			"template": {
		//			"type": "buttons",
		//				"thumbnailImageUrl": ,
		//				"imageSize": "contain",
		//				"imageBackgroundColor": "#FFFFFF",
		//				"title": "店到店資訊",
		//				"text": "AAA",
		//				"actions": [
		//			{
		//				"type": "message",
		//				"label": "確認產生單號",
		//				"text": "亦真門市"
		//			}
		//    ]
		//		}
		//		}
	}

	/**
	 * 組合取件人資訊
	 *
	 * @return String
	 */
	private String getReceiveInfo() {
		return convenientUserInfoConfig.getReceiverName() + " , "
			+ convenientUserInfoConfig.getReceiverPhone() + " ,  "
			+ convenientUserInfoConfig.getReturnStore() + " . ";
	}

	/**
	 * 產生店到店單號
	 * 打到7-11 api 建立單號
	 *
	 * @return ResponseEntity<String> ;
	 */
	private ResponseEntity<String> getConvenientStoreOrderShip() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, String> postParameters = new HashMap<>();
		postParameters.put("EshopId", convenientUserInfoConfig.getEshopId());
		postParameters.put("ParentId", convenientUserInfoConfig.getParentId());
		postParameters.put("PrintType", convenientUserInfoConfig.getPrintType());
		postParameters.put("orderAmount", convenientUserInfoConfig.getOrderAmount());

		postParameters.put("receiverEmail", convenientUserInfoConfig.getReceiverEmail());
		postParameters.put("receiverName", convenientUserInfoConfig.getReceiverName());
		postParameters.put("receiverPhone", convenientUserInfoConfig.getReceiverPhone());

		postParameters.put("returnStore", convenientUserInfoConfig.getReturnStore());
		postParameters.put("returnStoreAddr", convenientUserInfoConfig.getReturnStoreAddr());
		postParameters.put("returnStoreId", convenientUserInfoConfig.getReturnStoreId());

		postParameters.put("sendStore", convenientUserInfoConfig.getSendStore());
		postParameters.put("sendStoreAddr", convenientUserInfoConfig.getSendStoreAddr());
		postParameters.put("sendStoreId", convenientUserInfoConfig.getSendStoreId());
		postParameters.put("senderEmail", convenientUserInfoConfig.getSenderEmail());
		postParameters.put("senderName", convenientUserInfoConfig.getSenderName());
		postParameters.put("senderPhone", convenientUserInfoConfig.getSenderPhone());
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity(JSONObject.toJSONString(postParameters), headers);

		return restTemplate.postForEntity(UrlConstant.CREATEORDER_URL, requestEntity, String.class);

	}

	/**
	 * 因回傳的資料是一串字串
	 * 在這邊處理拿到正到的單號
	 *
	 * @param responseEntity;
	 * @return String 超商單號
	 */
	private String handleOrderShip(ResponseEntity<String> responseEntity) {
		if (responseEntity.getStatusCode().value() == 200) {
			//			/**
			//			 * String test = "{\"ParentId\":\"756\",\"EshopId\":\"888\",\"Url\":\"/C2C/Print\",\"OrderNo\":\"20112605933201\",\"PaymentNo\":\"M5726387\",
			//			 * 	\"ValidationNo\":\"2095\",\"Description\":\"\",\"IsPayment\":null}"
			//			 */
			String responseEntityBody = responseEntity.getBody();
			if (null != responseEntityBody) {
				String[] split = responseEntityBody.split(",");
				//"PaymentNo":"M5726387"
				String[] paymentNo = split[4].split(":");
				//"ValidationNo":"2095"
				String[] validationNo = split[5].split(":");
				//"M5726387""2095"
				String result = paymentNo[1] + validationNo[1];

				return result.replace("\"", "");
			}
		}
		return null;
	}

	private URI stringToUrl(String url) {
		try {
			return new URI(url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			log.error("stringToUrl is error" + url);
			return null;
		}
	}

}
